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
import ru.cti.cucmforcelogouter.model.dbmaintenance.AbstractDBMaintenance;
import ru.cti.cucmforcelogouter.model.dbmaintenance.DBMaintenance;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;
import ru.cti.cucmforcelogouter.model.factory.PhoneListFactory;
import ru.cti.cucmforcelogouter.model.repository.PhoneListRepository;

@ContextConfiguration(classes = ru.cti.cucmforcelogouter.Configuration.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDB {
    ApplicationContext context = new AnnotationConfigApplicationContext(ru.cti.cucmforcelogouter.Configuration.class);
    @Autowired
    private PhoneListRepository phoneListRepository;
    @Autowired
    private AbstractDBMaintenance dbMaintenance;
    @Autowired
    private PhoneListFactory phoneListFactory;
    private PhoneList phoneList;

    @Before
    public void setUp() {
        phoneList = phoneListRepository.save(phoneListFactory.create("SEP000000000000"));
    }

    @Test
    public void testSize() {
        Assert.assertTrue(phoneList != null);
    }

    @Test
    public void testRemoveOldPhonesFromPhoneList() {
        dbMaintenance.setMaximumPhoneListAge(0);
        int sizeOfRemovedPhones = dbMaintenance.removeOldPhonesFromPhoneList();
        Assert.assertTrue(sizeOfRemovedPhones > 0);
    }

    @After
    public void tearDown() {
        phoneListRepository.delete(phoneList);
    }
}
