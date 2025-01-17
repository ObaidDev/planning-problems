package ma.plutus.vehicle_routing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import ma.plutus.vehicle_routing.config.HintsConfig;

@SpringBootApplication
@ImportRuntimeHints(HintsConfig.class)
public class VehicleRoutingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRoutingApplication.class, args);
	}

}
