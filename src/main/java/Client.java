import com.jcraft.jsch.*;
import java.util.Properties;
import java.util.Scanner;

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
     * asks for source path of file to download and
     * destination path for file
     */
    public void get_file(){

        String src; //source path for file (home/agileteam6/<file>
        String dst; //destination path for file downloaded

        Scanner sc = new Scanner(System.in);

        System.out.println("What is the source path for the file you'd like to download?");
        src = sc.nextLine();

        System.out.println("What is the destination path for the file you're downloading?");
        dst = sc.nextLine();

        try{//get from server

           channelSftp.get(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while getting your file.");
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * uploads file to server from local machine
     */
    public void put_file(){

        String src; //source path for file (home/agileteam6/<file>
        String dst; //destination path for file downloaded

        Scanner sc = new Scanner(System.in);

        System.out.println("What is the source path for the file you'd like to upload?");
        src = sc.nextLine();

        System.out.println("What is the destination path for the file you're uploading?");
        dst = sc.nextLine();

        try{//put onto server

            channelSftp.put(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while uploading your file.");
            System.err.println(e);
            System.exit(1);
        }
    }


}