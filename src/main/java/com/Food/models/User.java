package com.Food.models;


import com.Food.dto.ResturantDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullname;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Enumerated(EnumType.STRING)
    private USER_ROLE role;

    @JsonIgnore// beacause during use fetch we dont want to get orders list
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "customer")//we are mapping custoemr to lsut and telling spring not to make seperate table for this
    private List<Order> orders=new ArrayList<>();
    @ElementCollection// we used this to integrate anpther entity
    private List<ResturantDto> favourites;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)// cascade refers to delete all the users whenevre we delete the user all the adresses will be deleted
    private List<Address> adresses=new ArrayList<>();



}
