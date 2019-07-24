import com.jcraft.jsch.*;
import java.util.Properties;
import java.util.Scanner;

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
     * Base constructor
     * TODO: Delete this? Add checking for valid User info before connect?
     */
    Client() {
        // create jsch
        this.jsch = new JSch();
        this.user = new User();

        // connect ?
        // pass in the user object
        // instantiate the session
    }

    /**
     * Constructor with <code>User</code> object
     * @param user
     *        The <code>User</code> class object containing Username, Password and Hostname
     */
    Client(User user) {
        this.jsch = new JSch();
        this.user = user;
    }

    /**
     * Constructor that creates <code>User</code> object with passed arguments
     * @param username
     *        The <code>username</code> to be used in the <code>User</code> object
     * @param password
     *        The <code>password</code> to be used in the <code>User</code> object
     * @param hostname
     *        The <code>hostname</code> to be used in the <code>User</code> object
     */
    Client(String username, String password, String hostname) {
        this.jsch = new JSch();
        this.user = new User(username, password, hostname);
    }


    /**
     * Opens the connection to the SFTP server using the information stored in the
     * <code>User</code> object
     */
    public void connect(){
        try {
            // function that runs the code...
            session = jsch.getSession(user.username, user.hostname, 22);

            // make user object for username and password
            session.setPassword(user.password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

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
     * Closes the connection to the SFTP server
     */
    public void logOff() {
        System.out.println("Logging off...");
        channelSftp.exit();
        session.disconnect();
    }

    /**
     * Downloads file from server, to local machine.
     * example: get src dst
     * get /home/agileteam6/text.txt /home/user/Downloads/
     */
    public void get_file(String src, String dst){
        try{
           channelSftp.get(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while getting your file.");
        }
    }
 
    /**
     * uploads file to server from local machine
     * example: put src dst
     * put /home/user/Downloads/text.txt /home/agileteam6/
     */
    public void put_file(String src, String dst){
        try{
            channelSftp.put(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while uploading your file.");
            System.err.println(e);
        }
    }
   
    /**
     * Will print local files within the current directory
     * Uses 'ls -la' functionality
     */
    public void listLocalFiles() {

    }

    /**
     * Will print remote files in the current SFTP server side directory
     * Uses 'ls -la' functionality
     */
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
    
    /**
     * Creates a new directory on the remote SFTP server
     * @param path
     *        The path, including filename of the new directory & directory location
     */
     public void createRemoteDirectory(String path){
        try {
            if (path!=null){
                channelSftp.mkdir(path);
            }
        } catch (SftpException e) {
            e.printStackTrace();
        }
     }

    /**
     * Will delete a directory on the remote SFTP server
     * @param path
     *        The path for the directory to delete
     * TODO: Add recursive argument/functionality
     */
     public void removeRemoteDirectory(String path){
        try {
            if(path!=null){
                channelSftp.rmdir(path);
            }
        } catch (SftpException e) {
            System.err.println(e);
            e.printStackTrace();
        }
     }

}