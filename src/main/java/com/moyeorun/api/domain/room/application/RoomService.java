package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.room.dto.request.CreateRoomRequest;
import com.moyeorun.api.domain.room.dto.response.CreateRoomResponse;
import com.moyeorun.api.domain.room.exception.LimitRoomUserCountException;
import com.moyeorun.api.domain.room.exception.NotDeleteRoomTimeException;
import com.moyeorun.api.domain.scheduler.application.RoomJobService;
import com.moyeorun.api.domain.user.dao.UserRepository;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.global.error.exception.AuthorizationFailException;
import com.moyeorun.api.global.error.exception.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final RoomPolicy policy;
  private final RoomJobService roomJobService;

  @Transactional
  public CreateRoomResponse createRoom(CreateRoomRequest dto, Long userId) {
    User findUser = findUserById(userId);

    policy.constraintRoomCheck(findUser);

    Room room = dto.toEntity(findUser.getId());
    room.initRunner(Running.builder().user(findUser)
        .room(room)
        .build());
    roomRepository.save(room);
    roomJobService.registerCloseRoomJob(room.getId(), room.getStartTime());
    return new CreateRoomResponse("방 생성 성공", room.getId());
  }

  @Transactional
  public void reserve(Long userId, Long roomId) {
    User findUser = findUserById(userId);
    Room findRoom = roomRepository.findWithReservationById(roomId)
        .orElseThrow(EntityNotFoundException::new);

    findRoom.validateReservation(LocalDateTime.now(), userId);

    if (findRoom.getReservationList().size() >= findRoom.getLimitUserCount()) {
      throw new LimitRoomUserCountException();
    }

    findRoom.addReservation(findUser);
  }

  @Transactional
  public void cancelReservation(Long userId, Long roomId) {
    User findUser = findUserById(userId);
    Room findRoom = roomRepository.findWithReservationById(roomId)
        .orElseThrow(EntityNotFoundException::new);

    findRoom.validateReservation(LocalDateTime.now(), userId);

    findRoom.cancelReservation(findUser);
  }

  @Transactional
  public void joinRoom(Long userId, Long roomId) {
    User findUser = findUserById(userId);
    Room room = roomRepository.findWithRunningById(roomId)
        .orElseThrow(EntityNotFoundException::new);

    room.validateJoin(LocalDateTime.now(), userId);

    if (room.getRunningList().size() >= room.getLimitUserCount()) {
      throw new LimitRoomUserCountException();
    }

    policy.constraintRoomCheck(findUser);

    room.joinRunner(findUser);
  }

  @Transactional
  public void joinCancel(Long userId, Long roomId) {
    User findUser = findUserById(userId);
    Room room = roomRepository.findWithRunningById(roomId)
        .orElseThrow(EntityNotFoundException::new);

    room.validateJoin(LocalDateTime.now(), userId);

    room.removeRunner(findUser);
  }

  @Transactional
  public void deleteRoom(Long userId, Long roomId) {
    Room findRoom = roomRepository.findById(roomId)
        .orElseThrow(EntityNotFoundException::new);

    if (!findRoom.getHostId().equals(userId)) {
      throw new AuthorizationFailException();
    }

    LocalDateTime canDeleteTime = findRoom.getStartTime().minusMinutes(10);
    if(LocalDateTime.now().isAfter(canDeleteTime)){
      throw new NotDeleteRoomTimeException();
    }

    roomRepository.delete(findRoom);
  }


  private User findUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(EntityNotFoundException::new);
  }


}
