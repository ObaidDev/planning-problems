package ma.plutus.planner.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import ma.plutus.planner.dtos.ExamSolution;
import ma.plutus.planner.dtos.TaskAssignmentSolution;
import ma.plutus.planner.entities.assignment.Employee;
import ma.plutus.planner.entities.assignment.TaskAssignment;
import ma.plutus.planner.entities.exam_scheduling.Exam;
import ma.plutus.planner.providers.ExamConstraintProvider;
import ma.plutus.planner.providers.TaskAssignmentConstraintProvider;

@Configuration
public class SolverBeanConfig {
    


    @Bean
    public Solver<TaskAssignmentSolution> taskAssignmentSolver() {

        // SolverConfig solverConfig = (new SolverConfig())
        //     .withSolutionClass(TaskAssignmentSolution.class)
        //     .withEntityClassList(List.of(TaskAssignment.class))
        //     .withConstraintProviderClass(TaskAssignmentConstraintProvider.class)
        //     .withTerminationConfig(new TerminationConfig().withSecondsSpentLimit(30L));


        // SolverFactory<TaskAssignmentSolution> solverFactory = SolverFactory.create(solverConfig);

        SolverFactory<TaskAssignmentSolution> solverFactory =   SolverFactory.createFromXmlResource("classpath:taskAssignmentSolverConfig.xml") ;

        return solverFactory.buildSolver();
    }


    @Bean
    public Solver<ExamSolution> examSchedulingSolver() {

        // SolverConfig solverConfig = (new SolverConfig())
        //     .withSolutionClass(ExamSolution.class)
        //     .withEntityClassList(List.of(Exam.class))
        //     .withConstraintProviderClass(ExamConstraintProvider.class)
        //     .withTerminationConfig(new TerminationConfig().withSecondsSpentLimit(30L));


        // SolverFactory<ExamSolution> solverFactory = SolverFactory.create(solverConfig);

        SolverFactory<ExamSolution> solverFactory =   SolverFactory.createFromXmlResource("classpath:examSchedulingSolverConfig.xml") ;

        return solverFactory.buildSolver();
    }
}
