package ma.plutus.planner.providers;

import org.jspecify.annotations.NonNull;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ma.plutus.planner.entities.assignment.TaskAssignment;

public class TaskAssignmentConstraintProvider implements ConstraintProvider{

    @Override
    public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory factory) {
        return new Constraint[] {
            oneAssignmentPerTaskConstraint(factory) ,
            requiredSkillsConstraint(factory) ,
            maxHoursPerDayConstraint(factory)
        } ;
    }
    


    private Constraint requiredSkillsConstraint(ConstraintFactory factory) {
        return factory.forEach(TaskAssignment.class)
                        .filter(
                            assignment -> !assignment.getEmployee().getSkills()
                                            .containsAll(assignment.getTask().getRequiredSkills())
                        )
                        .penalize("Missing required skills", HardSoftScore.ONE_HARD);
    }

    public Constraint maxHoursPerDayConstraint(ConstraintFactory factory) {

        return factory.forEach(TaskAssignment.class)
                        .groupBy(TaskAssignment::getEmployee , ConstraintCollectors.sum(TaskAssignment::getTaskDuration))
                        .filter((employee, totalHours)-> totalHours > employee.getMaxHoursDay())
                        .penalize("Max hours per day exceeded", HardSoftScore.ONE_HARD);
    }


    private Constraint oneAssignmentPerTaskConstraint(ConstraintFactory factory){
        return factory.forEach(TaskAssignment.class)
                        .filter(
                            assignment -> assignment.getEmployee() == null
                        )
                        .penalize("Task must be assigned to one employee", HardSoftScore.ONE_HARD) ;
    }
}
