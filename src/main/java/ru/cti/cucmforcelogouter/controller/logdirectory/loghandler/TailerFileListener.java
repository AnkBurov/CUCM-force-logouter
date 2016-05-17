package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.UncategorizedSQLException;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;

import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TailerFileListener extends AbstractFileListener {
    private Logger logger = LogManager.getLogger(TailerFileListener.class);
    private Pattern patternDeviceName;
    private Pattern patternMessageTime;
    private Observable tailer;

    public TailerFileListener(String deviceNameRegexp, String messageTimeRegexp) {
        super(deviceNameRegexp, messageTimeRegexp);
        patternDeviceName = Pattern.compile(deviceNameRegexp);
        patternMessageTime = Pattern.compile(messageTimeRegexp);
    }

    /**
     * update and handle response from observable tailer
     *
     * @param lineBufferObject
     */
    @Override
    public void update(Observable tailer, Object lineBufferObject) {
        String lineBuffer = (String) lineBufferObject;
        Matcher matcherDeviceName;
        Matcher matcherMessageTime;
        if (lineBuffer.contains(soughtString)) {
            matcherDeviceName = patternDeviceName.matcher(lineBuffer);
            matcherMessageTime = patternMessageTime.matcher(lineBuffer);
            if (matcherDeviceName.find() && matcherMessageTime.find()) {
                String bufferDeviceName = matcherDeviceName.group();
                String bufferMessageTime = matcherMessageTime.group();
                try {
                    daoFacade.getPhoneDAO().create(phoneFactory.create(bufferDeviceName, bufferMessageTime));
                    logger.info("Phone " + bufferDeviceName + " " + bufferMessageTime + " has been added to DB");
                    // read this phone
                    Phone phone;
                    if ((phone = daoFacade.getPhoneDAO().readNotClosedPhoneByDeviceNameAndTime(bufferDeviceName, bufferMessageTime)) != null) {
                        //send logout
                        if (cucmApiImplementation.sendLogout(phone.getDeviceName()) != 2) {
                            phone.setEnded(true);
                            daoFacade.getPhoneDAO().update(phone);
                            logger.debug("Phone " + phone.getDeviceName() + " has been marked as ended");
                        }
                        //temporary login this phone with tech user to kick out previous user
                        cucmApiImplementation.doTempLogin(phone.getDeviceName());
                    }
                } catch (UncategorizedSQLException e) {
                    logger.trace("Phone " + bufferDeviceName + " " + bufferMessageTime + " already added in the DB");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.catching(e);
                }
            }
        }
    }
}
