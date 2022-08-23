package com.moyeorun.api.domain.room.dao;

import com.moyeorun.api.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {

}
