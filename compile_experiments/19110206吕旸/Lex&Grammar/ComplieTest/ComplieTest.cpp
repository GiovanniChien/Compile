// ComplieTest.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include "Node.h"
#include <string>
#include <fstream>
#include <iostream>
using namespace std;
/*保留字----数组表对应;
  标识符----拉链法的Hash表
  数值-----不需要表*/
//规定源程序的格式：
//
//类型 mian()----->>函数
//{
//同一变量声明只占一行,分号结尾;
//}

//类型+函数名+（）+；-----》》函数声明
//类型+函数名+（）------》》函数体头
//类型+标示符+，+标示符+。。。。+；------》》变量定义
#define M 50
#define N 50 //定义每行最大字符数
#define LEN 7 //数据类型的个数
//char keys[][10]={"int","float","double","char","unsigned","short","static"};
char ValueType[][10]={"int","float","char","short","double","string","bool"};    //数据类型的关键字
char BaoLiuZi[][10]={"main","return","if","else","for","while","cout","cin"};    //非数据类型的关键字
char op[]={'+','-','*','/','(',')'};
char bound[]={',',';','{','}','=','[',']',' ','>','<','&','|','!'};
char num[]={'0','1','2','3','4','5','6','7','8','9'};
string sourcewords[100];
int index=0;
Node LocalTable[LEN];
Node GlobalTable[LEN];
Node *HeadTable[2];
int _tmain(int argc, _TCHAR* argv[])
{
	void InitTable(Node Table[]);
	void InsertTable(Node LocalTable[],char word[],char type[]);
	bool IsNum(char c);
	bool IsNumber(char s[],int n);
	bool IsOP(char c);
	bool IsValueType(string s);
	bool IsBound(char c);     //判断是否是分隔符
	//bool IsKey(string word);//判断是否是保留字
	bool IsLegalWord(string word);//判断单词命名是否合法
	bool IsIdentifier(string word);
	string ExtractWord(char data[],int &loc);//提炼出单词
	string IsDefineLine(char data[]);    //只有定义变量的句子才能用于创建表项
	int ExtractWords(string words[],char data[]);
    //判断是否是main函数
	bool IsMain(string word);
	bool IsMainFunc(char data[]);

	void InsertIntoTable(string word,string type);
	void InsertIntoGlobalTable(string word,string type);

	void LexicalAnalysis(string words[],int i,int &location);//词法分析
	void DisplayTable(Node LocalTable[]);
	char path[]="source.txt";
	char data[M];
	int location=0;
	int number=0;
	bool flag=false;
	//将全局表和局部表的地址放到head表中
	HeadTable[0]=LocalTable;
	HeadTable[1]=GlobalTable;
	cout<<"初始化保留字表......"<<endl;
	cout<<".............."<<endl;
	cout<<".............."<<endl;
	InitTable(LocalTable);
	InitTable(GlobalTable);
	cout<<"初始化表成功!!!!"<<endl;
	string words[N];
	ifstream is(path);
	if(!is.is_open())
	{
		cout<<"error opening file!";exit(1);
	}
	while(!is.eof())
	{
		is.getline(data,N);
		location++;
		cout<<data<<endl;
		number=ExtractWords(words,data);
		if(IsMainFunc(data)){ flag=true;}
		switch(flag){
		case true:
		if(IsDefineLine(data)!="")
		{
			/*InsertTable(Table,word,type);*/
			string type=words[0];
			for(int i=1;i<number;i++)
			{
				if(IsIdentifier(words[i]) && IsLegalWord(words[i]))
				{
					InsertIntoTable(words[i],type);
				}
			}
		} break;
		/*number=ExtractWords(words,data);*/
		case false:
	    {
			if(IsDefineLine(data)!="")
			{
				string type=words[0];
				for(int i=1;i<number;i++)
				{
					if(IsIdentifier(words[i]) && IsLegalWord(words[i]))
					{
						InsertIntoGlobalTable(words[i],type);
					}
				}
			}
		} break;
		}
		LexicalAnalysis(words,number,location);
		
	}
	cout<<endl;
	cout<<"所有单词如下:"<<endl;;
	for(int i=0;i<index;i++)
		cout<<sourcewords[i]<<"  ";
	cout<<endl;
	cout<<endl;
	cout<<endl;
	cout<<"表已经建好!显示如下:"<<endl;
	cout<<"局部表信息如下:"<<endl;
	DisplayTable(LocalTable);
	cout<<"全局表信息如下:"<<endl;
    DisplayTable(GlobalTable);
	/*cout<<HeadTable[0]<<"--->>>"<<HeadTable[1]<<endl;*/
	return 0;
}

