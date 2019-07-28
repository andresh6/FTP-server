import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.monitor.Monitor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class ClientTest {
    private  ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private  ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    //private User user = new User("agileteam6", "agileteam6", "34.83.11.14");
    private User user = new User("user", "password", "localhost");
    private Client client = new Client(user);
    private SshServer sshd;

    @Before
    public void setupServer() {
        try {
            //List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
            //userAuthFactories.add(new UserAuthPasswordFactory());

            //List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
            //namedFactoryList.add(new SftpSubsystemFactory());

            sshd = SshServer.setUpDefaultServer();
            sshd.setPort(22);
            sshd.setHost("localhost");
            sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
            sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" }));
            sshd.setCommandFactory(new ScpCommandFactory());


//            sshd.setUserAuthFactories(userAuthFactories);
//            sshd.setCommandFactory(new ScpCommandFactory());
//            sshd.setSubsystemFactories(namedFactoryList);
            sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
                public boolean authenticate(String s, String s1, ServerSession serverSession) throws PasswordChangeRequiredException {
                    return true;
                }
            });
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void shutdownServer(){
        try {
            sshd.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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