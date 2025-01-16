package ma.plutus.vehicle_routing.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import lombok.extern.slf4j.Slf4j;
import ma.plutus.vehicle_routing.TestConfig;

@Slf4j
@SpringBootTest
@ContextConfiguration
@Import(TestConfig.class)
public abstract class BaseIntegrationTest {
    
}
