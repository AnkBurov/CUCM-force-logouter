package ru.cti.cucmforcelogouter.model.factory;

import ru.cti.cucmforcelogouter.model.domainobjects.Phone;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;

/**
 * Factory for domain object PhoneList
 */
public class PhoneListFactory extends DomainEntitiesFactory<PhoneList> {
    @Override
    public PhoneList create() {
        return new PhoneList();
    }

    public PhoneList create(String deviceName) {
        return new PhoneList(deviceName, System.currentTimeMillis());
    }

    public PhoneList create(int id, String deviceName) {
        return new PhoneList(id, deviceName, System.currentTimeMillis());
    }
}
