package com.Food.service;

import com.Food.dto.ResturantDto;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.models.UserFavourites;
import com.Food.request.CreateResturantRequest;

import java.util.List;

public interface ResturantService {


    public Resturant createResturant(CreateResturantRequest req, User user);


    public Resturant updateResturant(Long ResturantId, CreateResturantRequest updatedResturant) throws Exception;

    public void deleteResturant (Long ResturantId) throws Exception;

    public List<Resturant> getAllResturant();

    public List<Resturant> searchResturant(String keyword);

    public Resturant findResturantById(Long id) throws Exception;

    public Resturant getResturantByUserId(Long userId) throws Exception;

    public UserFavourites addFavourites(Long resturantId, User user) throws Exception;

    public Resturant updateResturantStatus(Long id) throws Exception;

}
