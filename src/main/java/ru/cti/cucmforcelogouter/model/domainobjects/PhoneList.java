package ru.cti.cucmforcelogouter.model.domainobjects;

import javax.persistence.*;

/**
 * domain object for PhoneList table
 */
@Entity
public class PhoneList extends DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String deviceName;
    private long additionTime;

    public PhoneList() {
    }

    public PhoneList(String deviceName, long additionTime) {
        this.deviceName = deviceName;
        this.additionTime = additionTime;
    }

    public PhoneList(int id, String deviceName, long additionTime) {
        this.id = id;
        this.deviceName = deviceName;
        this.additionTime = additionTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public long getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(long additionTime) {
        this.additionTime = additionTime;
    }

    @Override
    public String toString() {
        return "PhoneList{" +
                "additionTime=" + additionTime +
                ", deviceName='" + deviceName + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneList phoneList = (PhoneList) o;

        if (id != phoneList.id) return false;
        if (additionTime != phoneList.additionTime) return false;
        return deviceName != null ? deviceName.equals(phoneList.deviceName) : phoneList.deviceName == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (int) (additionTime ^ (additionTime >>> 32));
        return result;
    }
}
