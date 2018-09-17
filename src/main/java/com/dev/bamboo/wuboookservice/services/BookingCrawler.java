package com.dev.bamboo.wuboookservice.services;


import com.dev.bamboo.wuboookservice.domains.CrawledPriceInfo;
import com.dev.bamboo.wuboookservice.repositories.HotelRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingCrawler {


    @Autowired
    private HotelRepository hotelRepository;

    public List<CrawledPriceInfo> CrawlPageURL(String url){

        List<CrawledPriceInfo> results = new ArrayList<CrawledPriceInfo>();

        try {
            Document doc = Jsoup.connect(url).get();


            String hotelID = doc.getElementsByAttributeValue("name","hotel_id").attr("value");
            String hotelName = doc.getElementById("hp_hotel_name").text();


            System.out.println("Hotel : "+ hotelName + " ID :"+hotelID);

            Element priceTable = doc.selectFirst("table.hprt-table");

            if(priceTable!=null){
                Elements priceRows = priceTable.select("tbody > tr");

                String currentRoomId="";
                String currentRoomName="";

                for(Element row : priceRows){

                    Element roomInfo = row.selectFirst(".hprt-roomtype-link");
                    if (roomInfo != null){
                        currentRoomId = roomInfo.attr("data-room-id");
                        currentRoomName =row.select(".hprt-roomtype-link").first().attr("data-room-name");
                    }

                    String price = "NA";
                    Integer occupancy=0;
                    if (row.selectFirst(".important_text") == null ){

                        Element priceSpan =  row.selectFirst("span.hprt-price-price-standard");

                        if(priceSpan != null){
                            price = priceSpan.text();
                            occupancy= row.select(".bicon-occupancy").size();
                            System.out.println("Room : "+ currentRoomName + " Occupancy :"+ occupancy.toString() +" Price:"+ price);

                            CrawledPriceInfo res = new CrawledPriceInfo();
                            res.setAvailable(true);
                            res.setHotelId(hotelID);
                            res.setRoomId(currentRoomId);
                            res.setRoomName(currentRoomName);
                            res.setHotelName(hotelName);
                            res.setOccupancy(occupancy);
                            res.setPrice(price);

                            results.add(res);
                        }

                    }else{
                        occupancy= row.select(".bicon-occupancy").size();
                        System.out.println("Room : "+ currentRoomName + " Occupancy :"+ occupancy.toString() +" No availability" );

                        CrawledPriceInfo res = new CrawledPriceInfo();
                        res.setAvailable(false);
                        res.setHotelId(hotelID);
                        res.setRoomId(currentRoomId);
                        res.setRoomName(currentRoomName);
                        res.setHotelName(hotelName);
                        res.setOccupancy(occupancy);

                        results.add(res);
                    }


                }
            }else{
                System.out.println("No available for this day");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;


    }



}
