import com.jcraft.jsch.*;
import java.util.Properties;
import java.util.Scanner;
import java.nio.charset.Charset;
import java.util.Vector;

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
     * @param port
     *        The <code>port</code> to be used in the <code>User</code> object
     */
    Client(String username, String password, String hostname, String port) {
        this.jsch = new JSch();
        this.user = new User(username, password, hostname, port);
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

            session.connect();

            Channel channel = session.openChannel("sftp");

            channel.connect();

            channelSftp = ((ChannelSftp) channel);

        } catch (Exception e) {
            System.err.println("You did something wrong.");
            System.err.println(e.toString());
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
    public void getFile(String src, String dst){
        try{
           channelSftp.get(src,dst);
        }
        catch (SftpException e){
            System.err.println("Something went wrong while getting your file.");
            System.err.println(e.toString());
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
            System.err.println(e.toString());
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
            System.err.println(e.toString());
        }
    }


    /**
     * delete the file that is passed in, return exception if file doesn't exist
     * @param fileToDelete name of file to be  deleted
     * ex: rm "filename"
     */
    public void deleteRemoteFiles(String fileToDelete) {
        boolean deletedFlag = false;

        try {
            channelSftp.rm(fileToDelete); //this method removes the file from remote directory
            deletedFlag = true;
            if (deletedFlag) {
                System.out.println("File deleted successfully.");
            }
            else

                System.out.println("File does not exist.");
            }
        catch(Exception ex){
            ex.printStackTrace();
        }
        }

    
    /**
     * Creates a new directory on the remote SFTP server
     * @param path
     *        The path, including filename of the new directory & directory location
     * Ex: mkdir "directory name"
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
     * Will delete a directory on the remote SFTP server - nonRecursive
     * @param path
     *        The path for the directory to delete
     * EX: rmdir "directory name"
     */
     public void removeRemoteDirectory(String path){
        try {
            if(path!=null){
                channelSftp.rmdir(path);
            }
        } catch (SftpException e) {
            System.err.println(e.toString());
        }
     }

    /**
     * recursively delete files in directory and then delete directory on remote server
     * @param path
     * @throws SftpException
     */
    public void recursiveRemoveRemoteDir(String path)  {
        try {

            //list source directory structure
            Vector<ChannelSftp.LsEntry> fileAndFolderList = channelSftp.ls(path);

            //iterate objects in the list to get file/folder names.
            for (ChannelSftp.LsEntry item : fileAndFolderList) {
                //if it is a file (not a directory)
                if (!item.getAttrs().isDir()) {
                    channelSftp.rm(path + "/" + item.getFilename()); //remove file
                } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { //if it is a subdirectory
                    try {
                        //removing sub directory
                        channelSftp.rmdir(path + "/" + item.getFilename());
                    } catch (Exception e) { //if subdir is not empty and error occurs.
                        //do recursiveRemoveRemoteDir on this subdir
                        recursiveRemoveRemoteDir(path + "/" + item.getFilename());
                    }
                }
            }
        }catch (SftpException e) {
            e.printStackTrace();
        }
        //delete the parent directory
        try {
            if(path!=null){
                channelSftp.rmdir(path);
            }
        } catch (SftpException e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Changes permissions of a file or directory on the remote server.
     * @param path
     *        The File/Directory of which to modify permissions.
     * @param permissions
     *        The new permissions, expects standard 3 digit octal 0-7 string.
     */
    public void changePermissions(String path, String permissions) {
        boolean valid = true;
        int foo = 0;
        for(int i = 0; i < permissions.length(); i ++) {
            char current = permissions.charAt(i);
            if(current < '0' || current > '7') {
                valid = false;
                break;
            }
            foo <<= 3;
            foo |= (current - '0');
        }
        if(valid) {
            try {
                channelSftp.chmod(foo, path);
            } catch (SftpException e) {
                System.err.println(e.toString());
            }
        } else {
            System.err.println("Invalid permissions setting: " + permissions);
            System.err.println("Expected format: a 3 digit octal number");
        }
    }

    /**
     * Changes group ID of a file or directory on the remote server.
     * @param path
     *        The File/Directory of which to modify permissions.
     * @param group
     *        The new Group ID to set
     */
    public void changeGroup(String path, String group) {
        int toGroup = Integer.parseInt(group);
        try {
            channelSftp.chgrp(toGroup, path);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes ownership ID of a file or directory on the remote server.
     * @param path
     *        The File/Directory of which to modify permissions.
     * @param owner
     *        The new Owner ID to set
     */
    public void changeOwner(String path, String owner) {
        int toOwner = Integer.parseInt(owner);
        try {
            channelSftp.chgrp(toOwner, path);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes the name of a file
     * @param old_name name of file
     * @param new_name name the file will be changed to
     * ex:  mv "oldname" "newname"
     */

    public void rename_file(String old_name, String new_name){
        try{
            channelSftp.rename(old_name, new_name);
        } catch (SftpException e){
            System.err.println("Something went wrong while renaming your file.");
            System.err.println(e.toString());
        }
    }

    /**
     *
     * @param dirName name of directory to be changed into
     *  ex: cd "directory name"
     */
    public void changeDir(String dirName){
        try{
            String path = channelSftp.pwd();
            channelSftp.cd(path + "/" + dirName);
        } catch(SftpException e){
            System.err.println("Something went wrong.");
            System.err.println(e.toString());
        }
    }

    /**
     * prints working directory
     *
     */
    public void pwd(){

        try{
           System.out.println(channelSftp.pwd());
        } catch(SftpException e){
            System.err.println("Something went wrong.");
            System.err.println(e.toString());
        }

    }
}
