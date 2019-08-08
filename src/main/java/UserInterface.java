import java.util.Scanner;

public class UserInterface {
    //logging stuff?

    UserInterface() {

    }

    /**
     * Loops and listens for input. Once input has been read in,
     * it parses out the correct sftp command and calls the corresponding
     * method
     *
     * @param client takes in a client object that is created in main when a user connects
     */
    public void listen_for_input(Client client) {

        boolean loop_flag = true;
        while (loop_flag) {

            String input;
            Scanner sc = new Scanner(System.in);
            input = sc.nextLine();

            if (input.startsWith("put ")) {
                put_file(client, input);
            } else if (input.startsWith("get ")) {
                get_file(client, input);
            } else if (input.startsWith("mget ")) {
                get_multiple(client, input);
            } else if (input.startsWith("mput ")) {
                put_multiple(client, input);
            } else if (input.startsWith("rn ")) {
                rename_file(client, input);
            } else if (input.startsWith("help")) {
                System.out.println(help(input));
            } else if (input.equals("quit") || input.equals("exit")) {
                loop_flag = false;  //end loop
                client.logOff();
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }

    }


    /**
     * @param client Client object so we can call the correct method in the Client class
     * @param input  User input that we will parse
     */
    private void put_file(Client client, String input) {
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String src = file_paths.substring(0, index_of_space);
        String dst = file_paths.substring(index_of_space + 1);

        client.putFile(src, dst);
    }


    /**
     * @param client Client object so we can call the correct method in the Client class
     * @param input  User input that we will parse
     */
    private void get_file(Client client, String input) {
        String file_paths = input.substring(4);
        int index_of_space = file_paths.indexOf(" ");
        String src = file_paths.substring(0, index_of_space);
        String dst = file_paths.substring(index_of_space + 1);

        client.getFile(src, dst);
    }


    /**
     * @param client Client object that calls the get function in client class
     * @param input  User input that will be parsed
     *               example: mget source_path destination_path file1 file2 file3 ...
     *               -file paths should not end in "/"
     */
    public void get_multiple(Client client, String input) {
        String pathAndFiles = input.substring(5);           //creates substring of paths and files
        int index_of_space = pathAndFiles.indexOf(" ");
        String src = pathAndFiles.substring(0, index_of_space); //makes new string of first path

        pathAndFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length());
        index_of_space = pathAndFiles.indexOf(" ");
        String dst = pathAndFiles.substring(0, index_of_space);

        String mFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length()); //creates new string of file names
        index_of_space = mFiles.indexOf(" ");

