import java.util.ArrayList;

/**
 * 
 * @author ֣��
 *�ʷ�������ݽṹ
 */
public class Struct {
    String [] reserveWord={"int","float","char","double","void","if","else","while","do","return","struct","for","static","long","switch","case","enum",
    		"continue","break","const","short","register","typedof","extern","union","unsigned","signed","default","goto","sizeof","auto","volatile"};      
	String [] varReserveWord={"double","int","long","char","float","short"};	
	String [] functionReserveWord={"void","double","int","char","float"};
	String [] noVarReserveWord={"void","if","else","while","do","return","struct","for","static","switch","case","enum",
    		"continue","break","const","register","typedof","extern","union","unsigned","signed","default","goto","sizeof","auto","volatile"};
    String [] operationalCharacter1=new String[]{"+","-","*","/","(",")","[","]",".","!","~","-","*","&","%",">","<","^","|","="};
    String [] operationalCharacter2=new String[]{"&&","||",">=","<=","++","--",">>","<<","==","!=","+=","-=","/=","%=","&=","^=","|=","->","?:"};
	String [] operationalCharacter3=new String[]{"<<=",">>="};
    String [] boundSymbol=new String[]{"(",")",",",";","=","{","}"};
    
}

class Content{
	String string;
	int line;
	int index;
	int flag;
}

/**
 * �﷨������ݽṹ
 * 
 */
 class Symbol
{
	String start;
	String replacedStr="";
	boolean replaced=false;
	String content=null;                                                                       //���������ķ�
	ArrayList<String> first=new ArrayList<String>();                                           //��ʵ����һ���Ϳ�����
	ArrayList<String> follow=new ArrayList<String>();
}

 /**
  * 
  * SlR��ݽṹ
  */
 
 class State
 {
	// int stateNum=0;
	 boolean isS=false;
	 boolean isR=false;
	 ArrayList<String> state=new ArrayList<String>();
	 ArrayList<Character> accChar=new ArrayList<Character>();    //可接收字符
	 ArrayList<Integer> arrNum=new ArrayList<Integer>();         //通过可接收字符能到达的下一个项目集序号
	 
	 
 }
