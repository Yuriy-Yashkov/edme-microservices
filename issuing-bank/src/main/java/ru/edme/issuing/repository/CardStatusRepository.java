package ru.edme.issuing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.issuing.model.CardStatus;

@Repository
public interface CardStatusRepository extends JpaRepository<CardStatus, Long> {

}
