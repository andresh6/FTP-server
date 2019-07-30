/**
 * command list
 */
public abstract class AbstractCommand {
    abstract void cd(String path);
    abstract void ls(String path);
    abstract void mkdir(String name);
    abstract void pwd();
    abstract void cmd();
}
