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
@Table(name = "users") // Changed from "user" to "users" as "user" is a reserved keyword in MySQL
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Column(name = "id")
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

//    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER; // Set default value

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_favourites", joinColumns = @JoinColumn(name = "user_id"))
    private List<ResturantDto> favourites = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // This creates a foreign key in the Address table
    private List<Address> adresses = new ArrayList<>();
}