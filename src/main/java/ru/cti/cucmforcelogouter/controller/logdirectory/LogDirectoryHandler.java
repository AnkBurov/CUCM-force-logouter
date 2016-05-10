package ru.cti.cucmforcelogouter.controller.logdirectory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.AbstractFileTailer;

import java.io.File;

/**
 * This concrete class observes sourcePath directory for log files and for each one starts new thread for parsing and analyzing
 */
public class LogDirectoryHandler extends AbstractLogDirectoryHandler {
    private static final Logger logger = LogManager.getLogger(LogDirectoryHandler.class);

    public LogDirectoryHandler(String logExtension, String destPath, String soughtString) {
        super(logExtension, destPath, soughtString);
    }

    /**
     * Method observes sourcePath directory for log files and for each one starts a new thread with Observable tailer
     * for parsing and analyzing
     */
    @Override
    public void observeLogBuffer() {
        File dir = new File(sourcePath);
        // collect files from sourcePath
        File[] files = dir.listFiles((dir1, name) -> {
            if (name.endsWith(logExtension)) {
                return true;
            }
            return false;
        });
        //start new Thread with Observable tailer for each file
        for (File file : files) {
            if (!AbstractLogDirectoryHandler.handledFiles.contains(file)) {
                logger.info("Trying to start new thread for file " + file.getAbsolutePath());
                abstractFileListener.setSoughtString(soughtString);
                // create a new bean, instead of using existing bean
                fileTailer = (AbstractFileTailer) applicationContext.getBean("fileTailer");
                fileTailer.setFile(file);
                fileTailer.addObserver(abstractFileListener);
                new Thread(fileTailer).start();
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
