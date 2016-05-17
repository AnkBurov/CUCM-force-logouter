package ru.cti.cucmforcelogouter.controller.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO object for PhoneList entity
 *
 * id integer primary key not null
 * deviceName TEXT not null UNIQUE
 * additionTime long not null
 */
public class PhoneListDAO implements GenericDAO<PhoneList, Integer> {
    private JdbcTemplate jdbcTemplate;

    public PhoneListDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createTable() {
        jdbcTemplate.update("create table if not exists PhoneList (id integer primary key not null, deviceName TEXT not null, additionTime long not null, UNIQUE (deviceName));");
    }

    @Override
    public int create(PhoneList entity) {
        return jdbcTemplate.update("INSERT INTO PhoneList VALUES (NULL, ?, ?);", entity.getDeviceName(), entity.getAdditionTime());
    }

    @Override
    public List<PhoneList> getAll() {
        return jdbcTemplate.query("SELECT * FROM PhoneList;", new ItemMapper());
    }

    public List<PhoneList> getAllOldPhones(long currentTime, long maximumPhoneListAge) {
        return jdbcTemplate.query("SELECT * FROM PhoneList where " + currentTime + " - additionTime > " + maximumPhoneListAge + ";", new ItemMapper());
    }

    @Override
    public PhoneList read(Integer key) {
        return jdbcTemplate.queryForObject("SELECT * FROM PhoneList where id = ?;", new ItemMapper(), key);
    }

    @Override
    public int update(PhoneList entity) {
        return jdbcTemplate.update("UPDATE PhoneList SET deviceName = ?, additionTime = ? where id = ?;",
                entity.getDeviceName(), entity.getAdditionTime(), entity.getId());
    }

    @Override
    public int delete(Integer key) {
        return jdbcTemplate.update("delete FROM PhoneList where id = ?;", key);
    }

    private static class ItemMapper implements RowMapper<PhoneList> {
        @Override
        public PhoneList mapRow(ResultSet rs, int i) throws SQLException {
            PhoneList entity = new PhoneList(rs.getInt(1), rs.getString(2), rs.getLong(3));
            return entity;
        }
    }
}
