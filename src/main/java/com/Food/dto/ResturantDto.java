package com.Food.dto;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
@Data
@Embeddable// used when we want to insert into another entity
public class ResturantDto {
    private String title;
    @Column(length=1000)
    private List<String> images;
    private String description;

}
