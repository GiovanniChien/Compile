package main;

import pojo.AnalysisTable;
import pojo.Grammar;
import pojo.Project;
import pojo.Rule;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Main {

    private Grammar grammar;
    private List<Project> stateSet;
    private AnalysisTable table;

    private void readGrammar(String line, int row) {
        if (line == null) return;
        line = line.trim();
        //第一行是文法的起始符号
        if (row == 0) {
            grammar = new Grammar();
            grammar.start = line;
            grammar.rules = new ArrayList<Rule>();
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
            String left = line.substring(0, index);
            index += 2;
            String[] rights = line.substring(index).split("\\|");
            if (left.equals(grammar.start) && rights.length > 1) {
                //扩展文法
                grammar.start = left + "1";
                Rule rule = new Rule();
                rule.left = grammar.start;
                rule.right = left;
                grammar.rules.add(rule);
            }
            for (String right : rights) {
                Rule rule = new Rule();
                rule.left = left;
                if (right.equals("$"))
                    rule.right = "";
                else
                    rule.right = right;
                grammar.rules.add(rule);
            }
        }
    }

    private void printGrammar() {
        if (grammar == null) return;
        System.out.println("文法起始符号:" + grammar.start);
        System.out.println("非终极符集合:" + grammar.VNs.toString());
        System.out.println("终结符集合:" + grammar.VTs.toString());
        System.out.println("规则:");
        for (Rule rule : grammar.rules) {
            System.out.print(rule.left + "->");
            System.out.print(rule.right);
            System.out.println();
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

    //计算一个项目的闭包
    private Project calcProject(Rule rule) {
        Project project = new Project();
        project.rules = new ArrayList<Rule>();
        Queue<Rule> queue = new LinkedList<Rule>();
        queue.offer(rule);
        while (!queue.isEmpty()) {
            Rule pollRule = queue.poll();
            if (!project.containsRule(pollRule)) {
                project.rules.add(pollRule);
                if (pollRule.index != pollRule.right.length()) {
                    Character tmp = pollRule.right.charAt(pollRule.index);
                    if (!grammar.VTs.contains(tmp)) {
                        //将有相同活前缀的项目放到队列里面
                        List<Rule> ruleByLeft = grammar.findRuleByLeft(tmp.toString());
                        for (Rule tmpRule : ruleByLeft) {
                            tmpRule.index = 0;
                            queue.offer(tmpRule);
                        }
                    }
                }
            }
        }
        return project;
    }

    //计算状态集规范族
    private void calcStateSet() {
        stateSet = new ArrayList<Project>();
        //初始项目
        Rule initRule = grammar.rules.get(0);
        Project initProject = calcProject(initRule);
        Queue<Project> queue = new LinkedList<Project>();
        queue.offer(initProject);
        while (!queue.isEmpty()) {
            //取出一个项目集
            Project pollProject = queue.poll();
            if (!isStateSetContainsProject(pollProject)) {
                stateSet.add(pollProject);
                //遍历文法的每一个符号，看是否会产生新的状态
                for (Character vt : grammar.VTs) {
                    Project newProject = new Project();
                    newProject.rules = new ArrayList<Rule>();
                    for (Rule rule : pollProject.rules) {
                        if (rule.index != rule.right.length()) {
                            if (rule.right.charAt(rule.index) == vt) {
                                Rule tmpRule = new Rule();
                                tmpRule.left = rule.left;
                                tmpRule.right = rule.right;
                                tmpRule.index = rule.index + 1;
                                newProject.rules.addAll(calcProject(tmpRule).rules);
                            }
                        }
                    }
                    if (newProject.rules.size() > 0)
                        queue.offer(newProject);
                }
                for (String vn : grammar.VNs) {
                    Project newProject = new Project();
                    newProject.rules = new ArrayList<Rule>();
                    for (Rule rule : pollProject.rules) {
                        if (rule.index != rule.right.length()) {
                            if (rule.right.charAt(rule.index) == vn.charAt(0)) {
                                Rule tmpRule = new Rule();
                                tmpRule.left = rule.left;
                                tmpRule.right = rule.right;
                                tmpRule.index = rule.index + 1;
                                newProject.rules.addAll(calcProject(tmpRule).rules);
                            }
                        }
                    }
                    if (newProject.rules.size() > 0)
                        queue.offer(newProject);
                }
            }
        }
    }

    private void printStateSet() {
        System.out.println("共有 " + stateSet.size() + " 个状态");
        int cnt = 0;
        for (Project project : stateSet) {
            System.out.print(cnt + " : ");
            for (Rule rule : project.rules) {
                System.out.print(rule.toString() + " ");
            }
            System.out.println();
            cnt++;
        }
    }

    private boolean isStateSetContainsProject(Project pollProject) {
        Rule rule = pollProject.rules.get(0);
        for (Project project : stateSet) {
            if (rule.equals(project.rules.get(0)))
                return true;
        }
        return false;
    }

    private void initAnalysisTable() {
        int rowNum = stateSet.size();
        int colNum = grammar.VTs.size() + grammar.VNs.size() + 1;
        table = new AnalysisTable();
        table.states = new int[rowNum];
        for (int i = 0; i < rowNum; i++) {
            table.states[i] = i;
        }
        table.sign = new char[colNum];
        int i = 0;
        for (Character vt : grammar.VTs) {
            table.sign[i] = vt;
            i++;
        }
        table.sign[i] = '#';
        i++;
        for (String vn : grammar.VNs) {
            table.sign[i] = vn.charAt(0);
            i++;
        }
        table.table = new String[rowNum][colNum];
    }

    private void calcAnalysisTable() {
        for (int i = 0; i < table.states.length; i++) {
            Project project = stateSet.get(i);
            int j = 0;
            boolean[] isUsed = new boolean[project.rules.size()];
            for (int k = 0; k < project.rules.size(); k++) {
                isUsed[k] = false;
                Rule rule = project.rules.get(k);
                if (rule.index == rule.right.length()) {
                    isUsed[k] = true;
                    //如果是接收项目，则让table[i][#]=accept
                    if (project.rules.size() == 1 && rule.left.equals(grammar.start)) {
                        table.table[i][grammar.VTs.size()] = "accept";
                        break;
                    }
                    //如果已经是一个待约项目，则对所有的非终结符和结束符号#做规约操作
                    int reduceIndex = grammar.findRule(rule);
                    for (int d = 0; d <= grammar.VTs.size(); d++) {
                        table.table[i][d] = "r" + reduceIndex;
                    }
                }
            }
            for (Character vt : grammar.VTs) {
                Project newProject = new Project();
                newProject.rules = new ArrayList<Rule>();
                for (int k = 0; k < project.rules.size(); k++) {
                    if (isUsed[k]) continue;
                    Rule rule = project.rules.get(k);
                    if (rule.right.charAt(rule.index) == vt) {
                        isUsed[k] = true;
                        Rule tmpRule = new Rule();
                        tmpRule.left = rule.left;
                        tmpRule.right = rule.right;
                        tmpRule.index = rule.index + 1;
                        newProject.rules.addAll(calcProject(tmpRule).rules);
                    }
                }
                if (newProject.rules.size() > 0) {
                    int stateIndex = findProject(newProject);
                    if (stateIndex != -1) {
                        if (table.table[i][j] != null && !table.table[i][j].equals(""))
                            table.table[i][j] += ",s" + stateIndex;
                        else
                            table.table[i][j] = "s" + stateIndex;
                    }
                }
                j++;
            }
            j++;
            for (String vn : grammar.VNs) {
                Project newProject = new Project();
                newProject.rules = new ArrayList<Rule>();
                for (int k = 0; k < project.rules.size(); k++) {
                    if (isUsed[k]) continue;
                    Rule rule = project.rules.get(k);
                    if (rule.right.charAt(rule.index) == vn.charAt(0)) {
                        isUsed[k] = true;
                        Rule tmpRule = new Rule();
                        tmpRule.left = rule.left;
                        tmpRule.right = rule.right;
                        tmpRule.index = rule.index + 1;
                        newProject.rules.addAll(calcProject(tmpRule).rules);
                    }
                }
                if (newProject.rules.size() > 0) {
                    int stateIndex = findProject(newProject);
                    if (stateIndex != -1) {
                        if (table.table[i][j] != null && !table.table[i][j].equals(""))
                            table.table[i][j] += ",s" + stateIndex;
                        else
                            table.table[i][j] = "s" + stateIndex;
                    }
                }
                j++;
            }
        }
    }

    private int findProject(Project newProject) {
        for (int i = 0; i < stateSet.size(); i++) {
            Project project = stateSet.get(i);
            if (project.equals(newProject))
                return i;
        }
        return -1;
    }

    private void printAnalysisTable() {
        System.out.printf("%10s", " ");
        for (char c : table.sign) {
            System.out.printf("%-10s", c);
        }
        System.out.println();
        for (int i = 0; i < table.table.length; i++) {
            System.out.printf("%-10s", table.states[i]);
            for (int j = 0; j < table.table[0].length; j++) {
                System.out.printf("%-10s", table.table[i][j]);
            }
            System.out.println();
        }
    }

    private void identifyStatement() {
        System.out.println("请输入一个句子：");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        s = s + "#";
        Stack<Integer> stateStack = new Stack<Integer>();
        Stack<Character> signStack = new Stack<Character>();
        stateStack.push(0);
        signStack.push('#');
        char[] chars = s.toCharArray();
        int index = 0;
        boolean success = false;
        while (index < chars.length) {
            int stateTop = stateStack.peek();
            char curChar = chars[index];
            int signIndex = indexOfSigns(curChar);
            if (signIndex == -1) {
                System.out.println("出错");
                return;
            }
            String op = table.table[stateTop][signIndex];
            if(op==null){
                System.out.println("出错");
                return;
            }
            if (op.contains(",")) {
                System.out.println("冲突，无法使用LR0识别");
                return;
            }
            //移进
            if (op.charAt(0) == 's') {
                int newStateIndex = Integer.valueOf(op.substring(1));
                stateStack.push(newStateIndex);
                signStack.push(curChar);
                index++;
            }
            //规约
            else if (op.charAt(0) == 'r') {
                int ruleIndex = Integer.valueOf(op.substring(1));
                Rule rule = grammar.rules.get(ruleIndex);
                for (int i = 0; i < rule.right.length(); i++) {
                    stateStack.pop();
                    signStack.pop();
                }
                signIndex = indexOfSigns(rule.left.charAt(0));
                stateTop = stateStack.peek();
                op = table.table[stateTop][signIndex];
                int newStateIndex = Integer.valueOf(op.substring(1));
                stateStack.push(newStateIndex);
                signStack.push(rule.left.charAt(0));
            } else{
                success = true;
                index++;
            }
        }
        if (success)
            System.out.println("句子识别成功！");
        else
            System.out.println("句子识别失败");
    }

    private int indexOfSigns(char sign) {
        for (int i = 0; i < table.sign.length; i++) {
            if (table.sign[i] == sign) return i;
        }
        return -1;
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
        main.calcStateSet();
        main.printStateSet();
        main.initAnalysisTable();
        main.calcAnalysisTable();
        main.printAnalysisTable();
        main.identifyStatement();
    }

}
