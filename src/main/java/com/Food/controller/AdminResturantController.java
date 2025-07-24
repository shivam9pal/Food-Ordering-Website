package com.Food.controller;


import com.Food.models.Resturant;
import com.Food.models.User;
import com.Food.request.CreateResturantRequest;
import com.Food.response.MessageResponse;
import com.Food.service.ResturantService;
import com.Food.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/resturants")
public class AdminResturantController {

    @Autowired
    private ResturantService resturantService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<Resturant> createResturant(
            @RequestBody CreateResturantRequest req,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Resturant resturant=resturantService.createResturant(req,user);
        return new ResponseEntity<>(resturant, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resturant> updateResturant(
            @RequestBody CreateResturantRequest req,
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Resturant resturant=resturantService.updateResturant(id,req);
        return new ResponseEntity<>(resturant, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteResturant(

            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        resturantService.deleteResturant(id);
        MessageResponse res=new MessageResponse();
        res.setMessage("Resturant Deleted Successfully");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Resturant> updateResturantStatus(

            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id
    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Resturant resturant=resturantService.updateResturantStatus(id);
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }

    @GetMapping("user")
    public ResponseEntity<Resturant> findResturantByuserId(

            @RequestHeader("Authorization") String jwt

    ) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Resturant resturant=resturantService.findResturantById(user.getId());
        return new ResponseEntity<>(resturant, HttpStatus.OK);
    }
}
