package com.diy.app.domain;

public class Lecture {
    Long id;
    String name;
    Long price;


    public Lecture() {

    }

    public Lecture(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }


}
