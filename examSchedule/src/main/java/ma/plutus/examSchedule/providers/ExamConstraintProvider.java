package ma.plutus.examSchedule.providers;

import java.util.Set;

import org.jspecify.annotations.NonNull;

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
                        .penalize(HardSoftScore.ONE_HARD)
                        .asConstraint("No two exams can be scheduled in the same room at the same time");
    }


    /***
     * 
     * The room capacity must not be exceeded.
     */

    private Constraint roomCapacityConstraint(ConstraintFactory factory) {

        return factory.forEach(Exam.class)
                        .filter(exam ->exam.getCourse().getNumberOfStudents() > exam.getRoom().getCapacity())
                       .penalize(HardSoftScore.ONE_HARD)
                       .asConstraint("The room capacity must not be exceeded.");

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
                            Joiners.filtering((exam1 , exam2)-> !exam1.equals(exam2) && hasStudentConflict(exam1, exam2))
                        )
                        .penalize(HardSoftScore.ONE_HARD)
                        .asConstraint("No student can have more than one exam at the same time");

    }


    private Constraint noStudentExamOverlap(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Exam.class) // Iterate over all exams
            .join(Exam.class, // Join with another exam
                Joiners.equal(Exam::getTimeSlot), // Same timeslot
                Joiners.filtering((exam1, exam2) -> 
                    !exam1.equals(exam2) && // Avoid comparing the same exam
                    exam1.getCourse().getStudentsEnrolled().stream()
                        .anyMatch(student -> exam2.getCourse().getStudentsEnrolled().contains(student))
                ) // Check if any student is enrolled in both exams
            )
            .penalize(HardSoftScore.ONE_HARD) // Penalize hard score for violations
            .asConstraint("No student can have two exams at the same time");
    }




    private boolean hasStudentConflict(Exam exam1 , Exam exam2) {

        Set<Student> exam1Students = exam1.getCourse().getStudentsEnrolled() ;
        Set<Student> exam2Students = exam2.getCourse().getStudentsEnrolled() ;
        return exam1Students.stream().anyMatch(exam2Students::contains);
    }
}
