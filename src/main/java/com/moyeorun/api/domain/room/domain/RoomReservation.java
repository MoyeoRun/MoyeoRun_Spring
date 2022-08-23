package com.moyeorun.api.domain.room.domain;

import com.moyeorun.api.domain.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "room_reservation")
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "room_reservation", columnNames = {"user_id", "room_id"})
})
public class RoomReservation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "room_id")
  private Room room;

  @Builder
  public RoomReservation(User user, Room room) {
    this.user = user;
    this.room = room;
  }
}
