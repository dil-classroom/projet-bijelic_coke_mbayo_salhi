package ch.heigvd.app.commands;

import ch.heigvd.app.utils.parsers.MarkdownConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import utils.watchDir.WatchDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.*;

@Command(name = "build")
public class Build implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to build " + "directory")
    private Path sourcePath;

    @CommandLine.Option(names = {"-w", "--watch"}, description = "Allows to regenerate site when modification are made")
    private boolean watchDir;

    final private String BUILD_DIRECTORY_NAME = "build";
    final private String MARKDOWN_FILE_TYPE = "md";
    final private Set<String> FILE_TYPE_TO_EXCLUDE = Set.of("yaml");

    @Override
    public Integer call() throws Exception {

        if (watchDir) {
            WatchDir watcher = new WatchDir(sourcePath, false);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> future = executor.submit(watcher);
            executor.shutdown();
            // The while loop allows to rebuild, but it make an infinite loop that should probably be corrected later
            while(!future.isCancelled()) {
                if (watcher.isRebuild()) {
                    building();
                    watcher.setRebuild(false);
                }
            }
            // Shutdown after 10 seconds
            executor.awaitTermination(30, TimeUnit.SECONDS);
            // abort watcher
            future.cancel(true);

            executor.awaitTermination(1, TimeUnit.SECONDS);
            executor.shutdownNow();
        }else {
            building();
        }

        return 0;
    }

    private void building() throws Exception{
        File myPath = new File(System.getProperty("user" + ".dir") + "/" + sourcePath + "/");

        System.out.println("Building in : " + sourcePath);

        Path buildPath = sourcePath.resolve(BUILD_DIRECTORY_NAME);

        System.out.println("buildPath = " + buildPath);

        copyFiles(sourcePath, buildPath);
    }

    /**
     * Recursive directory copiing with parsing for certain files
     * @param source Directory where files are located
     * @param destination Directory where files are being copied to
     */
    private void copyFiles(Path source, Path destination, CopyOption... options) throws IOException {
        if(Files.exists(destination)) {
            System.out.println("Directory build already exists. It will be deleted");
            FileUtils.deleteDirectory(destination.toFile());
            System.out.println("Directory build successfully deleted");
        }

        // Go through all directory and copy files and folders in build folder
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            /**
             * Visit directory and copy it in /build/
             * @param dir Path of the directory
             * @param attrs Attributes of directory
             * @return Status of the directory visit
             * @throws IOException Throw exception if creation fails
             */
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if(!dir.startsWith(destination)){
                    Path destinationPath = destination.resolve(source.relativize(dir));
                    Files.createDirectory(destinationPath);
                    System.out.println("Directory " + destinationPath + " successfully created");
                }

                return FileVisitResult.CONTINUE;
            }

            /**
             * Visit files and copy or convert markdown to html
             * @param file Path of the file
             * @param attrs Attributes the file
             * @return Status of the file visit
             * @throws IOException Throw exception if creation fails
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                String fileExtension = FilenameUtils.getExtension(file.toString());
                // Check if visited file extension is not in excluded list
                if(!FILE_TYPE_TO_EXCLUDE.contains(fileExtension)){
                    if(fileExtension.equals(MARKDOWN_FILE_TYPE)){
                        StringBuilder htmlContent = new StringBuilder();

                        try (FileInputStream fis = new FileInputStream(file.toString());
                             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                             BufferedReader reader = new BufferedReader(isr)
                        ) {
                            String str;
                            boolean startToCopy = false;
                            while ((str = reader.readLine()) != null) {
                                if(startToCopy){
                                    htmlContent.append(MarkdownConverter.convert(str));
                                }
                                // Ignore markdown file header and start copying markdown from specific line
                                if(str.equals("---"))
                                    startToCopy = true;
                            }
                        } catch (IOException e) {
                            System.err.println("Error while reading markdown file");
                        }

                        Path htmlFile = Paths.get(
                                FilenameUtils.removeExtension(
                                        destination.resolve(source.relativize(file)).toString()) + ".html"
                        );
                        Files.createFile(htmlFile);

                        // Write HTML content in destination file
                        OutputStreamWriter htmlWriter = new OutputStreamWriter(new FileOutputStream(htmlFile.toString()), StandardCharsets.UTF_8);
                        htmlWriter.write(htmlContent.toString());
                        htmlWriter.flush();
                        htmlWriter.close();
                        System.out.println("File " + htmlFile + " successfully created");
                    }
                    else {
                        // Bug fix Linux
                        if(!file.startsWith(destination)) {
                            Files.copy(file, destination.resolve(source.relativize(file)), options);
                            System.out.println("File " + file + " successfully copied");
                        }
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
}