package projectpackage.repository.reacteav.conditions;

import java.util.ArrayList;
import java.util.List;

public class ReactConditionBuilder {
    private String statement;
    private ReactCondition rootCondition;

    public ReactConditionBuilder(String statement) {
        this.statement = statement;
    }

    public ReactCondition build(){
        List<ReactStringCondition> stringConditions = new ArrayList<>();
        String[] lines = statement.split("-");
        for (String currentLine:lines){
            String[] tokens = currentLine.split("[():]");
            ReactStringCondition newCondition;
            if (tokens.length==3) {
                newCondition = new ReactStringCondition(tokens[0], tokens[1], tokens[2]);
            } else {
                newCondition = new ReactStringCondition(tokens[0], tokens[1], null);
            }
            stringConditions.add(newCondition);
        }
        ReactCondition previousCondition = null;
        for (ReactStringCondition stringCondition: stringConditions){
            ReactCondition condition;
            Class clazz = null;
            try {
                 clazz = Class.forName(stringCondition.getCurrentClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            condition = new ReactCondition(clazz, stringCondition.getTargetFieldName(), stringCondition.getNextClassFieldName(), previousCondition);
            if (null!=previousCondition){
                previousCondition.setNextCondition(condition);
            } else {
                rootCondition = condition;
            }
            previousCondition = condition;
        }
        return rootCondition;
    }


}
