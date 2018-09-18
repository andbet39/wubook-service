package com.dev.bamboo.wuboookservice.services;

import com.dev.bamboo.wuboookservice.domains.CrawledPriceInfo;
import com.dev.bamboo.wuboookservice.domains.Hotel;
import com.dev.bamboo.wuboookservice.domains.LatestBookingPrice;
import com.dev.bamboo.wuboookservice.domains.RoomDayPrice;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import com.dev.bamboo.wuboookservice.repositories.LatestBookingPriceRepository;
import com.dev.bamboo.wuboookservice.repositories.RoomDayPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Service
public class CrawlerService {

    @Autowired
    BookingCrawler bookingCrawler;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomDayPriceRepository roomDayPriceRepository;

    @Autowired
    LatestBookingPriceRepository latestBookingPriceRepository;

    @Autowired
    CrawlerService crawlerService;

    public void crawl(LocalDate start, LocalDate end, Hotel hotel){


        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {

            String url = hotel.getBaseUrl()+"?checkin="+date+";checkout="+date.plusDays(1);
            System.out.println(url);

            List<CrawledPriceInfo> lst = bookingCrawler.CrawlPageURL(url);

            Date dt = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            //Metto tutti i prezzi per quel giorno a non updatato
            List<LatestBookingPrice> lbs =  latestBookingPriceRepository.getAllByHotelAndDate(hotel,dt);
            for(LatestBookingPrice lb :lbs){
                lb.setUpdated(false);
                latestBookingPriceRepository.saveAndFlush(lb);
            }


            for (CrawledPriceInfo cpi :lst){
                this.writeCpi(cpi,hotel,date);
            }

        }
    }

    public void writeCpi(CrawledPriceInfo cpi,Hotel hotel,LocalDate date ){
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


    public void updateLatest(RoomDayPrice roomDayPrice){

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


            if(price.getPrice() > roomDayPrice.getPrice() && price.isUpdated()){
                //Ho trovato un prezzo migliore
                price.setPrice(roomDayPrice.getPrice());
                price.setUpdate_time(new Date());

            }else{

                price.setPrice(roomDayPrice.getPrice());
                price.setUpdate_time(new Date());
                price.setUpdated(true);

            }
        }


        latestBookingPriceRepository.save(price);

    }

}
