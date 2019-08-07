public class Main {

    public static void main(String[] args) {

       //login code here
        /*
        System.out.println("Username: ");
        String username;//agileteam6
        String password;//agileteam6
        String hostname;//34.83.11.14
        Scanner sc = new Scanner(System.in);
        username = sc.nextLine();
        System.out.println("Password: ");
        password = sc.nextLine();
        System.out.println("Hostname: ");
        hostname = sc.nextLine();
        Client client = new Client(username,password,hostname);

*/


        Client client = new Client("agileteam6","agileteam6","34.83.11.14");
        client.connect();
        UserInterface ui = new UserInterface();
        ui.listenForInput(client);
    }
}
