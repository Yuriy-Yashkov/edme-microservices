package ru.edme.service;

import java.util.List;

public interface AllService<K, T, U, E> {

    T save(U entity);

    E findById(K id);

    List<E> findAll();

    T update(U entity);

    boolean delete(K id);
}
