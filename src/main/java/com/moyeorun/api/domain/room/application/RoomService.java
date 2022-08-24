package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.dao.RoomReservationRepository;
import com.moyeorun.api.domain.room.dao.RunningRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomReservation;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.room.dto.request.CreateRoomRequest;
import com.moyeorun.api.domain.room.dto.response.CreateRoomResponse;
import com.moyeorun.api.domain.room.exception.AlreadyJoinRoomException;
import com.moyeorun.api.domain.room.exception.AlreadyReservationException;
import com.moyeorun.api.domain.room.exception.RequireJoinException;
import com.moyeorun.api.domain.room.exception.RequireReservationException;
import com.moyeorun.api.domain.scheduler.application.RoomJobService;
import com.moyeorun.api.domain.user.dao.UserRepository;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.global.error.exception.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final RunningRepository runningRepository;
  private final RoomPolicy policy;
  private final RoomJobService roomJobService;
  private final RoomReservationRepository roomReservationRepository;
  @Transactional
  public CreateRoomResponse createRoom(CreateRoomRequest dto, Long userId){
    User findUser = findUserById(userId);

    policy.constraintRoomCheck(findUser);

    Room room = dto.toEntity(findUser.getId());
    roomRepository.save(room);
    runningRepository.save(Running.builder()
            .user(findUser)
            .room(room)
        .build());

    roomJobService.registerCloseRoomJob(room.getId(), room.getStartTime());
    return new CreateRoomResponse("방 생성 성공", room.getId());
  }

  @Transactional
  public void reserve(Long userId, Long roomId){
    User findUser = findUserById(userId);
    Room room = findRoomById(roomId);

    room.validateReservation(LocalDateTime.now(), userId);

    if(roomReservationRepository.existsByUserAndRoom(findUser,room)){
      throw new AlreadyReservationException();
    }

    roomReservationRepository.save(RoomReservation.builder()
            .user(findUser)
            .room(room)
        .build());
  }

  @Transactional
  public void cancelReservation(Long userId, Long roomId){
    User findUser =  findUserById(userId);
    Room room = findRoomById(roomId);

    room.validateReservation(LocalDateTime.now(), userId);

    RoomReservation roomReservation = roomReservationRepository.findByUserAndRoom(findUser, room)
        .orElseThrow(RequireReservationException::new);

    roomReservationRepository.delete(roomReservation);
  }

  @Transactional
  public void joinRoom(Long userId, Long roomId){
    User findUser = findUserById(userId);
    Room room = findRoomById(roomId);

    room.validateJoin(LocalDateTime.now(), userId);

    if(runningRepository.existsByUserAndRoom(findUser,room)){
      throw new AlreadyJoinRoomException();
    }

    policy.constraintRoomCheck(findUser);

    runningRepository.save(Running.builder()
            .user(findUser)
            .room(room)
        .build());
  }

  @Transactional
  public void joinCancel(Long userId, Long roomId){
    User findUser = findUserById(userId);
    Room room = findRoomById(roomId);

    room.validateJoin(LocalDateTime.now(), userId);
    Running running = runningRepository.findByUserAndRoom(findUser, room)
        .orElseThrow(RequireJoinException::new);

    runningRepository.delete(running);
  }

  private User findUserById(Long userId){
    return userRepository.findById(userId)
        .orElseThrow(EntityNotFoundException::new);
  }

  private Room findRoomById(Long roomId){
    return roomRepository.findById(roomId)
        .orElseThrow(EntityNotFoundException::new);
  }

}
