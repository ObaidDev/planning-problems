package ma.plutus.planner.entities.assignment;

import java.util.List;

import lombok.Data;
import ma.plutus.planner.enums.Skill;


@Data
public class Task {
    
    private String id ;

    private int  duration ;

    private List<Skill> requiredSkills ;
}
