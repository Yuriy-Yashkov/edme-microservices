package ru.edme.issuing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.issuing.model.AccountType;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {

}