        while (index_of_space != -1) {   //  loops and "gets" files

            String file_name = mFiles.substring(0, index_of_space);
            String temp = src + "/" + file_name;
            client.getFile(temp, dst);

            mFiles = mFiles.substring(index_of_space + 1, mFiles.length());
            index_of_space = mFiles.indexOf(" ");

            if (index_of_space == -1) {   //handles last file in string
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
    public void put_multiple(Client client, String input) {
        String pathAndFiles = input.substring(5);
        int index_of_space = pathAndFiles.indexOf(" ");
        String src = pathAndFiles.substring(0, index_of_space);

        pathAndFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length());
        index_of_space = pathAndFiles.indexOf(" ");
        String dst = pathAndFiles.substring(0, index_of_space);

        String mFiles = pathAndFiles.substring(index_of_space + 1, pathAndFiles.length());
        index_of_space = mFiles.indexOf(" ");

        while (index_of_space != -1) {

            String file_name = mFiles.substring(0, index_of_space);
            String temp = src + "/" + file_name;
            client.putFile(temp, dst);

            mFiles = mFiles.substring(index_of_space + 1, mFiles.length());
            index_of_space = mFiles.indexOf(" ");

            if (index_of_space == -1) {
                file_name = mFiles.substring(0, mFiles.length());
                temp = src + "/" + file_name;
                client.putFile(temp, dst);
            }
        }
    }

    /**
     * @param client Client object that calls the get function in client class
     * @param input  User input that will be parsed
     *               example: rn old_name new_name
     */
    public void rename_file(Client client, String input) {
        String file_paths = input.substring(3);
        int index_of_space = file_paths.indexOf(" ");
        String old_name = file_paths.substring(0, index_of_space);
        String new_name = file_paths.substring(index_of_space + 1);

        client.rename_file(old_name, new_name);

    }

    /**
     * Method for communicating helpful info to the user
     * @param input
     *        The input to inquire on.
     */
    public String help(String input) {
        final String options = "Available options for input:\n" +
                "put - transfers local file to remote server\n" +
                "mput - retrieves multiple files from the remote server\n" +
                "get - retrieves file from the remote server\n" +
                "mget - transfers multiple files to remote server\n" +
                //"rn - rename a file on the remote server\n" +
                "ls - list files on remote server\n" +
                "lls - list files on local machine\n" +
                "cd - change directory on remote server\n" +
                "pwd - print working directory of remote server\n" +
                "lpwd - print working directory of local machine\n" +
                "mv - move or rename a file or directory on the remote server\n" +
                "lmv - move or rename a file or directory on your local machine\n" +
                "mkdir - create new directory on remote server\n" +
                "lmkdir - create new directory on local machine\n" +
                "rmdir - delete directory on remote server\n" +
                "rm - delete file on remote server\n" +
                "chmod - change permissions on remote server\n" +
                "help <cmd> - command specific help\n" +
                "exit - exit the application";

        final String putHelp = "put - transfers local file to remote server\n" +
                "put <local file path> <remote path>\n" +
                "ex. > put file.txt .";

        final String getHelp = "get - retrieves file from the remote server\n" +
                "get <remote file path> <local path>\n" +
                "ex. > get file.txt .";

        final String mputHelp = "mput - retrieves multiple files from the remote server\n" +
                "mput <local dir path> <remote path> <file1> <file2> ...\n" +
                "Local and remote paths should not contain '/' at the end\n" +
                "ex. > mput . . file1.txt file2.txt";

        final String mgetHelp = "mget - transfers multiple files to remote server\n" +
                "mget <remote dir path> <local path> <file1> <file2> ...\n" +
                "Local and remote paths should not contain '/' at the end\n" +
                "ex. > mget . . file1.txt file2.txt";

//        final String rnHelp = "rn - rename a file on the remote server\n" +
//                "rn <current name> <new name>\n" +
//                "ex. > rn old.txt new.txt";

        final String lsHelp = "ls - list files on remote server\n" +
                "ls\n" +
                "ex. > ls";

        final String llsHelp = "lls - list files on local machine\n" +
                "lls\n" +
                "ex. > lls";

        final String cdHelp = "cd - change directory on remote server\n" +
                "cd <path>\n" +
                "ex. > cd Directory/";

        final String pwdHelp = "pwd - print working directory of remote server\n" +
                "pwd\n" +
                "ex. > pwd";

        final String lpwdHelp = "lpwd - print working directory of local machine\n" +
                "lpwd\n" +
                "ex. > lpwd";

        final String mvHelp = "mv - move or rename a file or directory on the remote server\n" +
                "mv <current path> <new path>\n" +
                "ex. > mv file.txt Directory/newName.txt";

        final String lmvHelp = "lmv - move or rename a file or directory on your local machine\n" +
                "lmv <current path> <new path>\n" +
                "ex. > lmv file.txt Directory/newName.txt";

        final String mkdirHelp = "mkdir - create new directory on remote server\n" +
                "mkdir <new directory path>\n" +
                "ex. > mkdir NewDir";

        final String lmkdirHelp = "lmkdir - create new directory on local machine\n" +
                "lmkdir <new directory path>\n" +
                "ex. > lmkdir NewDir";

        final String rmdirHelp = "rmdir - delete directory on remote server\n" +
                "rmdir [-r] <directory path>\n" +
                "Use the [-r] option to delete a directory and all its contents\n" +
                "ex. > rmdir EmptyDirectory\n" +
                "ex. > rmdir -r NotEmptyDirectory";

        final String rmHelp = "rm - delete file on remote server\n" +
                "rm <file path>\n" +
                "ex. > rm file.txt";

        final String chmodHelp = "chmod - change permissions on remote server\n" +
                "chmod <file pate> <permissions>\n" +
                "Permissions are expected to be a standard octal permissions value\n" +
                "ex. > chmod file.txt 777";

        String[] helpCases = input.split("\\s+");

        String returnString = "";

        if (helpCases.length == 1) {
            returnString = options;
        } else {
            switch (helpCases[1]) {
                case "put":
                    returnString = putHelp;
                    break;
                case "get":
                    returnString = getHelp;
                    break;
                case "mget":
                    returnString = mgetHelp;
                    break;
                case "mput":
                    returnString = mputHelp;
                    break;
//                case "rn":
//                    returnString = rnHelp;
//                    break;
                case "ls":
                    returnString = lsHelp;
                    break;
                case "lls":
                    returnString = llsHelp;
                    break;
                case "cd":
                    returnString = cdHelp;
                    break;
                case "pwd":
                    returnString = pwdHelp;
                    break;
                case "lpwd":
                    returnString = lpwdHelp;
                    break;
                case "mv":
                    returnString = mvHelp;
                    break;
                case "lmv":
                    returnString = lmvHelp;
                    break;
                case "mkdir":
                    returnString = mkdirHelp;
                    break;
                case "lmkdir":
                    returnString = lmkdirHelp;
                    break;
                case "rmdir":
                    returnString = rmdirHelp;
                    break;
                case "rm":
                    returnString = rmHelp;
                    break;
                case "chmod":
                    returnString = chmodHelp;
                    break;
                default:
                    returnString = "Invalid input\n" + options;
            }
        }
        return returnString;
    }

}


