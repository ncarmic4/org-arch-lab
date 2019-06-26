import java.math.BigInteger;

class Registers {
    public int data;
}

public class cpu {
    // Instruction codes hard-coded in
    private final String arithmeticInstruction = "00";
    private final String conditionalInstruction = "01";
    private final String unconditionalInstruction = "10";
    private final String inputOutputInstruction = "11";

    // Determine opcode by converting binary to decimal and indexing opcodeArray
    private String opcode;
    private String[] opcodeArray = {"RD", "WR", "ST", "LW", "MOV", "ADD", "SUB", "MUL", "DIV", "AND",
            "OR", "MOVI", "ADDI", "MULI", "DIVI", "LDI", "SLT", "SLTI", "HLT", "NOP", "JMP", "BEQ",
            "BNE", "BEZ", "BNZ", "BGZ", "BLZ"};

    // Properties of binary instructions
    private String binary;
    private int reg1Index;
    private int reg2Index;
    private int reg3Index;
    private String address;
    private int addressIndex;

    private mmu mmu = new mmu();
    private Registers[] reg = new Registers[16];

    private int pc;
    private boolean cont = true;

    public static void main(String[] args) {
        // Declare a new cpu
        cpu cpu = new cpu();

        // Initialize registers
        for (int i = 0; i < cpu.reg.length; i++) {
            cpu.reg[i] = new Registers();
        }

        // Loads instructions into the mmu ram array
        loader.load(cpu.mmu);
        cpu.execute();
    }

    private String fetch(int index) {
        return mmu.load(index);
    }

    private void decode(String hex) {
        // Adds leading zeros if binary string is less than 32 chars long
        binary = new BigInteger(hex, 16).toString(2);
        while (binary.length() < 32) {
            binary = "0" + binary;
        }

        // Chars 3-8 specify opcode, we convert it to decimal and use it to index opcodeArray
        String opcodeBinary = binary.substring(2, 8);
        int opcodeIndex = Integer.parseInt(opcodeBinary, 2);
        opcode = opcodeArray[opcodeIndex];

        // First two chars of binary string specifies instruction format
        String instructionFormat = binary.substring(0, 2);
        switch (instructionFormat) {
            case arithmeticInstruction: {
                arithmetic();
                break;
            }

            case conditionalInstruction: {
                conditionalImmediate();
                break;
            }

            case unconditionalInstruction: {
                unconditional();
                break;
            }

            case inputOutputInstruction: {
                inputOutput();
                break;
            }
        }
    }

    private void execute() {
        String hex = fetch(pc);
        pc++;
        decode(hex);

        // Debugging statements
        System.out.println(opcode + " | pc: " + pc + "\n" + hex + "  |  " + binary);
        System.out.println("AddressIndex: " + addressIndex + " | REG " + reg1Index + ": " + reg[reg1Index].data
                + " | REG " + reg2Index + ": " + reg[reg2Index].data
                + " | REG " + reg3Index + ": " + reg[reg3Index].data);
        System.out.println("-------------------");

        // We set boolean cont to false when the HLT opcode is called
        if(cont) {
            execute();
        } else {
            // Final Output
            System.out.println("||||||||||||||||||||||||||||||||||");
            System.out.println(mmu.ram[43] + " | " + toDecimal(mmu.ram[43], 16));
        }
    }

    private int toDecimal(String hex, int radix) {
        return Integer.parseInt(hex, radix);
    }

    private void arithmetic() {
        String reg1 = binary.substring(8, 12);
        reg1Index = Integer.parseInt(reg1, 2);

        String reg2 = binary.substring(12, 16);
        reg2Index = Integer.parseInt(reg2, 2);

        String reg3 = binary.substring(16, 20);
        reg3Index = Integer.parseInt(reg3, 2);

        evaluate();
    }

    private void conditionalImmediate() {
        String reg1 = binary.substring(8, 12);
        reg1Index = Integer.parseInt(reg1, 2);

        String reg2 = binary.substring(12, 16);
        reg2Index = Integer.parseInt(reg2, 2);

        address = binary.substring(16, 32);
        addressIndex = Integer.parseInt(address, 2) / 4;

        evaluate();
    }

    private void unconditional() {
        address = binary.substring(8, 32);
        addressIndex = Integer.parseInt(address, 2) / 4;

        evaluate();
    }

