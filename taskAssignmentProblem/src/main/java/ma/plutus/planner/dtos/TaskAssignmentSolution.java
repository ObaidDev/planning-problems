package ma.plutus.planner.dtos;

import java.util.List;
import java.util.stream.Collectors;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.plutus.planner.entities.assignment.Employee;
import ma.plutus.planner.entities.assignment.Task;
import ma.plutus.planner.entities.assignment.TaskAssignment;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @PlanningSolution
public class TaskAssignmentSolution {


    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employeeRange")
    private List<Employee> employees ;

    @ProblemFactCollectionProperty
    private List<Task> tasks ;


    @PlanningEntityCollectionProperty
    private List<TaskAssignment> taskAssignments ;
    


    @PlanningScore
    private HardSoftScore score;
}
