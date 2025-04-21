package ru.edme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
