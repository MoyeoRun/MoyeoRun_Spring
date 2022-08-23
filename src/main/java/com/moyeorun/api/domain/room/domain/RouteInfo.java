package com.moyeorun.api.domain.room.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "route_info")
@Getter
@NoArgsConstructor
public class RouteInfo {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double latitude;

  private Double longitude;

  @Column(name = "\"current_time\"")
  private int currentTime;

  private Double currentDistance;

  private String currentPace;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "running_id")
  private Running running;

  @Builder
  public RouteInfo(Double latitude, Double longitude, Double currentDistance,
     int currentTime ,String currentPace, Running running) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.currentDistance = currentDistance;
    this.currentPace = currentPace;
    this.running = running;
    this.currentTime = currentTime;
  }
}
