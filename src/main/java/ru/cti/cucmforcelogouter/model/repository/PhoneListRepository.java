package ru.cti.cucmforcelogouter.model.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.cti.cucmforcelogouter.model.domainobjects.Phone;
import ru.cti.cucmforcelogouter.model.domainobjects.PhoneList;

import java.util.List;

@Repository
public interface PhoneListRepository extends CrudRepository<PhoneList, Integer> {
    @Query("SELECT p from PhoneList p where :currentTime - p.additionTime > :maximumPhoneAge")
    List<PhoneList> findOldPhones(@Param("currentTime") long currentTime, @Param("maximumPhoneAge") long maximumPhoneAge);

    //SELECT * FROM PhoneList where deviceName = ?
    PhoneList findOneByDeviceName(String deviceName);
}
