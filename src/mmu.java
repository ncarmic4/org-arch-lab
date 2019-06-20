public class mmu
{
    public String[] ram = new String[100];

    public String load(int address)
    {
        return ram[address].substring(0, 8);
    }

    public void store(int address, String data)
    {
        ram[address] = data;
    }
}