    private void inputOutput() {
        String reg1 = binary.substring(8, 12);
        reg1Index = Integer.parseInt(reg1, 2);

        String reg2 = binary.substring(12, 16);
        reg2Index = Integer.parseInt(reg2, 2);

        address = binary.substring(16, 32);
        addressIndex = Integer.parseInt(address, 2) / 4;

        evaluate();
    }

    private void evaluate() {
        switch (opcode) {
            case "RD": {
                if(addressIndex == 0) {
                    reg[reg1Index].data = toDecimal(mmu.ram[reg[reg2Index].data], 16);
                } else {
                    reg[reg1Index].data = toDecimal(mmu.ram[addressIndex], 16);
                }
                break;
            }

            case "WR": {
                mmu.ram[addressIndex] = Integer.toHexString(reg[reg1Index].data);
                break;
            }

            case "ST": {
                if (addressIndex == 0) {
                    mmu.ram[reg[reg2Index].data] = Integer.toHexString(reg[reg1Index].data);
                } else {
                    mmu.ram[addressIndex] = Integer.toHexString(reg[reg1Index].data);
                }
                break;
            }

            case "LW": {
                if (addressIndex == 0){
                    reg[reg2Index].data = toDecimal(mmu.ram[reg[reg1Index].data], 16);
                } else {
                    reg[reg2Index].data = toDecimal(mmu.ram[addressIndex], 16);
                }
                break;
            }

            case "MOV": {
                reg[reg3Index].data = reg[reg1Index].data;
                break;
            }

            case "ADD": {
                reg[reg3Index].data = reg[reg1Index].data + reg[reg2Index].data;
                break;
            }

            case "SUB": {
                reg[reg3Index].data = reg[reg1Index].data - reg[reg2Index].data;
                break;
            }

            case "MUL": {
                reg[reg3Index].data = reg[reg1Index].data * reg[reg2Index].data;
                break;
            }

            case "DIV": {
                if (reg[reg2Index].data != 0) {
                    reg[reg3Index].data = reg[reg1Index].data / reg[reg2Index].data;
                }
                break;
            }

            case "AND": {
                if (reg[reg1Index].data != 0 && reg[reg2Index].data != 0) {
                    reg[reg3Index].data = 1;
                } else {
                    reg[reg3Index].data = 0;
                }
                break;
            }

            case "OR": {
                if (reg[reg1Index].data == 1 || reg[reg2Index].data == 1) {
                    reg[reg3Index].data = 1;
                } else {
                    reg[reg3Index].data = 0;
                }
                break;
            }

            case "MOVI": {
                reg[reg2Index].data = toDecimal(address, 2);
                break;
            }

            case "ADDI": {
                reg[reg2Index].data++;
                break;
            }

            case "MULI": {
                reg[reg2Index].data = reg[reg2Index].data * addressIndex;
                break;
            }

            case "DIVI": {
                if (addressIndex != 0) {
                    reg[reg2Index].data = reg[reg2Index].data / addressIndex;
                }
                break;
            }

            case "LDI": {
                reg[reg2Index].data = addressIndex;
                break;
            }

            case "SLT": {
                if (reg[reg1Index].data < reg[reg2Index].data) {
                    reg[reg3Index].data = 1;
                } else {
                    reg[reg3Index].data = 0;
                }
                break;
            }

            case "SLTI": {
                if (reg[reg1Index].data < addressIndex) {
                    reg[reg2Index].data = 1;
                } else {
                    reg[reg2Index].data = 0;
                }
                break;
            }

            case "HLT": {
                cont = false;
                break;
            }

            case "NOP": {
                pc++;
                break;
            }

            case "JMP": {
                pc = addressIndex;
                break;
            }

            case "BEQ": {
                if (reg[reg1Index].data == reg[reg2Index].data) {
                    pc = addressIndex;
                }
                break;
            }

            case "BNE": {
                if (reg[reg1Index].data != reg[reg2Index].data) {
                    pc = addressIndex;
                }
                break;
            }

            case "BEZ": {
                if (reg[reg2Index].data == 0) {
                    pc = addressIndex;
                }
                break;
            }

            case "BNZ": {
                if (reg[reg1Index].data != 0) {
                    pc = addressIndex;
                }
                break;
            }

            case "BGZ": {
                if (reg[reg1Index].data > 0) {
                    pc = addressIndex;
                }
                break;
            }

            case "BLZ": {
                if (reg[reg1Index].data < 0) {
                    pc = addressIndex;
                }
                break;
            }
        }
    }
}