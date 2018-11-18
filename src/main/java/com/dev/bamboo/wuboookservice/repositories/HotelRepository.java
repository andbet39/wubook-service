package com.dev.bamboo.wuboookservice.repositories;

import com.dev.bamboo.wuboookservice.domains.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Hotel findByHotelId(String hotelId);

    Hotel findAllByReference(boolean b);
}
