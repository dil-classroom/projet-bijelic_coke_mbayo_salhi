package ch.heigvd.app;


import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
import picocli.CommandLine.IVersionProvider;

@Command(name = "static",
        mixinStandardHelpOptions = true,
        description = "Generate random static websites",
        subcommands = {New.class, Clean.class, Build.class, Serve.class,
                Init.class},
        versionProvider = Main.ManifestVersionProvider.class)
public class Main implements Callable<Integer>
{
    @CommandLine.Option(names = {"-V", "--version"}, versionHelp = true,
            description = "display version info")
    boolean versionRequested;


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

    /**
     * {@link IVersionProvider} implementation that returns version
     * information from the project-bijelic_coke_mbayo_sahli jar file's {@code
     * /META-INF/MANIFEST.MF} file.
     * @see <a href="https://picocli.info/#_dynamic_version_information">...</a>
     */
    static class ManifestVersionProvider implements IVersionProvider {
        public String[] getVersion() throws Exception {
            return new String[]{"Current version : " + Main.class.getPackage().getImplementationVersion()};
        }
    }
}
