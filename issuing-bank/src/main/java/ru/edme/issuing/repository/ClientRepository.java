package ru.edme.issuing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.issuing.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
