package ru.edme.service;

import java.util.List;

public interface AllService<K, U, E> {

    E save(U entity);

    E findById(K id);

    List<E> findAll();

    E update(U entity);

    boolean delete(K id);
}
