package com.dev.bamboo.wuboookservice.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String hotelName;
    private String hotelId;
    private String address;
    private String baseUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    private List<RoomDayPrice> roomDayPriceList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<RoomDayPrice> getRoomDayPriceList() {
        return roomDayPriceList;
    }

    public void setRoomDayPriceList(List<RoomDayPrice> roomDayPriceList) {
        this.roomDayPriceList = roomDayPriceList;
    }
}
