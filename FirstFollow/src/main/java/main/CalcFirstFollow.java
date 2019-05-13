package main;

import pojo.FAT;
import pojo.FirstFollow;
import pojo.Grammar;
import pojo.Statement;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CalcFirstFollow {

    private Grammar grammar;//读取的文法集合
    private List<FirstFollow> ffs;//存放first、follow集
    private FAT fat;

    private void readGrammar(String line, int row) {
        if (line == null) return;
        line = line.trim();
        //第一行是文法的起始符号
        if (row == 0) {
            grammar = new Grammar();
            grammar.start = line;
            grammar.statements = new ArrayList<Statement>();
        }
        //第二行是非终结符，空格分隔
        else if (row == 1) {
            grammar.VNs = Arrays.asList(line.split(" "));
        }
        //第三行是终结符,空格分隔
        else if (row == 2) {
            String[] strs = line.split(" ");
            grammar.VTs = new ArrayList<Character>();
            for (String str : strs) {
                grammar.VTs.add(str.charAt(0));
            }
        } else {
            int index = line.indexOf("->");
            Statement st = new Statement();
            st.left = line.substring(0, index);
            index += 2;
            st.rule = Arrays.asList(line.substring(index).split("\\|"));
            grammar.statements.add(st);
        }
    }

    private void openFile(String fileName) {
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
                readGrammar(line, row);
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

    private void printGrammar() {
        if (grammar == null) return;
        System.out.println("文法起始符号:" + grammar.start);
        System.out.println("非终极符集合:" + grammar.VNs.toString());
        System.out.println("终结符集合:" + grammar.VTs.toString());
        System.out.println("规则:");
        for (int i = 0; i < grammar.statements.size(); i++) {
            Statement st = grammar.statements.get(i);
            System.out.print(st.left + "->");
            for (int j = 0; j < st.rule.size(); j++) {
                System.out.print(st.rule.get(j));
                if (j != st.rule.size() - 1) System.out.print("|");
            }
            System.out.println();
        }
    }


    private void initFirstFollow() {
        ffs = new ArrayList<FirstFollow>();
        for (Statement st : grammar.statements) {
            FirstFollow ff = new FirstFollow();
            ff.s = st.left;
            ff.first = new HashSet<Character>();
            ff.follow = new HashSet<Character>();
            ffs.add(ff);
        }
    }

    private void calcAllFirst() {
        boolean[] hasCalced = new boolean[ffs.size()];
        for (int i = 0; i < hasCalced.length; i++) {
            if (!hasCalced[i])
                calcFirst(ffs.get(i).s, hasCalced);
        }
    }

    //返回值表示这个非终结符的first是否含有空串也即$
    private boolean calcFirst(String s, boolean[] hasCalced) {
        int i;
        for (i = 0; i < grammar.statements.size(); i++) {
            if (grammar.statements.get(i).left.equals(s)) break;
        }
        hasCalced[i] = true;
        Statement st = grammar.statements.get(i);
        FirstFollow fs = ffs.get(i);
        boolean result = false;
        for (int j = 0; j < st.rule.size(); j++) {
            String rule = st.rule.get(j);
            //若这一条规则是以终结符开始的
            if (grammar.VTs.contains(rule.charAt(0))) {
                if (rule.charAt(0) == '$') result = true;
                fs.first.add(rule.charAt(0));
            } else {
                boolean hasEmpty = true;
                int index = 0;
                while (hasEmpty && index < rule.length()) {
                    hasEmpty = false;
                    String vn;
                    //字母后面紧跟一个数字，则取两个字符
                    if (index + 1 < rule.length() && rule.charAt(index + 1) >= '1' && rule.charAt(index + 1) <= '9') {
                        vn = rule.substring(index, index + 2);
                        index += 2;
                    }
                    //字母后面没有数字，则取一个字符
                    else {
                        vn = rule.substring(index, index + 1);
                        index += 1;
                    }
                    int k;
                    for (k = 0; k < grammar.statements.size(); k++) {
                        if (grammar.statements.get(k).left.equals(vn)) break;
                    }
                    if (hasCalced[k]) {
                        fs.first.addAll(ffs.get(k).first);
                        if (ffs.get(k).first.contains('$')) hasEmpty = true;
                    } else {
                        hasEmpty = calcFirst(vn, hasCalced);
                        fs.first.addAll(ffs.get(k).first);
                    }
                }
            }
        }
        return result;
    }

    private void calcAllFollow() {
        //文法起始符号放入结束符号'#'
        ffs.get(0).follow.add('#');
        for (int i = 0; i < grammar.statements.size(); i++) {
            calcFollow(ffs.get(i).s);
        }
    }

    private int calcFollow(String s) {
        int i;
        for (i = 0; i < grammar.statements.size(); i++) {
            if (grammar.statements.get(i).left.equals(s)) break;
        }
        Statement st = grammar.statements.get(i);
        FirstFollow fs = ffs.get(i);
        //已经求解过follow
        if (!(fs.follow.size() == 0 || (fs.follow.size() == 1 && fs.follow.contains('#')))) {
            return i;
        }
        for (int sn = 0; sn < grammar.statements.size(); sn++) {
            for (int j = 0; j < grammar.statements.get(sn).rule.size(); j++) {
                String rule = grammar.statements.get(sn).rule.get(j);
                int index = rule.indexOf(s);
                int len = s.length();
                while (index != -1) {
                    //要找E找到了E1
                    if (len == 1 && index < rule.length() - 1 && rule.charAt(index + 1) >= '0' && rule.charAt(index + 1) <= '9') {
                        index = rule.indexOf(s, index + 1);
                        continue;
                    }
                    //找到的非终结符后面还有字符
                    if (index + len < rule.length()) {
                        boolean containNull = false;
                        //后面这个字符是非终结符
                        if (rule.charAt(index + len) >= 'A' && rule.charAt(index + len) <= 'Z') {
                            int k = index + len;
                            while (k < rule.length() && rule.charAt(k) >= 'A' && rule.charAt(k) <= 'Z') {
                                String tmp;
                                //字母后面紧跟一个数字，则取两个字符
                                if (k + 1 < rule.length() && rule.charAt(k + 1) >= '1' && rule.charAt(k + 1) <= '9') {
                                    tmp = rule.substring(k, k + 2);
                                    k += 2;
                                }
                                //字母后面没有数字，则取一个字符
                                else {
                                    tmp = rule.substring(k, k + 1);
                                    k += 1;
                                }
                                int f;
                                for (f = 0; f < ffs.size(); f++) {
                                    if (ffs.get(f).s.equals(tmp)) break;
                                }
                                for (Character character : ffs.get(f).first) {
                                    if (character == '$') containNull = true;
                                    else fs.follow.add(character);
                                }
                                if (!containNull) break;
                            }
                            //说明前面的所有非终结符都是可以推导到空串的,且后面有一个终结符
                            if (containNull && k < rule.length()) {
                                fs.follow.add(rule.charAt(k));
                            }
                            //推到A->asb 且b=>$
                            else if (containNull && k == rule.length()) {
                                if (!grammar.statements.get(sn).left.equals(s)) {
                                    int nexti = calcFollow(grammar.statements.get(sn).left);
                                    fs.follow.addAll(ffs.get(nexti).follow);
                                }
                            }
                        }
                        //找到的非终结符后面是一个终结符
                        else {
                            fs.follow.add(rule.charAt(index + len));
                        }
                    }
                    //找到的非终结符后面没有字符了
                    else {
                        if (!grammar.statements.get(sn).left.equals(s)) {
                            int nexti = calcFollow(grammar.statements.get(sn).left);
                            fs.follow.addAll(ffs.get(nexti).follow);
                        }
                    }
                    index = rule.indexOf(s, index + 1);
                }
            }
        }
        return i;
    }

    private void showFirstFollow() {
        for (FirstFollow ff : ffs) {
            System.out.println("非终结符 " + ff.s + " First集为 " + ff.first + " Follow集为 " + ff.follow);
        }
    }

    private void initFAT() {
        fat = new FAT();
        fat.VNs = new ArrayList<String>(grammar.VNs);
        fat.VTs = new ArrayList<Character>(grammar.VTs);
        fat.VTs.add('#');
        fat.table = new String[fat.VNs.size()][fat.VTs.size()];
    }

    private void calcFAT() {
        for (int i = 0; i < fat.VNs.size(); i++) {
            Statement st = grammar.statements.get(i);
            String left = st.left;
            for (int j = 0; j < st.rule.size(); j++) {
                String rule = st.rule.get(j);
                int index = 0;
                Set<Character> ruleFirst = new HashSet<Character>();
                boolean containNull = false;
                while (index < rule.length()) {
                    //left->rule
                    //求rule的first
                    if (rule.charAt(index) >= 'A' && rule.charAt(index) <= 'Z') {
                        String tmp;
                        //字母后面紧跟一个数字，则取两个字符
                        if (index + 1 < rule.length() && rule.charAt(index + 1) >= '1' && rule.charAt(index + 1) <= '9') {
                            tmp = rule.substring(index, index + 2);
                            index += 2;
                        }
                        //字母后面没有数字，则取一个字符
                        else {
                            tmp = rule.substring(index, index + 1);
                            index += 1;
                        }
                        int t = 0;
                        for (t = 0; t < ffs.size(); t++) {
                            if (tmp.equals(ffs.get(t).s)) break;
                        }
                        ruleFirst.addAll(ffs.get(t).first);
                        if (ffs.get(t).first.contains('$')) containNull = true;
                    } else {
                        ruleFirst.add(rule.charAt(index));
                        index += 1;
                    }
                    if (!containNull) break;
                }
                //left->rule,若a in first(rule),令fat[left][a]=left->rule
                for (Character a : ruleFirst) {
                    if (a != '$') fat.table[i][fat.VTs.indexOf(a)] = left + "->" + rule;
                }
                //$ in first(rule),对all b in follow(left),令fat[left][b]=left->rule
                if (ruleFirst.contains('$')) {
                    Set<Character> follow = ffs.get(i).follow;
                    for (Character b : follow) {
                        fat.table[i][fat.VTs.indexOf(b)] = left + "->" + rule;
                    }
                }
            }
        }
    }

    private void printFAT() {
        System.out.printf("%12s"," ");
        for (Character vt : fat.VTs) {
            System.out.printf("%-12s", vt);
        }
        System.out.println();
        for (int i = 0; i < fat.VNs.size(); i++) {
            System.out.printf("%-12s", fat.VNs.get(i));
            for (String rule : fat.table[i]) {
                System.out.printf("%-12s",rule);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CalcFirstFollow calc = new CalcFirstFollow();
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("请选择文件");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int res = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == res) {
            path = fileChooser.getSelectedFile().getPath();
        }
        calc.openFile(path);
        calc.printGrammar();
        calc.initFirstFollow();
        //对每一条文法左侧非终结符求解first集
        calc.calcAllFirst();
        calc.calcAllFollow();
        calc.showFirstFollow();
        calc.initFAT();
        calc.calcFAT();
        calc.printFAT();
    }

}
