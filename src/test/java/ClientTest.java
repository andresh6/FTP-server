import com.jcraft.jsch.JSch;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientTest {
    private User user = new User("agileteam6", "agileteam6", "34.83.11.14");
    private Client client = new Client(user);

    @Test
    public void connect() {
       client.connect();
       assertTrue(client.channelSftp.isConnected());
    }

    @Test
    public void listLocalFiles() {
    }

    @Test
    public void listRemoteFiles() {
    }
}