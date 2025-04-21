package ru.edme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.BankSetting;

@Repository
public interface BankSettingRepository extends JpaRepository<BankSetting, Long> {

}
