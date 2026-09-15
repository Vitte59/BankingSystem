package com.bank.repository;

import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findAll(Comparator<? super T> comparator);
    boolean deleteById(ID id);
}
