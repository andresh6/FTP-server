import com.jcraft.jsch.JSch;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ClientTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private User user = new User("agileteam6", "agileteam6", "34.83.11.14");
    private Client client = new Client(user);

    @Test
    public void testClientConnect1() {
       client.connect();
       assertTrue(client.channelSftp.isConnected());
    }

    @Test
    public void testlistLocalFiles1() {
    }

    @Test
    public void testListRemoteFiles1() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if(!client.channelSftp.isConnected()){
            fail("Could not connect to server");
        }
        client.listRemoteFiles();
        assertThat(outContent.toString(), containsString("text.txt"));
        System.setOut(originalOut);
    }

    @Test
    public void testListRemoteDirectory1() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if(!client.channelSftp.isConnected()){
            fail("Could not connect to server");
        }
        client.listRemoteFiles();
        assertThat(outContent.toString(), containsString("NewFolder"));
        System.setOut(originalOut);
    }

    @Test
    public void testLogOff() {
        client.connect();
        if(!client.channelSftp.isConnected()){
            fail("Could not connect to server");
        }
        assertTrue(client.channelSftp.isConnected());
        assertTrue(client.session.isConnected());
        client.logOff();
        assertFalse(client.channelSftp.isConnected());
        assertFalse(client.session.isConnected());
    }
}