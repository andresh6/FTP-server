import com.jcraft.jsch.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * TODO client class description goes here
 */
public class Client {
    JSch jsch;
    ChannelSftp channelSftp; // the connection for the client
    User user; // expand to data structure
    private String currentWorkingDirectory;

    Client() {
        // create jsch
        jsch = new JSch();
        user = new User();
        currentWorkingDirectory = System.getProperty("user.dir");
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

            Properties config = new java.util.Properties();     //skips host key checking
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


    /**
     * Downloads file from server, to local machine.
     * example: get src dst
     * get /home/agileteam6/text.txt /home/user/Downloads/
     */
    public void getFile(String src, String dst){

        try{

           channelSftp.get(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while getting your file.");
            System.err.println(e);
        }
    }


    /**
     * uploads file to server from local machine
     * example: put src dst
     * put /home/user/Downloads/text.txt /home/agileteam6/
     */
    public void putFile(String src, String dst){

        try{

            channelSftp.put(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while uploading your file.");
            System.err.println(e);
        }
    }

    public String getCurrentWorkingDirectory() {
        return this.currentWorkingDirectory;
    }

}