package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.dao.RunningRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.room.dto.request.CreateRoomRequest;
import com.moyeorun.api.domain.room.dto.response.CreateRoomResponse;
import com.moyeorun.api.domain.scheduler.application.RoomJobService;
import com.moyeorun.api.domain.user.dao.UserRepository;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final RunningRepository runningRepository;
  private final OneRoomParticipatePolicy policy;
  private final RoomJobService roomJobService;
  @Transactional
  public CreateRoomResponse createRoom(CreateRoomRequest dto, Long userId){
    User findUser = userRepository.findById(userId)
        .orElseThrow(EntityNotFoundException::new);

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

}
