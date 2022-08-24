package com.moyeorun.api.domain.room.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.dao.RoomReservationRepository;
import com.moyeorun.api.domain.room.dao.RunningRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomReservation;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import com.moyeorun.api.domain.room.exception.AlreadyReservationException;
import com.moyeorun.api.domain.room.exception.NotAllowHostSelfReqeustException;
import com.moyeorun.api.domain.room.exception.NotAllowReservationRequestException;
import com.moyeorun.api.domain.room.exception.NotReservationRoomException;
import com.moyeorun.api.domain.scheduler.job.RoomCloseJob;
import com.moyeorun.api.domain.user.dao.UserRepository;
import com.moyeorun.api.domain.user.domain.User;
import com.moyeorun.api.setup.domain.UserMockBuilder;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  RoomRepository roomRepository;
  @Mock
  RunningRepository runningRepository;
  @Mock
  RoomPolicy roomPolicy;
  @Mock
  RoomCloseJob roomCloseJob;
  @Mock
  RoomReservationRepository roomReservationRepository;

  @InjectMocks
  RoomService roomService;

  Optional<User> mockUser;
  Long userId;
  Room generalRoom;
  final Long hostId = 2L;
  final Long roomId = 1L;
  @BeforeEach
  void setUp() {
    mockUser = UserMockBuilder.ofOptional();
    userId = mockUser.get().getId();
    generalRoom = Room.builder()
        .startTime(LocalDateTime.now().plusDays(1))
        .roomStatus(RoomStatus.OPEN)
        .hostId(hostId)
        .name("방이름")
        .build();
  }

  @Test
  @DisplayName("방 예약 요청시 본인의 방에 요청으로 실패")
  public void room_reserve_host_self_request_fail() throws Exception {
    Room hostIdEqualRoom = Room.builder().hostId(userId).startTime(LocalDateTime.now().plusDays(1))
        .build();
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findById(roomId)).willReturn(Optional.of(hostIdEqualRoom));

    assertThrows(NotAllowHostSelfReqeustException.class, () ->
        roomService.reserve(userId, roomId)
    );
  }

  @Test
  @DisplayName("방 예약 요청시 시간 제한으로 실패")
  public void room_rserve_time_limit_fail() throws Exception {
    LocalDateTime after30MinStartTime = LocalDateTime.now().plusMinutes(30);
    Room room = Room.builder().hostId(2L).startTime(after30MinStartTime)
        .build();
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findById(roomId)).willReturn(Optional.of(room));

    assertThrows(NotAllowReservationRequestException.class, () ->
        roomService.reserve(userId, roomId));
  }

  @Test
  @DisplayName("이미 해당 방에 예약 한 상태여서 실패")
  public void already_room_reserve() throws Exception {
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findById(roomId)).willReturn(Optional.of(generalRoom));
    given(roomReservationRepository.existsByUserAndRoom(any(), any())).willReturn(true);

    assertThrows(AlreadyReservationException.class, () ->
        roomService.reserve(userId, roomId));

  }

  @Test
  @DisplayName("예약 성공")
  public void reserve_success() throws Exception {
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findById(roomId)).willReturn(Optional.of(generalRoom));
    given(roomReservationRepository.existsByUserAndRoom(any(), any())).willReturn(false);

    roomService.reserve(userId, roomId);

    verify(roomReservationRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("예약 취소요청시 방을 예약하고 있지 않은 상태")
  public void cancel_fail_not_reserve() throws Exception {

    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findById(roomId)).willReturn(Optional.of(generalRoom));
    given(roomReservationRepository.findByUserAndRoom(any(), any())).willReturn(Optional.empty());

    assertThrows(NotReservationRoomException.class,
        () -> roomService.cancelReservation(userId, roomId));
  }

  @Test
  @DisplayName("예약 성공")
  public void cancel_success() throws Exception {
    RoomReservation reservation = RoomReservation.builder().room(generalRoom).user(mockUser.get())
        .build();
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findById(roomId)).willReturn(Optional.of(generalRoom));
    given(roomReservationRepository.findByUserAndRoom(any(), any())).willReturn(Optional.of(reservation));

    roomService.cancelReservation(userId,roomId);

    verify(roomReservationRepository,times(1)).delete(reservation);
  }
}