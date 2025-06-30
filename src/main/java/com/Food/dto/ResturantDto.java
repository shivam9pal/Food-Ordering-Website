package com.Food.dto;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Embeddable// used when we want to insert into another entity
public class ResturantDto {
    @Id
    private Long id;
    private String title;
    @Column(length=1000)
    private List<String> images;
    private String description;

}
