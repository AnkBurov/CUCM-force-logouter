package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cti.cucmforcelogouter.controller.dao.DAOFacade;
import ru.cti.cucmforcelogouter.controller.cucmapi.CucmApiImplementation;
import ru.cti.cucmforcelogouter.model.factory.PhoneFactory;

import java.util.Observer;

/**
 * This class runs for each file in log buffer directory and parses AddrOutOfService messages and disconnects them by
 * sending logout message
 */
public abstract class AbstractFileListener implements Observer {
    @Autowired
    protected DAOFacade daoFacade;
    @Autowired
    protected PhoneFactory phoneFactory;
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
