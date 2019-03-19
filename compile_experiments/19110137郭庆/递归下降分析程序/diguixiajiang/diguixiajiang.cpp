#include <iostream>
using namespace std;

char a[20]="a*b+(c+d)*e+a";
char sym;
int i=0;
int flag=1;
void error();
void E();
void E1();
void T();
void T1();
void F();
void getsym();
bool IsLetter(char x);

int main()
{
	getsym();
	E();
	if (flag == 1)
	cout<<"成功识别串:"<<a<<endl;
	return 0;
}

void error()
{
	cout<<"error:第"<<i-1<<"字符处出现错误"<<endl;
	flag=0;
}

bool IsLetter(char x)
{
    if (x >= 'a' && x <= 'z')
		return true;
	return false;
}
void getsym()  
{
    sym=a[i];
	i++;
}

void E()
{
    T();
	E1();
}

void E1()
{
	if (sym == '+'||sym == '-')
	{
		getsym();
	    T();
	    E1();
	}
}

void T()
{
    F();
	T1();
}


void T1()
{
	if (sym == '*'||sym == '/')
	{
	    getsym();
        F();
		T1();
	}
}

void F()
{
	if (IsLetter(sym))
		getsym();
	else
	{
		if (sym =='(')
		{
		    getsym();
			E();
			if (sym == ')')
			{
		        getsym();
			}
		    else
			{
		        error();
			}
		}
		else
		{
		    error();
		}
		
	}
}