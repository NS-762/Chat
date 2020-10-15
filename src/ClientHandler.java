import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {

    private Server server = null;
    private Socket socket = null;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scan;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        scan = new Scanner(System.in);

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if(str.equals("/end")) {
                            sendMessage(str);
                            break;
                        }

                        server.acceptMessage("Клиент говорит: " + str); // перекинуть сообщение на сервер

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        server.acceptMessage("Клиент отключился");
                        in.close();
                        out.close();
                        socket.close();
                        server.deleteClient(this);
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