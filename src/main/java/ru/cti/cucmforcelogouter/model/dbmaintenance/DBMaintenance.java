package ru.cti.cucmforcelogouter.model.dbmaintenance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;

import java.util.List;

public class DBMaintenance extends AbstractDBMaintenance {
    private static final Logger logger = LogManager.getLogger(DBMaintenance.class);

    /**
     * Create DB with needed scheme and tables if not exists
     */
    @Override
    public void createDB() {
        daoFacade.getPhoneDAO().createTable();
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
                e.printStackTrace();
                logger.catching(e);
            }
        }
        return phones.size();
    }
}
