package pojo;

public class Rule {

    public String left;
    public String right;
    public int index;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule) {
            Rule rule1 = (Rule) obj;
            if (!rule1.left.equals(left)) return false;
            if (!rule1.right.equals(right)) return false;
            return rule1.index == index;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(left);
        sb.append("->");
        String subLeft = right.substring(0, index);
        String subRight = right.substring(index);
        sb.append(subLeft);
        sb.append(".");
        sb.append(subRight);
        return sb.toString();
    }

}
