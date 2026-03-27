package com.diy.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lecture {
    String name;
    String instructor;
    String category;
    int price;

    public Lecture(String name, String instructor, String category, int price) {
        this.name = name;
        this.instructor = instructor;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }
}
