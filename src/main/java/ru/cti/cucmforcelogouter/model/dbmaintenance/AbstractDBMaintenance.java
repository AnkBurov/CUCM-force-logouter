package ru.cti.cucmforcelogouter.model.dbmaintenance;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cti.cucmforcelogouter.model.repository.PhoneListRepository;
import ru.cti.cucmforcelogouter.model.repository.PhoneRepository;

public abstract class AbstractDBMaintenance {
    @Autowired
    protected PhoneRepository phoneRepository;
    @Autowired
    protected PhoneListRepository phoneListRepository;
    protected long maximumPhoneAge;
    protected long maximumPhoneListAge;

    public AbstractDBMaintenance() {
    }

    /**
     * @param maximumPhoneAge number of HOURS
     */
    public void setMaximumPhoneAge(int maximumPhoneAge) {
        this.maximumPhoneAge = maximumPhoneAge * 3600000;
    }

    /**
     * @param maximumPhoneListAge number of HOURS
     */
    public void setMaximumPhoneListAge(int maximumPhoneListAge) {
        this.maximumPhoneListAge = maximumPhoneListAge * 3600000;
    }

    /**
     * Remove old calls that older than specified amount of time
     */
    public abstract int removeOldPhones();

    /**
     * Remove old phones (device names) from PhoneList table which older than specified amount of time (maximumPhoneListAge)
     */
    public abstract int removeOldPhonesFromPhoneList();
}