void InitTable(Node Table[])
{
	int i;
	for(i=0;i<LEN;i++)
	{
		Table[i].next=NULL;
		strcpy(Table[i].type,ValueType[i]);
		strcpy(Table[i].value,"");
	}
}
bool IsNum(char c)
{
	int i;
	for(i=0;i<10;i++)
		if(c==num[i]) return true;
	return false;
}
bool IsNumber(char s[],int n)
{
	int i;
	for(i=0;i<n;i++)
	{
		if(!IsNum(s[i])) break;
	}
	if(i<n) return false;
	else    return true;
}

bool IsOP(char c)
{
	int i;
	for(i=0;i<6;i++)
		if(op[i]==c) return true;
	return false;
}
bool IsValueType(string s)
{
	 int i;
	 char tmp[10];
	 for(i=0;i<s.length();i++)
		 tmp[i]=s[i];
	 tmp[i]=0;
	 for(i=0;i<LEN;i++)
		 if(strcmp(tmp,ValueType[i])==0) break;
	 if(i<LEN) return true;
	 else      return false;
}
bool IsBound(char c)
{
	int i;
	for(i=0;i<13;i++)
		if(bound[i]==c) return true;
	return false;
}
string ExtractWord(char data[],int &loc)
{
	int first=loc,end;
	loc++;
	while(data[loc])
	{
		if(!IsOP(data[loc]) && !IsBound(data[loc]))  {loc++;}
		else                  {break;}
	}
	end=loc;
	string tmp;
	for(int i=first;i<end;i++)
	{	tmp.push_back(data[i]);}
	return tmp;
}

bool IsLegalWord(string word)
{
	int i=0;
	if(IsNum(word[i]))
	{
		for(int j=i+1;j<word.length();j++)
			if(!IsNum(word[j])) return false;
		return true;
	}
	else
	{
		/*if(!IsBound(word[i])&& !IsOP(word[i]))
		{
			return true;
		}
		else*/
		return true;
	}
}
bool IsIdentifier(string word)
{
	int i=0,j,len;
	char tmp[10];
	for(j=0;j<word.length();j++)
		tmp[j]=word[j];
	tmp[j]=0;
	len=word.length();
	if(!IsNumber(tmp,len))
	{
		if(!IsBound(word[i]) && !IsOP(word[i]))
		{
			return true;
		}
		else{ return false;}
	}
	else return false;
}

