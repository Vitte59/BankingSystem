package com.bank.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Comparator;

public class InMemoryRepository<T extends HasId<ID>, ID> implements CrudRepository<T, ID> {

    private final ConcurrentHashMap<ID ,T> storage = new ConcurrentHashMap<>();

    @Override
    public T save(T entity) {
        if(entity == null) {
            throw new IllegalArgumentException("сущность не может быть null");
        }
        if(entity.getId() == null) {
            throw new IllegalArgumentException("ID сущности не может быть null");
        }
        storage.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        if(id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(ID id) {
        if(id == null) {
            return false;
        }
        return storage.remove(id) != null;
    }

    @Override
    public List<T> findAll(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("компаратор не может быть null");
        }
        List<T> list = findAll();
        list.sort(comparator);
        return list;
    }


}
