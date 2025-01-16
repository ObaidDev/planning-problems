package ma.plutus.vehicle_routing.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.List;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.MemberCategory;

import ma.plutus.vehicle_routing.dto.Visit;



public class HintsConfig implements RuntimeHintsRegistrar{

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {

        /***
         * register updateArrivalTime
         */
        hints.reflection().registerType(
            Visit.class ,
            MemberCategory.values()
        ) ;
        

        /***
         * registre WebAppConfiguration class ²
         */


        hints.reflection().registerType(
            TypeReference.of("org.springframework.test.context.web.WebAppConfiguration"),
            MemberCategory.values()
        );


        hints.reflection().registerType(
            TypeReference.of("org.springframework.test.context.ContextConfiguration"),
            MemberCategory.values()
        );
    }
    
}
