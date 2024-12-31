package ma.plutus.examSchedule.entities.exam_scheduling;

import java.util.List;

import lombok.Data;


@Data
public class Student {


    private String id ;


    private String name ;
    

    private List<Course> coursesEnrolled ;
    
}
