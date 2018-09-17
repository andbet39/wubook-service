package com.dev.bamboo.wuboookservice.controllers;


import com.dev.bamboo.wuboookservice.domains.Room;
import com.dev.bamboo.wuboookservice.services.WubookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestController {


    @Autowired
    WubookService wubookService;

    @GetMapping("/getrooms")
    public ArrayList<Room> getRooms(){


        return  wubookService.fetchRoom();

    }

    @GetMapping("/getpricingplan")
    public String getPricingPlan(){

        wubookService.getPricingPlan();
        return  "OK";

    }

    @GetMapping("/getprice")
    public String getPrice(){

        Double price = wubookService.getPrice("17/09/2018",39596L);
        return price.toString();

    }
}
