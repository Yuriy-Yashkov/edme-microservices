package ru.edme.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.sales.model.PaymentSystem;

@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {

}
