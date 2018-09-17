package com.dev.bamboo.wuboookservice.controllers;


import com.dev.bamboo.wuboookservice.domains.Room;
import com.dev.bamboo.wuboookservice.repositories.LatestBookingPriceRepository;
import com.dev.bamboo.wuboookservice.services.WubookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
public class TestController {


    @Autowired
    WubookService wubookService;

    @Autowired
    LatestBookingPriceRepository latestBookingPriceRepository;


    @GetMapping("/testagg")
    public void testAverage() throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");


        Date date = parser.parse("2018-11-10");
        Date end = parser.parse("2018-11-15");
        System.out.println(latestBookingPriceRepository.getAvgPriceByOccupancyAndDate(date,2));
        System.out.println(latestBookingPriceRepository.getMaxPriceByOccupancyAndDate(date,2));
        System.out.println(latestBookingPriceRepository.getMinPriceByOccupancyAndDate(date,2));

        System.out.println(latestBookingPriceRepository.getAggregateByOccupancyAndDays(2,date,end));
    }

    @GetMapping("/getrooms")
    public ArrayList<Room> getRooms() {


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

    @GetMapping("/setprice")
    public String setPrice()
    {
        wubookService.updatePrice("17/09/2018",121.0D,39596L);
        return "OK";
    }
}
