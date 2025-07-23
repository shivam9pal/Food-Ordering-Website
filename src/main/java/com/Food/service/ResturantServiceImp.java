package com.Food.service;

import com.Food.dto.ResturantDto;
import com.Food.models.Address;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.repository.AddressRepository;
import com.Food.repository.ResturantRepository;
import com.Food.repository.UserRepository;
import com.Food.request.CreateResturantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ResturantServiceImp implements ResturantService {

    @Autowired
    private ResturantRepository resturantRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
    public Resturant updateResturant(Long ResturantId, CreateResturantRequest updatedResturant) throws Exception {
        Resturant resturant= findResturantById(ResturantId);
        if(resturant.getCuisineType()!=null){
            resturant.setCuisineType(updatedResturant.getCuisineType());
        }
        if(resturant.getDescription()!=null){
            resturant.setDescription(updatedResturant.getDescription());
        }
        if(resturant.getName()!=null){
            resturant.setName(updatedResturant.getName());
        }
        return resturantRepository.save(resturant);
    }

    @Override
    public void deleteResturant(Long resturantId) throws Exception {
        Resturant resturant=findResturantById(resturantId);
        resturantRepository.delete(resturant);
    }

    @Override
    public List<Resturant> getAllResturant() {
        return resturantRepository.findAll();
    }

    @Override
    public List<Resturant> searchResturant(String keyword) {
        return resturantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Resturant findResturantById(Long id) throws Exception {
        Optional<Resturant> opt=resturantRepository.findById(id);
        if(opt.isEmpty()){
            throw new Exception(" Resturant not find By Id"+id);
        }
        return opt.get();
    }

    @Override
    public Resturant getResturantByUserId(Long userId) throws Exception {
        Resturant resturant=resturantRepository.findByOwnerId(userId);
        if(resturant==null){
            throw new Exception("Resturant nit find By user ID"+ userId);
        }
        return resturant;
    }

    @Override
    public ResturantDto addFavourites(Long resturantId, User user) throws Exception {

        Resturant resturant= findResturantById(resturantId);
        ResturantDto dto=new ResturantDto();
        dto.setDescription(resturant.getDescription());
        dto.setImages(resturant.getImages());
        dto.setTitle(resturant.getName());
        dto.setId(resturantId);

        if(user.getFavourites().contains(dto)){
            user.getFavourites().remove(dto);
        }else{
            user.getFavourites().add(dto);
        }
        userRepository.save(user);
        return dto;
    }

    @Override
    public Resturant updateResturantStatus(Long id) throws Exception {

        Resturant resturant=findResturantById(id);
        resturant.setOpen(!resturant.isOpen());
        return resturantRepository.save(resturant);
    }
}
