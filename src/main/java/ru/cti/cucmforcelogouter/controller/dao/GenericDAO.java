package ru.cti.cucmforcelogouter.controller.dao;


import ru.cti.cucmforcelogouter.model.domainobjects.DomainEntity;

import java.util.List;

public interface GenericDAO<E extends DomainEntity, K extends Number> {
    void createTable();

    int create(E entity);

    List<E> getAll();

    E read(K key);

    int update(E entity);

    int delete(K key);
}