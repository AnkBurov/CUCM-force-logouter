package ru.cti.cucmforcelogouter.controller.logdirectory.loghandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.UncategorizedSQLException;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;

import java.util.List;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TailerFileListener extends AbstractFileListener {
    private static final Logger logger = LoggerFactory.getLogger("Mine");
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
                    // if this deviceName doesn't exist in PhoneList table, send logout message
                    List<PhoneList> phoneLists = daoFacade.getPhoneListDAO().readByDeviceName(bufferDeviceName);
                    if ((phoneLists.size() > 0 ? phoneLists.get(0) : null) == null) {
                        Phone phone;
                        daoFacade.getPhoneDAO().create(phoneFactory.create(bufferDeviceName, bufferMessageTime));
                        logger.info("Phone " + bufferDeviceName + " " + bufferMessageTime + " has been added to DB");
                        // if this phone not closed in Phones table or doesn't exist, then send logout and do temp login
                        if ((phone = daoFacade.getPhoneDAO().readNotClosedPhoneByDeviceNameAndTime(bufferDeviceName, bufferMessageTime)) != null) {
                            //send logout
                            if (cucmApiImplementation.sendLogout(phone.getDeviceName()) != 2) {
                                phone.setEnded(true);
                                daoFacade.getPhoneDAO().update(phone);
                                logger.debug("Phone " + phone.getDeviceName() + " has been marked as ended");
                                //add this phone to PhoneList table
                                PhoneList phoneList = phoneListFactory.create(bufferDeviceName);
                                daoFacade.getPhoneListDAO().create(phoneList);
                                logger.debug("Device " + phoneList.getDeviceName() + " has been added to PhoneList table");
                            }
                            //temporary login this phone with tech user to kick out previous user
                            cucmApiImplementation.doTempLogin(phone.getDeviceName());
                        }
                    }
                } catch (UncategorizedSQLException e) {
                    logger.trace("Phone " + bufferDeviceName + " " + bufferMessageTime + " already added in the DB");
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
