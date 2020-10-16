import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {

    private ServerSocket server = null;
    private Socket socket = null;
    private Scanner scan;
    private String str;
    private int numberOfClients;
    private Vector<ClientHandler> clients;

    public Server() {

        try {
            clients = new Vector<>();
            scan = new Scanner(System.in);
            server = new ServerSocket(8191); // создание сервера
            System.out.println("Сервер запустился");


            /*new Thread(() -> { //в потоке не работает прием клиентов

                int i = 1;
                while (true) {
                    try {
                        socket = server.accept(); // в сокет записываем подключившегося пользователя
                    } catch (IOException e) {
                        System.out.println("Вот проблема");
                        e.printStackTrace();
                    }
                    System.out.printf("Клиент №%d подключился", i);
                    clients.add(new ClientHandler(this, socket, i));
                }

            }).start();*/


            new Thread(() -> { //поток для отправки сообщений на клиентхендлеры
                while (true) {
                    str = scan.nextLine();

                    for (ClientHandler c : clients) {
                        if (!str.equals("/end")) { //чтоб правильно считалась команда на клиентхендлере
                            c.sendMessage("Сервер пишет: " + str);
                        } else {
                            c.sendMessage( str);
                        }

                    }

                }
            }).start();

            while (true) { //для добавления клиентов
                try {
                    socket = server.accept(); // в сокет записываем подключившегося пользователя
                    numberOfClients++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.printf("Клиент №%d подключился\n", numberOfClients);
                clients.add(new ClientHandler(this, socket, numberOfClients));

            }




        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public void acceptAndSendMessage(String str) { //принять сообщение, напечатать и отправить всем клиентам
        System.out.println(str);
        for (ClientHandler c : clients) {
            c.sendMessage(str);
        }

    }

    public void deleteClient(ClientHandler clientHandler, int number) {
        clients.remove(clientHandler);
        System.out.printf("Клиент №%d удален\n", number);
    }

}