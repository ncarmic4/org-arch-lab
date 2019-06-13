public class mmu {
    private static String[] ram = new String[67];

    public static String load(int address){
        return ram[address].substring(0,8);
    }

    public static void store(int address, String data){
        ram[address] = data;
    }
}