package ma.plutus.planner.entities.exam_scheduling;

import java.util.List;
import java.util.Set;

import lombok.Data;
import ma.plutus.planner.enums.CoursType;


@Data
public class Course {
    
    private String id ;


    private CoursType coursType ;


    private Set<Student> studentsEnrolled ;

    private int numberOfStudents ;
}
