import java.io.File;
import org.pmw.tinylog.Logger;


public class LocalCommand extends AbstractCommand {

    LocalCommand() {
        this.currentDirectory = System.getProperty("user.dir");
    }

    public void cd(String path) {
        if (path.equals(".")) {
            return;
        }
        if (path.equals("..")) {
            StringBuilder sb  = new StringBuilder(this.currentDirectory);
            sb.reverse();
            int posn = sb.indexOf("/") + 1; // include the final slash
            sb.delete(0, posn);
            sb.reverse();
            currentDirectory = sb.toString();
            return;
        }

        File file = new File(path);

        boolean exists =      file.exists();      // Check if the file exists
        boolean isDirectory = file.isDirectory(); // Check if it's a directory
        boolean isFile =      file.isFile();      // Check if it's a regular file
        if (isFile) {
            Logger.info("bash: cd: temp.txt: Not a directory");
        } else if (isDirectory) {
            // if the System.getProperty("user.dir") is a substring of the path provided, it's an absolute path
            // we know it's a directory so just set the value to path
            if(path.contains(currentDirectory)) {
                currentDirectory = path;
            } else {  // it's a relative path and we can just extend it.
                StringBuilder sb = new StringBuilder(currentDirectory);
                if (currentDirectory.charAt(currentDirectory.length() - 1) == '/') {
                    sb.append(path);
                    currentDirectory = sb.toString();
                } else {
                    sb.append("/" + path);
                    currentDirectory = sb.toString();
                }
            }
        } else if(!exists) {
            Logger.error("bash: cd: foo: No such file or directory");
        } else {
            Logger.error("There has been an error processing your path.");
            System.exit(1);
        }
    }

    /**
     * Lists all of the files and directories at the level specified by the path parameter
     */
    @Override
    public void ls() {
            try {
                File dir = new File(this.currentDirectory + "/");
                for (String file : dir.list()) {
                    Logger.info(file);
                }
            } catch (Exception e) {
                Logger.error(e.getMessage());
            }
    }




    /**
     * Create a direcroty. All intermediate directories that don't exist will be created.
     *
     * @param name    String name of a new directory.
     */
    @Override
    public void mkdir(String name) {
        File dir = new File(this.currentDirectory + "/"+name);
        try {
            if (dir.mkdirs()) {
                Logger.info("Directory was created successfully");
            } else {
                Logger.warn("Failed trying to create the directory");
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
        Logger.info(this.currentDirectory);
        return this.currentDirectory;
    }

    /**
     * Run a shell command on local host.
     */
    public void cmd() {
        // ? not sure what this does.
    }

    /**
     * Rename a file.
     *
     * @param oldName  old name of a file
     * @param newName  new name of a file
     */
    @Override
    public void mv(String oldName, String newName) {
        if(oldName == null || newName == null) {
            Logger.error("Name is invalid");
            return;
        }

        File ofile = new File(this.currentDirectory+"/"+oldName);
        File nfile = new File(this.currentDirectory+"/"+newName);

        if(ofile.renameTo(nfile)){
            Logger.info("Successfully rename the file.");;
        }else{
            Logger.info("Failed to rename the file");
        }

    }
}
