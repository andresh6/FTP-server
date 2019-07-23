import com.jcraft.jsch.JSch;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class ClientTest {
    private  ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private  ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private User user = new User("agileteam6", "agileteam6", "34.83.11.14");
    private Client client = new Client(user);

    /**
     * Simplest test, validates that a connection can be established to an SFTP server
     */
    @Test
    public void testClientConnect1() {
       client.connect();
       assertTrue(client.channelSftp.isConnected());
    }

    @Test
    public void testlistLocalFiles1() {
    }

    /**
     * Test that validates that the remote ls command will work with a remote file.
     * Expects a document <i>text.txt</i> on the server.
     */
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

    /**
     * Test that validates that the remote ls command will work with a remote directory.
     * Expects a directory <i>NewFolder</i> on the server.
     */
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

    /**
     * Test to validate closing the SFTP connection to the server
     */
    @Test
    public void testLogOff1() {
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

    /**
     * Test to delete a directory on the remote SFTP server.
     * Will create and then delete a folder using the current unix timestamp
     */
    @Test
    public void testCreateAndDeleteRemoteDirectory() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if(!client.channelSftp.isConnected()){
            fail("Could not connect to server");
        }
        String currentTime= String.valueOf(System.currentTimeMillis()/1000);
        client.createRemoteDirectory(currentTime);
        client.listRemoteFiles();
        assertThat(outContent.toString(), containsString(currentTime));
        outContent=new ByteArrayOutputStream();
        //System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        client.removeRemoteDirectory(currentTime);
        client.listRemoteFiles();
        assertThat(outContent.toString(), not(containsString(currentTime)));
        System.setOut(originalOut);

    }

    /**
     * TODO: Write recursive functionality for directory deletion in <code>Client</code> class
     * Test to create a directory with multiple sub-directories and documents, then delete them
     *  all with a recursive deletion of the parent directory.
     */
    @Test
    public void testRemoveRemoteDirectory() {
        System.setOut(new PrintStream(outContent));
        client.connect();
        if(!client.channelSftp.isConnected()){
            fail("Could not connect to server");
        }
        client.removeRemoteDirectory("currentTime");
        client.listRemoteFiles();
        assertThat(outContent.toString(), not(containsString("currentTime")));
        System.setOut(originalOut);
    }
}