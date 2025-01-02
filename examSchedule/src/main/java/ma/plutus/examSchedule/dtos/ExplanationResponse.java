package ma.plutus.examSchedule.dtos;


import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExplanationResponse {

    private HardSoftScore score;
    private Map<String, ConstraintViolation> constraintViolations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConstraintViolation { // Make this static
        private String constraintName;
        private HardSoftScore totalPenalty;
        private List<ViolationDetail> violations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViolationDetail { // Make this static
        private List<Object> justificationList;
        private HardSoftScore penalty;
    }
}