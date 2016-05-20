package ru.cti.cucmforcelogouter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cti.cucmforcelogouter.controller.logdirectory.AbstractLogDirectoryHandler;
import ru.cti.cucmforcelogouter.model.dbmaintenance.AbstractDBMaintenance;

/**
 * Main application class.
 * Consists of lower hierarchy classes
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger("Mine");
    @Autowired
    private AbstractLogDirectoryHandler abstractLogDirectoryHandler;
    @Autowired
    private AbstractDBMaintenance dbMaintenance;

    public void doWork() {
        logger.info("Application has been started");
        while (true) {
            dbMaintenance.createDB();
            abstractLogDirectoryHandler.observeLogBuffer();
            dbMaintenance.removeOldPhones();
            dbMaintenance.removeOldPhonesFromPhoneList();
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
