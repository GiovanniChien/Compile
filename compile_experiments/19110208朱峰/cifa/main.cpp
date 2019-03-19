#include "uthash.h"
#include <iostream>
#include <fstream>
#include <string>
using namespace std;
#define N 102
#define TIMES 5


#define NUMBER   0   // ���ּ�С����
#define ALPHA   1   // ��ĸ
#define SPACE   2   // �ո�
#define OPERATOR   3   // ������
#define APART   4   // �ָ���
#define XIAHUA 5
#define INTCONSTANCE 100  
#define DOUBLECONSTANCE 101  
#define IDENT 102  

#define BREAK 103  
#define CASE 104  
#define CHAR 105  
#define CONTINUE 106  
#define DEFAULT 107  
#define DO 108  
#define DOUBLE 109  
#define ELSE 110  
#define FLOAT 111  
#define FOR 112  
#define IF 113  
#define INCLUDE 114  
#define INT 115  
#define LONG 116  
#define MAIN 117  
#define RETURN 118  
#define SHORT 119  
#define SIGNED 120  
#define SIZEOF 121  
#define STATIC 122  
#define SWITCH 123  
#define UNSIGNED 124  
#define VOID 125  
#define WHILE 126  
	
#define JIA 200  
#define JIAN 201  
#define CHENG 202  
#define CHU 203  
#define JIADENG 204  
#define JIANDENG 205  
#define CHENGDENG 206  
#define CHUDENG 207  
#define JIAJIA 208  
#define JIANJIAN 209  
#define MO 210  
#define MODENG 211  
#define LEFTYIDENG 212   //  << 
#define RIGHTYIDENG 213   //  >> 
#define XIAOXIAO 214   //  <<
#define DADA 215    //  >>
#define XIAOYU 216    //  <
#define DAYU 217   // >
#define DENG 218    //  
#define DENGDENG 219    //   
#define BUDENG 220    //! 
#define BU 221    //!
#define DADENG 222    // > 
#define XIAODENG 223   // < 
#define YU 224    // &&
#define HUO 225   // ||
#define WEIYU 226   // &
#define WEIHUO 227    // |
#define WEIYUDENG 228   // & 
#define WEIHUODENG 229   // | 
#define WEIFAN 230    // ~
#define WEIFANDENG 231   // ~ 
#define WEIYIHUO 232   // ^
#define WEIYIHUODENG 233   // ^ 
#define AND 234
#define SHU 235

#define ZUOXIAOKUO 300  
#define YOUXIAOKUO 301  
#define ZUOZHONGKUO 302  
#define YOUZHONGKUO 303  
#define ZUODAKUO 304  
#define YOUDAKUO 305  
#define WENHAO 306  
#define MAOHAO 307  
#define JINHAO 308  
#define DOU 309  
#define FEN 310  
#define DANYINHAO 311  
#define DANYINHAOZY 312  

#define SHUANGYINHAO 313  
#define JUHAO 314  
#define YINHAO 315
#define ERROR 400  
#define END 500

struct value{
	const char* valuename;
	int type;
	int id;
	UT_hash_handle hh;
};

string reserve[N]={""};
char ch;//������¶�����Դ�����ַ�
string token;
ifstream openfile;
ifstream opencodefile;
char buffer[100];//�����洢���˻ص��ַ�
char chac;
int bufferpoint=0;
int fileend;

void reservefileopen();
void reservefileclose();
void codefileopen();
void codefileclose();
void initreserve();

int scanner();
void getsym();//����һ�������ַ�����ch���Ѷ���Դ�����ַ���ָ����ǰ�ƶ�1�ֽ�
void getbc();//ch�Ƿ�Ϊ�գ����з��������ǣ����������getsym()����ֱ�������ǿ��ҷǱ���ַ���chΪֹ
int getchoice(char);
void strcact();//��ch�е��ַ����ӵ�token����
bool isletter();//ch�Ƿ�Ϊ��ĸ
bool isdigit();//ch�Ƿ�Ϊ����
int findreserve();//��token�е��ַ����ұ����ֱ�������һ�������֣��򷵻����Ĵ����ʾ�����򷵻طǱ�������Ϣ
int findusrvalue();
void retract();//��ch�е��ַ��˻���Դ����
void buildlist(int);//Ϊ�û�����ı�ʾ���ͳ��콨�����ű�
void output(int flag,string point);//�ѵ��ʵĶ�Ԫʽ���ظ����ó��򣬼��﷨��������
void error();

struct value* val=NULL;

void add_value(struct value* v){
	HASH_ADD_STR(val,valuename,v);
}

value* find_value(char* a){
	struct value* v;
	HASH_FIND_STR(val,a,v);
	return v;
}

int main()
{
	fileend=0;
	reservefileopen();
	codefileopen();
	initreserve();
	printf("\n");
	reservefileclose();
	while(scanner()&&fileend==0);
	codefileclose();
	return 0;
}

void reservefileopen()
{
	openfile.open("word.txt");
	if(!openfile.is_open())
	{
		cout<<"Error opening file!"<<endl;
		exit(1);
	}
}

void reservefileclose()
{
	openfile.close();
}

void codefileopen()
{
	opencodefile.open("code.txt");
	if(!opencodefile.is_open())
	{
		cout<<"Error opening code file"<<endl;
		exit(1);
	}
}

void codefileclose()
{
	opencodefile.close();
}

