import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class loader {

    public static void load()
    {
        try {
            File f = new File("instructions.txt");
            Scanner scanner = new Scanner(f);

            String line = scanner.nextLine();
            int index = 0;
            while(line != null)
            {
                mmu.store(index, line.substring(2,11));
                index++;
                line = scanner.nextLine();
            }

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}