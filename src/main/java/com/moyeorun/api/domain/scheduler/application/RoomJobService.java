package com.moyeorun.api.domain.scheduler.application;


import com.moyeorun.api.domain.scheduler.job.RoomCloseJob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomJobService {

  private final Scheduler scheduler;
  private final String CLOSE_ROOM_GROUP = "CLOSE_ROOM_GROUP";

  public void registerCloseRoomJob(Long roomId, LocalDateTime startTime){
    try{
      scheduler.scheduleJob(closeRoomJobDetail(roomId), closeRoomJobTrigger(roomId, startTime));
    }catch (SchedulerException e ){
      log.error("room Close Job register Fail " + e.getMessage());
    }
  }

  private JobDetail closeRoomJobDetail(Long roomId){
    String jobName = "closeRoom"+roomId;
    return JobBuilder.newJob(RoomCloseJob.class)
        .withIdentity(jobName,CLOSE_ROOM_GROUP)
        .usingJobData("roomId",roomId)
        .build();
  }

  private Trigger closeRoomJobTrigger(Long roomId, LocalDateTime startTime){
    String triggerName = "closeRoom"+roomId;
    Date triggerTime = Timestamp.valueOf(startTime);
    return TriggerBuilder.newTrigger()
        .withIdentity(triggerName,CLOSE_ROOM_GROUP)
        .startAt(triggerTime)
        .build();
  }
}
