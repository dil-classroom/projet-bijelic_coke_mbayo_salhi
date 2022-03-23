package ch.heigvd.app;

import org.apache.commons.io.FileUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "Clean")
public class Clean implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build " +
            "directory")
    private String path;

    @Override
    public Integer call() throws Exception {

        File myPath =
                new File(System.getProperty("user" + ".dir") + path + "/build");

        FileUtils.deleteDirectory(myPath);

        System.out.println("Build directory cleaned");

        return 0;
    }
}
