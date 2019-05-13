package pojo;

import java.util.List;

/**
 * 存放文法集合
 */
public class Grammar {

    public String start;//文法的起始符号
    public List<String> VNs;//非终结符表
    public List<Character> VTs;//终结符表
    public List<Statement> statements;//每一条文法

}
