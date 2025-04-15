package ru.edme.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = {"currency", "issuingBank"})
    Optional<Account> findById(Long id);

    @EntityGraph(attributePaths = {"currency", "issuingBank"})
    List<Account> findAll();
}
