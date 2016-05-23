package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cti.cucmforcelogouter.controller.cucmapi.CucmApiImplementation;
import ru.cti.cucmforcelogouter.model.factory.PhoneFactory;
import ru.cti.cucmforcelogouter.model.factory.PhoneListFactory;
import ru.cti.cucmforcelogouter.model.repository.PhoneListRepository;
import ru.cti.cucmforcelogouter.model.repository.PhoneRepository;

import java.util.Observer;

/**
 * This class runs for each file in log buffer directory and parses AddrOutOfService messages and disconnects them by
 * sending logout message
 */
public abstract class AbstractFileListener implements Observer {
    @Autowired
    protected PhoneRepository phoneRepository;
    @Autowired
    protected PhoneListRepository phoneListRepository;
    @Autowired
    protected PhoneFactory phoneFactory;
    @Autowired
    protected PhoneListFactory phoneListFactory;
    @Autowired
    protected CucmApiImplementation cucmApiImplementation;
    protected String soughtString;
    protected String deviceNameRegexp;
    protected String messageTimeRegexp;

    public AbstractFileListener(String deviceNameRegexp, String messageTimeRegexp) {
        this.deviceNameRegexp = deviceNameRegexp;
        this.messageTimeRegexp = messageTimeRegexp;
    }

    public void setSoughtString(String soughtString) {
        this.soughtString = soughtString;
    }
}
