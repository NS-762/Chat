import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ThreadForWrite extends Thread {

    private DataOutputStream out;
    private Scanner sc;

    public ThreadForWrite(DataOutputStream out, Scanner sc) {
        this.out = out;
        this.sc = sc;
    }

    public void run() {
        while (true) {
            String str;
            str = sc.nextLine();


            try {
                out.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


}
