/**
 * command list
 */
public interface Command {
    void cd(String path);
    void ls(String path);
    void mkdir(String name);
    void pwd();
    void cmd();
}
