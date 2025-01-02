package ma.plutus.examSchedule.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeHint;

import ai.timefold.solver.core.api.solver.Solver;
import ma.plutus.examSchedule.providers.ExamConstraintProvider;


public class HintsConfig implements RuntimeHintsRegistrar{

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader arg1) {

        hints.reflection().registerType(
            Solver.class ,
            TypeHint.builtWith(
                MemberCategory.values()
            )
        ) ;

        hints.reflection().registerType(
            ExamConstraintProvider.class , 
            MemberCategory.values()
        ) ;
    }

}
