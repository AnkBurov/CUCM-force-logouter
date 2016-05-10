package ru.cti.cucmforcelogouter.model.factory;


import ru.cti.cucmforcelogouter.model.domainobjects.DomainEntity;

public abstract class DomainEntitiesFactory<T extends DomainEntity> {
    public DomainEntitiesFactory() {
    }

    public abstract T create();
}
