package com.moyeorun.api.domain.room.domain;

import com.moyeorun.api.domain.model.BaseTimeEntity;
import com.moyeorun.api.domain.room.exception.NotAllowHostSelfReqeustException;
import com.moyeorun.api.domain.room.exception.NotAllowReservationRequestException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "room")
  private List<Running> runningList = new ArrayList<>();

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

  public void close(){
    this.roomStatus = RoomStatus.CLOSE;
  }

  public void validateReservation(LocalDateTime currentTime, Long requestUserId){
    validateReservationTime(currentTime);
    validateIsHost(requestUserId);
  }

  private void validateReservationTime(LocalDateTime currentTime){
    LocalDateTime canReservationTime = this.startTime.minusHours(1);
    if(currentTime.isAfter(canReservationTime)){
      throw new NotAllowReservationRequestException();
    }
  }

  private void validateIsHost(Long requestId){
    if(hostId.equals(requestId)){
      throw new NotAllowHostSelfReqeustException();
    }
  }

  public void validationJoinTime(LocalDateTime currentTime){
    LocalDateTime canJoinTime = this.startTime.plusHours(1);
    if(currentTime.isAfter(canJoinTime)){
      /*
      *  불가능.
      */
    }
  }
}