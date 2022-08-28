package com.moyeorun.api.domain.room.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.room.exception.AlreadyJoinRoomException;
import com.moyeorun.api.domain.room.exception.AlreadyReservationException;
import com.moyeorun.api.domain.room.exception.LimitRoomUserCountException;
import com.moyeorun.api.domain.room.exception.NotAllowHostSelfReqeustException;
import com.moyeorun.api.domain.room.exception.NotAllowJoinRequestException;
import com.moyeorun.api.domain.room.exception.NotAllowReservationRequestException;
import com.moyeorun.api.domain.room.exception.RequireJoinException;
import com.moyeorun.api.domain.room.exception.RequireReservationException;
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
  RoomPolicy roomPolicy;
  @Mock
  RoomCloseJob roomCloseJob;

  @InjectMocks
  RoomService roomService;

  Optional<User> mockUser;
  Long userId;
  Room generalRoom;
  final Long hostId = 2L;
  final Long roomId = 1L;

  Room remain30MinRoom;

  @BeforeEach
  void setUp() {
    mockUser = UserMockBuilder.ofOptional();
    userId = mockUser.get().getId();
    generalRoom = Room.builder()
        .startTime(LocalDateTime.now().plusDays(1))
        .roomStatus(RoomStatus.OPEN)
        .limitUserCount(5)
        .hostId(hostId)
        .name("방이름")
        .build();
    generalRoom.initRunner(Running.builder()
        .user(mockUser.get())
        .room(generalRoom)
        .build());

    remain30MinRoom = Room.builder()
        .startTime(LocalDateTime.now().plusMinutes(30))
        .hostId(hostId)
        .limitUserCount(5)
        .name("방이름")
        .build();
    remain30MinRoom.initRunner(Running.builder()
        .user(mockUser.get())
        .room(remain30MinRoom)
        .build());
  }

  @Test
  @DisplayName("방 예약 요청시 본인의 방에 요청으로 실패")
  public void room_reserve_host_self_request_fail() throws Exception {
    Room hostIdEqualRoom = Room.builder().hostId(userId).startTime(LocalDateTime.now().plusDays(1))
        .build();
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithReservationById(roomId)).willReturn(Optional.of(hostIdEqualRoom));

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
    given(roomRepository.findWithReservationById(roomId)).willReturn(Optional.of(room));

    assertThrows(NotAllowReservationRequestException.class, () ->
        roomService.reserve(userId, roomId));
  }

  @Test
  @DisplayName("이미 해당 방에 예약 한 상태여서 실패")
  public void already_room_reserve() throws Exception {
    Room alreadyReservationRoom = Room.builder()
        .hostId(2L)
        .limitUserCount(5)
        .startTime(LocalDateTime.now().plusHours(2))
        .build();
    alreadyReservationRoom.addReservation(mockUser.get());
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithReservationById(roomId)).willReturn(
        Optional.of(alreadyReservationRoom));

    assertThrows(AlreadyReservationException.class, () ->
        roomService.reserve(userId, roomId));
  }

  @Test
  @DisplayName("방 예약 인원수가 다 차서 실패.")
  public void full_reservation_room() throws Exception {
    Room fullReservationRoom = Room.builder()
        .hostId(2L)
        .limitUserCount(1)
        .startTime(LocalDateTime.now().plusHours(2))
        .build();
    fullReservationRoom.addReservation(mockUser.get());
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithReservationById(roomId)).willReturn(
        Optional.of(fullReservationRoom));

    assertThrows(LimitRoomUserCountException.class, () ->
        roomService.reserve(userId, roomId));
  }

  @Test
  @DisplayName("예약 성공")
  public void reserve_success() throws Exception {
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithReservationById(roomId)).willReturn(Optional.of(generalRoom));

    roomService.reserve(userId, roomId);

    assertEquals(generalRoom.getReservationList().size(), 1);
  }

  @Test
  @DisplayName("예약 취소요청시 방을 예약하고 있지 않은 상태")
  public void cancel_fail_not_reserve() throws Exception {

    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithReservationById(roomId)).willReturn(Optional.of(generalRoom));

    assertThrows(RequireReservationException.class,
        () -> roomService.cancelReservation(userId, roomId));
  }

  @Test
  @DisplayName("예약 취소 성공")
  public void cancel_success() throws Exception {
    generalRoom.addReservation(mockUser.get());
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithReservationById(roomId)).willReturn(Optional.of(generalRoom));

    roomService.cancelReservation(userId, roomId);

    assertEquals(generalRoom.getReservationList().size(), 0);
  }

  @Test
  @DisplayName("방 참여 시 시간 제한으로 실패(최대)")
  public void join_time_limit_max_fail() throws Exception {
    LocalDateTime after61MinStartTime = LocalDateTime.now().plusMinutes(61);
    Room room = Room.builder().hostId(2L).startTime(after61MinStartTime)
        .build();
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithRunningById(roomId)).willReturn(Optional.of(room));

    assertThrows(NotAllowJoinRequestException.class, () -> roomService.joinRoom(userId, roomId));
  }

  @Test
  @DisplayName("방 참여 시 시간 제한으로 실패(최소)")
  public void join_time_limit_min_fail() throws Exception {
    LocalDateTime after9MinStartTime = LocalDateTime.now().plusMinutes(9);
    Room room = Room.builder().hostId(2L).startTime(after9MinStartTime)
        .build();
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithRunningById(roomId)).willReturn(Optional.of(room));

    assertThrows(NotAllowJoinRequestException.class, () -> roomService.joinRoom(userId, roomId));

  }


  @Test
  @DisplayName("방 참여 시 이미 참여한 방이여서 실패")
  public void join_already_fail() throws Exception {
    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithRunningById(roomId)).willReturn(Optional.of(remain30MinRoom));

    assertThrows(AlreadyJoinRoomException.class, () -> roomService.joinRoom(userId, roomId));
  }

  @Test
  @DisplayName("방 참여시 인원수 제한으로 실패")
  public void join_user_Count_fail() throws Exception {
    Room limitUserRoom = Room.builder()
        .startTime(LocalDateTime.now().plusMinutes(30))
        .hostId(2L)
        .limitUserCount(1)
        .name("방이름")
        .build();
    limitUserRoom.initRunner(
        Running.builder().room(limitUserRoom).user(User.builder().build()).build());
    given(userRepository.findById(userId)).willReturn(Optional.of(User.builder().build()));
    given(roomRepository.findWithRunningById(roomId)).willReturn(Optional.of(limitUserRoom));

    assertThrows(LimitRoomUserCountException.class, () -> roomService.joinRoom(userId, roomId));
  }

  @Test
  @DisplayName("방 참여 성공")
  public void join_success() throws Exception {
    given(userRepository.findById(userId)).willReturn(Optional.of(User.builder().build()));
    given(roomRepository.findWithRunningById(roomId)).willReturn(Optional.of(remain30MinRoom));

    roomService.joinRoom(userId, roomId);
    assertEquals(remain30MinRoom.getRunningList().size(), 2);
  }

  @Test
  @DisplayName("방 참여 취소 시 참여하지 않은 방 실패")
  public void joinCancel_not_joined_fail() throws Exception {
    Room notJoinRoom = Room.builder()
        .startTime(LocalDateTime.now().plusMinutes(30))
        .hostId(2L)
        .limitUserCount(4)
        .name("방이름")
        .build();
    notJoinRoom.initRunner(
        Running.builder().room(notJoinRoom).user(User.builder().build()).build());

    given(userRepository.findById(userId)).willReturn(mockUser);
    given(roomRepository.findWithRunningById(roomId)).willReturn(Optional.of(notJoinRoom));

    assertThrows(RequireJoinException.class, () -> roomService.joinCancel(userId, roomId));
  }
}