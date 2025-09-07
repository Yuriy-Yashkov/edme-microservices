package ru.edme.processing.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.processing.model.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @EntityGraph(attributePaths = {"cardStatus", "paymentSystem", "account", "account.currency", "account.issuingBank"})
    Optional<Card> findById(Long id);

    @EntityGraph(attributePaths = {"cardStatus", "paymentSystem", "account", "account.currency", "account.issuingBank"})
    List<Card> findAll();
}
