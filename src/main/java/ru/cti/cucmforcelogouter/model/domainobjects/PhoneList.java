package ru.cti.cucmforcelogouter.model.domainobjects;

/**
 * domain object for PhoneList table
 */
public class PhoneList extends DomainEntity {
    private int id;
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
}
