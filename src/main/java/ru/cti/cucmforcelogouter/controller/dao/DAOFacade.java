package ru.cti.cucmforcelogouter.controller.dao;

import javax.sql.DataSource;

public class DAOFacade {
    private DataSource dataSource;

    public DAOFacade(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PhoneDAO getPhoneDAO() {
        return new PhoneDAO(dataSource);
    }
}
