package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cti.cucmforcelogouter.controller.logdirectory.AbstractLogDirectoryHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileTailer extends AbstractFileTailer {
    private static Logger logger = LogManager.getLogger(FileTailer.class);

    public FileTailer() {
    }

    /**
     * @param timeoutUntilInterrupt - number of HOURS
     */
    public FileTailer(int timeoutUntilInterrupt) {
        super(timeoutUntilInterrupt);
    }

    @Override
    public void run() {
        logger.info("Thread " + Thread.currentThread().getName() + " " + file.getAbsolutePath() + " has been created");
        AbstractLogDirectoryHandler.addHandledFile(file);
        try (InputStream is = Files.newInputStream(file.toPath(), StandardOpenOption.DSYNC)) {
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            BufferedReader lineReader = new BufferedReader(reader);
            // Process all lines.
            String line;
            while (file.exists()) {
                line = lineReader.readLine();
                if (line != null) {
                    setChanged();
                } else {
                    try {
                        Thread.currentThread().sleep(500);
                        // check if life timer exceeded available live of object
                        if (System.currentTimeMillis() - startTime > timeoutUntilInterrupt) {
                            Thread.currentThread().interrupt();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                notifyObservers(line);
            }
            throw new RuntimeException("File has been removed");
        } catch (IOException e) {
            e.printStackTrace();
            logger.catching(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info("File " + file.getName() + " has been removed");
        } finally {
            AbstractLogDirectoryHandler.removeHandledFile(file);
        }
    }
}
