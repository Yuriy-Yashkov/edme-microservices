package ru.edme.issuing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.issuing.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
