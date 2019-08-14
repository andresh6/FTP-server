/**
 * command list
 */

public abstract class AbstractCommand {

    protected final String currentWorkingDirectory;
    protected String currentDirectory;

    public AbstractCommand() {
        this.currentWorkingDirectory = System.getProperty("user.dir");
        this.currentDirectory = currentWorkingDirectory;
    }

    public String getcwd() {
        return currentDirectory;
    }

    abstract void cd(String path);
    abstract void ls();
    abstract void mkdir(String name);
    abstract String pwd();
    abstract void mv(String oldName, String newName);
    abstract void cmd();
}
