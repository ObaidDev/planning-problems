package ma.plutus.examSchedule.providers;

import java.util.Set;

import org.jspecify.annotations.NonNull;

import ai.timefold.solver.core.api.domain.constraintweight.ConstraintConfiguration;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import ma.plutus.examSchedule.entities.exam_scheduling.Exam;
import ma.plutus.examSchedule.entities.exam_scheduling.Student;



public class ExamConstraintProvider implements ConstraintProvider{

    @Override
    public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory factory) {
        return new Constraint[] {
            roomCapacityConstraint(factory) ,
            roomConflictConstraint(factory) ,
            studentConflictConstraint(factory)
        } ;
    }
    


    /**
     * 
     * No two exams can be scheduled in the same room at the same time.
     */

    private Constraint roomConflictConstraint(ConstraintFactory factory) {

        return factory.forEach(Exam.class)
                        .join(
                            Exam.class ,
                            Joiners.equal(Exam::getRoom) ,
                            Joiners.equal(Exam::getTimeSlot) ,
                            Joiners.lessThan(Exam::getId)
                        )
                        .penalize("No two exams can be scheduled in the same room at the same time", HardSoftScore.ONE_HARD);
    }


    /***
     * 
     * The room capacity must not be exceeded.
     */

    private Constraint roomCapacityConstraint(ConstraintFactory factory) {

        return factory.forEach(Exam.class)
                        .filter(exam ->exam.getCourse().getNumberOfStudents() > exam.getRoom().getCapacity())
                       .penalize("The room capacity must not be exceeded.", HardSoftScore.ONE_HARD);

    }



    /**
     * 
     * 
     * No student can have two exams at the same time.
     */

    private Constraint studentConflictConstraint(ConstraintFactory factory) {
        return factory.forEach(Exam.class)
                        .join(
                            Exam.class ,
                            Joiners.equal(Exam::getTimeSlot),
                            Joiners.filtering((exam1 , exam2)-> hasStudentConflict(exam1, exam2))
                        )
                        .penalize("No student can have two exams at the same time.", HardSoftScore.ONE_HARD);

    }





    private boolean hasStudentConflict(Exam exam1 , Exam exam2) {

        Set<Student> exam1Students = exam1.getCourse().getStudentsEnrolled() ;
        Set<Student> exam2Students = exam2.getCourse().getStudentsEnrolled() ;
        return exam1Students.stream().anyMatch(exam2Students::contains);
    }
}
