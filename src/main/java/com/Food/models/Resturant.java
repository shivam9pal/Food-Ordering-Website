package com.Food.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resturant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User owner;

    private String name;
    private String description;
    private String cuisineType;

    @OneToOne
    private Address address;

    @Embedded
    private ContactInfomation contactInfomation;

    private String openingHours;

    @OneToMany(mappedBy = "resturant",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Order> orders=new ArrayList<>();

    @ElementCollection
    @Column(length = 1000)
    private List<String> images=new ArrayList<>();

    private LocalDateTime registrationDate;

    private boolean open;

    @JsonIgnore
    @OneToOne(mappedBy = "resturant",cascade = CascadeType.ALL)
    private List<Food> foods =new ArrayList<>();




}
