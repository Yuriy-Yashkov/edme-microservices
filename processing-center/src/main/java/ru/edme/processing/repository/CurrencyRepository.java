package ru.edme.processing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.processing.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

}
