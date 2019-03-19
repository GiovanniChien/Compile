package com.java.edu.morphology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class morphology {

	public static int getType(char c) {
		Character ch = Character.valueOf(c);
		if (Character.isDigit(c))
			return 0;	// ����(.98 ������ 0.98������c  1.�൱��1.0 ����c)
		if (Character.isLetter(c) || ch.equals('_'))
			return 1;	// ��ĸ���»���
		if (Character.isWhitespace(c))
			return 2;	// �ո�
		if (ch.equals('+') || ch.equals('-') || ch.equals('*')
			|| ch.equals('/') || ch.equals('%') || ch.equals('<')
			|| ch.equals('>') || ch.equals('=') || ch.equals('!')
			|| ch.equals('&') || ch.equals('|') || ch.equals('^')
			|| ch.equals('~'))
			return 3;	// ������
		if (ch.equals('(') || ch.equals(')') || ch.equals('[')
			|| ch.equals(']') || ch.equals('{') || ch.equals('}')
			|| ch.equals('?') || ch.equals(':') || ch.equals('#') 
			|| ch.equals(',')|| ch.equals(';') || ch.equals('"') 
			|| ch.equals('\'')|| ch.equals('.'))
			return 4;	// �ָ������أ�
		return 500;		// ��������ʶ���ַ�	
	}
	
	private static ArrayList<Data> datas = new ArrayList<Data>();	//��ŷ�������Ķ�ά������
	
	public static void main(String[] args) {
		
		int count_row = 1;	// ��ʼΪ��һ��
		
		final int   NUMBER = 0;	// ���ּ�С����
		final int    ALPHA = 1;	// ��ĸ
		final int    SPACE = 2;	// �ո�
		final int OPERATOR = 3;	// ������
		final int    APART = 4;	// �ָ���
		
		//����ʶ���ַ�
		final int  UNKNOWN = 500;	// ����δʶ���ַ�
		
		//�����ͱ���ֵ
		final int INTCONSTANCE = 100;// ��������
		final int DOUBLECONSTANCE = 101;	// ʵ������
		final int IDENT = 102;	// ��ʶ��
		
		//������
		final int BREAK = 103;
		final int CASE = 104;
		final int CHAR = 105;
		final int CONTINUE = 106;
		final int DEFAULT = 107;
		final int DO = 108;
		final int DOUBLE = 109;
		final int ELSE = 110;
		final int FLOAT = 111;
		final int FOR = 112;
		final int IF = 113;
		final int INCLUDE = 114;
		final int INT = 115;
		final int LONG = 116;
		final int MAIN = 117;
		final int NAMESPACE = 118;	//�����ռ�
		final int RETURN = 119;
		final int SHORT = 120;
		final int SIGNED = 121;
		final int SIZEOF = 122;
		final int STATIC = 123;
		final int SWITCH = 124;
		final int UNSIGNED = 125;
		final int USING = 126;
		final int VOID = 127;
		final int WHILE = 128;
		
		//�������������
		final int JIA = 200;
		final int JIAN = 201;
		final int CHENG = 202;
		final int CHU = 203;
		final int JIADENG = 204;
		final int JIANDENG = 205;
		final int CHENGDENG = 206;
		final int CHUDENG = 207;
		final int JIAJIA = 208;
		final int JIANJIAN = 209;
		final int MO = 210;
		final int MODENG = 211;
		final int LEFTYIDENG = 212; // <<=
		final int RIGHTYIDENG = 213; // >>=
		final int XIAOXIAO = 214; // <<
		final int DADA = 215;  // >>
		final int XIAO = 216;  // <
		final int DA = 217; // >
		final int DENG = 218;  // = 
		final int DENGDENG = 219;  // ==
		final int BUDENG = 220;  // !=
		final int BU = 221;  // !
		final int DADENG = 222;  // >=
		final int XIAODENG = 223; // <=
		final int YU = 224;  // &&
		final int HUO = 225; // ||
		final int WEIYU = 226; // &
		final int WEIHUO = 227;  // |
		final int WEIYUDENG = 228; // &=
		final int WEIHUODENG = 229; // |=
		final int WEIFAN = 230;  // ~
		final int WEIFANDENG = 231; // ~=
		final int WEIYIHUO = 232; // ^
		final int WEIYIHUODENG = 233; // ^=
		
		//�ָ���
		final int LEFTXIAOKUOHAO = 300;
		final int RIGHTXIAOKUOHAO = 301;
		final int LEFTZHONGKUOHAO = 302;
		final int RIGHTZHONGKUOHAO = 303;
		final int LEFTDAKUOHAO = 304;
		final int RIGHTDAKUOHAO = 305;
		final int WENHAO = 306;
		final int MAOHAO = 307;
		final int JINHAO = 308;
		final int DOUHAO = 309;
		final int FENHAO = 310;
		final int DANYINHAO = 311;
		final int DANYINHAOZY = 312;
		final int SHUANGYINHAO = 313;
		final int JUHAO = 314;
		
		final int ERROR = 400;
		final int ANNOTATATION = 401;
		
		try{
			String dir;
			String filename = JOptionPane.showInputDialog("������Ҫ���дʷ��������ļ���");
			dir = System.getProperty("user.dir");	// �õ��û���ǰĿ¼
			PushbackReader file;
			file = new PushbackReader(new FileReader(dir	+ "\\"+filename), 3);	// ���ӵ�ǰĿ¼
			
			System.out.println("��" + count_row + "��");
			char ch;
			int n, m;
			while ((n = file.read()) != -1)	// ÿ�ζ�һ���ַ�
			{
				ch = (char) n;	// ��ǰ������ַ�����ch��
				String token = null;	// ��ǰȡ���ĵ��ʷ���token��
				m = morphology.getType(ch);
				switch (m) {
//NUMBER				
				case NUMBER:	// ���� Ū�����ͳ����򸡵��ͳ���
					int point=0;
					token = "" + ch;
					while ((n = file.read()) != -1 &&
							((char) n == '.' || Character.isDigit((char) n)))	// �����ֻ�С����
					{
						if ((char) n == '.' && point == 0)
						   point++;
						else
							if ((char) n == '.' && point > 0)
								break;	// ���ǵ�һ��С�����ˣ�����
						token += (char) n;
					}
					if ((char) n == 'e')	// ��1.2e����
					{
						if ((n = file.read()) != -1	&&
							Character.isDigit((char) n))	// ��1.2e3dea
						{
							token = token + 'e' + (char) n;
							while ((n = file.read()) != -1 && Character.isDigit((char) n))
								token += (char) n;
							if (n != -1)
								file.unread(n);	 // �˻�d
						}
						else
							if ((char) n == '-') // ��1.2e-����
							{
								if ((n = file.read()) != -1 &&
									Character.isDigit((char) n))	// ��1.2e-3daq
								{
									token = token + 'e' + '-' + (char) n;
									while ((n = file.read()) != -1 &&
											Character.isDigit((char) n))
										token += (char) n;
									if (n != -1)
										file.unread(n);	 // �˻�d
								}
								else
									if (n != -1)	// ��1.2e-adsf
									{
										file.unread(n);	// �˻�a
										file.unread('-');	// �˻�-
										file.unread('e');	// �˻�e
									}
							}
							else	// ��1.2ea
							{
								if(n != -1)
									file.unread(n);	// �˻�a
								file.unread('e');	// �˻�e
							}
					}
					else	// ��1.2dfk �˻�d
					{
						if (n != -1)
							file.unread(n);
					}
					System.out.println("" + count_row + "  NUMBER:  " + token);
					if(token.contains(".") || token.contains("e"))	// ������С�����e����double����
						datas.add(new Data(DOUBLECONSTANCE,token));
					else
						datas.add(new Data(INTCONSTANCE,token));
					break;
//ALPHA					
				case ALPHA:// ��ĸ Ū����ʶ����ؼ���
					token=""+ch;
					while ((n = file.read()) != -1 &&
							(Character.isLetter((char) n) || (char) n=='_' || Character.isDigit((char) n)) )
						token += (char) n;	
					if(n!=-1)
					   file.unread(n);				
					// �ؼ���
					if(token.equals("break"))
						datas.add(new Data(BREAK,token));			
					else if(token.equals("case"))
						datas.add(new Data(CASE,token));
					else if(token.equals("char"))
						datas.add(new Data(CHAR,token));
					else if(token.equals("continue"))
						datas.add(new Data(CONTINUE,token));
					else if(token.equals("default"))
						datas.add(new Data(DEFAULT,token));
					else if(token.equals("do"))
						datas.add(new Data(DO,token));
					else if(token.equals("double"))
						datas.add(new Data(DOUBLE,token));
					else if(token.equals("else"))
						datas.add(new Data(ELSE,token));
					else if(token.equals("float"))
						datas.add(new Data(FLOAT,token));
					else if(token.equals("for"))
						datas.add(new Data(FOR,token));
					else if(token.equals("if"))
						datas.add(new Data(IF,token));
					else if(token.equals("include"))
						datas.add(new Data(INCLUDE,token));
					else if(token.equals("int"))
						datas.add(new Data(INT,token));
					else if(token.equals("long"))
						datas.add(new Data(LONG,token));
					else if(token.equals("main"))
						datas.add(new Data(MAIN,token));
					else if(token.equals("namespace"))
						datas.add(new Data(NAMESPACE,token));
					else if(token.equals("return"))
						datas.add(new Data(RETURN,token));
					else if(token.equals("short"))
						datas.add(new Data(SHORT,token));
					else if(token.equals("signed"))
						datas.add(new Data(SIGNED,token));
					else if(token.equals("sizeof"))
						datas.add(new Data(SIZEOF,token));
					else if(token.equals("static"))
						datas.add(new Data(STATIC,token));
					else if(token.equals("switch"))
						datas.add(new Data(SWITCH,token));
					else if(token.equals("unsigned"))
						datas.add(new Data(UNSIGNED,token));
					else if(token.equals("using"))
						datas.add(new Data(USING,token));
					else if(token.equals("void"))
						datas.add(new Data(VOID,token));
					else if(token.equals("while"))
							datas.add(new Data(WHILE,token));
					else	// ��ʶ��
						datas.add(new Data(IDENT,token));
						
					System.out.println(""+count_row+"  IDENT:  "+token);
					break;
//SPACE
				case SPACE:	// �ո� ��������
					if (n == 13)
					{
						count_row++;
						System.out.println("��" + count_row + "��");
					}			
					break;
//OPERATOR
				case OPERATOR:	// ������	
					if (ch == '+' || ch == '-' || ch == '=' || ch == '&' || ch == '|')
					{
						if( (n = file.read()) != -1)
						{
						   char temp = (char) n;
						   if (temp == '=')	// ��+= == &=
						   {
							   token = "" + ch + temp;
						       if(ch == '+')
						    	   datas.add(new Data(JIADENG,token));
						       if(ch == '-')
						    	   datas.add(new Data(JIANDENG,token));
						       if(ch == '=')
						    	   datas.add(new Data(DENGDENG,token));
						       if(ch == '&')
						    	   datas.add(new Data(WEIYUDENG,token));
						       if(ch == '|')
						    	   datas.add(new Data(WEIHUODENG,token));						 						       
						   }
						   else
							   if(temp == ch)	// ��++ --  ��==��������
							   {
								   token = ""+ ch + temp; 
								   if(ch == '+')
									   datas.add(new Data(JIAJIA,token));
								   if(ch == '-')
									   datas.add(new Data(JIANJIAN,token));
								   if(ch == '&')
									   datas.add(new Data(YU,token));
								   if(ch == '|')
									   datas.add(new Data(HUO,token));   
							   }
							   else
							   {
								   token = "" + ch;	//�� +  - = 
								   if(ch == '+')
									   datas.add(new Data(JIA,token));
								   if(ch == '-')
									   datas.add(new Data(JIAN,token));
								   if(ch == '=')
									   datas.add(new Data(DENG,token));
								   if(ch == '&')
									   datas.add(new Data(WEIYU,token));
								   if(ch == '|')
									   datas.add(new Data(WEIHUO,token));
								   file.unread(temp);
							   }
						}
						else
						{
							token = "" + ch;
							if(ch == '+')
								datas.add(new Data(JIA,token));
						    if(ch == '-')
						    	datas.add(new Data(JIAN,token));
						    if(ch == '=')
						    	datas.add(new Data(DENG,token));
						    if(ch == '&')
						    	datas.add(new Data(WEIYU,token));
						    if(ch == '|')
						    	datas.add(new Data(WEIHUO,token));
						}
					}

					if (ch == '>' || ch == '<')
					{
						if ((n = file.read()) != -1)
						{
						    char temp1 = (char) n;
						    if ((n = file.read()) != -1)
						    {
						        char temp2 = (char) n;
						        if (temp1 == ch && temp2 == '=')	// �� <<=3
						        {
						        	token = "" + ch + temp1 + temp2;
						            if (ch == '<')	// <<=
						            	datas.add(new Data(LEFTYIDENG,token));
						            if (ch == '>')	// >>=
						            	datas.add(new Data(RIGHTYIDENG,token));
						        }
						        else
						        	if (temp1 == ch)	// �� <<dsf
						        	{
						        		token = "" + ch + temp1;
						        		if (ch == '<')	// <<
						        			datas.add(new Data(XIAOXIAO,token));
						        		if (ch == '>')	// >>
						        			datas.add(new Data(DADA,token));
						        		file.unread(temp2);
						        	}
						        	else
						        		if ( temp1 == '=')	// <= >=
						        		{
						        			token = "" + ch + temp1;
						        			if (ch == '<')	// <=
						        				datas.add(new Data(XIAODENG,token));
						        			if (ch == '>')	// >=
						        				datas.add(new Data(DADENG,token));
						        			file.unread(temp2);				
						        		}
						        		else	// �� <adf
						        		{
						        			token = "" + ch;
						        			if (ch == '<')	// <
						        				datas.add(new Data(XIAO,token));
						        			if (ch == '>')	// >
						        				datas.add(new Data(DA,token));
						        			file.unread(temp2);
						        			file.unread(temp1);
						        		}
						    }
						    else
						    {
						    	if (temp1==ch)	// �� <<
						    	{
						    		token = "" + ch + temp1;
						    		if (ch == '<')	// <<
						    			datas.add(new Data(XIAOXIAO,token));
						    		if (ch == '>')	// >>
						    			datas.add(new Data(DADA,token));
						    	}
						    	else
						    		if (temp1 == '=')
						    		{
						    			token=""+ch+temp1;
						    			if (ch == '<')	// <=
						    				datas.add(new Data(XIAODENG,token));
						    			if (ch == '>')	// >=
						    				datas.add(new Data(DADENG,token));    		
						    		}
						    		else	// ��<a
						    		{
						    			token = "" + ch;
						    			if (ch == '<')	// <
						    				datas.add(new Data(XIAO,token));
						    			if (ch == '>')	// >
						    				datas.add(new Data(DA,token));
						    			file.unread(temp1);
						    		}						    					     
						    }
						}
						else	// ��<
						{
							token = "" + ch;
							if (ch == '<')	// <
								datas.add(new Data(XIAO,token));
				            if (ch == '>')	// >
				            	datas.add(new Data(DA,token));
						}
					}

					if (ch == '*' || ch == '%' || ch == '!' || ch == '~' || ch == '^')
					{
						if ((n = file.read()) != -1)
						{
							char temp = (char) n;
						    if (temp == '=')	// ��*=3 !=3
						    {
						    	token = "" + ch + temp;
							    if (ch == '*')	// *=
							    	datas.add(new Data(CHENGDENG,token));
					            if (ch == '%')	// %=
					            	datas.add(new Data(MODENG,token));
					            if (ch == '!')	// !=
					            	datas.add(new Data(BUDENG,token));
					            if (ch == '~')	// ~=
					            	datas.add(new Data(WEIFANDENG,token));
					            if (ch == '^')	// ~=
					            	datas.add(new Data(WEIYIHUODENG,token));				            
						    }   
						    else	//��*10  !11  %12
						    {
						    	token = "" + ch;
						    	if (ch == '*')	//*
							    	datas.add(new Data(CHENG,token));
						    	if (ch == '%')	// %
					            	datas.add(new Data(MO,token));
						    	if (ch == '!')	// !
					            	datas.add(new Data(BU,token));
						    	if (ch == '~')	// ~
					            	datas.add(new Data(WEIFAN,token));
						    	if (ch == '^')	// ^
					            	datas.add(new Data(WEIYIHUO,token));		
							    file.unread(temp);
						    }
						}
						else
						{
							token = "" + ch;	// �� ��
							if (ch == '*')	// *
								datas.add(new Data(CHENG,token));
				            if (ch == '%')	// %
				            	datas.add(new Data(MO,token));
				            if (ch == '!')	// !
				            	datas.add(new Data(BU,token));
				            if (ch == '~')	// ~
				            	datas.add(new Data(WEIFAN,token));
				            if (ch == '^')	// ^
				            	datas.add(new Data(WEIYIHUO,token));			
						}
					}

					if (ch == '/')
					{
						if ((n = file.read()) != -1)
						{
							char temp = (char) n;
							if (temp == '=')	// �� /=
							{
								token = "" + ch + temp;
								datas.add(new Data(CHUDENG,token));
							}
							else
								if (temp == '/')	// �� //
								{
									token = "//";
									while ((n = file.read()) != -1)
									{
										if (n == 13)
										{
											if ((n = file.read()) != -1)
												if (n == 10)
													break;
										}
										else
											token += (char) n;
											
									}
									System.out.println("" + count_row + "annotation: " + token);
									datas.add(new Data(ANNOTATATION,token));
									token = null;
									count_row++;
								}
								else
									if (temp == '*')	// ��  /*
									{
										token = "/*";
										while ((n = file.read()) != -1)
										{
											char temp1 = (char) n;
											if (n != 10 && n != 13)
												token += temp1;
											if (temp1 == '*')
											{
												if ((n = file.read()) != -1)
												{
													char temp2 = (char) n;
													if (temp2 == '/')
														break;	// �ҵ� */����
												}
											}
										}
										token += '/';
										System.out.println("" + count_row + "annotation: " + token);
										datas.add(new Data(ANNOTATATION,token));
										token = null;
						            }
									else
									{
										token = "" + ch;	// /
										datas.add(new Data(CHU,token));
										file.unread(temp);
									}
						}
						else
						{
							token = "" + ch;	// /
							datas.add(new Data(CHU,token));
						}			
					}
					if (token != null)
					   System.out.println("" + count_row + "   operator: " + token);
					break;
//APART
				case APART:	// �ָ���					
					if (ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}' || 
						ch == '?' || ch == ':' || ch == '#' || ch == ',' || ch == ';' || ch == '.')
					{
						token = "" + ch;
						if (ch == '(')
							datas.add(new Data(LEFTXIAOKUOHAO,token));
						if (ch == ')')
							datas.add(new Data(RIGHTXIAOKUOHAO,token));
						if (ch == '[')
							datas.add(new Data(LEFTZHONGKUOHAO,token));
						if (ch == ']')
							datas.add(new Data(RIGHTZHONGKUOHAO,token));
						if (ch == '{')
							datas.add(new Data(LEFTDAKUOHAO,token));
						if (ch == '}')
							datas.add(new Data(RIGHTDAKUOHAO,token));
						if (ch == '?')
							datas.add(new Data(WENHAO,token));
						if (ch == ':')
							datas.add(new Data(MAOHAO,token));
						if (ch == '#')
							datas.add(new Data(JINHAO,token));
						if (ch == ',')
							datas.add(new Data(DOUHAO,token));
						if (ch == ';')
							datas.add(new Data(FENHAO,token));
						if (ch == '.')
							datas.add(new Data(JUHAO,token));
						System.out.println(""+count_row+"   apart:   "+ch);
					}
					else	// ������  ˫�����е�ת���ַ�û����ֻ�Ǵ�����
					{
						if (ch == '"')
						{
							token="";
							while ((n = file.read()) != -1 && (char) n != '"')
							{
								if ((char) n != '\\')
									token = token + (char) n;
								else if ((char) n == '\\')	// ת���ַ�  \������
								{
									if ((n = file.read()) != -1 )	// ת���ַ�����c�е�\a  \v java��û��
									{
										char temp = (char) n;
										/*if (temp == 'n')							
										    token=token+"\n";
										else if (temp == 't')							
										    token=token+"\t";
										else if (temp == 'b')							
										    token=token+"\b";
										else if (temp == '\'')							
										    token=token+"'";
										else if (temp == '\"')							
										    token=token+"\"";
										else if (temp == '\\')							
										    token=token+"\\";
										else if (temp == '\f')							
										    token=token+"\f";
										else if (temp == '\r')							
										    token=token+"\r";
										else {
											datas.add(new Data(ERROR,"error ת���ַ�"));
											System.out.println(""+count_row+"  error ת���ַ�");
										}*/
										token = token + "\\" + temp;
									}
									else
									{
										datas.add(new Data(ERROR,"\""));	// ��ƥ��� "
										System.out.println("" + count_row + "  error \" ");
									}
								}
							}
			                if (n == -1)
			                {
			                	datas.add(new Data(ERROR,"\""));	// ��ƥ��� "
			                	System.out.println("" + count_row + "  error \" ");
			                }
			                else
			                {
			                	datas.add(new Data(SHUANGYINHAO,token));	
			                	System.out.println("" + count_row + "   \"apart\":   " + token);
			                }
						}
						
						if (ch == '\'')
						{
							token = "";
							if ((n = file.read()) != -1)
							{
								char temp1 = (char) n;
								if (temp1 == '\\')
								{
									if ((n = file.read()) != -1)
									{
										char temp2 = (char) n;
										token = "" + temp2;
										if ((n = file.read()) != -1 && (char) n == '\'')
										{
											datas.add(new Data(DANYINHAOZY,token));	// �� char c='\a'
											System.out.println("" + count_row+"   'apart':ת��   "+token);
										}										
										else
										{
											datas.add(new Data(ERROR,"'"));	// �� char c='\sdf'  //error
											System.out.println("" + count_row + "  error ' ");
										}
									}
									else
									{
										datas.add(new Data(ERROR,"'"));	// �� char c='\
										System.out.println("" + count_row + "  error ' ");
									}
								}
								else
									if (temp1 != '\'')
									{
										token=""+temp1;
										if ((n = file.read()) != -1 && (char) n == '\'')	// �� char c='i'
										{
											datas.add(new Data(DANYINHAO,token));
											System.out.println("" + count_row + "   'apart':   " + token);
										}
										else	// �� char c='rt' //error
										{
											datas.add(new Data(ERROR,"'"));
											System.out.println("" + count_row + "  error ' ");
										}
									}
									else	// char c=''  //error
									{
										datas.add(new Data(ERROR,"'"));
										System.out.println(""+count_row+"  error ' ");
									}
							}
							else
							{
								datas.add(new Data(ERROR,"'"));
								System.out.println("" + count_row + "  error ' ");
							}
						}
					}				
					break;
//UNKONWN
				case UNKNOWN:
					System.out.println(""+count_row+" UNKNOWN:  "+ch);
					token=""+ch;
					datas.add(new Data(UNKNOWN,token));
					break;
				}
			}
			file.close();
			
			FileWriter fw;
			fw = new FileWriter(new File(dir+"\\result.txt"));
			PrintWriter pw=new PrintWriter(fw);
			for(Data data:datas)	// ���������ļ�result��txt
			{
				pw.println(data.getType()+"    "+data.getValue());
			}
			pw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
