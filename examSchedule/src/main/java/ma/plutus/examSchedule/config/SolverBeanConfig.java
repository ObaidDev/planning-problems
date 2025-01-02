package ma.plutus.examSchedule.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ai.timefold.solver.core.api.score.ScoreManager;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import ma.plutus.examSchedule.dtos.ExamSolution;
import ma.plutus.examSchedule.entities.exam_scheduling.Exam;
import ma.plutus.examSchedule.providers.ExamConstraintProvider;


@Configuration
public class SolverBeanConfig {


    @Bean
    public Solver<ExamSolution> examSchedulingSolver() {

        SolverConfig solverConfig = (new SolverConfig())
            .withSolutionClass(ExamSolution.class)
            .withEntityClassList(List.of(Exam.class))
            .withConstraintProviderClass(ExamConstraintProvider.class)
            .withTerminationConfig(new TerminationConfig().withSecondsSpentLimit(30L));


        SolverFactory<ExamSolution> solverFactory = SolverFactory.create(solverConfig);


        return solverFactory.buildSolver();
    }


    @Bean
    public ScoreManager<ExamSolution, HardSoftScore> scoreManager() {

        SolverConfig solverConfig = (new SolverConfig())
            .withSolutionClass(ExamSolution.class)
            .withEntityClassList(List.of(Exam.class))
            .withConstraintProviderClass(ExamConstraintProvider.class)
            .withTerminationConfig(new TerminationConfig().withSecondsSpentLimit(30L));


        SolverFactory<ExamSolution> solverFactory = SolverFactory.create(solverConfig);
        
        return ScoreManager.create(solverFactory);
    }
}
