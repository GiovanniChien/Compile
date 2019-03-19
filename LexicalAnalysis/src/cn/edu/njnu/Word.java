package cn.edu.njnu;

/**
 * 存放取出的每个单词的具体信息
 */
public class Word {

    private String word;
    private Integer row;//第几行
    private Integer col;//第几个
    private String category;//类型

    public Word() {
    }

    public Word(String word, Integer row, Integer col, String category) {
        this.word = word;
        this.row = row;
        this.col = col;
        this.category = category;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", row=" + row +
                ", col=" + col +
                ", category='" + category + '\'' +
                '}';
    }
}
