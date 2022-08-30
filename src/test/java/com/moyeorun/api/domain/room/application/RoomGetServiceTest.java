package com.moyeorun.api.domain.room.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.dao.RoomReservationRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RoomGetServiceTest {

  @Mock
  RoomRepository roomRepository;

  @Mock
  RoomReservationRepository roomReservationRepository;

  @InjectMocks
  RoomGetService roomGetService;

  final Long roomId = 1L;

  @Test
  @DisplayName("방 상세 조회시 예약 시간때 조회시 예약자 목록 불러옴.")
  public void room_getOne_isReservationTime() throws Exception {
    Room twoHourStartRoom = Room.builder()
        .roomStatus(RoomStatus.OPEN)
        .hostId(2L)
        .name("2시간뒤 시작하는 방.")
        .startTime(LocalDateTime.now().plusHours(2))
        .build();

    given(roomRepository.findById(any())).willReturn(Optional.of(twoHourStartRoom));

    roomGetService.getOne(roomId);

    verify(roomReservationRepository,times(1)).findByRoom(twoHourStartRoom);
  }

  @Test
  @DisplayName("방 상세 조회시 참여 시간때 조회하면 예약자 목록을 안불러옴.")
  public void room_getOne_isJoinTime() throws Exception {
    Room After30MinStartRoom = Room.builder()
        .roomStatus(RoomStatus.OPEN)
        .hostId(2L)
        .name("2시간뒤 시작하는 방.")
        .startTime(LocalDateTime.now().plusMinutes(30))
        .build();

    given(roomRepository.findById(any())).willReturn(Optional.of(After30MinStartRoom));

    roomGetService.getOne(roomId);

    verify(roomReservationRepository,times(0)).findByRoom(After30MinStartRoom);

  }

}