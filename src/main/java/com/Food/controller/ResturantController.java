package com.Food.controller;


import com.Food.dto.ResturantDto;
import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.request.CreateResturantRequest;
import com.Food.service.ResturantService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resturants")
public class ResturantController {

    @Autowired
    private ResturantService resturantService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<Resturant>> searchResturant(
           @RequestParam String keyword,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        List<Resturant> resturant=resturantService.searchResturant(keyword);
        return new ResponseEntity<>(resturant, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Resturant>> getAllResturant(

            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        List<Resturant> resturant=resturantService.getAllResturant();
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resturant> findResturantById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Resturant resturant=resturantService.findResturantById(id);
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

    @PutMapping("/{id}/add-favorite")
    public ResponseEntity<ResturantDto> addToFavorites(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        ResturantDto resturant=resturantService.addFavourites(id,user);
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }
}
