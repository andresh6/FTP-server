import java.io.*;
import java.util.Scanner;

/** User class for storing user data.
 *  Contains Username, Password, and other relevant information
 */
public class User {
    String username;
    String password;
    String hostname;
    int port;

    User(){
        /** Base instantiation of User object.
         *  String settings to come later.
         */
        username = null;
        password = null;
        hostname = null;
        port = 0;
    }

    /**
     * Creates a new <code>User</code> object.
     *
     * @param username
     *        The username
     *
     * @param password
     *        The User's password
     *
     * @param hostname
     *        The server to which the user will connect
     *
     * @param port
     *        The SFTP server port
     */
    User(String username, String password, String hostname, String port) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.port = Integer.parseInt(port);
    }

}
