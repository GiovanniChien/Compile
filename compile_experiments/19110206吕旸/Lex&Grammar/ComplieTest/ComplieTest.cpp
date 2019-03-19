// ComplieTest.cpp : �������̨Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include "Node.h"
#include <string>
#include <fstream>
#include <iostream>
using namespace std;
/*������----������Ӧ;
  ��ʶ��----��������Hash��
  ��ֵ-----����Ҫ��*/
//�涨Դ����ĸ�ʽ��
//
//���� mian()----->>����
//{
//ͬһ��������ֻռһ��,�ֺŽ�β;
//}

//����+������+����+��-----������������
//����+������+����------����������ͷ
//����+��ʾ��+��+��ʾ��+��������+��------������������
#define M 50
#define N 50 //����ÿ������ַ���
#define LEN 7 //�������͵ĸ���
//char keys[][10]={"int","float","double","char","unsigned","short","static"};
char ValueType[][10]={"int","float","char","short","double","string","bool"};    //�������͵Ĺؼ���
char BaoLiuZi[][10]={"main","return","if","else","for","while","cout","cin"};    //���������͵Ĺؼ���
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
	bool IsBound(char c);     //�ж��Ƿ��Ƿָ���
	//bool IsKey(string word);//�ж��Ƿ��Ǳ�����
	bool IsLegalWord(string word);//�жϵ��������Ƿ�Ϸ�
	bool IsIdentifier(string word);
	string ExtractWord(char data[],int &loc);//����������
	string IsDefineLine(char data[]);    //ֻ�ж�������ľ��Ӳ������ڴ�������
	int ExtractWords(string words[],char data[]);
    //�ж��Ƿ���main����
	bool IsMain(string word);
	bool IsMainFunc(char data[]);

	void InsertIntoTable(string word,string type);
	void InsertIntoGlobalTable(string word,string type);

	void LexicalAnalysis(string words[],int i,int &location);//�ʷ�����
	void DisplayTable(Node LocalTable[]);
	char path[]="source.txt";
	char data[M];
	int location=0;
	int number=0;
	bool flag=false;
	//��ȫ�ֱ�;ֲ���ĵ�ַ�ŵ�head����
	HeadTable[0]=LocalTable;
	HeadTable[1]=GlobalTable;
	cout<<"��ʼ�������ֱ�......"<<endl;
	cout<<".............."<<endl;
	cout<<".............."<<endl;
	InitTable(LocalTable);
	InitTable(GlobalTable);
	cout<<"��ʼ����ɹ�!!!!"<<endl;
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
	cout<<"���е�������:"<<endl;;
	for(int i=0;i<index;i++)
		cout<<sourcewords[i]<<"  ";
	cout<<endl;
	cout<<endl;
	cout<<endl;
	cout<<"���Ѿ�����!��ʾ����:"<<endl;
	cout<<"�ֲ�����Ϣ����:"<<endl;
	DisplayTable(LocalTable);
	cout<<"ȫ�ֱ���Ϣ����:"<<endl;
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
					if(data[i]){ return "";}     //˵���Ǻ����������
					else{
						return tmp;              //ֻ�е�һ���е�һ������Ϊ�������͵Ĺؼ��֣���������ؼ��ֺ����һ���ո�˵���������Ƕ������
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
			//������ʾ������ֵ������
			string tmp=ExtractWord(data,loc);        //��������һ�е����е���
			words[i]=tmp;i++;
			sourcewords[index]=tmp;
			index++;
		}
		else 
		{
			if(IsOP(data[loc]))
			{
				//���������
				string tmp;
				tmp.push_back(data[loc]);
				words[i]=tmp;
				sourcewords[index]=tmp;
			    index++;
				loc++;i++;
			}
			if(IsBound(data[loc]))
			{
				//�����綨��
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
	//		//������ʾ������ֵ������
	//		string tmp=ExtractWord(data,loc);        //��������һ�е����е���
	//		words[i]=tmp;i++;
	//		sourcewords[index]=tmp;
	//		index++;
	//	}
	//	else 
	//	{
	//		if(IsOP(data[loc]))
	//		{
	//			//���������
	//			string tmp;
	//			tmp.push_back(data[loc]);
	//			words[i]=tmp;
	//			sourcewords[index]=tmp;
	//		    index++;
	//			loc++;i++;
	//		}
	//		if(IsBound(data[loc]))
	//		{
	//			//�����綨��
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
		cout<<words[j]<<"  ";               //��������ĵ���
	cout<<endl;
	for(int j=0;j<i;j++)
		if(!IsLegalWord(words[j]))
		{
			cout<<"��"<<location<<"�У�"<<words[j]<<"���ʷ�����"<<endl;
			count++;
		}
	if(count==0){cout<<"��һ���޴ʷ�����"<<endl;}
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

