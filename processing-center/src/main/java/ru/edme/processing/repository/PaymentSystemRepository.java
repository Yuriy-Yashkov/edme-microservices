package ru.edme.processing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.processing.model.PaymentSystem;

@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {

}
