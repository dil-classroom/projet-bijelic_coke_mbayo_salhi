package ch.heigvd.app;


import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "static",
        mixinStandardHelpOptions = true,
        version = "static 0.1",
        description = "Generate random static websites",
        subcommands = {New.class, Clean.class, Build.class, Serve.class,
                Init.class})
public class Main implements Callable<Integer>
{
    @Override
    public Integer call() throws Exception {
        System.out.println("Static");
        return 0;
    }

    public static void main( String[] args )
    {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}
