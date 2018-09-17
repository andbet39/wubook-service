package com.dev.bamboo.wuboookservice.repositories;



import com.dev.bamboo.wuboookservice.domains.RoomDayPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomDayPriceRepository extends JpaRepository<RoomDayPrice, Long> {

    public List<RoomDayPrice> getRoomDayPricesByCheckinAndOccupancy(Date checkin, Integer occupancy);

    @Query(
            value = "select * from room_day_price a where checkin = ?1 and occupancy= ?2 and crawl_time=( select max(crawl_time) from room_day_price where hotel_id=a.hotel_id and  room_id=a.room_id and checkin = a.checkin and occupancy=a.occupancy)",
            nativeQuery = true)
    public List<RoomDayPrice> getLatestPriceByOccupancyAAndCheckin(String checkin, Integer occupancy);

    @Query(
            value = "select a.* from room_day_price a where checkin = ?1  and occupancy= ?2 and hotel_id= ?3 and price is not null and crawl_time=( select max(crawl_time) from room_day_price where hotel_id=a.hotel_id and  room_id=a.room_id and checkin = a.checkin and occupancy=a.occupancy) order by price LIMIT 1",
            nativeQuery = true)
    public RoomDayPrice getLowestPriceByOccupancyCheckinAndHotel(String checkin,Integer occupancy,Long hotel_id);
}
