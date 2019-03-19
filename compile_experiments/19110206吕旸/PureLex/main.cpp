#include <string>
#include <fstream>
#include <vector>
#include <iostream>
using namespace std;

#define M 50 //定义每行最大字符数
#define LEN 10000 //定义所有单词总数
//特殊值编码
#define NULL 0
//数据类型编码
#define INT 1
#define FLOAT 2
#define CHAR 3
#define SHORT 4
#define DOUBLE 5
#define STRING 6
#define BOOL 7
//系统保留字编码
#define MAIN 8
#define RETURN 9
#define IF 10
#define ELSE 11
#define FOR 12
#define WHILE 13
#define COUT 14
#define CIN 15
//运算符编码
#define LT 16 //"<"
#define LE 17 //"<="
#define GT 18 //">"
#define GE 19 //">="
#define NE 20 //"!="
#define EQ 21 //"=="
#define Plusop 22 //"+"
#define Minusop 23//"-"
#define Multiplyop 24 //"*"
#define Divisionop 25 //"/"
#define Andop 26 //"&&"
#define Orop 27 //"||"
#define Bitandop 28 //"&"
#define Bitorop 29 //"|"
//分界符编码
#define LBB 30 //"{"
#define RBB 31 //"}"
#define LSB 32 //"("
#define RSB 33 //")"
#define ASSIGN 34 //"="赋值
#define SEMI 35 //";"
#define COMMA 36 //","
// 用户自定义标示符编码
#define ID 37
//常量数字编码
#define NUM 38
//错误字符编码
#define Error 39
//定义符号表最大长度
#define MAX 40

//定义保留字、数据类型编码
#define BaoLiu 99
#define ValueKind 98
struct Node
{
	char data[15];
	Node *next;
};
struct Head
{
	int id;//编号
	char type[15];//对应的类型,字符串表示
	Node *first;
	bool exist;//判断这个表示是否出现过
};
//类型与符号表索引一一对应
//例如：下标1对应INT类型；下标37对应所有的常量节点
//规定用户自定义标示符的长度不大于15
char kind[MAX][15]={"NULL","int","float","char","short","double","string","bool",
	                "main","return","if","else","for","while","cout","cin","<","<=",
	                ">",">=","!=","==","+","-","*","/","&&","||","&","|",
					"{","}","(",")","=",";",",","ID","NUM","ERROR"};
//char ValueType[][10]={"int","float","char","short","double","string","bool"};    //数据类型的关键字
char BaoLiuZi[][15]={"int","float","char","short","double","string","bool",
	                 "main","return","if","else","for","while","cout","cin"};    //非数据类型的关键字
