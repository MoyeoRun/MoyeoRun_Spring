package com.moyeorun.api.domain.room.dao;

import static com.moyeorun.api.domain.room.domain.QRoom.room;
import static com.moyeorun.api.domain.room.domain.QRunning.running;

import com.moyeorun.api.domain.room.domain.QRunning;
import com.moyeorun.api.domain.room.domain.Room;
import com.moyeorun.api.domain.room.domain.RoomStatus;
import com.moyeorun.api.domain.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoomQueryRepository {

  private final JPAQueryFactory queryFactory;

  public Room findCurrentRunningRoom(Long findUser){
    return queryFactory.selectFrom(room)
        .distinct()
        .join(room.runningList, running).fetchJoin()
        .where(room.roomStatus.eq(RoomStatus.CLOSE).not(),
            running.user.id.eq(findUser))
        .fetchOne();

  }
  public List<Room> findAllAndReservation(Long id, int count) {
    return queryFactory.select(room)
        .from(room)
        .where(reservationCondition(id))
        .limit(count)
        .orderBy(room.startTime.asc())
        .fetch();
  }

  public List<Room> findAllAndJoinMember(Long id, int count) {
    return queryFactory.select(room)
        .from(room)
        .where(realtimeCondition(id))
        .limit(count)
        .orderBy(room.startTime.asc())
        .fetch();
  }

  private BooleanExpression reservationCondition(Long id) {
    return roomIsOpen()
        .and(lastId(id))
        .and(room.startTime.after(LocalDateTime.now().plusHours(1)))
        .and(maxReservationUser());
  }

  private BooleanExpression realtimeCondition(Long id) {
    return roomIsOpen()
        .and(lastId(id))
        .and(room.startTime.between(LocalDateTime.now().plusMinutes(10),
            LocalDateTime.now().plusHours(1)))
        .and(maxRunningUser());
  }

  private BooleanExpression roomIsOpen() {
    return room.roomStatus.eq(RoomStatus.OPEN);
  }


  private BooleanExpression maxRunningUser() {
    return room.limitUserCount.eq(room.runningList.size()).not();
  }

  private BooleanExpression maxReservationUser() {
    return room.limitUserCount.eq(room.reservationList.size()).not();
  }

  private BooleanExpression lastId(Long id) {
    return id == null ? null : room.id.gt(id);
  }
}
