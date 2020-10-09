import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {

    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        try {

            socket = new Socket("localHost", 8189);

            in = new DataInputStream(socket.getInputStream()); // то, что приходит клиенту (считывание)
            out = new DataOutputStream(socket.getOutputStream()); // то, что отправляет клиент


            ThreadForRead t1 = new ThreadForRead("Сервер", in); //поток приема сообщений от сервера
            t1.start();

            ThreadForWrite t2 = new ThreadForWrite(out, sc);  // поток отправки сообщений
            t2.start();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}