package com.moyeorun.api.domain.room.dao;

import com.moyeorun.api.domain.room.domain.Room;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

  @Query("select rm  from Room rm left join fetch rm.reservationList where rm.id = :id")
  Optional<Room> findWithReservationById(Long id);

  @Query("select rm from Room  rm join fetch rm.runningList where rm.id = :id")
  Optional<Room> findWithRunningById(Long id);
}
