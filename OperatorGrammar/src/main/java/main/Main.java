package main;

import pojo.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * 对于文法的要求:只能用A-Z表示非终结符号
 */
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
        pTable.element.add('#');
        pTable.table = new Integer[pTable.element.size()][pTable.element.size()];
        for (int i = 0; i < pTable.element.size() - 1; i++) {
            pTable.table[pTable.element.size() - 1][i] = -1;
            pTable.table[i][pTable.element.size() - 1] = 1;
        }
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
                    int tmpIndex = VTs.indexOf(chars[k]);
                    if (k < len - 1) {
                        //A->...ab...
                        if (VTs.contains(chars[k + 1])) {
                            pTable.table[tmpIndex][VTs.indexOf(chars[k + 1])] = 0;
                        }
                        //A->...aR...
                        else {
                            String R = String.valueOf(chars[k + 1]);
                            for (FirstLastVT fl : fls) {
                                if (fl.P.equals(R)) {
                                    Set<Character> firstVT = fl.firstVT;
                                    for (Character c : firstVT) {
                                        pTable.table[tmpIndex][VTs.indexOf(c)] = -1;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    if (k < len - 2) {
                        //A->...aRb...
                        if (!VTs.contains(chars[k + 1]) && VTs.contains(chars[k + 2])) {
                            pTable.table[tmpIndex][VTs.indexOf(chars[k + 2])] = 0;
                        }
                    }
                    if (k > 0) {
                        //A->...Rb...
                        if (!VTs.contains(chars[k - 1])) {
                            String R = String.valueOf(chars[k - 1]);
                            for (FirstLastVT fl : fls) {
                                if (fl.P.equals(R)) {
                                    Set<Character> lastVT = fl.lastVT;
                                    for (Character c : lastVT) {
                                        pTable.table[VTs.indexOf(c)][tmpIndex] = 1;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void showPriorityTable() {
        System.out.printf("%10s", " ");
        for (Character c : pTable.element) {
            System.out.printf("%-10s", c);
        }
        System.out.println();
        for (int i = 0; i < pTable.table.length; i++) {
            System.out.printf("%-10s", pTable.element.get(i));
            for (int j = 0; j < pTable.table.length; j++) {
                System.out.printf("%-10s", pTable.table[i][j]);
            }
            System.out.println();
        }
    }

    private void identifyStatement() {
        System.out.println("请输入一个算符优先表达式：");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        s = s + "#";
        Stack<Character> stack = new Stack<>();
        stack.push('#');
        int top = 0;
        int pos;
        char[] chars = s.toCharArray();
        int index = 0;
        while (true) {
            Character a = chars[index];
            if (a == '#' && top == 1) break;
            //先找到当前栈顶的终结符
            if (grammar.VTs.contains(stack.get(top)) || stack.get(top) == '#') {
                pos = top;
            } else {
                pos = top - 1;
            }
            int indexA = pTable.element.indexOf(stack.get(pos));
            int indexB = pTable.element.indexOf(a);
            if (pTable.table[indexA][indexB] == null) {
                System.out.println("此表达式有错");
                return;
            }
            if (pTable.table[indexA][indexB] == 0 || pTable.table[indexA][indexB] == -1) {
                //当前读入的终结符的优先级比栈顶的终结符的优先级高或相等，则将当前读入的终结符压栈
                stack.push(a);
                top++;
                index++;
            } else {
                //当前读入的终结符的优先级比栈顶的终结符的优先级低，则需要规约
                // 需要先找到待规约子串即素短语
                // 寻找素短语的方法：
                // 1.已知当前栈顶的终结符优先级大于当前读入的终结符
                // 2.根据素短语的定义也就是要一直向下搜索找到一个终结符其优先级比其上方相邻的终结符优先级小
                // 3.tmpPos表示当前搜索到的最上面的一个终结符
                // 4.pre表示当前搜索的终结符的下面一个终结符
                // 5.如果找到pre所对应的终结符优先级小于tmpPos,则结束搜索,从当前栈顶到pre的所有符号组成当前的素短语
                // 6.如果pre的优先级大于或等于tmpPos,那么更新pre和tmpPos的值,使得tmpPos=pre,pre--;继续向下搜索
                int tmpPos = pos;
                int pre;
                for (pre = pos - 1; pre >= 0; pre--) {
                    if (!(grammar.VTs.contains(stack.get(pre)) || stack.get(pre) == '#')) {
                        continue;
                    }
                    int tmpIndexB = pTable.element.indexOf(stack.get(tmpPos));
                    int tmpIndexA = pTable.element.indexOf(stack.get(pre));
                    if (pTable.table[tmpIndexA][tmpIndexB] == -1)
                        break;
                    else {
                        tmpPos = pre;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < top - pre; k++) {
                    sb.append(stack.pop());
                }
                String right = sb.reverse().toString();
                String left = null;
                boolean isMatched = false;
                for (Statement st : grammar.statements) {
                    left = st.left;
                    for (String s1 : st.rule) {
                        if (compareTwoRule(s1, right)) {
                            isMatched=true;
                            break;
                        }
                    }
                    if(isMatched) break;
                }
                if (!isMatched) {
                    System.out.println("此表达式有错");
                    return;
                } else {
                    stack.push(left.charAt(0));
                    top = stack.size() - 1;
                }
            }
        }
        System.out.println("此表达式是一个算符优先文法表达式");
    }

    private boolean compareTwoRule(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        for (int i = 0; i < chars1.length; i++) {
            if ((chars1[i] >= 'A' && chars1[i] <= 'Z') && (chars2[i] >= 'A' && chars2[i] <= 'Z')) {
            } else if ((chars1[i] >= 'A' && chars1[i] <= 'Z') || (chars2[i] >= 'A' && chars2[i] <= 'Z'))
                return false;
            else if (chars1[i] == chars2[i]) {
            } else
                return false;
        }
        return true;
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
        main.initPriorityTable();
        main.calcPriorityTable();
        main.showPriorityTable();
        main.identifyStatement();
    }

}
