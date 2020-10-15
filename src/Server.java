import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private ServerSocket server = null;
    private Socket socket = null;
    private Scanner scan;
    private String str;

    public Server() {

        try {
            server = new ServerSocket(8191); // создание сервера
            System.out.println("Сервер запустился");

            socket = server.accept(); // в сокет записываем подключившегося пользователя
            System.out.println("Клиент подключился");

            ClientHandler client1 = new ClientHandler(this, socket);

            scan = new Scanner(System.in);


            new Thread(() -> { //поток для отправки сообщений на клиентхендлер
                while (true) {
                    str = scan.nextLine();
                    if (client1.clientIsOff()) {
                        System.out.println("Невозможно отправить сообщение данному клиенту");
                        break;
                    }
                    client1.sendMessage(str); //прекинуть в клиентхендлер
                }
            }).start();


        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void acceptMessage(String str) { //принять сообщение
        System.out.println(str);
    }

}