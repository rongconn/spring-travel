package com.project.travel.models;

import com.project.travel.enums.ETour;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "interests")
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Enumerated(EnumType.STRING)
    @Column(name = "key_name", length = 20)
    private ETour key;
    @NotNull
    private String name;

    public Integer getId() {
        return Id;
    }

    public ETour getKey() {
        return key;
    }

    public void setKey(ETour key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
