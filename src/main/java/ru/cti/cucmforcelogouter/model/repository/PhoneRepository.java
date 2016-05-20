package ru.cti.cucmforcelogouter.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;

import java.util.List;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, Integer> {
    //getOldCalls
    /*SELECT * FROM Phones where " + currentTime + " - additionTime > " + maximumPhoneAge + "*/
    @Query("SELECT p from Phone p where :currentTime - p.additionTime > :maximumPhoneAge")
    List<Phone> findOldCalls(long currentTime, long maximumPhoneAge);

    //readNotClosedPhoneByDeviceNameAndTime
    /*SELECT * FROM Phones where deviceName = ? and messageTime = ? and isEnded = 0*/
    @Query("SELECT p from Phone p where p.deviceName = :deviceName and p.messageTime = :messageTime and p.isEnded = false ")
    Phone findOneNotClosedPhoneByDeviceNameAndTime(String deviceName, String messageTime);
}
