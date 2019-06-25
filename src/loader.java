import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class loader
{

    public static void main(String[] args) {
        mmu mmu = new mmu();
        load(mmu);
        for (int i = 0; i < mmu.ram.length; i++) {
            System.out.println(mmu.ram[i]);
        }
    }

    public static void load(mmu memMgr) {
        try {
            File f = new File("src/instructions.txt");
            Scanner scanner = new Scanner(f);

            int index = 0;
            while (scanner.hasNext()) {
                String line = scanner.next();
                memMgr.store(index, line.substring(2, 10));
                index++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}