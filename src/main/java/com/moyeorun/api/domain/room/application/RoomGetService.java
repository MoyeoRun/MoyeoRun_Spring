package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.room.dao.RoomQueryRepository;
import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.dao.RoomReservationRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomReservation;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.room.dto.response.RoomPagingResponse;
import com.moyeorun.api.domain.room.dto.response.RoomResponse;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.global.error.exception.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomGetService {

  private final RoomRepository roomRepository;
  private final RoomReservationRepository roomReservationRepository;
  private final RoomQueryRepository roomQueryRepository;

  @Transactional(readOnly = true)
  public RoomResponse getOne(Long roomId) {
    Room findRoom = findRoomById(roomId);
    if (isReservation(findRoom.getStartTime())) {
      List<RoomReservation> reservationList = roomReservationRepository.findByRoom(findRoom);
      List<User> users = reservationList.stream().map(RoomReservation::getUser).toList();
      return RoomResponse.toDetailReservation(findRoom, users);
    } else {
      return RoomResponse.toDetailJoin(findRoom);
    }
  }

  @Transactional(readOnly = true)
  public RoomPagingResponse getReservationPaging(Long roomId, int count) {
    List<Room> reservationRoom = roomQueryRepository.findAllAndReservation(roomId, count);

    List<RoomResponse> result = reservationRoom.stream().map(room -> RoomResponse.toListDto(room,
        room.getReservationList().stream().map(RoomReservation::getUser)
            .collect(Collectors.toList())
    )).toList();

    return new RoomPagingResponse(reservationRoom.isEmpty(), result);
  }

  @Transactional(readOnly = true)
  public RoomPagingResponse getRealTimePaging(Long roomId, int count) {
    List<Room> joinRoomList = roomQueryRepository.findAllAndJoinMember(roomId, count);

    List<RoomResponse> result = joinRoomList.stream()
        .map(room -> RoomResponse.toListDto(room,
            room.getRunningList().stream().map(Running::getUser).collect(
                Collectors.toList())))
        .toList();

    return new RoomPagingResponse(joinRoomList.isEmpty(), result);
  }

  @Transactional(readOnly = true)
  public RoomResponse getCurrentJoinRoom(Long userId) {
    Room currentRoom = roomQueryRepository.findCurrentRunningRoom(userId);

    if (currentRoom == null) {
      return null;
    }

    return RoomResponse.toListDto(currentRoom,
        currentRoom.getRunningList().stream().map(Running::getUser).toList()
    );
  }


  private Boolean isReservation(LocalDateTime startTime) {
    LocalDateTime reservationTime = startTime.minusHours(1);
    LocalDateTime currentTime = LocalDateTime.now();
    return reservationTime.isAfter(currentTime);
  }

  private Room findRoomById(Long roomId) {
    return roomRepository.findById(roomId)
        .orElseThrow(EntityNotFoundException::new);
  }
}
