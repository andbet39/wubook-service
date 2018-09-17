package com.dev.bamboo.wuboookservice.controllers;

import com.dev.bamboo.wuboookservice.domains.*;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import com.dev.bamboo.wuboookservice.repositories.LatestBookingPriceRepository;
import com.dev.bamboo.wuboookservice.repositories.RoomDayPriceRepository;
import com.dev.bamboo.wuboookservice.services.BookingCrawler;
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
    BookingCrawler bookingCrawler;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomDayPriceRepository roomDayPriceRepository;

    @Autowired
    LatestBookingPriceRepository latestBookingPriceRepository;

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

            this.crawl(start,end,hotel);

        }

    }

    @GetMapping("/crawlhotel")
    public void crawlHotel(@RequestParam("startdt") String startdt,@RequestParam("enddt") String enddt,@RequestParam("hotel_id")Long hotel_id){

        //Optional<Hotel> h = hotelRepository.findById(1L);//findByHotelId("1351655");
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

        this.crawl(start,end,hotel);

    }


    private void crawl(LocalDate start,LocalDate end,Hotel hotel){


        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {

            String url = hotel.getBaseUrl()+"?checkin="+date+";checkout="+date.plusDays(1);
            System.out.println(url);

            List<CrawledPriceInfo> lst = bookingCrawler.CrawlPageURL(url);

            for (CrawledPriceInfo cpi :lst){

                this.writeCpi(cpi,hotel,date);

            }

        }
    }

    private void writeCpi(CrawledPriceInfo cpi,Hotel hotel,LocalDate date ){
        RoomDayPrice roomDayPrice = new RoomDayPrice();

        roomDayPrice.setCheckin(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        roomDayPrice.setCheckout(Date.from(date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        roomDayPrice.setAvailable(cpi.getAvailable());
        roomDayPrice.setPriceStr(cpi.getPrice());

        if(cpi.getPrice() != null){
            String s = cpi.getPrice().substring(2, cpi.getPrice().length()).replace(",",".");
            Float d = Float.valueOf(s);
            roomDayPrice.setPrice(d);
        }

        roomDayPrice.setRoomId(cpi.getRoomId());
        roomDayPrice.setRoomName(cpi.getRoomName());
        roomDayPrice.setOccupancy(cpi.getOccupancy());
        roomDayPrice.setCrawlTime(Instant.now().getEpochSecond());

        roomDayPrice.setHotel(hotel);

        roomDayPriceRepository.saveAndFlush(roomDayPrice);

        updateLatest(roomDayPrice);
    }


    private void updateLatest(RoomDayPrice roomDayPrice){

        LatestBookingPrice price =
                latestBookingPriceRepository.findByHotelIdAndRoomNameAndDateAndOccupancy(
                        roomDayPrice.getHotel().getId(),
                        roomDayPrice.getRoomName(),
                        roomDayPrice.getCheckin(),
                        roomDayPrice.getOccupancy());

        if(price==null){

            price=new LatestBookingPrice();
            price.setAvailable(roomDayPrice.getAvailable());
            price.setPrice(roomDayPrice.getPrice());
            price.setDate(roomDayPrice.getCheckin());
            price.setOccupancy(roomDayPrice.getOccupancy());
            price.setHotel(roomDayPrice.getHotel());
            price.setRoomName(roomDayPrice.getRoomName());
            price.setUpdate_time(new Date());

        }else{


            if(price.getPrice() != null && price.getPrice() > roomDayPrice.getPrice()){
                price.setPrice(roomDayPrice.getPrice());
                price.setUpdate_time(new Date());
            }
        }


        latestBookingPriceRepository.save(price);

    }





}
