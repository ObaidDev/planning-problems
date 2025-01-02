package ma.plutus.examSchedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import ma.plutus.examSchedule.config.HintsConfig;

@SpringBootApplication
@ImportRuntimeHints(HintsConfig.class)
public class ExamScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamScheduleApplication.class, args);
	}

}
