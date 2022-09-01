package com.moyeorun.api.domain.room.dto.request;

import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateRoomRequest {

  @NotBlank
  @Size(min = 1, max = 30)
  private String name;

  @NotBlank
  private String thumbnailImage;

  @NotNull
  @FutureOrPresent
  private LocalDateTime startTime;

  @NotBlank
  private String targetPace;

  @NotNull
  @Positive
  private int targetDistance;

  @NotNull
  @Positive
  private int limitTime;

  @NotNull
  @Min(1)
  @Max(8)
  private int limitUserCount;

  public Room toEntity(Long userId){
    return Room.builder()
        .name(name)
        .targetDistance(targetDistance)
        .limitUserCount(limitUserCount)
        .limitTime(limitTime)
        .targetPace(targetPace)
        .startTime(startTime)
        .thumbnailImage(thumbnailImage)
        .hostId(userId)
        .roomStatus(RoomStatus.OPEN)
        .build();
  }
}
