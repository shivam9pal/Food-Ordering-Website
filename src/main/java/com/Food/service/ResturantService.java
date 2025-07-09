package com.Food.service;

import com.Food.dto.ResturantDto;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.request.CreateResturantRequest;

import java.util.List;

public interface ResturantService {


    public Resturant createResturant(CreateResturantRequest req, User user);


    public Resturant updateResturant(Long ResturantId, CreateResturantRequest createResturantRequest) throws Exception;

    public void deleteResturant (Long ResturantId) throws Exception;

    public List<Resturant> getAllResturant();

    public List<Resturant> searchResturant();

    public Resturant fundResturantById(Long id) throws Exception;

    public Resturant getResturantByUserId(Long UserId) throws Exception;

    public ResturantDto addFavourites(Long resturantId,User user) throws Exception;

    public Resturant updateResturantStatus(Long id) throws Exception;

}
