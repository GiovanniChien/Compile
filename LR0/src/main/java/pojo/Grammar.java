package pojo;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

    public String start;
    public List<Character> VTs;
    public List<String> VNs;
    public List<Rule> rules;

    public List<Rule> findRuleByLeft(String left) {
        List<Rule> result = new ArrayList<Rule>();
        for (Rule rule : rules) {
            if (rule.left.equals(left))
                result.add(rule);
        }
        return result;
    }

    public int findRule(Rule rule) {
        if (rule == null) return -1;
        for (int i = 0; i < rules.size(); i++) {
            Rule tmp = rules.get(i);
            if (tmp.left.equals(rule.left) && tmp.right.equals(rule.right))
                return i;
        }
        return -1;
    }
}
