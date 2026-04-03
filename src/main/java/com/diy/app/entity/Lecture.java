package com.diy.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lecture {
    Long id;
    String name;
    int price;

    public Lecture() {}

    public Lecture(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
