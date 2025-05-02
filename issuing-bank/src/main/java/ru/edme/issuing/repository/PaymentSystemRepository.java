package ru.edme.issuing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.issuing.model.PaymentSystem;

@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {

}
