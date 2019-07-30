/**
 * command list
 */
import java.

public abstract class AbstractCommand {
    String base_dir;

    public AbstractCommand() {
        this.base_dir = System.getProperty("user.dir");
    }

    abstract void cd(String path);
    abstract void ls(String path);
    abstract void mkdir(String name);
    abstract void pwd();
    abstract void cmd();
}
