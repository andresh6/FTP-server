import com.jcraft.jsch.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Client test = new Client("agileteam6","agileteam6","34.83.11.14");
        test.connect();
        test.get_file();
    }
}
