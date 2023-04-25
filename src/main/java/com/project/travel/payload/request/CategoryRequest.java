package com.project.travel.payload.request;

import com.project.travel.enums.ECate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class CategoryRequest {
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ECate key;

    @NotNull
    private String name;

    public ECate getKey() {
        return key;
    }

    public void setKey(ECate key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
