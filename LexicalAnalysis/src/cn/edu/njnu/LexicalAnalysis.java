package cn.edu.njnu;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class LexicalAnalysis {

    private Set<String> keyWordSet;//关键字集合
    private Set<String> operatorSet;//操作符集合
    private Set<String> boundarySet;//界限符集合
    private List<Word> words;//存放分析的结果
    private StringBuilder tmpWord;
    private int col;

    public List<Word> getWords() {
        return words;
    }

    public LexicalAnalysis() {
        String[] keyWords = {"auto", "short", "int", "float", "long", "double", "char", "struct", "union", "enum", "typedef", "const",
                "unsigned", "signed", "extern", "register", "static", "volatile", "void", "if", "else", "switch", "case",
                "for", "do", "while", "goto", "continue", "break", "default", "sizeof", "return"};
        keyWordSet = new HashSet<>(Arrays.asList(keyWords));
        String[] operator = {"(", ")", "[", "]", "->", ".", "!", "~", "++", "--", "+", "-", "*", "&", "/", "%"
                , "<<", ">>", "<", ">", ">=", "<=", "==", "!=", "^", "|", "&&", "||", "?", ":", "+=", "-="
                , "*=", "/=", "%=", "&=", "|=", "^=", "<<=", ">=", "="};
        operatorSet = new HashSet<>(Arrays.asList(operator));
        String[] boundary = {",", ";", "\"", "\'","{","}"};
        boundarySet = new HashSet<>(Arrays.asList(boundary));
        words = new ArrayList<>();
        tmpWord = new StringBuilder();
        col = 0;
    }

    private Boolean isOperator(String str) {
        return operatorSet.contains(str);
    }

    private Boolean isBoundary(String str) {
        return boundarySet.contains(str);
    }

    private Boolean isKeyWords(String str) {
        return keyWordSet.contains(str);
    }

    private Boolean isDigit(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //分析一个单词
    private void analysisWord(String word, int row) {
        if (word == null || word.equals("")) return;
        if (isKeyWords(word)) {
            words.add(new Word(word, row, col, "关键字"));
        } else if (isDigit(word)) {
            words.add(new Word(word, row, col, "数值"));
        } else {
            words.add(new Word(word, row, col, ""));
        }
        col++;
    }

    //分析一行
    private void analysisLine(String line, int row) {
        col = 0;
        if (line == null || line.length() == 0) return;
        line = line.trim();//去除字符串两端的空格
        char tmpChar;
        for (int i = 0; i < line.length(); i++) {
            tmpChar = line.charAt(i);
            if (tmpChar == ' ') {
                analysisWord(tmpWord.toString(), row);
                tmpWord = new StringBuilder();
            } else if (isBoundary(Character.toString(tmpChar))) {
                analysisWord(tmpWord.toString(), row);
                tmpWord = new StringBuilder();
                words.add(new Word(Character.toString(tmpChar), row, col, "边界符"));
                col++;
            } else if (isOperator(Character.toString(tmpChar))) {
                if (tmpChar == '.') {
                    tmpWord.append(tmpChar);
                    continue;
                }
                analysisWord(tmpWord.toString(), row);
                tmpWord = new StringBuilder();
                tmpWord.append(tmpChar);
                while (i + 1 < line.length() && isOperator(Character.toString(line.charAt(i + 1)))) {
                    i++;
                    tmpWord.append(line.charAt(i));
                }
                if (isOperator(tmpWord.toString())) {
                    words.add(new Word(tmpWord.toString(), row, col, "操作符"));
                    col++;
                } else {
                    for (int k = 0; k < tmpWord.length(); k++) {
                        words.add(new Word(Character.toString(tmpWord.charAt(k)), row, col, "操作符"));
                        col++;
                    }
                }
                tmpWord = new StringBuilder();
            } else {
                tmpWord.append(tmpChar);
            }
        }
        if (tmpWord.length() > 0) {
            analysisWord(tmpWord.toString(), row);
            tmpWord = new StringBuilder();
        }
    }

    public void openFile(String fileName) {
        if (fileName == null) return;
        File file = new File(fileName);
        FileReader fileReader = null;
        BufferedReader buffReader = null;
        try {
            fileReader = new FileReader(file);
            buffReader = new BufferedReader(fileReader);
            String line;
            int row = 0;
            do {
                line = buffReader.readLine();
                analysisLine(line, row);
                row++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (buffReader != null) {
                    buffReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        LexicalAnalysis analysis = new LexicalAnalysis();
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("请选择文件");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int res = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == res) {
            path = fileChooser.getSelectedFile().getPath();
        }
        analysis.openFile(path);
        List<Word> words = analysis.getWords();
        for (Word word : words) {
            System.out.println(word.toString());
        }
    }

}
