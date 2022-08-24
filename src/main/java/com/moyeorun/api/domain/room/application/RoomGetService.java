package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.dao.RoomReservationRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomReservation;
import com.moyeorun.api.domain.room.dto.response.RoomResponse;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.global.error.exception.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomGetService {

  private final RoomRepository roomRepository;
  private final RoomReservationRepository roomReservationRepository;

  @Transactional(readOnly = true)
  public RoomResponse getOne(Long roomId){
    Room findRoom = findRoomById(roomId);
    if(isReservation(findRoom.getStartTime())){
      List<RoomReservation> reservationList = roomReservationRepository.findByRoom(findRoom);
      List<User> users = reservationList.stream().map(RoomReservation::getUser).toList();
      return RoomResponse.toDetailReservation(findRoom, users);
    }else{
      return RoomResponse.toDetailJoin(findRoom);
    }
  }


  private Boolean isReservation(LocalDateTime startTime){
    LocalDateTime reservationTime = startTime.minusHours(1);
    LocalDateTime currentTime = LocalDateTime.now();
    return reservationTime.isAfter(currentTime);
  }
  private Room findRoomById(Long roomId){
    return roomRepository.findById(roomId)
        .orElseThrow(EntityNotFoundException::new);
  }
}
