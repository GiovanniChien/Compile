#include <string>
#include <fstream>
#include <vector>
#include <iostream>
using namespace std;

#define M 50 //����ÿ������ַ���
#define LEN 10000 //�������е�������
//����ֵ����
#define NULL 0
//�������ͱ���
#define INT 1
#define FLOAT 2
#define CHAR 3
#define SHORT 4
#define DOUBLE 5
#define STRING 6
#define BOOL 7
//ϵͳ�����ֱ���
#define MAIN 8
#define RETURN 9
#define IF 10
#define ELSE 11
#define FOR 12
#define WHILE 13
#define COUT 14
#define CIN 15
//���������
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
//�ֽ������
#define LBB 30 //"{"
#define RBB 31 //"}"
#define LSB 32 //"("
#define RSB 33 //")"
#define ASSIGN 34 //"="��ֵ
#define SEMI 35 //";"
#define COMMA 36 //","
// �û��Զ����ʾ������
#define ID 37
//�������ֱ���
#define NUM 38
//�����ַ�����
#define Error 39
//������ű���󳤶�
#define MAX 40

//���屣���֡��������ͱ���
#define BaoLiu 99
#define ValueKind 98
struct Node
{
	char data[15];
	Node *next;
};
struct Head
{
	int id;//���
	char type[15];//��Ӧ������,�ַ�����ʾ
	Node *first;
	bool exist;//�ж������ʾ�Ƿ���ֹ�
};
//��������ű�����һһ��Ӧ
//���磺�±�1��ӦINT���ͣ��±�37��Ӧ���еĳ����ڵ�
//�涨�û��Զ����ʾ���ĳ��Ȳ�����15
char kind[MAX][15]={"NULL","int","float","char","short","double","string","bool",
	                "main","return","if","else","for","while","cout","cin","<","<=",
	                ">",">=","!=","==","+","-","*","/","&&","||","&","|",
					"{","}","(",")","=",";",",","ID","NUM","ERROR"};
//char ValueType[][10]={"int","float","char","short","double","string","bool"};    //�������͵Ĺؼ���
char BaoLiuZi[][15]={"int","float","char","short","double","string","bool",
	                 "main","return","if","else","for","while","cout","cin"};    //���������͵Ĺؼ���
char op[]={'+','-','*','/','(',')','{','}','='};
char bound[]={',',';','[',']',' ','>','<','&','|','!'};
char character[26]={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
Head table[MAX];
char words[LEN][15];
int location=0;//���±�
int loc=0;//����������±�
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
	//��ʼ�����ű�
	cout<<"���ڳ�ʼ�����ű�..........."<<endl;
	bool result=InitTable(table);
	if(result){ cout<<"��ʼ�����ű�ɹ�!"<<endl;}
	else { cout<<"��ʼ�����ű�ʧ��!"<<endl;}
	//���ļ�
	char path[]="source.txt";
	char data[M];
	int number=0;//ÿ�����������ĵ�����
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
		cout<<"��"<<location<<"������Ϊ:"<<endl;
		cout<<data<<endl;
		//number=ExactWords(data);
		ExactWords(data);
		newval=loc;
		//cout<<"�ܹ�������"<<number<<"������"<<endl;
		cout<<"���������ĵ�������:"<<endl;
		for(int k=oldval;k<=newval;k++)
		{
			cout<<words[k]<<"  ";
		}
		cout<<endl;
	}
	cout<<endl<<endl;
	cout<<"���ű���Ϣ����:"<<endl;
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
	//��ʱ�������¼���Ľڵ�ֵ���������ַ�����
	//��ʼ���ڵ�
	strcpy(words[loc],tmp);
	loc++;
	if(id==37||id==38||id==0||id==39)
	{
		//�û��Զ����ʾ����������NULL��ERROR
	Node *node=new Node;
	strcpy(node->data,tmp);
	node->next=NULL;
	//�����������Ľڵ���뵽���ű���
	if(table[id].first==NULL)
	{
		table[id].first=node;
		table[id].exist=true;//�ñ�ʾ���Ǵ��ڵ�
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
		//������
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
			char tmp[15];//��ʱ�洢��;
			int j=0;
			//���������ж�
			//......
			//......
			//�ַ�����ʾ��
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
				//����:�����֡��û��Զ����ʾ����NULL
				int loct;
				loct=IsNULL(tmp);
				if(loct!=-1)
				{
					flag=0;
				}else if((loct=IsBaoLiuZi(tmp))!=-1)
				{
					flag=MapFromVToR(loct);//��������������±�ӳ�䵽ʵ�ʵ�kind�����±�
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
			//�������������
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
		cout<<"���:"<<table[i].id<<endl;
		cout<<"����"<<table[i].type<<endl;
		cout<<"�Ƿ����?";
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