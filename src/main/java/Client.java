import com.jcraft.jsch.*;

public class Client {
    JSch jsch;
    ChannelSftp channelSftp; // the connection for the client
    User user; // expand to data structure

    Client() {
        // create jsch
        jsch = new JSch();
        user = new User();

        // connect ?
        // pass in the user object
        // instantiate the session
    }

    Client(User user) {
        jsch = new JSch();
        this.user = user;
    }

    Client(String username, String password, String hostname) {
        jsch = new JSch();
        user = new User(username, password, hostname);
    }


    public void connect(){
        try {
            // function that runs the code...
            Session session = jsch.getSession(user.username, user.hostname, 22);

            // make user object for username and password
            session.setPassword(user.password);

            session.connect();

            Channel channel = session.openChannel("sftp");

            channel.connect();

            channelSftp = ((ChannelSftp) channel);

        } catch (Exception e) {
            System.err.println("You did something wrong.");
            System.err.println(e);
            System.exit(1);
        }
    }
}