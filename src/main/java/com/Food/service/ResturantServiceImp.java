package com.Food.service;

import com.Food.dto.ResturantDto;
import com.Food.models.Address;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.models.UserFavourites;
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
    public UserFavourites addFavourites(Long restaurantId, User user) throws Exception {
        Resturant restaurant = findResturantById(restaurantId);

        // Check if already exists
        Optional<UserFavourites> existing = user.getFavourites().stream()
                .filter(fav -> fav.getRestaurant().getId().equals(restaurantId))
                .findFirst();

        if (existing.isPresent()) {
            // Remove from favourites
            user.getFavourites().remove(existing.get());
            userRepository.save(user);
            return null; // or return a status indicator
        } else {
            // Add to favourites
            UserFavourites favourite = new UserFavourites();
            favourite.setUser(user);
            favourite.setRestaurant(restaurant);
            user.getFavourites().add(favourite);
            userRepository.save(user);
            return favourite;
        }
    }

    @Override
    public Resturant updateResturantStatus(Long id) throws Exception {

        Resturant resturant=findResturantById(id);
        resturant.setOpen(!resturant.isOpen());
        return resturantRepository.save(resturant);
    }
}
