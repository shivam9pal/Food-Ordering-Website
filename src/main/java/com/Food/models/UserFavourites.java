package com.Food.models;
import com.Food.models.Resturant;
import com.Food.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_favourites")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFavourites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Resturant restaurant;

    @Column(name = "added_date")
    private LocalDateTime addedDate = LocalDateTime.now();
}