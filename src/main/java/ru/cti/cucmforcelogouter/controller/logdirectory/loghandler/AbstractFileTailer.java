package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public abstract class AbstractFileTailer extends Observable implements Runnable {
    protected long startTime;
    protected long timeoutUntilInterrupt;
    protected File file;

    public AbstractFileTailer() {
        this.timeoutUntilInterrupt = 129600000;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * @param timeoutUntilInterrupt - number of HOURS
     */
    public AbstractFileTailer(int timeoutUntilInterrupt) {
        this.timeoutUntilInterrupt = timeoutUntilInterrupt * 3600000;
        this.startTime = System.currentTimeMillis();
    }

    public void setFile(File file) {
        this.file = file;
    }
}
