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

            ThreadForRead t1 = new ThreadForRead("Клиент", in); // поток приема сообщений от клиента
            t1.start();

            ThreadForWrite t2 = new ThreadForWrite(out, sc); // поток отправки сообщений
            t2.start();

            t2.interrupt();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}

