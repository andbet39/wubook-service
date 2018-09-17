package com.dev.bamboo.wuboookservice.domains;

public class RateInfo {

    private  String hotel_name;
    private String room_name;
    private  String hotel_id;
    private Integer occupancy;
    private Float price;

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

}
