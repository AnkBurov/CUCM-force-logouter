package ru.cti.cucmforcelogouter.model.domainobjects;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"deviceName", "messageTime"}))
public class Phone extends DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", messageTime='" + messageTime + '\'' +
                ", isEnded=" + isEnded +
                ", additionTime=" + additionTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;

        if (id != phone.id) return false;
        if (isEnded != phone.isEnded) return false;
        if (additionTime != phone.additionTime) return false;
        if (deviceName != null ? !deviceName.equals(phone.deviceName) : phone.deviceName != null) return false;
        return messageTime != null ? messageTime.equals(phone.messageTime) : phone.messageTime == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (deviceName != null ? deviceName.hashCode() : 0);
        result = 31 * result + (messageTime != null ? messageTime.hashCode() : 0);
        result = 31 * result + (isEnded ? 1 : 0);
        result = 31 * result + (int) (additionTime ^ (additionTime >>> 32));
        return result;
    }
}
