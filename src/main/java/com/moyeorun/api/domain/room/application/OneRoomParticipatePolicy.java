package com.moyeorun.api.domain.room.application;

import com.moyeorun.api.domain.room.dao.RunningRepository;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import com.moyeorun.api.domain.room.exception.AlreadyParticipateRoomException;
import com.moyeorun.api.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OneRoomParticipatePolicy implements RoomPolicy{

  private final RunningRepository runningRepository;

  @Override
  public void constraintRoomCheck(User user) {
    Boolean check = runningRepository.checkUserAlreadyRoom(user, RoomStatus.CLOSE);
    
    if(check){

      throw new AlreadyParticipateRoomException();
    }
  }
}
