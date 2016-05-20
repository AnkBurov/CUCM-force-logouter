package ru.cti.cucmforcelogouter.model.dbmaintenance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;

import java.util.List;

public class DBMaintenance extends AbstractDBMaintenance {
    private static final Logger logger = LoggerFactory.getLogger("Mine");

    /**
     * Remove old calls that older than specified amount of time
     */
    @Override
    public int removeOldPhones() {
        List<Phone> oldPhones = phoneRepository.findOldPhones(System.currentTimeMillis(), maximumPhoneAge);
        for (Phone oldPhone : oldPhones) {
            try {
                phoneRepository.delete(oldPhone);
                logger.info("Phone " + oldPhone.getDeviceName() + " with message time " + oldPhone.getMessageTime() +
                        " has been deleted");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return oldPhones.size();
    }

    /**
     * Remove old phones (device names) from PhoneList table which older than specified amount of time (maximumPhoneListAge)
     */
    @Override
    public int removeOldPhonesFromPhoneList() {
        long currentTime = System.currentTimeMillis();
        List<PhoneList> oldPhones = phoneListRepository.findOldPhones(currentTime, maximumPhoneListAge);
        for (PhoneList oldPhone : oldPhones) {
            try {
                phoneListRepository.delete(oldPhone);
                logger.info("Device " + oldPhone.getDeviceName() + " has been deleted from PhoneList due to exceeding " +
                        "maximumPhoneListAge");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return oldPhones.size();
    }
}
