package com.example.order_service.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String country;
    private String postalCode;

    // getter/setter
}