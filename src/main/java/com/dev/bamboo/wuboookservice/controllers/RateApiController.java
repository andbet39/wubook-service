package com.dev.bamboo.wuboookservice.controllers;

import com.dev.bamboo.wuboookservice.domains.Hotel;
import com.dev.bamboo.wuboookservice.domains.RateInfo;
import com.dev.bamboo.wuboookservice.domains.RoomDayPrice;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import com.dev.bamboo.wuboookservice.repositories.RoomDayPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RateApiController {


    @Autowired
    RoomDayPriceRepository roomDayPriceRepository;

    @Autowired
    HotelRepository hotelRepository;


    @GetMapping("/getlowests")
    public Map<String,List<RoomDayPrice>> getLowestByDateOccupancy(@RequestParam("occupancy") Integer occupancy,
                                                                   @RequestParam("checkin") String checkin,
                                                                   @RequestParam("days")Integer days) throws Exception {

        List<Hotel> lstHotels = hotelRepository.findAll();
        Map<String,List<RoomDayPrice>> resp= new HashMap<>();

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = parser.parse(checkin);

        LocalDate start = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = start.plusDays(days);

        for(Hotel hotel : lstHotels){
            List<RoomDayPrice> res = new ArrayList<>();

            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {

                RoomDayPrice rate = roomDayPriceRepository.getLowestPriceByOccupancyCheckinAndHotel(date.toString(), occupancy, hotel.getId());

                if(rate != null){
                    res.add(rate);

                }else{
                    rate  = new RoomDayPrice();
                    rate.setOccupancy(occupancy);
                    rate.setRoomName("NA");
                    rate.setPriceStr("NA");
                    rate.setHotel(hotel);
                    res.add(rate);
                }

            }

            resp.put(hotel.getHotelName(),res);


        }

        return resp;

    }

    @GetMapping("/getlowest")
    public RoomDayPrice getLowestByDateOccupancyandHotel(@RequestParam("occupancy") Integer occupancy,
                                                         @RequestParam("checkin") String checkin,
                                                         @RequestParam("hotel_id")Long hotel_id) throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");


        Date date = parser.parse(checkin);

        return roomDayPriceRepository.getLowestPriceByOccupancyCheckinAndHotel(checkin,occupancy,hotel_id);

    }


    @GetMapping("/rateapi")
    public Map<String,List<RateInfo>> getRatesByDateAndOccupancy(@RequestParam("occupancy") Integer occupancy,
                                                                 @RequestParam("checkin") String checkin,
                                                                 @RequestParam("days")Integer days) throws Exception {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

        Map<String,List<RateInfo>> resp= new HashMap<>();

        Date date = parser.parse(checkin);

        LocalDate start = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currdate = start;

        for (int i=0; i<days; i++) {

            currdate = currdate.plusDays(1);
            System.out.println(currdate);

            List<RoomDayPrice> dayPrices = roomDayPriceRepository.getLatestPriceByOccupancyAAndCheckin(currdate.toString(), occupancy);
            List<RateInfo> res = new ArrayList<>();

            Map<Hotel, RoomDayPrice> hmap = new HashMap<>();

            for (RoomDayPrice dpr : dayPrices) {

                RoomDayPrice tmpdp = hmap.get(dpr.getHotel());

                if (tmpdp != null) {
                    if (dpr.getPrice() != null && tmpdp.getPrice() > dpr.getPrice()) {
                        hmap.put(dpr.getHotel(), dpr);
                    }
                } else {
                    hmap.put(dpr.getHotel(), dpr);
                }
            }

            for (Hotel h : hmap.keySet()) {
                RateInfo ri = new RateInfo();
                ri.setHotel_id(h.getHotelId());
                ri.setHotel_name(h.getHotelName());
                ri.setOccupancy(hmap.get(h).getOccupancy());
                ri.setPrice(hmap.get(h).getPrice());
                ri.setRoom_name(hmap.get(h).getRoomName());
                res.add(ri);

            }

            resp.put(currdate.toString(),res);

        }

        return resp;

    }



}