string IsDefineLine(char data[])
{
	int i=0,loc=0;
	while(data[loc])
	{
		if(!IsOP(data[loc]) && !IsBound(data[loc]))
		{
			string tmp=ExtractWord(data,loc);
			if(IsValueType(tmp))
			{
				if(data[loc]==' '){ 
					for(i=loc+1;data[i];i++)
					{
						if(data[i]=='('||data[i]==')') break;
					}
					if(data[i]){ return "";}     //说明是函数定义语句
					else{
						return tmp;              //只有当一行中第一个单词为数据类型的关键字，并且这个关键字后紧跟一个空格，说明这个语句是定义语句
					}
				}  
				else return "";
			}
			else
			{
				return "";
			}
		}
		else
		{
			loc++;
		}
	}
	return "";
}
bool IsMain(string word)
{
	char tmp[10];
	char src[]="main";
	int i;
	for(i=0;i<word.length();i++)
		tmp[i]=word[i];
	tmp[i]=0;
	if(strcmp(tmp,src)==0){return true;}
	else {return false;}
}
bool IsMainFunc(char data[])
{
	int loc=0;
    while(data[loc])
	{
		if(!IsBound(data[loc]) && !IsOP(data[loc]))
		{
			string tmp=ExtractWord(data,loc);
			if(IsValueType(tmp))
			{
				if(data[loc]==' ')
				{
					loc++;
					while(data[loc])
					{
						if(!IsBound(data[loc]) && !IsOP(data[loc]))
						{
							string tmp1=ExtractWord(data,loc);
							if(IsMain(tmp1))  return true;
							else return false;
						}
						else{loc++;}
					}
				}else{return false;}
			}
			else{ return false;}
		}
		else
		{
			loc++;
		}
	}
	return false;
}
int ExtractWords(string words[],char data[])
{
	int loc=0,i=0;
	while(data[loc])
	{
		if(!IsOP(data[loc]) && !IsBound(data[loc]))
		{
			//产生标示符和数值型数字
			string tmp=ExtractWord(data,loc);        //提炼出这一行的所有单词
			words[i]=tmp;i++;
			sourcewords[index]=tmp;
			index++;
		}
		else 
		{
			if(IsOP(data[loc]))
			{
				//产生运算符
				string tmp;
				tmp.push_back(data[loc]);
				words[i]=tmp;
				sourcewords[index]=tmp;
			    index++;
				loc++;i++;
			}
			if(IsBound(data[loc]))
			{
				//产生界定符
				/*if(data[loc]!=' ')
				{
				string tmp;
				tmp.push_back(data[loc]);
				words[i]=tmp;
				sourcewords[index]=tmp;
			    index++;
				loc++;
				i++;
				}else{ loc++;}*/
				switch(data[loc])
				{
				case ' ': loc++;break;
				case '=':{if(data[loc+1]=='=')
						 {
							 string tmp;tmp.push_back(data[loc]);
							 loc++;tmp.push_back(data[loc]);
							 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
						 }
						 else
						 {
							 string tmp;
				             tmp.push_back(data[loc]);
				             words[i]=tmp;
				             sourcewords[index]=tmp;
			                 index++;
				             loc++;
				             i++;
						 }
						 } ;break;
				case '>':{if(data[loc+1]=='=')
						 {
							 string tmp;tmp.push_back(data[loc]);
							 loc++;tmp.push_back(data[loc]);
							 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
						 }
						 else
						 {
							 string tmp;
				             tmp.push_back(data[loc]);
				             words[i]=tmp;
				             sourcewords[index]=tmp;
			                 index++;
				             loc++;
				             i++;
						 }
						 } ;break;
				case '<':{if(data[loc+1]=='=')
						 {
							 string tmp;tmp.push_back(data[loc]);
							 loc++;tmp.push_back(data[loc]);
							 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
						 }
						 else
						 {
							 string tmp;
				             tmp.push_back(data[loc]);
				             words[i]=tmp;
				             sourcewords[index]=tmp;
			                 index++;
				             loc++;
				             i++;
						 }
						 } ;break;
				case '&':{if(data[loc+1]=='&')
						 {
							 string tmp;tmp.push_back(data[loc]);
							 loc++;tmp.push_back(data[loc]);
							 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
						 }
						 else
						 {
							 string tmp;
				             tmp.push_back(data[loc]);
				             words[i]=tmp;
				             sourcewords[index]=tmp;
			                 index++;
				             loc++;
				             i++;
						 }
						 };break;
				case '|':{if(data[loc+1]=='|')
						 {
							 string tmp;tmp.push_back(data[loc]);
							 loc++;tmp.push_back(data[loc]);
							 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
						 }
						 else
						 {
							 string tmp;
				             tmp.push_back(data[loc]);
				             words[i]=tmp;
				             sourcewords[index]=tmp;
			                 index++;
				             loc++;
				             i++;
						 }
						 };break;
				case '!':{if(data[loc+1]=='=')
						 {
							 string tmp;tmp.push_back(data[loc]);
							 loc++;tmp.push_back(data[loc]);
							 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
						 }
						 else
						 {
							 string tmp;
				             tmp.push_back(data[loc]);
				             words[i]=tmp;
				             sourcewords[index]=tmp;
			                 index++;
				             loc++;
				             i++;
						 }
						 };break;
				default:{
					string tmp;
				    tmp.push_back(data[loc]);
				    words[i]=tmp;
				    sourcewords[index]=tmp;
			        index++;
				    loc++;
				    i++;
						}
				}
			}
			/*loc++;*/
		}
	}
	return i;
}
void InsertIntoTable(string word,string type)
{
	int i,j;
	for(i=0;i<LEN;i++)
	{
		if(LocalTable[i].type==type) break; 
	}
	if(i>=LEN) {cout<<"ERROR!"; return ;}
	Node *node=new Node;
	Node *p=LocalTable[i].next;
	Node *pre_p=&LocalTable[i];
	while(p)
	{
		pre_p=p;
		p=p->next;
	}
	char tmp[10];
	for(j=0;j<type.length();j++)
		tmp[j]=type[j];
	tmp[j]=0;
	strcpy(node->type,tmp);
	for(j=0;j<word.length();j++)
		tmp[j]=word[j];
	tmp[j]=0;
	strcpy(node->value,tmp);
	node->next=NULL;
	pre_p->next=node;
}

