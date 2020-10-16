import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Socket socket = null;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Scanner scan;

    public static void main(String[] args) {

        try {
            socket = new Socket("localHost", 8191);
            scan = new Scanner(System.in);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Произошло подключение к сети");

            new Thread(() -> { //отправка сообщений в клиентхендлер


                    try {
                        while (true) {
                            String str = scan.nextLine();
                            out.writeUTF(str); //прекинуть в клиентхендлер
                        }
                    } catch (IOException e) {
                        System.out.println("Невозможно отправить сообщение");
                    } finally {
                        try {
                            in.close();
                            out.close(); //он в отправке не обрабатывается так
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

            }).start();


            new Thread (() -> { //для приема сообщений
                    try {
                        while (true) {
                            String str = in.readUTF();
                            if (str.equals("/end")) {
                                System.out.println("Отключение от сервера");
                                out.writeUTF(str); //чтобы вырубился и на клиентхендлере
                                break;
                            }
                            System.out.println(str);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        try {
                            in.close();
                            out.close();
                            socket.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}