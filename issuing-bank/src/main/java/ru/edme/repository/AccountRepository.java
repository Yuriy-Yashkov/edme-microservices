package ru.edme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
