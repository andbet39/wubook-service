package com.dev.bamboo.wuboookservice.repositories;

import com.dev.bamboo.wuboookservice.domains.AggregatedPriceInfoResult;
import com.dev.bamboo.wuboookservice.domains.Hotel;
import com.dev.bamboo.wuboookservice.domains.LatestBookingPrice;
import com.dev.bamboo.wuboookservice.domains.RoomDayPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;

@Repository
public interface LatestBookingPriceRepository extends JpaRepository<LatestBookingPrice, Long> {

    LatestBookingPrice findByHotelIdAndRoomNameAndDateAndOccupancy(Long hotelId, String roomName, Date date,Integer occupancy);

    @Query("select min(price) from LatestBookingPrice lb where lb.date = ?1 and lb.occupancy= ?2 ")
    Float getMinPriceByOccupancyAndDate(Date date, Integer occupancy);

    @Query("select max(price) from LatestBookingPrice lb where lb.date = ?1 and lb.occupancy= ?2 ")
    Float getMaxPriceByOccupancyAndDate(Date date, Integer occupancy);

    @Query("select avg(price) from LatestBookingPrice lb where lb.date = ?1 and lb.occupancy= ?2 ")
    Float getAvgPriceByOccupancyAndDate(Date date, Integer occupancy);


    @Query("SELECT new com.dev.bamboo.wuboookservice.domains.AggregatedPriceInfoResult(" +
            " AVG(price)," +
            " MAX(price),"+
            " MIN(price)," +
            " date )" +
            " FROM LatestBookingPrice WHERE price is not null and  occupancy= ?1 and date between ?2 and ?3  group by date order by date")
    List<AggregatedPriceInfoResult> getAggregateByOccupancyAndDays(Integer occupancy,Date start,Date end);

    @Query("SELECT new com.dev.bamboo.wuboookservice.domains.AggregatedPriceInfoResult(" +
            " AVG(price)," +
            " MAX(price),"+
            " MIN(price)," +
            " date )" +
            " FROM LatestBookingPrice WHERE  price is not null and occupancy= ?1 and date between ?2 and ?3 and hotel_id= ?4 and room_name= ?5  group by date order by date")
    List<AggregatedPriceInfoResult> getAggregateByOccupancyAndDaysAndHotelId(Integer occupancy,Date start,Date end,Long hotel_id,String roomName);

    List<LatestBookingPrice> getAllByHotelAndDate(Hotel hotel,Date date);
 }
