//06060533 秦瑞琳 2009年5月17日21:35:11 OPG+优先关系矩阵
//				  2009年5月22日12:52:52	表达式求值
//编译原理		  
//OPG+优先关系矩阵+表达式求值
//识别程序
//假设VN为A-Z，1个字符，VT为键盘能输入的非大写字母
//程序中出现的符号含义说明：VN：非终结符，VT：终结符，ss：符号串
//程序认为文件中第一个合法规则的首字符为起始符号
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <set>
#include <stack>
using namespace std;
//************************Global Data Structure:************************//
//size of PR Matrix
int MatrixSize;
//Rule
struct Rule 
{
	int VN;
	vector<int> ss;
};
//Grammer
vector<Rule*> grammer;
//VN List
vector<char> VNList;
//VT List
vector<char> VTList;
//VN's FirstTerm
set<int>* FirstTerms;
//VN's LastTerm
set<int>* LastTerms;
//Term Copy Struct
class TermCopy//VNlarge's FirstTerm/LastTerm set contains VNsmall's
{
public:
	int VNsmall;
	int VNlarge;
	TermCopy(int s,int l){VNsmall=s;VNlarge=l;}
};
//Term Copy Table:used in step 2&3 when getting FirstTerm/LastTerm
vector<TermCopy*> TermCopies;
//PR Matrix|0:=|1:<|2:>|-1:null|
int** M;
//Prior Function
int* f;
int* g;
//************************Step1:Read Grammer From File and Save in data structure************************//
//print a rule
void PrintRule(const Rule*& rule)
{
	cout<<VNList[rule->VN-100]<<"::=";
	if (rule->ss.size()==0)
		cout<<'@';
	else
		for (int j=0;j<rule->ss.size();j++)
		{
			if(rule->ss[j]>=100)
				cout<<VNList[rule->ss[j]-100];
			else
				cout<<VTList[rule->ss[j]];
		}
}
//print grammer
void PrintGrammer()
{
	cout<<"Grammer:\n";
	for (int i=0;i<grammer.size();i++)
	{
		PrintRule(grammer[i]);
		cout<<endl;
	}
}
//Find a charactor from Vector<char>
int FindInVector(const vector<char>& v,char c)
{
	for (int i=0;i<v.size();i++)
	{
		if (v[i]==c)
			return i;
	}
	return -1;
}
//Analyse one VN or one VT
int FindInList(vector<char>& v,char c)
{
	int pos=FindInVector(v,c);
	if (pos==-1)
	{
		v.push_back(c);
		pos=v.size()-1;
	}
	return pos;
}
//Analyse one Rule
void AnalyseRule(const string& rule)
{
	int VNpos=FindInList(VNList,rule[0])+100;
	//deal with '|'
	int pos=4,prevOR=4;//start of right part 
	vector<string> ss;//a rule can have many right parts
	while(1)
	{
		int pos=rule.find('|',prevOR);
		if (pos==-1)
		{
			ss.push_back(rule.substr(prevOR,rule.size()-prevOR));
			break;
		}
		else
		{
			ss.push_back(rule.substr(prevOR,pos-prevOR));
			prevOR=pos+1;
		}
	}
	//construct rule and add to grammer
	for (int i=0;i<ss.size();i++)
	{
		string s=ss[i];
		Rule* pRule=new Rule;
		pRule->VN=VNpos;
		if (s!="@")
			for (int j=0;j<s.size();j++)
			{
				
				if(s[j]>='A'&&s[j]<='Z')// is VN
					pos=FindInList(VNList,s[j])+100;//ID of VN is the pos in vector adding 100,distinguish with VT 
				else
					pos=FindInList(VTList,s[j]);
				pRule->ss.push_back(pos);
			}
			grammer.push_back(pRule);
	}
}
//If one Rule is valid
bool IsValidRule(const string& rule)
{
	if (rule.size()<5||rule[0]<'A'||rule[0]>'Z'||rule[rule.size()-1]=='|'||rule.find("||",0)!=string::npos||rule.substr(1,3)!="::=")
		return false;//if length<5 or first character is not VN or the last character is '|' or
	//rule contains "||" or rule[1...3] is not ::=,then the rule is not valid
	return true;
}
//************************Tool************************//
//insert s2's items into s1
void SetUnion(set<int>& s1,set<int>& s2)//can't use const???
{
	set<int>:: iterator it;
	for (it=s2.begin();it!=s2.end();it++)
		s1.insert(*it);
}
//Judge: if s2 is the subset of s1
bool IsSubSet(set<int>& s1,set<int>& s2)
{
	set<int>::iterator it;
	for (it=s2.begin();it!=s2.end();it++)
	{
		if (s1.find(*it)==s1.end())
			return false;
	}
	return true;
}
//************************Step2:Get FirstTerms************************//
//Use an item of Term Copy Table
bool UseFirstTermCopy(TermCopy* pTermCopy)
{
	bool flag=false;
	if(!IsSubSet(FirstTerms[pTermCopy->VNlarge-100],FirstTerms[pTermCopy->VNsmall-100]))
	{
		flag=true;
		SetUnion(FirstTerms[pTermCopy->VNlarge-100],FirstTerms[pTermCopy->VNsmall-100]);
	}
	return flag;	
}
//Use First Term Copy Table, return value:if we should Use First Term Copy Table once again,
bool UseFirstTermCopies()
{
	bool flag=false;
	for (int i=0;i<TermCopies.size();i++)
	{
		if (UseFirstTermCopy(TermCopies[i]))
			flag=true;
	}
	return flag;
}
void GetFirstTerms()
{
	FirstTerms=new set<int>[VNList.size()];
	for (int i=0;i<grammer.size();i++)//for each rule,find the first VN and VT
	{
		vector<int> ss=grammer[i]->ss;
		int vn=grammer[i]->VN;
		//don't need to loop the rule's ss,just consider some situations
		if (ss[0]<100)//first charactor is VT(no null rule)
		{
			FirstTerms[vn-100].insert(ss[0]);
			continue;
		}
		else if(ss[0]!=vn)
			TermCopies.push_back(new TermCopy(ss[0],vn));
		//now it means ss[0] isVN,because of OPG so ss[1] is VT
		if (ss.size()>=2)
		{
			//if (ss[1]<100)
			FirstTerms[vn-100].insert(ss[1]);
		}
	}
	while (UseFirstTermCopies());
/*	set<int>::iterator it;
	for (i=0;i<VNList.size();i++)
	{
		for (it=FirstTerms[i].begin();it!=FirstTerms[i].end();it++)
		{
			cout<<*it<<" ";

		}
		cout<<endl;
	}
*/
}
//************************Step3:Get LastTerms************************//
//Use an item of Term Copy Table
bool UseLastTermCopy(TermCopy* pTermCopy)
{
	bool flag=false;
	if(!IsSubSet(LastTerms[pTermCopy->VNlarge-100],LastTerms[pTermCopy->VNsmall-100]))
	{
		flag=true;
		SetUnion(LastTerms[pTermCopy->VNlarge-100],LastTerms[pTermCopy->VNsmall-100]);
	}
	return flag;	
}
//Use Last Term Copy Table, return value:if we should Use Last Term Copy Table once again,
bool UseLastTermCopies()
{
	bool flag=false;
	for (int i=0;i<TermCopies.size();i++)
	{
		if (UseLastTermCopy(TermCopies[i]))
			flag=true;
	}
	return flag;
}
void GetLastTerms()
{
	LastTerms=new set<int>[VNList.size()];
	for (int i=0;i<grammer.size();i++)//for each rule,find the first VN and VT
	{
		vector<int> ss=grammer[i]->ss;
		int vn=grammer[i]->VN;
		int size=ss.size();
		//don't need to loop the rule's ss,just consider some situations
		if (ss[size-1]<100)//last charactor is VT(no null rule)
		{
			LastTerms[vn-100].insert(ss[size-1]);
			continue;
		}
		else if(ss[size-1]!=vn)
			TermCopies.push_back(new TermCopy(ss[size-1],vn));
		//now it means ss[size-1] isVN,because of OPG so ss[size-2] is VT
		if (size>=2)
		{
			//if (ss[1]<100)
			LastTerms[vn-100].insert(ss[size-2]);
		}
	}
	while (UseLastTermCopies());
/*	set<int>::iterator it;
	for (i=0;i<VNList.size();i++)
	{
		for (it=LastTerms[i].begin();it!=LastTerms[i].end();it++)
		{
			cout<<*it<<" ";
			
		}
		cout<<endl;
	}
*/
}
//************************Step4:Get PR Matrix************************//
void InitMatrix()
{
	VTList.push_back('#');
	MatrixSize=VTList.size();
	M=new int* [MatrixSize];
	for (int i=0;i<MatrixSize;i++)
		M[i]=new int[MatrixSize];
	for (i=0;i<MatrixSize;i++)
		for (int j=0;j<MatrixSize;j++)
			M[i][j]=-1;
	for (i=0;i<MatrixSize-1;i++)
		M[MatrixSize-1][i]=1;
	for (i=0;i<MatrixSize-1;i++)
		M[i][MatrixSize-1]=2;
}
void FillSmallOP(int vt,int vn)//VT<FirstTerm(VN)
{
	set<int>::iterator it;
	for (it=FirstTerms[vn-100].begin();it!=FirstTerms[vn-100].end();it++)
		M[vt][*it]=1;
}
void FillLargeOP(int vn,int vt)//LastTerm(VN)>VT
{
	set<int>::iterator it;
	for (it=LastTerms[vn-100].begin();it!=LastTerms[vn-100].end();it++)
		M[*it][vt]=2;
}
void ConstructMatrix()
{
	InitMatrix();
	for (int i=0;i<grammer.size();i++)//for each rule
	{
		vector<int> ss=grammer[i]->ss;
		if (ss.size()>=2)//ignore Single Rule
		{
			int p=0,q=1;
			while (q<ss.size())
			{
				if (ss[p]<100&&ss[q]>=100)
					FillSmallOP(ss[p],ss[q]);//VT<FirstTerm(VN)
				else if(ss[p]>=100&&ss[q]<100)
					FillLargeOP(ss[p],ss[q]);//LastTerm(VN)>VT
				else if(ss[p]<100&&ss[q]<100)
					M[ss[p]][ss[q]]=0;//Fill Equal Operator	
				if (p!=0&&ss[p-1]<100&&ss[p]>=100&&ss[q]<100)
					M[ss[p-1]][ss[q]]=0;//Fill Equal Operator
				p++;
				q++;
			}
		}
	}
}
char ToOperator(int i)
{
	switch(i)
	{
	case 0:return '=';
	case 1:return '<';
	case 2:return '>';
	case -1:return '\0';
	}
}
void PrintMatrix()
{
	cout<<"PR Matrix:\n"<<"	";
	for (int i=0;i<MatrixSize;i++)
		cout<<VTList[i]<<"	";
	cout<<endl;
	for (i=0;i<MatrixSize;i++)
	{
		cout<<VTList[i]<<"	";
		for (int j=0;j<MatrixSize;j++)
			cout<<ToOperator(M[i][j])<<"	";
		cout<<endl;
	}
}
//************************Step5:Get Prior Function************************//
bool TestFunction()
{
	for (int i=0;i<MatrixSize;i++)
		for (int j=0;j<MatrixSize;j++)
			if (M[i][j]==0&&f[i]!=g[j]||M[i][j]==1&&f[i]>=g[j]||M[i][j]==2&&f[i]<=g[j])
				return false;
	return true;
}
void  GetPriorFunction()
{
	f=new int[MatrixSize];
	g=new int[MatrixSize];
	for (int i=0;i<MatrixSize;i++)
		f[i]=g[i]=1;
	while (!TestFunction())
	{
		for (int i=0;i<MatrixSize;i++)
			for (int j=0;j<MatrixSize;j++)
			{
				if (M[i][j]==0&&f[i]!=g[j])
					f[i]=g[j]=(f[i]>g[j])?f[i]:g[j];
				else if (M[i][j]==1&&f[i]>=g[j])
					g[j]=f[i]+1;
				else if (M[i][j]==2&&f[i]<=g[j])
					f[i]=g[j]+1;
			}
	}
	cout<<"Prior Function:\nf[i]:";
	for (i=0;i<MatrixSize;i++)
		cout<<f[i]<<" ";
	cout<<endl<<"g[i]:";
	for (int j=0;j<MatrixSize;j++)
		cout<<g[j]<<" ";
	cout<<endl;
}
//************************Step6:Calculate Expression************************//
float Operate(float a,char theta,float b)
{
	switch(theta)
	{
	case '+':return a+b;
	case '-':return a-b;
	case '*':return a*b;
	case '/':if (b==0){cout<<"devided by zero!";exit(0);}else return a/b;
	}
}
void CalExpression()
{
	//not the identification program,only calculate the value,so not common
	while(1)
	{
		cout<<"input an expression:\n";
		string s;
		cin>>s;
		s=s+"#";
		stack<char> OPTR;
		stack<float> OPND;
		OPTR.push('#');
		int p=0;
		char theta;
		float b,a;
		bool flag=true;
		string num;
		//
		while(s[p]!='#'||OPTR.top()!='#'||num!="")
		{
			if(flag==false)break;
			if (s[p]>='0'&&s[p]<='9'||s[p]=='.')//number 10#
			{
				num+=s[p];
				p++;
			}
			else if (num!="")
			{
				int m=num.find('.',0);
				if (m!=-1&&num.find('.',m+1)!=-1)
				{
					cout<<"too many '.'!\n";
					flag=false;
					break;
				}
				OPND.push((float)atof(num.c_str()));
				num="";
			}
			else //non-number
			{
				int pos=FindInVector(VTList,s[p]);
				if (pos==-1) {cout<<"find unknown character "<<s[p];flag=false;}
				else
				{
					switch (M[FindInVector(VTList,OPTR.top())][pos])
					{
					case 0:if (OPTR.top()!='('||s[p]!=')'){cout<<"Brackets not match!\n";flag=false;break;} OPTR.pop();p++;break;
					case 1:OPTR.push(s[p]);p++;break;
					case 2:
						{
						theta=OPTR.top();OPTR.pop();
						if (OPND.size()<2){cout<<"character " <<s[p]<<" is a spilth！";flag=false;break;}
						b=OPND.top();OPND.pop();
						a=OPND.top();OPND.pop();
						OPND.push(Operate(a,theta,b));
						break;	
						}
					default:cout<<"expression is invalid!";flag=false;break;
					}
				}
			}
		}
		if (flag==true&&!OPND.empty())
			cout<<OPND.top();
		cout<<endl;
	}
}
//************************main:entrace************************//
void main()
{
	//Step1:Construct Grammer
	ifstream is("grammer.txt");
	while (!is.eof())
	{
		string rule;
		is>>rule;
		if(IsValidRule(rule))
			AnalyseRule(rule);
		else
			cout<<"Rule:"<<rule<<" is invalid!it will be ignored!\n";
	}
	PrintGrammer();
	//Step2:Get First Terms
	GetFirstTerms();
	//Step3:Get Last Terms
	TermCopies.clear();//ignore mem leak
	GetLastTerms();
	//Step4:Construct PR Matrix
	ConstructMatrix();
	PrintMatrix();
	//Step5:Get Prior Function
	GetPriorFunction();
	//Step5.5:indenfy input string,no
	//Step6:Cal Expression
	CalExpression();
}
