package com.moyeorun.api.domain.room.domain;

import com.moyeorun.api.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "running_unique", columnNames = {"user_id", "room_id"})
})
public class Running {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  private String runPace;

  private Integer runDistance;

  private Integer runTime;

  private Boolean isFinish;

  @OneToMany(mappedBy = "running")
  private List<RouteInfo> routeInfoList = new ArrayList<>();

  @Builder
  public Running(User user, Room room, String runPace, int runDistance, int runTime,
      Boolean isFinish) {
    this.user = user;
    this.room = room;
    this.runPace = runPace;
    this.runDistance = runDistance;
    this.runTime = runTime;
    this.isFinish = isFinish;
  }
}
