package org.example;

import java.math.BigDecimal;

import org.example.ValidationUtils.*;

public class Journey {
    public Journey(String name, String description, BigDecimal price) {
        setName(name);
        setDescription(description);
        setPrice(price);
    }

    private String name;
    private String description;
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = ValidationUtils.lengthShouldBe(name, 1, 30);
    }

    public void setDescription(String description) {
        this.description = ValidationUtils.lengthShouldBe(description, 0, 300);
    }

    public void setPrice(BigDecimal price) {
        this.price = ValidationUtils.shouldBeBiggerThan(price, 1);
    }
}