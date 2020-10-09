import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static ServerSocket server = null;
    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            server = new ServerSocket(8189); // создание сервера
            System.out.println("Сервер запустился");
            socket = server.accept(); // в сокет записываем подключившегося пользователя
            System.out.println("Клиент подключился");

            in = new DataInputStream(socket.getInputStream()); // то, что приходит на сервер (считывание)
            out = new DataOutputStream(socket.getOutputStream()); // то, что отправляет сервер


                /*Scanner in = new Scanner(socket.getInputStream()); //слушать входящий поток из сокета
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // то, что печатает сервер*/

            new Thread(() -> { //поток для считывания сообщения от клиента
                try {
                    while (true) {
                        String str = in.readUTF(); // считать сообщение от клиента
                        if (str.equals("/end")) {
                            break;
                        }
                        System.out.println("Клиент говорит: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


            new Thread(() -> { //поток для отправки сообщения клиенту
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

