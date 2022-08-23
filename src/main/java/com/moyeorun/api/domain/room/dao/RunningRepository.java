package com.moyeorun.api.domain.room.dao;

import com.moyeorun.api.domain.room.domain.RoomStatus;
import com.moyeorun.api.domain.room.domain.Running;
import com.moyeorun.api.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RunningRepository extends JpaRepository<Running, Long> {

  @Query("select count(run) > 0 "
      + "FROM Running run join run.room R"
      + " WHERE run.user = :user and R.roomStatus <> :status ")
  Boolean checkUserAlreadyRoom(
      @Param("user") User user,
      @Param("status")RoomStatus status
      );
}


