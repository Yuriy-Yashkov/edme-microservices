package ru.edme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

}
