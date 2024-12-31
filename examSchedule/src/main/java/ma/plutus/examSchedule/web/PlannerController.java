package ma.plutus.examSchedule.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ai.timefold.solver.core.api.solver.Solver;
import ma.plutus.examSchedule.dtos.ExamSolution;


@RestController
public class PlannerController {
    


    private final Solver<ExamSolution> examSolver;


    @Autowired
    public PlannerController(
        Solver<ExamSolution> examSolver
        
    ) {

        this.examSolver = examSolver ;
    }



    @PostMapping("/exam/solve")
    public ExamSolution solveExamProblem(@RequestBody ExamSolution examProblem) {
        
        return examSolver.solve(examProblem) ;
    }
}
