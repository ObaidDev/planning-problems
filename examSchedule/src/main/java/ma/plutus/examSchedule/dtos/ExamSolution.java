package ma.plutus.examSchedule.dtos;

import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.plutus.examSchedule.entities.exam_scheduling.Course;
import ma.plutus.examSchedule.entities.exam_scheduling.Exam;
import ma.plutus.examSchedule.entities.exam_scheduling.Room;
import ma.plutus.examSchedule.entities.exam_scheduling.TimeSlot;


@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class ExamSolution {

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "roomRange")
    private List<Room> rooms ;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "timeSlotRange")
    private List<TimeSlot> timeSlots ;

    @ProblemFactCollectionProperty
    private List<Course> courses ;


    @PlanningEntityCollectionProperty
    private List<Exam> exams ;

    @PlanningScore
    private HardSoftScore score ;
    
}
