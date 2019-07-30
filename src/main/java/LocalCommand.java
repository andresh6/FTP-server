import java.io.File;


public class LocalCommand extends AbstractCommand {

    public void cd(String path) // probably needs two parameters,
    {                           // one for the directory to move into, another for the path
                                // should probably just return the path that the user is now on

        if (path.equals(".")) {
            return;
        }
        if (path.equals("..")) {

        }
        // check if path is `.`
            // return

        // check if path `..`
            // reverse the string and dropWhile char != `/`

        // check to see if the `path` is a directory
        // if it is a file not directory
            // bash: cd: temp.txt: Not a directory
        // else if it is non existent
            // bash: cd: foo: No such file or directory
        // else
            // confirm it has a leading forward slash
            // append the path to the Client.currentWorkingDirectory attribute
    }

    /**
     * Lists all of the files and directories at the level specified by the path parameter
     * @param path The full path that the user is currently on
     */
    public void ls(String path) {
        if(path != null) {
            try {
                File dir = new File(path);
                for (String file : dir.list()) {
                    System.out.println(file);
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void mkdir(String name) {
        // needs to take another parameter, the path that the user is looking to mkdir in
        // name of the directory
    }

    public void pwd() {
        // uhm, we'd need something to track the current working directory throughout.
        // which would actually benefit the previous commands.
        // can't put it in the interface, needs to be a class member because it's non-static
    }

    public void cmd() {
        // ? not sure what this does.
    }
}
