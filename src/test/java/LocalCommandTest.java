//import jdk.vm.ci.meta.Local;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocalCommandTest {
    LocalCommand localCommand = new LocalCommand();

    @Test
    public void testlsCommandTestDirectory() {
        // File new create directory if one doesn't already exit
        // add temp folder to show that paths work
        // list everything at the project's root level
        localCommand.ls(System.getProperty("user.dir") + "/testing");
        // clean up temp dirs made
    }

    @Test
    public void testcdCommandTestDirectoryAbove() {
        localCommand.currentWorkingDirectory = generateTestingDir();
        localCommand.cd("..");
        assertThat(localCommand.currentWorkingDirectory, equalTo(System.getProperty("user.dir")));
    }

    // todo more tests

    public String generateTestingDir() {
        File testDir = new File(System.getProperty("user.dir") + "/testing");
        testDir.mkdir();

        return testDir.getPath();
    }

    public String generateTestingDirAndFile() {
        File testDir = new File(System.getProperty("user.dir") + "/testing");
        String pathToFile = new String();
        if (testDir.mkdir()) {
            File testFile = new File(System.getProperty("user.dir") + "/testing/test.txt");
            try {
                if (testFile.createNewFile()) {
                    pathToFile = testFile.getPath();
                } else {
                    System.err.println("There was an error creating the file.");
                    System.exit(1);
                }
            } catch (IOException e) {}
        }

        return pathToFile;
    }
}
