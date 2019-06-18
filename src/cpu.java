import java.math.BigInteger;

class Registers
{
    public int data;
    public String address;
}

public class cpu
{
    private static String binary;
    private static String opcode;
    private static String reg1;
    private static String reg2;
    private static String reg3;
    private static String address;


    public Registers[] reg = new Registers[16];

    public static String fetch(int index)
    {
        return mmu.load(index);
    }

    public static String decode(String hex)
    {
        binary = new BigInteger(hex, 16).toString(2);
        opcode = binary.substring(2, 8);
        String instructionFormat = binary.substring(0, 2);

        switch (instructionFormat)
        {
            case "00":
            {
                arithmetic();
                break;
            }

            case "01":
            {
                conditionalImmediate();
                break;
            }
        }

        return "";
    }

    public static void execute(String binary)
    {

    }

    public static void arithmetic(){
        reg1 = binary.substring(8,12);
        reg2 = binary.substring(12, 16);
        reg3 = binary.substring(16,20);
    }

    public static void conditionalImmediate(){
        reg1 = binary.substring(8,12);
        reg2 = binary.substring(12, 16);
        address = binary.substring(16, 32);
    }

}