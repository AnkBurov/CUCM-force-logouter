package ru.cti.cucmforcelogouter.controller.logdirectory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.AbstractFileTailer;
import ru.cti.cucmforcelogouter.controller.logdirectory.loghandler.AbstractFileListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * This abstract class observes sourcePath directory for log files and for each one starts new thread for parsing and analyzing
 */
public abstract class AbstractLogDirectoryHandler {
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected AbstractFileTailer fileTailer;
    @Autowired
    protected AbstractFileListener abstractFileListener;
    protected String logExtension;
    protected String sourcePath;
    protected String soughtString;
    protected static volatile Set<File> handledFiles = new HashSet<>();

    public AbstractLogDirectoryHandler(String logExtension, String sourcePath, String soughtString) {
        this.logExtension = logExtension;
        this.sourcePath = sourcePath;
        this.soughtString = soughtString;
    }

    /**
     * Method observes sourcePath directory for log files and for each one starts a new thread with Observable tailer
     * for parsing and analyzing
     */
    public abstract void observeLogBuffer();

    public static synchronized boolean addHandledFile(File file) {
        return handledFiles.add(file);
    }

    public static synchronized boolean removeHandledFile(File file) {
        return handledFiles.remove(file);
    }
}
