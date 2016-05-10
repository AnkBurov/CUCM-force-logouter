package ru.cti.cucmforcelogouter.controller.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PhoneDAO implements GenericDAO<Phone, Integer> {
    private JdbcTemplate jdbcTemplate;

    public PhoneDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createTable() {
        jdbcTemplate.update("create table if not exists Phones (id integer primary key not null, deviceName TEXT not null, " +
                "messageTime text not null, isEnded boolean not null, additionTime long not null, UNIQUE (deviceName, messageTime));");
    }

    @Override
    public int create(Phone entity) {
        return jdbcTemplate.update("INSERT INTO Phones VALUES (NULL, ?, ?, ?, ?);", entity.getDeviceName(), entity.getMessageTime(),
                entity.isEnded(), entity.getAdditionTime());
    }

    @Override
    public List<Phone> getAll() {
        return jdbcTemplate.query("SELECT * FROM Phones;", new ItemMapper());
    }

    public List<Phone> getAllOldCalls(long currentTime, long maximumPhoneAge) {
        return jdbcTemplate.query("SELECT * FROM Phones where " + currentTime + " - additionTime > " + maximumPhoneAge + ";", new ItemMapper());
    }

    @Override
    public Phone read(Integer key) {
        return jdbcTemplate.queryForObject("SELECT * FROM Phones where id = ?;", new ItemMapper(), key);
    }

    public Phone readNotClosedPhoneByDeviceNameAndTime(String deviceName, String messageTime) {
        return jdbcTemplate.queryForObject("SELECT * FROM Phones where deviceName = ? and messageTime = ? and isEnded = 0;",
                new ItemMapper(), deviceName, messageTime);
    }

    @Override
    public int update(Phone entity) {
        return jdbcTemplate.update("UPDATE Phones SET deviceName = ?, messageTime = ?, isEnded = ?, additionTime = ? where id = ?;",
                entity.getDeviceName(), entity.getMessageTime(), entity.isEnded(), entity.getAdditionTime(), entity.getId());
    }

    @Override
    public int delete(Integer key) {
        return jdbcTemplate.update("delete FROM Phones where id = ?;", key);
    }

    private static class ItemMapper implements RowMapper<Phone> {
        @Override
        public Phone mapRow(ResultSet rs, int i) throws SQLException {
            Phone entity = new Phone(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getLong(5));
            return entity;
        }
    }
}
