import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc;
        String username;//agileteam6
        String password;//agileteam6
        String hostname;//34.83.11.14
        String port;

        if (args.length != 3) {
            System.out.println("Please enter SFTP server information:");
            System.out.print("\nHostname: ");
            sc = new Scanner(System.in);
            hostname = sc.nextLine();
            System.out.print("\nPort: ");
            port = sc.nextLine();

        } else {
            hostname = args[1];
            port = args[2];
        }
       //login code here
        System.out.println("Please enter user login information:");
        System.out.print("\nUsername: ");
        sc = new Scanner(System.in);
        username = sc.nextLine();
        System.out.print("\nPassword: ");
        password = sc.nextLine();
        Client client = new Client(username,password,hostname, port);



        //Client client = new Client("agileteam6","agileteam6","34.83.11.14", "22");
        client.connect();
        UserInterface ui = new UserInterface();
        LocalCommand lc = new LocalCommand();
        ui.listenForInput(client, lc);
    }
}
