package ru.cti.cucmforcelogouter.model.domainobjects;

public class Phone extends DomainEntity {
    private int id;
    private String deviceName;
    private String messageTime;
    private boolean isEnded;
    private long additionTime;

    public Phone() {
    }

    public Phone(String deviceName, String messageTime, boolean isEnded, long additionTime) {
        this.deviceName = deviceName;
        this.messageTime = messageTime;
        this.isEnded = isEnded;
        this.additionTime = additionTime;
    }

    public Phone(int id, String deviceName, String messageTime, boolean isEnded, long additionTime) {
        this.id = id;
        this.deviceName = deviceName;
        this.messageTime = messageTime;
        this.isEnded = isEnded;
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

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public long getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(long additionTime) {
        this.additionTime = additionTime;
    }
}
