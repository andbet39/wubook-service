package com.dev.bamboo.wuboookservice.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {

    @Id
    private Long id;// the id of the room (what else?)

    private String name;// the main name of the room.
    private String shortname;// the room shortname;// unique (per property)
    private Integer occupancy;// the room occupancy
    private Integer men;// adults (note;// adults + children = occupancy)
    private Integer children;// children (note;// adults + children = occupancy)
    private Long subroom;// if the room is virtual, this field is the id of the Parent Room (0 otherwise)
    private Long anchorate;// price anchorage. The field contains the Parent Room (0 otherwise)
    private Float price;// the default price for this room
    private Integer availability;// the default availability
    private String woodoo;// is that room woodoo only? (not sellable on the booking engine and Fount (TripAdvisor, Trivago and so on))
    private String board;

    @JsonIgnore
    private String boards;

    private String dec_avail;// availability to be decreased in Parent Room
    private Float min_price;// the minimun price for this room (0 otherwise)
    private Float max_price;// the maximun price for this room (0 otherwise)
    private String rtype;// rtype ID (room, apartment and so on)
    private String rtype_name;// rtype name (rtype human description)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public Integer getMen() {
        return men;
    }

    public void setMen(Integer men) {
        this.men = men;
    }

    public Integer getChildren() {
        return children;
    }

    public void setChildren(Integer children) {
        this.children = children;
    }

    public Long getSubroom() {
        return subroom;
    }

    public void setSubroom(Long subroom) {
        this.subroom = subroom;
    }

    public Long getAnchorate() {
        return anchorate;
    }

    public void setAnchorate(Long anchorate) {
        this.anchorate = anchorate;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getWoodoo() {
        return woodoo;
    }

    public void setWoodoo(String woodoo) {
        this.woodoo = woodoo;
    }

    public String getDec_avail() {
        return dec_avail;
    }

    public void setDec_avail(String dec_avail) {
        this.dec_avail = dec_avail;
    }

    public Float getMin_price() {
        return min_price;
    }

    public void setMin_price(Float min_price) {
        this.min_price = min_price;
    }

    public Float getMax_price() {
        return max_price;
    }

    public void setMax_price(Float max_price) {
        this.max_price = max_price;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getRtype_name() {
        return rtype_name;
    }

    public void setRtype_name(String rtype_name) {
        this.rtype_name = rtype_name;
    }


    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBoards() {
        return boards;
    }

    public void setBoards(String boards) {
        this.boards = boards;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortname='" + shortname + '\'' +
                ", occupancy=" + occupancy +
                ", men=" + men +
                ", children=" + children +
                ", subroom=" + subroom +
                ", anchorate=" + anchorate +
                ", price=" + price +
                ", availability=" + availability +
                ", woodoo='" + woodoo + '\'' +
                ", dec_avail='" + dec_avail + '\'' +
                ", min_price=" + min_price +
                ", max_price=" + max_price +
                ", rtype='" + rtype + '\'' +
                ", rtype_name='" + rtype_name + '\'' +
                '}';
    }
}
