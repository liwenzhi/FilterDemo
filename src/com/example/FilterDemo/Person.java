package com.example.FilterDemo;

/**
 *  Bean
 */
public class Person {

    public String name;
    public String home;

    public Person(String name, String home) {
        this.name = name;
        this.home = home;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", home='" + home + '\'' +
                '}';
    }
}