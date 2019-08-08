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
    public void listenForInput(Client client, LocalCommand lc){

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
            else if(input.startsWith("mget ")){
                get_multiple(client, input);
            }
            else if(input.startsWith("mput ")){
                put_multiple(client,input);
            }
            else if(input.startsWith("ls")){
                listRemoteFiles(client);
            }
            else if(input.startsWith("rm ")){
                deleteRemoteFiles(client, input);
            }
            else if(input.startsWith("mkdir ")){
                createRemoteDirectory(client, input);
            }
            else if(input.startsWith("rmdir -r ")){
                recursiveRemoveRemoteDirectory(client, input);
            }
            else if(input.startsWith("rmdir ")){
                removeRemoteDirectory(client, input);
            }
            else if(input.startsWith("chmod ")){
                changePermissions(client,input);
            }
            else if(input.startsWith("mv ")){
                rename_file(client, input);
            }
            else if(input.startsWith("cd ")){
                changeDir(client, input);
            }
            else if(input.startsWith("pwd")){
                client.pwd();
            }
            else if(input.startsWith("lmkdir ")){
                createLocalDirectory(lc, input);
            }
            else if(input.startsWith("lpwd")){
                printLocalWorkingDir(lc);
            }
            else if(input.startsWith("lmv")){
                renameLocalFile(lc, input);
            }
            else if(input.startsWith("lls")){
                listLocalFiles(lc);
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
            client.getFile(temp, dst);

            mFiles = mFiles.substring(index_of_space + 1, mFiles.length());
            index_of_space = mFiles.indexOf(" ");

            if(index_of_space == -1){   //handles last file in string
                file_name = mFiles.substring(0, mFiles.length());
                temp = src + "/" + file_name;
                client.getFile(temp, dst);
            }
        }
    }


    /**

     * follows the same logic as the get_multiple function above
     * example: mput sourcePath destinationPath file1 file2 file3...
     * -paths should not contain '/' at the very end
     */
    public void put_multiple(Client client, String input){
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
            client.putFile(temp, dst);

            mFiles = mFiles.substring(index_of_space + 1, mFiles.length());
            index_of_space = mFiles.indexOf(" ");

            if(index_of_space == -1){
                file_name = mFiles.substring(0, mFiles.length());
                temp = src + "/" + file_name;
                client.putFile(temp, dst);
            }
        }
    }
    public void changeDir(Client client,String input){
        String dirName = input.substring(3);
        client.changeDir(dirName);
    }

    public void listRemoteFiles(Client client){
        client.listRemoteFiles();
    }

    public void deleteRemoteFiles(Client client, String input){
        String filename = input.substring(3);
        client.deleteRemoteFiles(filename);
    }
    public void createRemoteDirectory(Client client, String input){
        String dirname = input.substring(6);
        client.createRemoteDirectory(dirname);
    }
    public void createLocalDirectory(LocalCommand lc, String input){
        String dirname = input.substring(7);
        lc.mkdir(dirname);
    }
    public void removeRemoteDirectory(Client client, String input){
        String dirname = input.substring(6);
        client.removeRemoteDirectory(dirname);
    }
    public void recursiveRemoveRemoteDirectory(Client client, String input){
        String dirname = input.substring(9);
        client.recursiveRemoveRemoteDir(dirname);
    }
    public void changePermissions(Client client, String input){
        String pathPermissions= input.substring(6);
        int index_of_space = pathPermissions.indexOf(" ");
        String path = pathPermissions.substring(0,index_of_space);
        String permissions = pathPermissions.substring(index_of_space + 1);

        client.changePermissions(path, permissions);

    }
    public void rename_file(Client client, String input) {
        String file_paths = input.substring(3);
        int index_of_space = file_paths.indexOf(" ");
        String old_name = file_paths.substring(0, index_of_space);
        String new_name = file_paths.substring(index_of_space + 1);

        client.rename_file(old_name, new_name);

    }
    public void printLocalWorkingDir(LocalCommand lc){
        lc.pwd();
    }
    public void renameLocalFile(LocalCommand lc, String input){
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String old_name = file_paths.substring(0, index_of_space);
        String new_name = file_paths.substring(index_of_space + 1);

        lc.mv(old_name, new_name);
    }
    public void listLocalFiles(LocalCommand lc){
        lc.ls();
    }
}


