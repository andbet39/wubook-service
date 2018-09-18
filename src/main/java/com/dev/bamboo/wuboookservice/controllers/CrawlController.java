package com.dev.bamboo.wuboookservice.controllers;

import com.dev.bamboo.wuboookservice.domains.*;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import com.dev.bamboo.wuboookservice.repositories.LatestBookingPriceRepository;
import com.dev.bamboo.wuboookservice.repositories.RoomDayPriceRepository;
import com.dev.bamboo.wuboookservice.services.BookingCrawler;
import com.dev.bamboo.wuboookservice.services.CrawlerService;
import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class CrawlController {

     @Autowired
    HotelRepository hotelRepository;

    @Autowired
    CrawlerService crawlerService;

    @GetMapping("/crawl")
    public void testCrawler(@RequestParam("startdt") String startdt,@RequestParam("enddt") String enddt){

        List<Hotel> lhotels = hotelRepository.findAll();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;

        try {
            endDate = formatter.parse(enddt);
            startDate = formatter.parse(startdt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Hotel hotel :lhotels){
            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            crawlerService.crawl(start,end,hotel);
        }

    }

    @GetMapping("/crawlhotel")
    public void crawlHotel(@RequestParam("startdt") String startdt,@RequestParam("enddt") String enddt,@RequestParam("hotel_id")Long hotel_id){

         Hotel hotel = hotelRepository.findById(hotel_id).get();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;

        try {
            endDate = formatter.parse(enddt);
            startDate = formatter.parse(startdt);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        crawlerService.crawl(start,end,hotel);

    }

}
