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

    private User user = new User("agileteam6", "agileteam6", "34.83.11.14");
    private Client client = new Client(user);
    private UserInterface ui = new UserInterface();


    @Test
    public void testputfile() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if (!client.channelSftp.isConnected()) {
            fail("Could not connect to server");
        }
<<<<<<< HEAD
        client.put_file("/home/andres/Desktop/TestFolder/testing.txt", "/home/agileteam6");
=======
        client.put_file("/home/andres/Desktop/TestFolder/testing.txt", "/home/agileteam6");//perfect
>>>>>>> master
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
<<<<<<< HEAD
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
=======
>>>>>>> master
}
