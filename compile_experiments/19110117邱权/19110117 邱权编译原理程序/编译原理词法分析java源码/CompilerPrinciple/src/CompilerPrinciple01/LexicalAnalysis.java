package CompilerPrinciple01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;




public class LexicalAnalysis {

	/**
	 * @param args
	 */
//	public String stringTest = new String("int main(){int a,b;a=1;b=2;}");
	public String[] keyWords = {"auto","short","int","float","long","double","char","struct","union","enum","typedef","const",
				"unsigned","signed","extern","register","static","volatile","void","if","else","switch","case",
				"for","do","while","goto","continue","break","default","sizeof","return"};//关键字数组
	public String[] operator = {"(",")","[","]","->",".","!","~","++","--","+","-","*","&","/","%"
				,"<<",">>","<",">",">=","<=","==","!=","^","|","&&","||","?",":","+=","-="
				,"*=","/=","%=","&=","|=","^=","<<=",">=","="};//操作符数组
	public String[] boundary = {",",";","\"\"","\'\'"};//界符数组
	public static String tempString = "";//暂时存放为判断的字符(串)
	public static String otherString = "";//暂时存放一定不是关键字的不能判断的其他字符(串)
	public char tempChar;//暂存从一行中取出的一个字符
	public static String result="";//最终的分析结果
	
	public LexicalAnalysis(){}

	public void IsDigit(int count){//数值类型判断函数
		if (tempString!=""){
			Object tempDouble = null;
			if (tempString.charAt(0)>='0'&&tempString.charAt(0)<='9'){
				tempDouble = Double.parseDouble(tempString);
				if (tempDouble!=null&&(tempDouble+"").length()==tempString.length()){
					result+=tempString+"\t"+"78"+"\t"+"\n";
				}
				else{
					if (IsKeyWord(tempString)){
						result+="\n";
					}
					else{
						result+=tempString+"\t\t"+"\n";
					}
				}
			}
			else{
				if (IsKeyWord(tempString)){
					result+="\n";
				}
				else{
					result+=tempString+"\t\t"+"\n";
				}
			}
		}
	}
	
	public boolean IsKeyWord(String string){//关键字判断函数
		if (string!=""){
			for (int i = 0;i < keyWords.length;i++){
				if (string.equals(keyWords[i])){
					result+=string+"\t"+(i+1)+"\t"+"关键字";
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean IsOperator(String string){//操作符判断函数
		if (string!=""){
			for (int i = 0;i < operator.length;i++){
				if (string.equals(operator[i])){
					result+=string+"\t"+(i+33)+"\t"+"操作符"+"\t";
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean IsBoundary(String string){//界符判断函数
		if (string!=""){
			for (int i = 0;i < boundary.length;i++){
				if (string.equals(boundary[i])){
					result+=string+"\t"+(i+74)+"\t"+"界符"+"\t";
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean Iscontained(String string){//判断字符串是不是包含在操作符中的函数
		if (string != "") {
			for (int i = 0; i < operator.length; i++) {
				if (operator[i].equals(string))
					return true;
			}
		}
		return false;
	}
	
	public void lexicalAnalysis(String targetString,int count){//分析函数
		targetString = targetString.trim();
		for (int i = 0;i < targetString.length();i++){
			tempChar = targetString.charAt(i);
			if ((tempChar >= 'a' && tempChar <= 'z')
					|| (tempChar >= 'A' && tempChar <= 'Z')) {
				if (otherString!=""){//字母后面紧跟界限符或者操作符的情况
					if (IsBoundary(otherString)||IsOperator(otherString)){
						result+="\n";
					}
					else{
						result+=otherString+"\t\t"+"\n";
					}
					otherString="";
				}
				tempString += tempChar;
			} 
			else if ((tempChar >= '0' && tempChar <= '9') || tempChar == '.') {
				if (otherString!=""){//数字后面紧跟界限符或者操作符的情况
					if (IsBoundary(otherString)||IsOperator(otherString)){
						result+="\t"+"\n";
					}
					else{
						result+=otherString+"\t\t"+"\n";
					}
					otherString="";
				}
				tempString += tempChar;
			}
			else if (tempChar==' '){
				IsDigit(count);
				if (otherString!=""){
					if (IsBoundary(otherString)||IsOperator(otherString)){
						result+="\n";
					}
					else{
						result+=otherString+"\t\t"+"\n";
					}
					otherString="";
				}
				tempString = "";
			}
			else{
				IsDigit(count);
				String otherTmp = otherString;
				otherString += tempChar;
				if (Iscontained(otherString)&&otherString.length()>1){
					if (IsOperator(otherString)){
						result+="\n";
					}
					otherString = "";
				}
				else if (!Iscontained(otherString)&&otherString.length()>1){
					if (otherTmp!=""){
						if (IsBoundary(otherTmp)||IsOperator(otherTmp)){
							result+="\n";
						}
						else{
							result+=otherTmp+"\t\t"+"\n";
						}
					}
					if (IsBoundary(tempChar+"")||IsOperator(tempChar+"")){
						result+="\n";
					}
					else{
						result+=tempChar+""+"\t\t"+"\n";
					}
					otherString = "";
				}
				else if (!Iscontained(tempChar+"")){
					if (IsBoundary(tempChar+"")){
						result+="\n";
					}
					else{
						result+=tempChar+"\t\t"+"\n";
					}
					otherString = "";
				}
				tempString = "";
			}
		}
	}
	
	public void openCFile(String fileName){//获取文件内容函数
		File file = new File(fileName);
		FileReader fr = null;
		BufferedReader br = null;
		int count = 1;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String targetString = br.readLine();
			while (targetString != null){
				lexicalAnalysis(targetString,count);
				count++;
				targetString = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fr.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LexicalAnalysis lA = new LexicalAnalysis();
		String fileName = "E:\\test.c";
		lA.openCFile(fileName);
		System.out.println(result);
	}

}
