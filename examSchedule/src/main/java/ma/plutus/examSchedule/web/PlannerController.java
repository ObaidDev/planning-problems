package ma.plutus.examSchedule.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ai.timefold.solver.core.api.score.ScoreExplanation;
import ai.timefold.solver.core.api.score.ScoreManager;
import ai.timefold.solver.core.api.score.analysis.ScoreAnalysis;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.constraint.ConstraintMatch;
import ai.timefold.solver.core.api.score.constraint.ConstraintMatchTotal;
import ai.timefold.solver.core.api.solver.ScoreAnalysisFetchPolicy;
import ai.timefold.solver.core.api.solver.Solver;
import ma.plutus.examSchedule.dtos.ExamSolution;
import ma.plutus.examSchedule.dtos.ExplanationResponse;
import ma.plutus.examSchedule.dtos.ExplanationResponse.ConstraintViolation;
import ma.plutus.examSchedule.dtos.ExplanationResponse.ViolationDetail;


@RestController
@RequestMapping("/exam")
public class PlannerController {
    

    private final Solver<ExamSolution> examSolver;
    private final ScoreManager<ExamSolution, HardSoftScore> scoreManager ;

    @Autowired
    public PlannerController(
        Solver<ExamSolution> examSolver ,
        ScoreManager<ExamSolution, HardSoftScore> scoreManager
        
    ) {

        this.examSolver = examSolver ;
        this.scoreManager = scoreManager ;
    }



    @PostMapping("/solve")
    public ExamSolution solveExamProblem(@RequestBody ExamSolution examProblem) {
        
        return examSolver.solve(examProblem) ;
    }


    @PostMapping("/explanation")
    public ExplanationResponse analyze(@RequestBody ExamSolution solution) {

        ScoreExplanation<ExamSolution, HardSoftScore> explanation = scoreManager.explainScore(solution);

        // Map constraint violations to the response structure
        Map<String, ConstraintViolation> constraintViolations = new HashMap<>();
        for (ConstraintMatchTotal<HardSoftScore> constraintMatchTotal : explanation.getConstraintMatchTotalMap().values()) {
            List<ViolationDetail> violations = constraintMatchTotal.getConstraintMatchSet().stream()
                    .map(constraintMatch -> new ViolationDetail(
                            constraintMatch.getJustificationList(),
                            constraintMatch.getScore()))
                    .collect(Collectors.toList());

            constraintViolations.put(
                    constraintMatchTotal.getConstraintName(),
                    new ConstraintViolation(
                            constraintMatchTotal.getConstraintName(),
                            constraintMatchTotal.getScore(),
                            violations));
        }

        // Create and return the response
        return new ExplanationResponse(explanation.getScore(), constraintViolations);
    
    }
}
