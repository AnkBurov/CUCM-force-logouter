package ru.cti.cucmforcelogouter.model.dbmaintenance;

import org.springframework.beans.factory.annotation.Autowired;
import ru.cti.cucmforcelogouter.controller.dao.DAOFacade;

public abstract class AbstractDBMaintenance {
    @Autowired
    protected DAOFacade daoFacade;
    protected long maximumPhoneAge;

    public AbstractDBMaintenance() {
    }

    /**
     * @param maximumPhoneAge number of HOURS
     */
    public void setMaximumPhoneAge(int maximumPhoneAge) {
        this.maximumPhoneAge = maximumPhoneAge * 3600000;
    }

    /**
     * Create DB with needed scheme and tables if not exists
     */
    public abstract void createDB();

    /**
     * Remove old calls that older than specified amount of time
     */
    public abstract int removeOldPhones();
}
