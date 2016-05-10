package ru.cti.cucmforcelogouter.model.factory;

import ru.cti.cucmforcelogouter.model.domainobjects.Phone;

public class PhoneFactory extends DomainEntitiesFactory<Phone> {
    @Override
    public Phone create() {
        return new Phone();
    }

    public Phone create(String deviceName, String messageTime) {
        return new Phone(deviceName, messageTime, false, System.currentTimeMillis());
    }

    public Phone create(int id, String deviceName, String messageTime) {
        return new Phone(id, deviceName, messageTime, false, System.currentTimeMillis());
    }
}
