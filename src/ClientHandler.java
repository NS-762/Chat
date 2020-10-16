import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {

    private Server server = null;
    private Socket socket = null;
    private int number;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scan;


    public ClientHandler(Server server, Socket socket, int number) {
        this.server = server;
        this.socket = socket;
        this.number = number;

        scan = new Scanner(System.in);

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (!socket.isClosed()) {
                        String str = in.readUTF();
                        if(str.equals("/end")) {
                            sendMessage(str);
                            server.deleteClient(this, number);
                            break;
                        }

                        server.acceptAndSendMessage("Клиент №"+ number + ": " + str); // перекинуть сообщение на сервер

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        /*server.acceptMessage("Клиент отключился");*/
                        in.close();
                        out.close();
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }




            }).start();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String str) { //отправка сообщения клиенту от сервера
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}