package ru.edme.processing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.processing.model.CardStatus;

@Repository
public interface CardStatusRepository extends JpaRepository<CardStatus, Long> {

}
