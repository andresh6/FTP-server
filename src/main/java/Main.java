public class Main {

    public static void main(String[] args) {

        //login code here

        Client client = new Client("agileteam6","agileteam6","34.83.11.14");
        client.connect();
        UserInterface ui = new UserInterface();
        ui.listenForInput(client);
    }
}
