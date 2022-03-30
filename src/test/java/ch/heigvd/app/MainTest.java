package ch.heigvd.app;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class MainTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    /**
     * Test the "statique Clean /my/site" command
     */
    public void statiqueCleanShouldDeleteDirectory() {

        // Creates a test directory
        String dirName = "monTEST/siteTEST/build";

        // Tests if the test directory already exists to avoid overwriting a
        // legit directory
        assertFalse(Files.exists(new File("monTEST").toPath()));
        new File(dirName).mkdirs();

        // Creates test files
        try {
            for(int i = 0; i < 10; ++i) {
                File f = new File(dirName + "/test" + i + ".txt");
                f.createNewFile();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        // Delete the file directory using "statique clean PATH" by running
        // from the command line

        Main app = new Main();
        StringWriter sw = new StringWriter();
        CommandLine cmd = new CommandLine(app);
        cmd.setOut(new PrintWriter(sw));

        File path = new File("monTEST/siteTEST/build");

        int exitCode = cmd.execute("Clean", "/monTEST/siteTEST/");
        assertEquals(0, exitCode);

        assertFalse("Directory still exists", Files.exists(path.toPath()));

        File pathToTestDir =
                new File(System.getProperty("user" + ".dir") + "/monTEST");

        // Delete the test directory
        try {
            FileUtils.deleteDirectory(pathToTestDir);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
