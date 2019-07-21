import com.jcraft.jsch.*;

import java.nio.charset.Charset;

/**
 * Client class allows connections to server, lists files
 * on both local and remote servers.
 */
public class Client {
    JSch jsch;
    ChannelSftp channelSftp; // the connection for the client
    User user; // expand to data structure

    /**
     *
     */
    Client() {
        // create jsch
        jsch = new JSch();
        user = new User();

        // connect ?
        // pass in the user object
        // instantiate the session
    }

    /**
     * @param user
     */
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
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

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

    public void listLocalFiles() {

    }

    public void listRemoteFiles() {
        try {
            java.util.Vector path = channelSftp.ls(Character.toString('.'));
            if(path != null) {
                for(int i = 0; i < path.size(); i++){
                    Object obj = path.elementAt(i);
                    if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry){
                        System.out.println(((com.jcraft.jsch.ChannelSftp.LsEntry)obj).getLongname());
                    }
                }
            }
        } catch (SftpException e) {
            System.err.println(e);
        }
    }
}