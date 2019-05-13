package pojo;

import java.util.List;

/**
 * 存放每一条文法
 */
public class Statement {

    public String left;//文法左侧非终结符
    public List<String> rule;//文法右侧每一条规则("|"分割)

}
