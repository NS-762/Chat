import java.io.DataInputStream;
import java.io.IOException;

public class ThreadForRead extends Thread {

    private String sender; // от кого принимается сообщение: клиент или сервер
    private DataInputStream in;

    public ThreadForRead(String sender, DataInputStream in) {
        this.sender = sender;
        this.in = in;
    }

    public void run() {
        try {
            while (true) {
                String str = in.readUTF(); // считать сообщение от клиента
                System.out.println(sender + " говорит: " + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
