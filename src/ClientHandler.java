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
    private String login;
    public String nickname;


    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;

        scan = new Scanner(System.in);

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true) { //цикл аутентификации
                String str = in.readUTF();
                if(str.startsWith("/auth")) {
                    String[] token = str.split(" ");
                    String nick = server.getAuthService().getNickByLoginAndPassword(token[1], token[2]);
                    if (nick != null) {
                        sendMessageClient("/authok "+ nick);
                        nickname = nick;
                        login = token[1];
                        server.subscribe(this); //добавить клиента в список
                        server.acceptAndSendMessage(nickname + " подключился к чату"); // перекинуть сообщение на сервер
                        break;
                    } else {
                        sendMessageClient("/error");
                    }
                }
            }



            new Thread(() -> {
                try {
                    while (!socket.isClosed()) {
                        String str = in.readUTF();
                        if(str.equals("/end")) {
                            sendMessageClient(str); //отправить клиенту
                            break;
                        }
                        if (str.startsWith("/w")) { //для личных сообщений
                            String[] privateMessage = str.split(" ", 3); //делим на 3 части
                            server.sendPrivateMessage(privateMessage[1], nickname + "(лс): " + privateMessage[2]);
                        } else { //для отправки всем пользователям
                            server.acceptAndSendMessage(nickname + ": " + str); // перекинуть сообщение на сервер
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        server.unsubscribe(this, nickname);
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

    public void sendMessageClient(String str) { //отправка сообщения клиенту от сервера
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}