void initreserve()
{
	char temp=NULL;
	int state=0;
	int count=0;
	while(1)
	{
		int numword=0;
		string restemp="";
		while(1)
		{
			if(temp=='*')
				break;
			openfile.get(temp);
			if(temp==' '&&state==0)
			{
				int loc=0;
				for(int i=0; i<restemp.length(); i++)
				{
					loc=loc+restemp.at(i)-'a';
				}
				if(!reserve[loc].empty())
					loc=(loc+1)%N;
				reserve[loc]=restemp;
				count++;
				break;
			}
			if(temp!=' '&&temp!='*')
			{
				restemp=restemp+temp;
			}
		}
		if(temp=='*')
			break;
	}
	cout<<count<<"reserve words has been inited"<<endl;
	cout<<reserve[53];
	for(int i=0; i<N; i++)
	{
		cout<<reserve[i];
	}
}

int scanner()
{
	int flag;
	token="";
	getsym();
			if(fileend==1)
				return 0;
	getbc();
	int choice=getchoice(ch);
	switch(choice)
	{
	case ALPHA:
	case XIAHUA:
		strcact();
		getsym();
			if(fileend==1)
				return 0;
		while(isletter()||isdigit()||(ch=='_'))
		{
			strcact();
			getsym();
			if(fileend==1)
				return 0;
		}
		retract();
		flag=findreserve();
		if(!flag)
		{
			flag=findusrvalue();
			if(!flag)
				buildlist(flag);
			output(flag,"ָ��");
		}else
		{
			output(flag,"ָ��");
		}
		break;
	case NUMBER:
		strcact();
		getsym();
			if(fileend==1)
				return 0;
		while(isdigit())
		{
			strcact();
			getsym();
			if(fileend==1)
				return 0;
		}
		retract();
		output(NUMBER,"����");
		break;
	case JIA:
		output(JIA,"+");
		break;
	case JIAN:
		output(JIAN,"-");
		break;
	case CHENG:
		output(CHENG,"*");
		break;
	case CHU:
		output(CHU,"/");
		break;
	case SHU:
		strcact();
		getsym();
			if(fileend==1)
				return 0;
		if(ch=='|')
			output(HUO,"||");
		else
		{
			retract();
			output(SHU,"|");
		}
		break;
	case AND:
		strcact();
		getsym();
			if(fileend==1)
				return 0;
		if(ch=='&')
			output(YU,"&&");
		else
		{
			retract();
			output(AND,"&");
		}
		break;
	case DAYU:
		strcact();
		getsym();
			if(fileend==1)
				return 0;
		if(ch=='=')
			output(DADENG,">=");
		else
		{
			retract();
			output(DAYU,">");
		}
		break;
	case XIAOYU:
		strcact();
		getsym();
			if(fileend==1)
				return 0;
		if(ch=='=')
			output(XIAODENG,"<=");
		else
		{
			retract();
			output(XIAOYU,"<");
		}
		break;
	case ZUODAKUO:
		output(ZUODAKUO,"{");
		break;
	case YOUDAKUO:
		output(YOUDAKUO,"}");
		break;
	case ZUOXIAOKUO:
		output(ZUOXIAOKUO,"(");
		break;
	case YOUXIAOKUO:
		output(YOUXIAOKUO,")");
		break;
	case DENG:
		output(DENG,"=");
		break;
	case FEN:
		output(FEN,";");
		break;
	case DOU:
		output(DOU,",");
		break;
	case END:
		return 0;
	case YINHAO:
		output(YINHAO,"'");
	default:error();
	}
	return 1;
}

void getsym()
{
	if(chac!=NULL)
	{
		ch=chac;
		chac=NULL;
	}
	else
	{
		if(!opencodefile.eof())
			opencodefile.get(ch);
		else
			fileend=1;
	}
}

void getbc()
{
	while(1)
	{
		if(ch==' '||ch=='\n'||ch=='*')
			getsym();
		else
			break;
	}
}

int getchoice(char a)
{
	if(a>='a'&&a<='z'||a>='A'&&a<='Z')
		return ALPHA;
	if(a=='_')
		return XIAHUA;
	if(a>='0'&&a<='9')
		return NUMBER;
	if(a=='+')
		return JIA;
	if(a=='-')
		return JIAN;
	if(a=='*')
		return CHENG;
	if(a=='/')
		return CHU;
	if(a=='|')
		return SHU;
	if(a=='&')
		return AND;
	if(a=='>')
		return DAYU;
	if(a=='<')
		return XIAOYU;
	if(a=='{')
		return ZUODAKUO;
	if(a=='}')
		return YOUDAKUO;
	if(a=='(')
		return ZUOXIAOKUO;
	if(a==')')
		return YOUXIAOKUO;
	if(a=='=')
		return DENG;
	if(a==';')
		return FEN;
	if(a==',')
		return DOU;
	if(a=='"')
		return YINHAO;
	return END;
}
void strcact()
{
	token=token+ch;
}

bool isletter()
{
	if(ch>='a'&&ch<='z'||ch>='A'&&ch<='Z')
		return true;
	else
		return false;
}

bool isdigit()
{
	if(ch>='0'&&ch<='9')
		return true;
	else
		return false;
}

void buildlist(int f)
{//��token�е�string�ͱ�ʾ���洢
	value a;
	a.valuename=token.c_str();
	a.type=f;
	a.id=0;
	add_value(&a);
}

int findreserve()
{
	int num=0;
	for(int i=0; i<token.length(); i++)
	{
		num=num+token.at(i)-'a';
	}
	for(int j=0; j<TIMES; j++)
		if(!reserve[(num+j)%N].compare(token))
			return (num+j)%N;
	return -1;//�Ǳ�����
}

int findusrvalue()
{
	const char* a ;
	a=token.c_str();
	return (int)find_value((char*)a);
}

void retract()
{
	chac=ch;
}

void output(int flag,string point)
{
	cout<<token<<"\tflag:"<<flag<<"  "<<point<<endl;
}

void error()
{
	cout<<"error"<<endl;
}