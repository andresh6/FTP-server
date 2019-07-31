import java.util.Scanner;

public class UserInterface {
    //logging stuff?

    UserInterface(){

    }

    /**
     * Loops and listens for input. Once input has been read in,
     * it parses out the correct sftp command and calls the corresponding
     * method
     *
     * @param client
     * takes in a client object that is created in main when a user connects
     */
    public void listenForInput(Client client){

        boolean loop_flag = true;
        while(loop_flag) {

            String input;
            Scanner sc = new Scanner(System.in);
            input = sc.nextLine();

            if(input.startsWith("put ")){
                putFile(client,input);
            }
            else if(input.startsWith("get ")){
                getFile(client,input);
            }
            else if(input.equals("quit") || input.equals("exit")){
                loop_flag = false;  //end loop
                System.out.println("quiting session...");
            }
            else{
               System.out.println("Invalid command. Try again.");
            }
        }
    }


    /**
     *
     * @param client
     * Client object so we can call the correct method in the Client class
     * @param input
     * User input that we will parse
     */
    private void putFile(Client client, String input){
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String src = file_paths.substring(0,index_of_space);
        String dst = file_paths.substring(index_of_space + 1);

        client.putFile(src,dst);
    }


    /**
     *
     * @param client
     * Client object so we can call the correct method in the Client class
     * @param input
     * User input that we will parse
     */
    private void getFile(Client client, String input){
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String src = file_paths.substring(0,index_of_space);
        String dst = file_paths.substring(index_of_space + 1);

        client.getFile(src,dst);
    }
}


