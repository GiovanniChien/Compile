package pojo;

import java.util.List;

/**
 * 优先关系表
 */
public class PriorityTable {

    public List<Character> element;//所有的终结符,也是行列对应的元素
    public int[][] table;//-1表示'<.',0表示'=.',1表示'.>'

}
