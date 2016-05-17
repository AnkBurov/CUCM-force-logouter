package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.cti.cucmforcelogouter.controller.dao.DAOFacade;
import ru.cti.cucmforcelogouter.model.dbmaintenance.AbstractDBMaintenance;
import ru.cti.cucmforcelogouter.model.dbmaintenance.DBMaintenance;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;
import ru.cti.cucmforcelogouter.model.factory.PhoneListFactory;

public class TestDB {
    ApplicationContext context = new AnnotationConfigApplicationContext(ru.cti.cucmforcelogouter.Configuration.class);

    private AbstractDBMaintenance dbMaintenance = context.getBean(AbstractDBMaintenance.class);
    private DAOFacade daoFacade = context.getBean(DAOFacade.class);
    private PhoneListFactory phoneListFactory = context.getBean(PhoneListFactory.class);
    private PhoneList phoneList;


    public DAOFacade getDaoFacade() {
        return daoFacade;
    }

    public void setDaoFacade(DAOFacade daoFacade) {
        this.daoFacade = daoFacade;
    }

    public PhoneListFactory getPhoneListFactory() {
        return phoneListFactory;
    }

    public void setPhoneListFactory(PhoneListFactory phoneListFactory) {
        this.phoneListFactory = phoneListFactory;
    }

    public AbstractDBMaintenance getDbMaintenance() {
        return dbMaintenance;
    }

    public void setDbMaintenance(AbstractDBMaintenance dbMaintenance) {
        this.dbMaintenance = dbMaintenance;
    }

    public PhoneList getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(PhoneList phoneList) {
        this.phoneList = phoneList;
    }

    @Before
    public void setUp() throws Exception {
        dbMaintenance.createDB();
        phoneList = phoneListFactory.create("SEP000000000000");
        daoFacade.getPhoneListDAO().create(phoneList);
        phoneList = daoFacade.getPhoneListDAO().readByDeviceName(phoneList.getDeviceName()).get(0);
    }

    @Test
    public void testSize() {
        int phoneListSize = daoFacade.getPhoneListDAO().readByDeviceName(phoneList.getDeviceName()).size();
        Assert.assertTrue(phoneListSize > 0);
    }

    @Test
    public void testRemoveOldPhonesFromPhoneList() {
        dbMaintenance.setMaximumPhoneListAge(0);
        int sizeOfRemovedPhones = dbMaintenance.removeOldPhonesFromPhoneList();
        Assert.assertTrue(sizeOfRemovedPhones > 0);
    }

    @After
    public void tearDown() throws Exception {
        daoFacade.getPhoneListDAO().delete(phoneList.getId());
    }
}
