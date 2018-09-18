package com.dev.bamboo.wuboookservice.controllers;

import com.dev.bamboo.wuboookservice.domains.Hotel;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HotelController {

    @Autowired
    HotelRepository hotelRepository;

    @PostMapping("/hotels")
    public Hotel createHotel(@RequestBody Hotel hotel){

        hotelRepository.save(hotel);

        return hotel;
    }

    @GetMapping("/hotels")
    public List<Hotel> getHotels(){
        return  hotelRepository.findAll();
    }

}
