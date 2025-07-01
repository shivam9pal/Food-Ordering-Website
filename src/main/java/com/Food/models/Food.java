package com.Food.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    private Long id;
    private String name;
    private  String description;
    private Long price;

    @ManyToOne
    private Category foodCategory;
    @Column(length = 1000)
    @ElementCollection// by this spring will create seperate table for this
    private List<String> images;

    private boolean available;

    @ManyToOne
    private Resturant resturant;

    private boolean isVegetarian;
    private boolean isSeasonal;

    @ManyToMany
    private List<IngredientsItem> ingredients =new ArrayList<>();

    private Date creationDate;


}
