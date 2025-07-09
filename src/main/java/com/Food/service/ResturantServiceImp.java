package com.Food.service;

import com.Food.dto.ResturantDto;
import com.Food.models.Address;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.repository.AddressRepository;
import com.Food.repository.ResturantRepository;
import com.Food.request.CreateResturantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ResturantServiceImp implements ResturantService {

    @Autowired
    private ResturantRepository resturantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Override
    public Resturant createResturant(CreateResturantRequest req, User user) {
        Address address=addressRepository.save(req.getAddress());

        Resturant resturant=new Resturant();
        resturant.setAddress(address);
        resturant.setContactInfomation(req.getContactInformation());
        resturant.setCuisineType(req.getCuisineType());
        resturant.setDescription(req.getDescription());
        resturant.setImages(req.getImages());
        resturant.setName(req.getName());
        resturant.setOpeningHours(resturant.getOpeningHours());
        resturant.setRegistrationDate(LocalDateTime.now());
        resturant.setOwner(user);

        return resturantRepository.save(resturant);
    }

    @Override
    public Resturant updateResturant(Long ResturantId, CreateResturantRequest createResturantRequest) throws Exception {
        Resturant resturant=fundResturantById(ResturantId);
        if(resturant.getCuisineType()!=null){
            resturant.setCuisineType(upda);
        }
        return null;
    }

    @Override
    public void deleteResturant(Long ResturantId) throws Exception {

    }

    @Override
    public List<Resturant> getAllResturant() {
        return List.of();
    }

    @Override
    public List<Resturant> searchResturant() {
        return List.of();
    }

    @Override
    public Resturant fundResturantById(Long id) throws Exception {
        return null;
    }

    @Override
    public Resturant getResturantByUserId(Long UserId) throws Exception {
        return null;
    }

    @Override
    public ResturantDto addFavourites(Long resturantId, User user) throws Exception {
        return null;
    }

    @Override
    public Resturant updateResturantStatus(Long id) throws Exception {
        return null;
    }
}
