package ma.plutus.planner.entities.assignment;

import java.util.List;

import lombok.Data;
import ma.plutus.planner.enums.Skill;


@Data
public class Employee {

    private String id ;

    private String name ;

    private List<Skill> skills ;

    private int maxHoursDay ;
}
