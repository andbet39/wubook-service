package com.dev.bamboo.wuboookservice.controllers;

import com.dev.bamboo.wuboookservice.domains.AggregatedPriceInfoResult;
import com.dev.bamboo.wuboookservice.domains.Hotel;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import com.dev.bamboo.wuboookservice.repositories.LatestBookingPriceRepository;
import com.dev.bamboo.wuboookservice.repositories.RoomRepository;
import com.dev.bamboo.wuboookservice.services.PriceService;
import com.dev.bamboo.wuboookservice.services.WubookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class PriceInfoController {
    @Autowired
    WubookService wubookService;

    @Autowired
    LatestBookingPriceRepository latestBookingPriceRepository;

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    PriceService priceService;

    @GetMapping("/price/getdiff")
    public HashMap<Date,Float> getDiffs(@RequestParam("startdt") String startdt,
                                        @RequestParam("enddt") String enddt,
                                        @RequestParam("occupancy") Integer occupancy
    ) throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = parser.parse(startdt);
        Date end = parser.parse(enddt);

        List<AggregatedPriceInfoResult> other =
                latestBookingPriceRepository.getAggregateByOccupancyAndDays(occupancy,date,end);


        Hotel ref_hotel = hotelRepository.findAllByReference(true);

        List<AggregatedPriceInfoResult> bamboo =
                latestBookingPriceRepository.getAggregateByOccupancyAndDaysAndHotelId(occupancy,date,end,ref_hotel.getId());
                //latestBookingPriceRepository.getAggregateByOccupancyAndDaysAndHotelId(oc,date,end,ref_hotel.getId(),"Double Room");


        return priceService.calculateDiffs(bamboo,other);
    }
}
