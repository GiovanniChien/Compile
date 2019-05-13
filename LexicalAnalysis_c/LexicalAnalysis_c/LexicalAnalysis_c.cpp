#include <iostream>
#include <string>
#include <vector>
#include <fstream>

using namespace std;

//基本数据类型
#define INT 1
#define FLOAT 2
#define CHAR 3
#define SHORT 4
#define DOUBLE 5
#define BOOL 6
//系统保留字
#define MAIN 7
#define RETURN 8
#define IF 9
#define ELSE 10
#define FOR 11
#define WHILE 12
#define VOID 13
//运算符
#define LT 14		  //<
#define LE 15		  //<=
#define GT 16		  //>
#define GE 17		  //>=
#define NE 18		  //!=
#define EQ 19		  //==
#define PLUSOP 20	 //+
#define MINUSOP 21	//-
#define MULTIPLYOP 22 //*
#define DIVIDIONOP 23 //'/'
#define ANDOP 24	  //&&
#define OROP 25		  //||
#define BITANDOP 26   //&
#define BITOROP 27	//|
#define BITNOTOP 28   //!
//分界符
#define LBB 29	//{
#define RBB 30	//}
#define LSB 31	//(
#define RSB 32	//)
#define ASSIGN 33 //=
#define SEMI 34   //;
#define COMMA 35  //,
//用户自定义标识符
#define ID 36
//常数标识符
#define NUM 37
//错误字符
#define EROOR 38

class LexicalAnalysis_c
{
  private:
	string token;
	string preStr;
	char ch;
	vector<string> keyWords;
	vector<string> dataTypes;
	ifstream in;
	vector<void *> constValueList; //常量表
	vector<void *> signalList;	 //标识符表
	vector<void *> keyList;		   //保留字表
  public:
	LexicalAnalysis_c();
	void readFile();
	void getsym();
	void getbc();
	void strcatc();
	bool isletter();
	bool isdigit();
	int reserve();
	void retract();
	void output(int category, void *point);
	void output(string token, int category, void *point);
	void scanner();
	void error();
	void *buildList(int i); //i=0表示构建一个数字常量表 i=1表示构建一个符号表 i=2表示构建一个保留字表
};

LexicalAnalysis_c::LexicalAnalysis_c()
{
	preStr = "";
	keyWords = {"main", "return", "if", "else", "for", "while", "void"};
	dataTypes = {"int", "float", "char", "short", "double", "bool"};
}

void LexicalAnalysis_c::readFile()
{
	/*string path;
	cin >> path;*/
	in.open("source.c");
}

void LexicalAnalysis_c::getsym()
{
	ch = in.get();
}

void LexicalAnalysis_c::getbc()
{
	while (ch == ' ' || ch == '\n')
	{
		ch = in.get();
	}
}

void LexicalAnalysis_c::strcatc()
{
	token += ch;
}

bool LexicalAnalysis_c::isletter()
{
	if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
		return true;
	return false;
}

bool LexicalAnalysis_c::isdigit()
{
	if (ch >= '0' && ch <= '9')
		return true;
	return false;
}

int LexicalAnalysis_c::reserve()
{
	for (int i = 0; i < keyWords.size(); i++)
	{
		if (keyWords[i] == token)
			return 7 + i;
	}
	for (int i = 0; i < dataTypes.size(); i++)
	{
		if (dataTypes[i] == token)
			return 1 + i;
	}
	return 0;
}

void LexicalAnalysis_c::retract()
{
	//token = token.substr(0, token.size() - 1);
	in.seekg(-1, ios::cur);
}

void LexicalAnalysis_c::output(int flag, void *point)
{
	cout << "Flag:" << flag << "  Point:" << point << endl;
}

void LexicalAnalysis_c::output(string token, int flag, void *point)
{
	cout << "Value:" << token << " Flag:" << flag << " Point:" << point << endl;
}

void *LexicalAnalysis_c::buildList(int i)
{
	void *p = new string(token);
	switch (i)
	{
	case 0: //常量表
		constValueList.push_back(p);
		break;
	case 1: //标识符表
		signalList.push_back(p);
		break;
	case 2: //保留字表
		signalList.push_back(p);
		break;
	}
	return p;
}

void LexicalAnalysis_c::scanner()
{
	token = "";
	preStr = "";
	readFile();
	getsym();
	getbc();
	while (true)
	{
		if (ch == EOF)
			break;
		if (isletter() || (ch == '_'))
		{
			strcatc();
			getsym();
			while (isletter() || isdigit() || (ch == '_'))
			{
				strcatc();
				getsym();
			}
			retract();
			int flag = reserve();
			if (!flag)
			{
				//标识符
				output(token, ID, buildList(1));
			}
			else
			{
				output(token, flag, buildList(2));
			}
			preStr = token;
		}
		else if (isdigit())
		{
			strcatc();
			getsym();
			while (isdigit())
			{
				strcatc();
				getsym();
			}
			retract();
			output(token, NUM, buildList(0));
			preStr = "";
			//token="";
		}
		else if (ch == '+')
			output(PLUSOP, NULL);
		else if (ch == '-')
			output(DIVIDIONOP, NULL);
		else if (ch == '*')
			output(MULTIPLYOP, NULL);
		else if (ch == '/')
			output(DIVIDIONOP, NULL);
		else if (ch == '|')
		{
			strcatc();
			getsym();
			if (ch == '|')
				output(OROP, NULL);
			else
			{
				retract();
				output(BITOROP, NULL);
			}
		}
		else if (ch == '&')
		{
			strcatc();
			getsym();
			if (ch == '&')
				output(ANDOP, NULL);
			else
			{
				retract();
				output(BITANDOP, NULL);
			}
		}
		else if (ch == '>')
		{
			strcatc();
			getsym();
			if (ch == '=')
				output(GE, NULL);
			else
			{
				retract();
				output(GT, NULL);
			}
		}
		else if (ch == '<')
		{
			strcatc();
			getsym();
			if (ch == '=')
				output(LE, NULL);
			else
			{
				retract();
				output(LT, NULL);
			}
		}
		else if (ch == '{')
			output(LBB, NULL);
		else if (ch == '}')
			output(RBB, NULL);
		else if (ch == '(')
			output(LSB, NULL);
		else if (ch == ')')
			output(RSB, NULL);
		else if (ch == '=')
		{
			strcatc();
			getsym();
			if (ch == '=')
				output(EQ, NULL);
			else
			{
				retract();
				output(ASSIGN, NULL);
			}
		}
		else if (ch == '!')
		{
			strcatc();
			getsym();
			if (ch == '=')
				output(NE, NULL);
			else
			{
				retract();
				output(BITNOTOP, NULL);
			}
		}
		else if (ch == ';')
			output(SEMI, NULL);
		else if (ch == ',')
			output(COMMA, NULL);
		else
			error();
		token = "";
		getsym();
		getbc();
	}
}

void LexicalAnalysis_c::error()
{
	cout << "ERROR" << endl;
}

int main()
{
	LexicalAnalysis_c analysis;
	analysis.scanner();
	system("pause");
	return 0;
}