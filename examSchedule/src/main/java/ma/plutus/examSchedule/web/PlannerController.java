package ma.plutus.examSchedule.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.timefold.solver.core.api.score.analysis.ScoreAnalysis;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.ScoreAnalysisFetchPolicy;
import ai.timefold.solver.core.api.solver.Solver;
import ma.plutus.examSchedule.dtos.ExamSolution;


@RestController
@RequestMapping("/exam")
public class PlannerController {
    


    private final Solver<ExamSolution> examSolver;


    @Autowired
    public PlannerController(
        Solver<ExamSolution> examSolver
        
    ) {

        this.examSolver = examSolver ;
    }



    @PostMapping("/solve")
    public ExamSolution solveExamProblem(@RequestBody ExamSolution examProblem) {
        
        return examSolver.solve(examProblem) ;
    }


    // @GetMapping("/analyze")
    // public ScoreAnalysis<HardSoftLongScore> analyze(ExamSolution problem,
    //                                                 @RequestParam("fetchPolicy") ScoreAnalysisFetchPolicy fetchPolicy) {
    //     return fetchPolicy == null ? examSolver.analyze(problem) : examSolver.analyze(problem, fetchPolicy);
    // }
}
