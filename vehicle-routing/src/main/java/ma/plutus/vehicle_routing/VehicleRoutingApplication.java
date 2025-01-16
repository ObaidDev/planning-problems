package ma.plutus.vehicle_routing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ma.plutus.vehicle_routing.config.HintsConfig;

@SpringBootApplication
@ImportAutoConfiguration(HintsConfig.class)
public class VehicleRoutingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRoutingApplication.class, args);
	}

}
