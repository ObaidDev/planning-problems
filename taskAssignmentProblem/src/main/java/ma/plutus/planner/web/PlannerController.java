package ma.plutus.planner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.timefold.solver.core.api.solver.Solver;
import ma.plutus.planner.dtos.ExamSolution;
import ma.plutus.planner.dtos.TaskAssignmentSolution;

@RestController
public class PlannerController {
    


    private final Solver<TaskAssignmentSolution> solver;
    private final Solver<ExamSolution> examSolver;


    @Autowired
    public PlannerController(
        Solver<TaskAssignmentSolution> solver ,
        Solver<ExamSolution> examSolver
        
    ) {
        this.solver = solver;

        this.examSolver = examSolver ;
    }



    @PostMapping("/task/solve")
    public TaskAssignmentSolution solveTaskAssignmentProblem(@RequestBody TaskAssignmentSolution taskAssignmentProblem) {
        
        return solver.solve(taskAssignmentProblem) ;
    }


    @PostMapping("/exam/solve")
    public ExamSolution solveExamProblem(@RequestBody ExamSolution examProblem) {
        
        return examSolver.solve(examProblem) ;
    }
}
