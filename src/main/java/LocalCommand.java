import java.io.File;


public class LocalCommand implements Command {

    public void cd(String path)
    {

    }

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

    }

    public void pwd() {

    }

    public void cmd() {

    }
}
