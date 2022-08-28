package com.moyeorun.api.domain.room.dao;

import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomReservation;
import com.moyeorun.api.domain.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
  Boolean existsByUserAndRoom(User user, Room room);

  Long countByRoom(Room room);
  Optional<RoomReservation> findByUserAndRoom(User user, Room room);
  List<RoomReservation> findByRoom(Room room);
}
