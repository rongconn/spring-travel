package com.project.travel.models;

import com.project.travel.enums.ECate;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "key_name", length = 50)
    private ECate key;
    @NotNull
    @Nationalized
    private String name;

    private String image;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "tour_category",
//            joinColumns = @JoinColumn(name = "category_id"),
//            inverseJoinColumns = @JoinColumn(name = "tour_id"))
//    private Set<Tour> tours;

    public Integer getId() {
        return id;
    }


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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public Set<Tour> getTours() {
//        return tours;
//    }
}
