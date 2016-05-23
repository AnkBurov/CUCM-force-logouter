package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cti.cucmforcelogouter.controller.logdirectory.AbstractLogDirectoryHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileTailer extends AbstractFileTailer {
    private static final Logger logger = LoggerFactory.getLogger("Mine");

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
                        logger.error(e.getMessage(), e);
                    }
                }
                notifyObservers(line);
            }
            throw new RuntimeException("File has been removed");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (RuntimeException e) {
            logger.info("File " + file.getName() + " has been removed");
        } finally {
            AbstractLogDirectoryHandler.removeHandledFile(file);
        }
    }
}
