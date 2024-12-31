package ma.plutus.examSchedule.entities.exam_scheduling ;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class TimeSlot {


    private String id ;


    private LocalDateTime startTime  ;


    private LocalDateTime endTime  ;
    
}
