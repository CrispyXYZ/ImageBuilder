import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        System.out.print("port: ");
        Scanner scanner = new Scanner(System.in);
        String unparsed = scanner.nextLine();
        int port;
        try {
            port = Integer.parseInt(unparsed);
        } catch (NumberFormatException e){
            port = 23333;
        }
        scanner.close();
        MyWSS wss = new MyWSS(port);
        wss.start();
    }
}
