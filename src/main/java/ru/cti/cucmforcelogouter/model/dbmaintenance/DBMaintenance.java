package ru.cti.cucmforcelogouter.model.dbmaintenance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;

import java.util.List;

public class DBMaintenance extends AbstractDBMaintenance {
    private static final Logger logger = LoggerFactory.getLogger("Mine");

    /**
     * Create DB with needed scheme and tables if not exists
     */
    @Override
    public void createDB() {
        daoFacade.getPhoneDAO().createTable();
        daoFacade.getPhoneListDAO().createTable();
    }

    /**
     * Remove old calls that older than specified amount of time
     */
    @Override
    public int removeOldPhones() {
        List<Phone> phones = daoFacade.getPhoneDAO().getAllOldCalls(System.currentTimeMillis(), maximumPhoneAge);
        for (Phone phone : phones) {
            try {
                if (daoFacade.getPhoneDAO().delete(phone.getId()) == 1) {
                    logger.info("Phone " + phone.getDeviceName() + " with message time " + phone.getMessageTime() +
                            " has been deleted");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return phones.size();
    }

    /**
     * Remove old phones (device names) from PhoneList table which older than specified amount of time (maximumPhoneListAge)
     */
    @Override
    public int removeOldPhonesFromPhoneList() {
        List<PhoneList> oldPhones = daoFacade.getPhoneListDAO().getAllOldPhones(System.currentTimeMillis(), maximumPhoneListAge);
        for (PhoneList oldPhone : oldPhones) {
            try {
                if (daoFacade.getPhoneListDAO().delete(oldPhone.getId()) == 1) {
                    logger.info("Device " + oldPhone.getDeviceName() + " has been deleted from PhoneList due to exceeding " +
                            "maximumPhoneListAge");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return oldPhones.size();
    }
}
