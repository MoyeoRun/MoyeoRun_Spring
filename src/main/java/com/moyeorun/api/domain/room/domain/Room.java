package com.moyeorun.api.domain.room.domain;

import com.moyeorun.api.domain.model.BaseTimeEntity;
import com.moyeorun.api.domain.room.exception.AlreadyJoinRoomException;
import com.moyeorun.api.domain.room.exception.AlreadyReservationException;
import com.moyeorun.api.domain.room.exception.NotAllowHostSelfReqeustException;
import com.moyeorun.api.domain.room.exception.NotAllowJoinRequestException;
import com.moyeorun.api.domain.room.exception.NotAllowReservationRequestException;
import com.moyeorun.api.domain.room.exception.RequireJoinException;
import com.moyeorun.api.domain.room.exception.RequireReservationException;
import com.moyeorun.api.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Room extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private int targetDistance;

  @Column(nullable = false)
  private int limitUserCount;

  @Column(nullable = false)
  private int limitTime;

  @Column(nullable = false)
  private String targetPace;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private String thumbnailImage;

  @Column(nullable = false)
  private Long hostId;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private RoomStatus roomStatus;

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Running> runningList = new ArrayList<>();

  @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RoomReservation> reservationList = new ArrayList<>();

  @Builder
  public Room(String name, int targetDistance, int limitUserCount, int limitTime, String targetPace,
      LocalDateTime startTime, String thumbnailImage, Long hostId, RoomStatus roomStatus) {
    this.name = name;
    this.targetDistance = targetDistance;
    this.limitUserCount = limitUserCount;
    this.limitTime = limitTime;
    this.targetPace = targetPace;
    this.startTime = startTime;
    this.thumbnailImage = thumbnailImage;
    this.hostId = hostId;
    this.roomStatus = roomStatus;
  }

  public void initRunner(Running running) {
    this.runningList.add(running);
  }

  public void addReservation(User user) {
    if (isAlreadyReservationUser(user)) {
      throw new AlreadyReservationException();
    }
    this.reservationList.add(RoomReservation.builder()
        .room(this)
        .user(user)
        .build());
  }

  private boolean isAlreadyReservationUser(User user) {
    Optional<RoomReservation> result = this.reservationList.stream().filter(roomReservation ->
        roomReservation.getUser().equals(user)
    ).findAny();
    return result.isPresent();
  }

  public void cancelReservation(User user) {
    if (!isAlreadyReservationUser(user)) {
      throw new RequireReservationException();
    }
    reservationList.removeIf(reservation -> reservation.getUser().equals(user));
  }

  private boolean isAlreadyJoinRoomUser(User user) {
    Optional<Running> result = this.runningList.stream().filter(running ->
        running.getUser().equals(user)
    ).findAny();
    return result.isPresent();
  }

  public void joinRunner(User user) {
    if (isAlreadyJoinRoomUser(user)) {
      throw new AlreadyJoinRoomException();
    }
    this.runningList.add(Running.builder()
        .room(this)
        .user(user)
        .build());
  }

  public void removeRunner(User user) {
    if (!isAlreadyJoinRoomUser(user)) {
      throw new RequireJoinException();
    }
    this.runningList.removeIf(running -> running.getUser().equals(user));
  }

  public void close() {
    this.roomStatus = RoomStatus.CLOSE;
  }

  public void validateReservation(LocalDateTime currentTime, Long requestUserId) {
    validateReservationTime(currentTime);
    validateIsHost(requestUserId);
  }

  public void validateJoin(LocalDateTime currentTime, Long requestUserId) {
    validationJoinTime(currentTime);
    validateIsHost(requestUserId);
  }

  private void validateReservationTime(LocalDateTime currentTime) {
    LocalDateTime canReservationTime = this.startTime.minusHours(1);
    if (currentTime.isAfter(canReservationTime)) {
      throw new NotAllowReservationRequestException();
    }
  }

  private void validateIsHost(Long requestId) {
    if (hostId.equals(requestId)) {
      throw new NotAllowHostSelfReqeustException();
    }
  }

  private void validationJoinTime(LocalDateTime currentTime) {
    LocalDateTime canJoinTimeOneHour = this.startTime.minusHours(1);
    LocalDateTime canJoinTimeOneMin = this.startTime.minusMinutes(10);
    if (currentTime.isBefore(canJoinTimeOneHour) || currentTime.isAfter(canJoinTimeOneMin)) {
      throw new NotAllowJoinRequestException();
    }
  }
}
