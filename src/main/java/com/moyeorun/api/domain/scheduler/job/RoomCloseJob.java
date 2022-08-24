package com.moyeorun.api.domain.scheduler.job;

import com.moyeorun.api.domain.room.dao.RoomRepository;
import com.moyeorun.api.domain.room.domain.Room;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomCloseJob extends QuartzJobBean {

  private final PlatformTransactionManager transactionManager;
  private final RoomRepository roomRepository;

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    Long roomId = Long.valueOf(context.getJobDetail().getJobDataMap().get("roomId").toString());
    log.info("ROME CLOSE, ROOM_ID = " + roomId);

    TransactionStatus status = transactionManager.getTransaction(
        new DefaultTransactionDefinition());
    try{
      Optional<Room> findRoom = roomRepository.findById(roomId);

      findRoom.ifPresent(Room::close);

      transactionManager.commit(status);
    }catch (Exception e ){
     log.error("error 발생 " + e.getMessage());
      transactionManager.rollback(status);
    }
  }
}