void InsertIntoGlobalTable(string word,string type)
{
	int i,j;
	for(i=0;i<LEN;i++)
	{
		if(GlobalTable[i].type==type) break; 
	}
	if(i>=LEN) {cout<<"ERROR!"; return ;}
	Node *node=new Node;
	Node *p=GlobalTable[i].next;
	Node *pre_p=&GlobalTable[i];
	while(p)
	{
		pre_p=p;
		p=p->next;
	}
	char tmp[10];
	for(j=0;j<type.length();j++)
		tmp[j]=type[j];
	tmp[j]=0;
	strcpy(node->type,tmp);
	for(j=0;j<word.length();j++)
		tmp[j]=word[j];
	tmp[j]=0;
	strcpy(node->value,tmp);
	node->next=NULL;
	pre_p->next=node;
}

void LexicalAnalysis(string words[],int i,int &location)
{
	int count=0;
	//int loc=0,i=0;
	//int count=0;
	//while(data[loc])
	//{
	//	if(!IsOP(data[loc]) && !IsBound(data[loc]))
	//	{
	//		//产生标示符和数值型数字
	//		string tmp=ExtractWord(data,loc);        //提炼出这一行的所有单词
	//		words[i]=tmp;i++;
	//		sourcewords[index]=tmp;
	//		index++;
	//	}
	//	else 
	//	{
	//		if(IsOP(data[loc]))
	//		{
	//			//产生运算符
	//			string tmp;
	//			tmp.push_back(data[loc]);
	//			words[i]=tmp;
	//			sourcewords[index]=tmp;
	//		    index++;
	//			loc++;i++;
	//		}
	//		if(IsBound(data[loc]))
	//		{
	//			//产生界定符
	//			/*if(data[loc]!=' ')
	//			{
	//			string tmp;
	//			tmp.push_back(data[loc]);
	//			words[i]=tmp;
	//			sourcewords[index]=tmp;
	//		    index++;
	//			loc++;
	//			i++;
	//			}else{ loc++;}*/
	//			switch(data[loc])
	//			{
	//			case ' ': loc++;break;
	//			case '=':{if(data[loc+1]=='=')
	//					 {
	//						 string tmp;tmp.push_back(data[loc]);
	//						 loc++;tmp.push_back(data[loc]);
	//						 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
	//					 }
	//					 else
	//					 {
	//						 string tmp;
	//			             tmp.push_back(data[loc]);
	//			             words[i]=tmp;
	//			             sourcewords[index]=tmp;
	//		                 index++;
	//			             loc++;
	//			             i++;
	//					 }
	//					 } ;break;
	//			case '>':{if(data[loc+1]=='=')
	//					 {
	//						 string tmp;tmp.push_back(data[loc]);
	//						 loc++;tmp.push_back(data[loc]);
	//						 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
	//					 }
	//					 else
	//					 {
	//						 string tmp;
	//			             tmp.push_back(data[loc]);
	//			             words[i]=tmp;
	//			             sourcewords[index]=tmp;
	//		                 index++;
	//			             loc++;
	//			             i++;
	//					 }
	//					 } ;break;
	//			case '<':{if(data[loc+1]=='=')
	//					 {
	//						 string tmp;tmp.push_back(data[loc]);
	//						 loc++;tmp.push_back(data[loc]);
	//						 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
	//					 }
	//					 else
	//					 {
	//						 string tmp;
	//			             tmp.push_back(data[loc]);
	//			             words[i]=tmp;
	//			             sourcewords[index]=tmp;
	//		                 index++;
	//			             loc++;
	//			             i++;
	//					 }
	//					 } ;break;
	//			case '&':{if(data[loc+1]=='&')
	//					 {
	//						 string tmp;tmp.push_back(data[loc]);
	//						 loc++;tmp.push_back(data[loc]);
	//						 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
	//					 }
	//					 else
	//					 {
	//						 string tmp;
	//			             tmp.push_back(data[loc]);
	//			             words[i]=tmp;
	//			             sourcewords[index]=tmp;
	//		                 index++;
	//			             loc++;
	//			             i++;
	//					 }
	//					 };break;
	//			case '|':{if(data[loc+1]=='|')
	//					 {
	//						 string tmp;tmp.push_back(data[loc]);
	//						 loc++;tmp.push_back(data[loc]);
	//						 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
	//					 }
	//					 else
	//					 {
	//						 string tmp;
	//			             tmp.push_back(data[loc]);
	//			             words[i]=tmp;
	//			             sourcewords[index]=tmp;
	//		                 index++;
	//			             loc++;
	//			             i++;
	//					 }
	//					 };break;
	//			case '!':{if(data[loc+1]=='=')
	//					 {
	//						 string tmp;tmp.push_back(data[loc]);
	//						 loc++;tmp.push_back(data[loc]);
	//						 words[i]=tmp;sourcewords[index]=tmp;index++;loc++;i++;
	//					 }
	//					 else
	//					 {
	//						 string tmp;
	//			             tmp.push_back(data[loc]);
	//			             words[i]=tmp;
	//			             sourcewords[index]=tmp;
	//		                 index++;
	//			             loc++;
	//			             i++;
	//					 }
	//					 };break;
	//			default:{
	//				string tmp;
	//			    tmp.push_back(data[loc]);
	//			    words[i]=tmp;
	//			    sourcewords[index]=tmp;
	//		        index++;
	//			    loc++;
	//			    i++;
	//					}
	//			}
	//		}
	//		/*loc++;*/
	//	}
	//}
	for(int j=0;j<i;j++)
		cout<<words[j]<<"  ";               //输出提炼的单词
	cout<<endl;
	for(int j=0;j<i;j++)
		if(!IsLegalWord(words[j]))
		{
			cout<<"第"<<location<<"行："<<words[j]<<"；词法出错。"<<endl;
			count++;
		}
	if(count==0){cout<<"这一行无词法错误。"<<endl;}
}
void DisplayTable(Node Table[])
{
	int i;
	Node *p;
	for(i=0;i<LEN;i++)
	{
		if(Table[i].next==NULL) continue;
		char type[10];
		strcpy(type,Table[i].type);
		p=Table[i].next;
		while(p)
		{
			cout<<"<"<<type<<","<<p->value<<">"<<endl;
			p=p->next;
		}
	}
}

