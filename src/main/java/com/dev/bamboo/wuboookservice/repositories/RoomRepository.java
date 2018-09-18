package com.dev.bamboo.wuboookservice.repositories;

import com.dev.bamboo.wuboookservice.domains.Hotel;
import com.dev.bamboo.wuboookservice.domains.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    }
