package com.bank.client;

import com.bank.repository.HasId;


public class Client implements HasId<String> {
    private final String id;
    private final String name;
    private final int age; // Возраст клиента

    public Client(String id, String name, int age) {
        if (age < 18) {
            throw new IllegalArgumentException("Клиент должен быть совершеннолетним");
        }
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}