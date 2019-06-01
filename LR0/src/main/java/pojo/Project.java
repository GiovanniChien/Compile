package pojo;

import java.util.List;

//项目集
public class Project {

    public List<Rule> rules;

    public boolean containsRule(Rule rule) {
        if (rules == null) return false;
        for (Rule rule1 : rules) {
            if (rule.equals(rule1))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Project) {
            Project project = (Project) obj;
            if (rules.size() != project.rules.size())
                return false;
            for (Rule rule : rules) {
                if (!project.rules.contains(rule))
                    return false;
            }
        }
        return true;
    }
}
