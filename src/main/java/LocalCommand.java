import java.io.File;


public class LocalCommand extends AbstractCommand {

    public LocalCommand()
    {

    }

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
    @Override
    public void ls(String path) {
        if(path != null) {
            try {
                File dir = new File(this.currentWorkingDirectory+"/"+path);
                for (String file : dir.list()) {
                    System.out.println(file);
                }
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Create a direcroty. All intermediate directories that don't exist will be created.
     *
     * @param name    String name of a new directory.
     */
    @Override
    public void mkdir(String name) {
        File dir = new File(this.currentWorkingDirectory + "/"+name);
        try {
            if (dir.mkdirs()) {
                System.out.println("Directory was created successfully");
            } else {
                System.out.println("Failed trying to create the directory");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieve current directory;
     * @return
     */
    @Override
    public String pwd() {
        System.out.println(this.currentDirectory);
        return this.currentDirectory;
    }

    public void cmd() {
        // ? not sure what this does.
    }
}
