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
    public void listen_for_input(Client client){

        boolean loop_flag = true;
        while(loop_flag) {

            String input;
            Scanner sc = new Scanner(System.in);
            input = sc.nextLine();

            if(input.startsWith("put ")){
                put_file(client,input);
            }
            else if(input.startsWith("get ")){
                get_file(client,input);
            }
            else if(input.startsWith("mget ")){
                get_multiple(client, input);
            }
            else if(input.startsWith("mput ")){
                put_multiple(client,input);
            }
            else if(input.equals("quit") || input.equals("exit")){
                loop_flag = false;  //end loop
                client.logOff();
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
    private void put_file(Client client, String input){
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String src = file_paths.substring(0,index_of_space);
        String dst = file_paths.substring(index_of_space + 1);

        client.put_file(src,dst);
    }


    /**
     *
     * @param client
     * Client object so we can call the correct method in the Client class
     * @param input
     * User input that we will parse
     */
    private void get_file(Client client, String input){
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String src = file_paths.substring(0,index_of_space);
        String dst = file_paths.substring(index_of_space + 1);

        client.get_file(src,dst);
    }


    /**
     *
     * @param client
     * Client object that calls the get function in client class
     * @param input
     * User input that will be parsed
     * example: mget source_path destination_path file1 file2 file3 ...
     * -file paths should not end in "/"
     */
    private void get_multiple(Client client, String input){
        String pathAndFiles = input.substring(5);           //creates substring of paths and files
        int index_of_space = pathAndFiles.indexOf(" ");
        String src = pathAndFiles.substring(0, index_of_space); //makes new string of first path

        pathAndFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length());
        index_of_space = pathAndFiles.indexOf(" ");
        String dst = pathAndFiles.substring(0, index_of_space);

        String mFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length()); //creates new string of file names
        index_of_space = mFiles.indexOf(" ");

        while(index_of_space != -1 ){   //  loops and "gets" files

            String file_name = mFiles.substring(0, index_of_space);
            String temp = src + "/" + file_name;
            client.get_file(temp, dst);

            mFiles = mFiles.substring(index_of_space + 1, mFiles.length());
            index_of_space = mFiles.indexOf(" ");

            if(index_of_space == -1){   //handles last file in string
                file_name = mFiles.substring(0, mFiles.length());
                temp = src + "/" + file_name;
                client.get_file(temp, dst);
            }
        }
    }


    /**
     *
     * follows the same logic as the get_multiple function above
     * example: mput sourcePath destinationPath file1 file2 file3...
     * -paths should not contain '/' at the very end
     */
    private void put_multiple(Client client, String input){
        String pathAndFiles = input.substring(5);
        int index_of_space = pathAndFiles.indexOf(" ");
        String src = pathAndFiles.substring(0, index_of_space);

        pathAndFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length());
        index_of_space = pathAndFiles.indexOf(" ");
        String dst = pathAndFiles.substring(0, index_of_space);

        String mFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length());
        index_of_space = mFiles.indexOf(" ");

        while(index_of_space != -1 ){

            String file_name = mFiles.substring(0, index_of_space);
            String temp = src + "/" + file_name;
            client.put_file(temp, dst);

            mFiles = mFiles.substring(index_of_space + 1, mFiles.length());
            index_of_space = mFiles.indexOf(" ");

            if(index_of_space == -1){
                file_name = mFiles.substring(0, mFiles.length());
                temp = src + "/" + file_name;
                client.put_file(temp, dst);
            }
        }
    }
}


