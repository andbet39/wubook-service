package com.dev.bamboo.wuboookservice.controllers;


import com.dev.bamboo.wuboookservice.domains.AggregatedPriceInfoResult;
import com.dev.bamboo.wuboookservice.domains.Room;
import com.dev.bamboo.wuboookservice.repositories.LatestBookingPriceRepository;
import com.dev.bamboo.wuboookservice.repositories.RoomRepository;
import com.dev.bamboo.wuboookservice.services.WubookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.HsqlMaxValueIncrementer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class TestController {


    @Autowired
    WubookService wubookService;

    @Autowired
    LatestBookingPriceRepository latestBookingPriceRepository;

    @Autowired
    RoomRepository roomRepository;

    @GetMapping("/testagg")
    public String testAverage() throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");


        Date date = parser.parse("2018-11-10");
        Date end = parser.parse("2018-11-15");
        System.out.println(latestBookingPriceRepository.getAvgPriceByOccupancyAndDate(date,2));
        System.out.println(latestBookingPriceRepository.getMaxPriceByOccupancyAndDate(date,2));
        System.out.println(latestBookingPriceRepository.getMinPriceByOccupancyAndDate(date,2));

        System.out.println(latestBookingPriceRepository.getAggregateByOccupancyAndDays(2,date,end));

        return latestBookingPriceRepository.getAggregateByOccupancyAndDays(2,date,end).toString();
    }

    @GetMapping("/getdiff")
    public HashMap<Date,Float> getDiffs() throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");


        Date date = parser.parse("2018-11-01");
        Date end = parser.parse("2018-11-25");

        List<AggregatedPriceInfoResult> other =
                latestBookingPriceRepository.getAggregateByOccupancyAndDays(2,date,end);
        List<AggregatedPriceInfoResult> bamboo =
                latestBookingPriceRepository.getAggregateByOccupancyAndDaysAndHotelId(2,date,end,263L,"Double Room");


        return calculateDiffs(bamboo,other);
    }



    @GetMapping("/getrooms")
    public ArrayList<Room> getRooms() {

        ArrayList<Room> rooms = wubookService.fetchRoom();

        for (Room room:rooms){
            roomRepository.save(room);
        }

        return  rooms;

    }

    @GetMapping("/getpricingplan")
    public String getPricingPlan(){

        wubookService.getPricingPlan();
        return  "OK";

    }

    @GetMapping("/getprice")
    public String getPrice(){

        Double price = wubookService.getPrice("17/09/2018",39594L);
        return price.toString();

    }

    @GetMapping("/setprice")
    public String setPrice()
    {
        wubookService.updatePrice("17/09/2018",121.0D,39594L);
        return "OK";
    }

    @GetMapping("/mergeprice")
    public String mergePrice() throws ParseException {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = parser.parse("2018-11-01");
        Date endDate = parser.parse("2018-11-25");



        List<AggregatedPriceInfoResult> other = latestBookingPriceRepository.getAggregateByOccupancyAndDays(2,startDate,endDate);
        List<AggregatedPriceInfoResult> bamboo = latestBookingPriceRepository.getAggregateByOccupancyAndDaysAndHotelId(2,startDate,endDate,263L,"Double Room");

        HashMap<Date,Float> diffs = calculateDiffs(bamboo,other);

        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int idx = 0;
        for (Date dt :diffs.keySet()) {

            LocalDate date = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String txt_date = date.format(formatters);

            Double current_price = wubookService.getPrice(txt_date,39594L);

            Double newPrice = current_price-(diffs.get(dt)*0.7);

            System.out.println("Current : "+current_price.toString()+" Diff :" + diffs.get(dt).toString()+"  - NewPrice for : " +txt_date +" is " + newPrice.toString());

            wubookService.updatePrice(txt_date,newPrice,39594L);
            idx++;
        }


        return "OK";
    }

    public HashMap<Date,Float>  calculateDiffs(List<AggregatedPriceInfoResult> bamboo , List<AggregatedPriceInfoResult> other)
    {
        int i=0;
        HashMap<Date,Float> diffs = new HashMap<>();

        for(AggregatedPriceInfoResult o: other){
            AggregatedPriceInfoResult b = bamboo.get(i);

            if (o.getDate().equals(b.getDate())){

                Float diff= b.getMin() - o.getMin();

               /* if(diff.equals(0.0F)){
                    diff = b.getMin()-(float)o.getAvg();
                }*/

                diffs.put(b.getDate(),diff);
                i++;
            }else{
                diffs.put(o.getDate(),0.0F);
            }
        }

        return diffs;
    }

}
