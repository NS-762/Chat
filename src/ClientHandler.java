import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {

    private static ServerSocket server = null;
    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            socket = new Socket("localHost", 8189);

            in = new DataInputStream(socket.getInputStream()); // то, что приходит клиенту (считывание)
            out = new DataOutputStream(socket.getOutputStream()); // то, что отправляет клиент


            new Thread(() -> { //поток для считывания сообщения от сервера
                try {
                    while (true) {
                        String str = in.readUTF(); // считать сообщение от сервера
                        if (str.equals("/end")) {
                            break;
                        }
                        System.out.println("Сервер говорит: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


            new Thread(() -> {
                while (true) {
                    String str;
                    str = sc.nextLine();
                    if (str.equals("/end")) {
                        break;
                    }
                    try {
                        out.writeUTF(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

