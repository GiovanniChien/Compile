package main;

import pojo.FirstLastVT;
import pojo.Grammar;
import pojo.PriorityTable;
import pojo.Statement;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {

    private Grammar grammar;//读取的文法集合
    private List<FirstLastVT> fls;//所有非终结符的firstVt和lastVt集合
    private PriorityTable pTable;//优先关系表

    private void readGrammar(String line, int row) {
        if (line == null) return;
        line = line.trim();
        //第一行是文法的起始符号
        if (row == 0) {
            grammar = new Grammar();
            grammar.start = line;
            grammar.statements = new ArrayList<>();
        }
        //第二行是非终结符，空格分隔
        else if (row == 1) {
            grammar.VNs = Arrays.asList(line.split(" "));
        }
        //第三行是终结符,空格分隔
        else if (row == 2) {
            String[] strs = line.split(" ");
            grammar.VTs = new ArrayList<>();
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

    private void initFirstLastVTList() {
        fls = new ArrayList<>();
        for (Statement st : grammar.statements) {
            FirstLastVT fl = new FirstLastVT();
            fl.P = st.left;
            fl.firstVT = new HashSet<>();
            fl.lastVT = new HashSet<>();
            fls.add(fl);
        }
    }

    private void calcFirstVT() {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < grammar.statements.size(); i++) {
                Statement st = grammar.statements.get(i);
                FirstLastVT fl = fls.get(i);
                for (int j = 0; j < st.rule.size(); j++) {
                    String rule = st.rule.get(j);
                    int preSize = fl.firstVT.size();
                    //规则为P->a...
                    if (grammar.VTs.contains(rule.charAt(0))) {
                        fl.firstVT.add(rule.charAt(0));
                    }
                    //规则为P->Q...
                    else {
                        //规则为P->Qa...
                        if (rule.length() > 1 && grammar.VTs.contains(rule.charAt(1))) {
                            fl.firstVT.add(rule.charAt(1));
                        }
                        //找到Q的firstVt
                        int k = 0;
                        for (; k < grammar.statements.size(); k++) {
                            if (grammar.statements.get(k).left.equals(String.valueOf(rule.charAt(0))))
                                break;
                        }
                        fl.firstVT.addAll(fls.get(k).firstVT);
                    }
                    if (fl.firstVT.size() > preSize) changed = true;
                }
            }
        } while (changed);
    }

    private void calcLastVT() {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < grammar.statements.size(); i++) {
                Statement st = grammar.statements.get(i);
                FirstLastVT fl = fls.get(i);
                for (int j = 0; j < st.rule.size(); j++) {
                    String rule = st.rule.get(j);
                    int preSize = fl.lastVT.size();
                    int len = rule.length();
                    //规则为P->...a
                    if (grammar.VTs.contains(rule.charAt(len - 1))) {
                        fl.lastVT.add(rule.charAt(len - 1));
                    }
                    //规则为P->...Q
                    else {
                        //规则为P->...aQ
                        if (len - 1 > 0 && grammar.VTs.contains(rule.charAt(len - 2))) {
                            fl.lastVT.add(rule.charAt(len - 2));
                        }
                        //找到Q的lastVt
                        int k = 0;
                        for (; k < grammar.statements.size(); k++) {
                            if (grammar.statements.get(k).left.equals(String.valueOf(rule.charAt(0))))
                                break;
                        }
                        fl.lastVT.addAll(fls.get(k).lastVT);
                    }
                    if (fl.lastVT.size() > preSize) changed = true;
                }
            }
        } while (changed);
    }

    private void showFirstLastVT() {
        for (FirstLastVT fl : fls) {
            System.out.print(fl.P);
            System.out.print(" FirstVt+" + fl.firstVT);
            System.out.println(" LastVt+" + fl.lastVT);
        }
    }

    private void initPriorityTable() {
        pTable = new PriorityTable();
        pTable.element = grammar.VTs;
        pTable.table = new int[pTable.element.size()][pTable.element.size()];
    }

    private void calcPriorityTable() {
        List<Character> VTs = pTable.element;
        for (int i = 0; i < grammar.statements.size(); i++) {
            Statement st = grammar.statements.get(i);
            for (int j = 0; j < st.rule.size(); j++) {
                String rule = st.rule.get(j);
                char[] chars = rule.toCharArray();
                int len = chars.length;
                for (int k = 0; k < chars.length; k++) {
                    if (!VTs.contains(chars[k])) {
                        continue;
                    }
                    if (k < len - 1) {
                        //A->...ab...
                        if (VTs.contains(chars[k + 1])) {
                            pTable.table[VTs.indexOf(chars[k])][VTs.indexOf(chars[k + 1])] = 0;
                        }
                        //A->...aR...
                        else {
                            String R = String.valueOf(chars[k + 1]);

                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("请选择文件");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int res = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == res) {
            path = fileChooser.getSelectedFile().getPath();
        }
        main.openFile(path);
        main.printGrammar();
        main.initFirstLastVTList();
        main.calcFirstVT();
        main.calcLastVT();
        main.showFirstLastVT();
    }

}