char op[]={'+','-','*','/','(',')','{','}','='};
char bound[]={',',';','[',']',' ','>','<','&','|','!'};
char character[26]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
Head table[MAX];
char words[LEN][15];
int location=0;//行下标
int loc=0;//单词数组的下标
int main()
{
	bool InitTable(Head table[]);
    bool IsCharacter(char c);
	int IsNULL(char tmp[]);
    bool IsNum(char c);
	int IsBaoLiuZi(char tmp[]);
	int MapFromVToR(int v);
    void InsertIntoTable(char tmp[],int id);
	void ExactWords(char data[]);
	void DisplayTable(Head tabel[]);
	//初始化符号表
	cout<<"正在初始化符号表..........."<<endl;
	bool result=InitTable(table);
	if(result){ cout<<"初始化符号表成功!"<<endl;}
	else { cout<<"初始化符号表失败!"<<endl;}
	//读文件
	char path[]="source.txt";
	char data[M];
	int number=0;//每行提炼出来的单词数
	ifstream is(path);
	if(!is.is_open())
	{
		cout<<"error opening file"<<endl;
	}
	while(!is.eof())
	{
		is.getline(data,M);
		location++;
		int oldval=loc,newval=0;
		cout<<"第"<<location<<"行数据为:"<<endl;
		cout<<data<<endl;
		//number=ExactWords(data);
		ExactWords(data);
		newval=loc;
		//cout<<"总共提炼出"<<number<<"个单词"<<endl;
		cout<<"提炼出来的单词如下:"<<endl;
		for(int k=oldval;k<=newval;k++)
		{
			cout<<words[k]<<"  ";
		}
		cout<<endl;
	}
	cout<<endl<<endl;
	cout<<"符号表信息如下:"<<endl;
	DisplayTable(table);
	return 0;
}
bool InitTable(Head table[])
{
	int i;
	for(i=0;i<MAX;i++)
	{
		table[i].first=NULL;
		table[i].id=i;
		strcpy(table[i].type,kind[i]);
		table[i].exist=false;
	}
	return true;
}
bool IsCharacter(char c)
{
	for(int i=0;i<26;i++)
	{
		char up=character[i]+32;		
		if(c==character[i]||c==up)
		{
			return true;
		}
	}
	return false;
}
int IsNULL(char tmp[])
{
	if(strcmp(tmp,"NULL")==0)
		return 0;
	else return -1;
}
bool IsNum(char c)
{
	int tmp=c-'0';
	for(int i=0;i<10;i++)
	{
		if(tmp==i)
			return true;
	}
	return false;
}
int IsBaoLiuZi(char tmp[])
{
	int i;
	int baoLiuZiLen=8;
	for(i=0;i<baoLiuZiLen;i++)
	{
		if(strcmp(tmp,BaoLiuZi[i])==0) return i;
	}
	return -1;
}
int MapFromVToR(int v)
{
	return v+1;
}
void InsertIntoTable(char tmp[],int id)
{
	//暂时将所有新加入的节点值都看成是字符串。
	//初始化节点
	strcpy(words[loc],tmp);
	loc++;
	if(id==37||id==38||id==0||id==39)
	{
		//用户自定义标示符、常量、NULL、ERROR
	Node *node=new Node;
	strcpy(node->data,tmp);
	node->next=NULL;
	//将新提炼出的节点加入到符号表中
	if(table[id].first==NULL)
	{
		table[id].first=node;
		table[id].exist=true;//该标示符是存在的
	}else
	{
		Node *p=table[id].first;
		Node *pre=NULL;
		while(p)
		{
			pre=p;
			p=p->next;
		}
		pre->next=node;
	}
	}else 
	{
		//保留字
		table[id].exist=true;
	}
}
void ExactWords(char data[])
{
	int i=0;
	while(data[i])
	{
		if(data[i]!=' ')
		{
			char tmp[15];//临时存储单;
			int j=0;
			//单词类型判断
			//......
			//......
			//字符串标示符
			int flag;
			if(IsCharacter(data[i])||data[i]=='_')
			{
				tmp[j]=data[i];i++;j++;
				while(data[i]&&(IsCharacter(data[i])||IsNum(data[i])||data[i]=='_'))
				{
					tmp[j]=data[i];
					j++;i++;
				}
				tmp[j]=0;
				//分类:保留字、用户自定义标示符、NULL
				int loct;
				loct=IsNULL(tmp);
				if(loct!=-1)
				{
					flag=0;
				}else if((loct=IsBaoLiuZi(tmp))!=-1)
				{
					flag=MapFromVToR(loct);//将保留字数组的下标映射到实际的kind数组下标
				}else
				{
					flag=37;
				}
				InsertIntoTable(tmp,flag);
			}else if(IsNum(data[i]))
			{
				tmp[j]=data[i];j++;i++;
				while(data[i]&&(data[i]=='.'||IsNum(data[i])))
				{
					tmp[j]=data[i];j++;i++;
				}
				tmp[j]=0;
				flag=38;
				InsertIntoTable(tmp,flag);
			}
			else{
			//其他情况的讨论
			switch(data[i])
			{
			case '<': {
				         tmp[j]=data[i];j++;i++;
				         if(data[i]!='=')
					     {
							 flag=16;
							 tmp[j]=0;
					     }else
						 {
							 tmp[j]=data[i];j++;tmp[j]=0;
							 flag=17;i++;
						 }
						 InsertIntoTable(tmp,flag);
					  };break;
			case '>':{
				         tmp[j]=data[i];j++;i++;
				         if(data[i]!='=')
					     {
							 flag=18;
							 tmp[j]=0;
					     }else
						 {
							 tmp[j]=data[i];j++;tmp[j]=0;
							 flag=19;i++;
						 }
						 InsertIntoTable(tmp,flag);
					 };break;
			case '!':{
				         tmp[j]=data[i];j++;i++;
						 if(data[i]=='=')
						 {
							 tmp[j]=data[i];j++;i++;tmp[j]=0;flag=20;
						 }else
						 {
							 flag=39;
							 tmp[j]=0;
						 }
						 InsertIntoTable(tmp,flag);
					 };break;
			case '=':{
				         tmp[j]=data[i];j++;i++;
						 if(data[i]=='=')
						 {
							 tmp[j]=data[i];j++;i++;tmp[j]=0;flag=21;
						 }else
						 {
							 flag=34;
							 tmp[j]=0;
						 }
						 InsertIntoTable(tmp,flag);
					 };break;
			case '+':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=22;
						 InsertIntoTable(tmp,flag);
					 };break;
			case '-':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=23;
						 InsertIntoTable(tmp,flag);
					 };break;
			case '*':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=24;
						 InsertIntoTable(tmp,flag);
					 };break;
			case '/':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=25;
						 InsertIntoTable(tmp,flag);
					 };break;
			case '&':{
				         tmp[j]=data[i];j++;i++;
				         if(data[i]!='&')
					     {
							 flag=28;
							 tmp[j]=0;
					     }else
						 {
							 tmp[j]=data[i];j++;tmp[j]=0;
							 flag=26;i++;
						 }
						 InsertIntoTable(tmp,flag); 
					 };break;
			case '|':{
				         tmp[j]=data[i];j++;i++;
				         if(data[i]!='|')
					     {
							 flag=29;
							 tmp[j]=0;
					     }else
						 {
							 tmp[j]=data[i];j++;tmp[j]=0;
							 flag=27;i++;
						 }
						 InsertIntoTable(tmp,flag); 
					 };break;
			case '{':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=30;
						 InsertIntoTable(tmp,flag);
					 };break;
			case '}':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=31;
						 InsertIntoTable(tmp,flag);
					 };break;
			case '(':{ 
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=32;
						 InsertIntoTable(tmp,flag);
					 };break;
			case ')':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=33;
						 InsertIntoTable(tmp,flag);
					 };break;
			case ';':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=35;
						 InsertIntoTable(tmp,flag);
					 };break;
			case ',':{
				         tmp[j]=data[i];j++;i++;
						 tmp[j]=0;
						 flag=36;
						 InsertIntoTable(tmp,flag);
					 };break;
			}
			}
		}else
		{
			i++;
		}
	}
}
void DisplayTable(Head table[])
{
	int i;
	
	for(i=0;i<MAX;i++)
	{
		cout<<"编号:"<<table[i].id<<endl;
		cout<<"类型"<<table[i].type<<endl;
		cout<<"是否存在?";
		if(table[i].exist) cout<<"YES!"<<endl;
		else    cout<<"NO!"<<endl;
		if(table[i].exist)
		{
			Node *p=table[i].first;
			while(p)
			{
				cout<<"-->"<<p->data;
				p=p->next;
			}
		}
		cout<<endl;
	}
}