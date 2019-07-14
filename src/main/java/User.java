import java.io.*;

/** User class for storing user data.
 *  Contains Username, Password, and other relevant information
 */
public class User {
    String username;
    String password;
    String hostname;

    User(){
        /** Base instantiation of User object.
         *  String settings to come later.
         */
        username = null;
        password = null;
        hostname = null;
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
     */
    User(String username, String password, String hostname) {
        this.username = username;
        this.password = password;
        this.hostname = hostname;
    }


}
