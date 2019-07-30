/**
 * command list
 */

public abstract class AbstractCommand {

    String currentWorkingDirectory;

    public AbstractCommand() {
        this.currentWorkingDirectory = System.getProperty("user.dir");
    }

    abstract void cd(String path);
    abstract void ls(String path);
    abstract void mkdir(String name);
    abstract void pwd();
    abstract void cmd();
}
