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
    private static boolean authenticated;
    private static String nickname;

    public static void main(String[] args) {

        authenticated = false; //изначально пользолватель не аутентифицирован
        try {
            socket = new Socket("localHost", 8191);
            scan = new Scanner(System.in);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            System.out.println("Введите логин и пароль через пробел:");

            while (true) { //цикл аутентификации
                String str = scan.nextLine();
                out.writeUTF("/auth " + str); //прекинуть в клиентхендлер логин и пароль
                System.out.println("Запрос отправлен на клиентхендлера!");
                str = in.readUTF(); //ждать ответа от клинтхенедлера и сервера
                if (str.startsWith("/authok")) { //если пришло одобрение, то выходим из этого цикла: аутентиф. успешна
                    nickname = str.split(" ")[1]; //взять то, что после /authok
                    break;
                } else {
                    System.out.println("Неверный логин или пароль. Повторите попытку:");
                }
            }

            new Thread(() -> { //отправка сообщений в клиентхендлер
                    try {
                        while (true) {
                            String str = scan.nextLine();
                            if (!str.equals("")) {
                                out.writeUTF(str); //прекинуть в клиентхендлер
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Невозможно отправить сообщение");
                    } finally {
                        try {
                            in.close();
                            out.close();
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