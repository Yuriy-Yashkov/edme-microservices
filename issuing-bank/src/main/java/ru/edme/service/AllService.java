package ru.edme.service;

import java.util.List;

public interface AllService<T, K> {

    T save(T entity);

    T findById(K id);

    List<T> findAll();

    T update(T entity);

    boolean delete(K id);
}
