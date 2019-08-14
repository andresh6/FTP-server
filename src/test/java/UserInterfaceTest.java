import com.jcraft.jsch.JSch;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class UserInterfaceTest {
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private User user = new User("agileteam6", "agileteam6", "34.83.11.14", "22");
    private Client client = new Client(user);
    private UserInterface ui = new UserInterface();


    @Test
    public void testputfile() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if (!client.channelSftp.isConnected()) {
            fail("Could not connect to server");
        }
        client.putFile("/home/andres/Desktop/TestFolder/testing.txt", "/home/agileteam6");//perfect
        client.listRemoteFiles();
        assertThat(outContent.toString(), containsString("testing.txt"));
        System.setOut(originalOut);
    }

    @Test
    public void testputmultiple() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if (!client.channelSftp.isConnected()) {
            fail("Could not connect to server");
        }
        ui.put_multiple(client, "mput /home/andres/Desktop/TestFolder /home/agileteam6 testing.txt testing2.txt testing3.txt");//perfect
        client.listRemoteFiles();
        assertThat(outContent.toString(), containsString("testing.txt"));
        assertThat(outContent.toString(), containsString("testing2.txt"));
        assertThat(outContent.toString(), containsString("testing3.txt"));

        System.setOut(originalOut);
    }

    @Test
    public void testgetfile(){
        System.setOut(new PrintStream(outContent));
        client.connect();
        if (!client.channelSftp.isConnected()) {
            fail("Could not connect to server");
        }
    }

    @Test
    public void testremovefile() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if (!client.channelSftp.isConnected()) {
            fail("Could not connect to server");
        }
        client.rename_file("testing.txt", "testing1.txt");
        client.listRemoteFiles();
        assertThat(outContent.toString(), containsString("testing1.txt"));
        System.setOut(originalOut);
    }

    @Test
    public void testHelpInterface01() {
        String content = ui.help("help");
        assertThat(content, containsString("Available options for input"));
    }
    @Test
    public void testHelpInterface02() {
        String content = ui.help("help put");
        assertThat(content, containsString("put <local file path> <remote path>"));
    }
    @Test
    public void testHelpInterface03() {
        String content = ui.help("help mput");
        assertThat(content, containsString("mput <local dir path> <remote path> <file1> <file2> .."));
    }
    @Test
    public void testHelpInterface04() {
        String content = ui.help("help get");
        assertThat(content, containsString("get <remote file path> <local path>"));
    }
    @Test
    public void testHelpInterface05() {
        String content = ui.help("help mget");
        assertThat(content, containsString("mget <remote dir path> <local path> <file1> <file2> .."));
    }
//    @Test
//    public void testHelpInterface06() {
//        String content = ui.help("help rn");
//        assertThat(content, containsString("rn - rename a file on the remote server"));
//    }
    @Test
    public void testHelpInterface07() {
        String content = ui.help("help ls");
        assertThat(content, containsString("ls - list files on remote server"));
    }
    @Test
    public void testHelpInterface08() {
        String content = ui.help("help lls");
        assertThat(content, containsString("lls - list files on local machine"));
    }
    @Test
    public void testHelpInterface09() {
        String content = ui.help("help cd");
        assertThat(content, containsString("change directory on remote server"));
    }
    @Test
    public void testHelpInterface10() {
        String content = ui.help("help pwd");
        assertThat(content, containsString("print working directory of remote server"));
    }
    @Test
    public void testHelpInterface11() {
        String content = ui.help("help lpwd");
        assertThat(content, containsString("print working directory of local machine"));
    }
    @Test
    public void testHelpInterface12() {
        String content = ui.help("help mv");
        assertThat(content, containsString("move or rename a file or directory on the remote server"));
    }
    @Test
    public void testHelpInterface13() {
        String content = ui.help("help lmv");
        assertThat(content, containsString("move or rename a file or directory on your local machine"));
    }
    @Test
    public void testHelpInterface14() {
        String content = ui.help("help mkdir");
        assertThat(content, containsString("create new directory on remote server"));
    }
    @Test
    public void testHelpInterface15() {
        String content = ui.help("help lmkdir");
        assertThat(content, containsString("create new directory on local machine"));
    }
    @Test
    public void testHelpInterface16() {
        String content = ui.help("help rmdir");
        assertThat(content, containsString("delete directory on remote server"));
    }
    @Test
    public void testHelpInterface17() {
        String content = ui.help("help rm");
        assertThat(content, containsString("delete file on remote server"));
    }
    @Test
    public void testHelpInterface18() {
        String content = ui.help("help chmod");
        assertThat(content, containsString("change permissions on remote server"));
    }
    @Test
    public void testHelpInterface19() {
        String content = ui.help("help failure");
        assertThat(content, containsString("Invalid input"));
    }
}
