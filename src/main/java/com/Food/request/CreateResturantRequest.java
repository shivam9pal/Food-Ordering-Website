package com.Food.request;


import com.Food.models.ContactInfomation;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateResturantRequest {

    private Long id;
    private String name;
    private  String description;
    private String cuisineType;
    private  String address;
    private ContactInfomation contactInformation;
    private String openingHours;
    private List<String> images;


}
