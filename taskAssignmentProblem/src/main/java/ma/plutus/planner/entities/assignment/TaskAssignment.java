package ma.plutus.planner.entities.assignment;

import java.time.LocalDateTime;
import java.util.List;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.solution.ProblemFactProperty;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningEntity
public class TaskAssignment {


    @PlanningVariable(valueRangeProviderRefs = "employeeRange")
    private Employee employee ;

    @ProblemFactProperty
    private Task task ;

    private LocalDateTime date ;
    



    public int getTaskDuration() {

        return this.task.getDuration() ;
    }
}
