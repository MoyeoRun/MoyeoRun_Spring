package com.moyeorun.api.domain.room.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomResponse {

  private Long roomId;
  private String name;
  private String thumbnailImage;
  private int targetDistance;
  private int limitUserCount;
  private int limitTime;
  private String targetPace;
  private LocalDateTime startTime;
  private Long hostId;
  private RoomStatus roomStatus;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<RoomUserResponse> userList;

  private int currentUserCount;

  @Builder
  public RoomResponse(Long roomId, String name, String thumbnailImage, int targetDistance,
      int limitUserCount, int limitTime, String targetPace, LocalDateTime startTime, Long hostId,
      RoomStatus roomStatus, LocalDateTime createdAt, LocalDateTime updatedAt,
      List<RoomUserResponse> userList, int currentUserCount) {
    this.roomId = roomId;
    this.name = name;
    this.thumbnailImage = thumbnailImage;
    this.targetDistance = targetDistance;
    this.limitUserCount = limitUserCount;
    this.limitTime = limitTime;
    this.targetPace = targetPace;
    this.startTime = startTime;
    this.hostId = hostId;
    this.roomStatus = roomStatus;
    this.userList = userList;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.currentUserCount = currentUserCount;
  }

  public static RoomResponse toDetailReservation(Room room, List<User> userList) {
    return getRoomResponse(room, userList);
  }

  public static RoomResponse toDetailJoin(Room room) {
    List<User> users = room.getRunningList().stream().map(Running::getUser).toList();
    return getRoomResponse(room, users);
  }

  private static RoomResponse getRoomResponse(Room room, List<User> users) {
    return RoomResponse.builder()
        .roomId(room.getId())
        .name(room.getName())
        .targetDistance(room.getTargetDistance())
        .limitUserCount(room.getLimitUserCount())
        .limitTime(room.getLimitTime())
        .targetDistance(room.getTargetDistance())
        .targetPace(room.getTargetPace())
        .startTime(room.getStartTime())
        .thumbnailImage(room.getThumbnailImage())
        .hostId(room.getHostId())
        .createdAt(room.getCreatedAt())
        .updatedAt(room.getUpdatedAt())
        .userList(users.stream().map(RoomUserResponse::from).toList())
        .build();
  }

  public static RoomResponse toListDto(Room room){
    return RoomResponse.builder()
        .roomId(room.getId())
        .name(room.getName())
        .thumbnailImage(room.getThumbnailImage())
        .targetPace(room.getTargetPace())
        .targetDistance(room.getTargetDistance())
        .limitTime(room.getLimitTime())
        .startTime(room.getStartTime())
        .limitUserCount(room.getLimitUserCount())
//        .currentUserCount(room.getCurrentUserCount())
        .build();
  }
}
