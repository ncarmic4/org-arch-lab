import java.math.BigInteger;

class Registers {
    public int data;
    public String address;
}

public class cpu {
    public Registers[] reg = new Registers[16];

    public String fetch(String address)
    {
        int addressIndex = Integer.parseInt(address,16) / 4;
        return mmu.load(addressIndex);
    }

    public String decode(String hex)
    {
        return new BigInteger(hex,16).toString(2);
    }

    public static void execute(String binary)
    {
        String instructionFormat = binary.substring(0,2);
        switch (instructionFormat)
        {
            case "00": {

            }
        }
    }
}
