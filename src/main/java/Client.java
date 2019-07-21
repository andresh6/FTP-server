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
    Session session; //Session covers all of Client

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
     *        The <code>User</code> class object containing Username, Password and Hostname
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
            session = jsch.getSession(user.username, user.hostname, 22);

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

    public void logOff() {
        System.out.println("Logging off...");
        channelSftp.exit();
        session.disconnect();
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


     public void createRemoteDirectory(String path){
        try {
            if (path!=null){
                channelSftp.mkdir(path);
            }


        } catch (SftpException e) {
            e.printStackTrace();
        }
     }

     public void removeRemoteDirectory(String path){
        try {
            if(path!=null){
                channelSftp.rmdir(path);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
     }


}