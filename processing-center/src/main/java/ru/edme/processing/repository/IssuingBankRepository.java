package ru.edme.processing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.processing.model.IssuingBank;

@Repository
public interface IssuingBankRepository extends JpaRepository<IssuingBank, Long> {

}
