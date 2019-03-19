#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <map>
#include "struct.h"

using namespace std;

extern void defWordsHash(string s[],int n,const char fileName[]);
extern int defWordsHashSearch(const string defWords[],const int n,const string find);  //hash查找
extern void HashLoadKeyWords(string keyWords[],const int n,const char fileName[]);
extern int HashSearch(string keyWords[],const int n,string find);
extern bool Judge(string variable);


string defw[26],keyWords[41];

vector<char> tChar;
vector<vector<int>> state;

bool inWord(char ch,string type)  //正在扫描一个词的过程中
{
	bool ok = ch == '_' || (ch>='A' && ch<='Z') || (ch>='a' && ch<='z') || (ch>='0' && ch<='9');
	if(type == " float" || type == " double")
	return ok || (ch=='.');
	else return ok;
}

bool checkVariable(const vector<char>& tChar,
				   const vector<vector<int>>& state,
				   string var)
{
	const char* pvar=var.c_str();
	int s = 0;
	for(int i=0;pvar[i];++i)
	{
		bool found=false;
		int j;
		for(j=0;j<tChar.size();++j)
		{
			if(tChar[j]==pvar[i]) {found=true;break;}
		}
		if(found==false) return false;
		s=state[s][j];
		if(s==-1) return false;
	}
	return true;
}

int checkVar(const Global& g,const Var& newVar,const int funflag)
{
	if(HashSearch(keyWords,41,newVar.name)!=-1) return -1; //变量定义与关键字冲突；

	//if(!checkVariable(tChar,state,newVar.name)) return 2; //变量定义不符要求
	if(!Judge(newVar.name)) return 2; //变量定义不符要求
	if(funflag != 0) //在函数内部
	{
		if(g.fun.size()>0)
		{

			Node<Var> *curp=g.fun[g.fun.size()-1].link->GetCurrentPointer();			
			for(vector<Var>::const_iterator it = curp->data.begin();it != curp->data.end(); it++)
				if((*it).name==newVar.name) return 0;
			Node<Var> *root=g.fun[g.fun.size()-1].link->GetRoot(); 
			if(root==curp) //考虑是否与参数表中的变量重复
			{
				for(vector<Var>::const_iterator it=g.fun[g.fun.size()-1].vTable.begin();
					it != g.fun[g.fun.size()-1].vTable.end() ; it++)
					if((*it).name==newVar.name) return 0;
			}

		}
		else //还没有函数
		{
			for(vector<Var>::const_iterator it=g.gVars.begin();it != g.gVars.end() ;it++)
					if((*it).name==newVar.name) return 0;	
		}
		return 1;
	}
	else
	{
		for(vector<Var>::const_iterator it=g.gVars.begin();it != g.gVars.end() ;it++)
					if((*it).name==newVar.name) return 0;	
		return 1;
	}
}

void display(Global& g)
{
	cout<<"全局变量：\n";
	for(int i=0;i<g.gVars.size();++i)
	{
		cout<<g.gVars[i].type+" ";
		cout<<g.gVars[i].name+" ";
		cout<<g.gVars[i].value<<endl;
	}
	cout<<"函数相关：\n";
	for(int i=0;i<g.fun.size();++i)
	{
		cout<<"函数返回类型："<<g.fun[i].type;
		cout<<" 函数名："<<g.fun[i].name;
		for(int j=0;j<g.fun[i].vTable.size();++j)
		{
			cout<<"参数表：";
			cout<<g.fun[i].vTable[j].type<<" ";
			cout<<g.fun[i].vTable[j].name<<" ";
			cout<<g.fun[i].vTable[j].value<<endl;
		}
		cout<<"变量\n";
		Node<Var> *r = g.fun[i].link->GetRoot();
		g.fun[i].link->PreOrder(r);
		cout<<"变量结束\n";
	}
	
}

void writeFile()
{
	ofstream out("state.txt");
	for(char ch='A';ch<='Z';ch++)
		out<<ch<<' ';
	for(char ch='a';ch<='z';ch++)
		out<<ch<<' ';
	out<<"_ ";
	for(char ch='0';ch<='9';ch++)
		out<<ch<<' ';
	out<<endl;
	for(int i=0;i<53;++i)
		out<<1<<' ';
	for(int i=0;i<10;++i)
		out<<-1<<' ';
	out<<endl;
	for(int i=0;i<63;++i)
		out<<1<<' ';
}

void loadState()
{
	ifstream inf("state.txt");
	if(!inf)
	{
		cout<<"找不到文件\'state.txt\'";
		exit(0);
	}
	int i=0;
	
	vector<int> temp;
	
	for(string s;getline(inf,s);++i)
	{
		//cout<<s<<endl;
		char ch;
		int index;
		if(i==0)
		{
			int j=0;
			for(istringstream sin(s);sin>>ch;)
				tChar.push_back(ch);
		}
		else
		{
			for(istringstream sin(s);sin>>index;)
				temp.push_back(index);
			state.push_back(temp);
			temp.clear();
		}
	}
	
	inf.close();
}

void main(){	
	
	defWordsHash(defw,26,"defineWords.txt");
	HashLoadKeyWords(keyWords,41,"keywords.txt");
	loadState();

	ifstream in("maintest.cpp");
	bool inNote = false; //在注释中扫描的标记
	int defining = 0; //变量或函数定义=-1,正在变量定义=1，或函数定义=2，或函数声明=3，或函数中的变量定义＝4,函数定义结束准备进函数体＝5
	string	type;	//现在的一个词
	string  preWord;	//刚才的一个词

	string  val;	//变量的值

	int defVar = 0;	//正在定义某一个变量过程中

	int forflag = -1;//扫描到for语句时的标志,forflag = 1,2,3 分别执行for中的第1，2，3条语句
	int line = 1; //扫描到哪一行
	
	int funflag = 0; //进入函数的标志

	Global g;
	int count = -1;
	for(string s;getline(in,s);++line)
	{
		const char *ps = s.c_str();
		const int len = s.length();
		
		for(int i = 0;i<len;++i)
		{
			char word[40];
			if(i+1<len && ps[i]=='/' && ps[i+1]=='/') break; //遇到 // 跳到下一行
			if(i+1<len && ps[i]=='/' && ps[i+1]=='*') {inNote = true;}	//遇到 /* 则将inNote标记为 true
			if(i+1<len && ps[i]=='*' && ps[i+1]=='/') {inNote = false;  i+=2;} //遇到 */ 则将inNote标记为 false
			if(inNote == false && i<len)
			{
				if(inWord(ps[i],type))
				{
					word[++count] = ps[i];
				}
				else {		//不在扫描词的过程中
					if(count>=0) {
						word[++count] = 0;
						string sword(word);
						if(sword != "else" && g.fun.size()>0)
						{
							Node<Var>* curp= g.fun[g.fun.size()-1].link->GetCurrentPointer();
							if(curp->children.size()>0)
							{								 
								Node<Var>* lastChildp = curp->children[curp->children.size()-1]; //这边编译器有 bug ? 该句和下一句不能合起来写
								string lastChildDes = lastChildp->description;
								int lastChidFlag = lastChildp->flag;
								if(lastChildDes == "if" || lastChildDes=="else" || lastChildDes =="elseif" && lastChidFlag!=0)
								{
									string curDes = g.fun[g.fun.size()-1].link->GetCurDes();
									int curflag = g.fun[g.fun.size()-1].link->GetCurFlag();
									if(curflag == 3 || (curDes==""&&curflag==0)) lastChildp->flag = 0; //当前在有{}的if,else if,else层，或最外层
									if(curflag == 4)
									{
										Node<Var> *f;
										do{
										f=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
										g.fun[g.fun.size()-1].link->SetCurrentPointer(f); //树的指针向上走一层
										}while(f->flag != 3&&(f->description == "if"||f->description == "else"||f->description == "elseif"));
									}
								}
							}
						}

						if(sword == "for" ) 
							forflag = 0;  //遇到 for 语句了
						else if(sword == "if")
						{
							//cout<<line<<" if"<<endl;
							string curDes = g.fun[g.fun.size()-1].link->GetCurDes();
							int curflag = g.fun[g.fun.size()-1].link->GetCurFlag();
							if(curDes == "" && curflag ==0 || curDes == "if" && (curflag ==2||curflag ==3||curflag ==4)) //遇到第一个if
							{
								g.fun[g.fun.size()-1].link->AddChild(); //添加该if层，并进入子层
								g.fun[g.fun.size()-1].link->SetCurDes("if"); //设 if层的 des ＝ if，且 flag ＝ 1
								g.fun[g.fun.size()-1].link->SetCurFlag(1);
							}

							if(curDes == "else" && curflag == -1)
							{
								g.fun[g.fun.size()-1].link->SetCurDes("elseif"); //else if的情况 设des ＝ elseif，且 flag ＝ 1
								g.fun[g.fun.size()-1].link->SetCurFlag(1);
							}
						}
						else if( sword == "else")
						{
							Node<Var>* curp= g.fun[g.fun.size()-1].link->GetCurrentPointer();
							Node<Var>* lastChildp = curp->children[curp->children.size()-1]; //这边编译器有 bug ? 该句和下一句不能合起来写
							string lastChildDes = lastChildp->description;
							int lastChidFlag = lastChildp->flag;
							if(lastChidFlag == 0 || lastChildDes=="else") cout<<"第"<<line<<"行：else 无 if 匹配"<<endl;
							
							g.fun[g.fun.size()-1].link->AddChild(); //添加该if层，并进入子层
							g.fun[g.fun.size()-1].link->SetCurDes("else"); //设 if层的 des ＝ if，且 flag ＝ 1
							g.fun[g.fun.size()-1].link->SetCurFlag(-1);

						}
						else if(defWordsHashSearch(defw,26,sword) != -1) //遇到定义的关键字
						{
							if(defining == 2 || defining == 4) defining = 4; //在函数
							else if(funflag == 0 && defining == 0) defining = -1;	//标记设置为变量或函数定义过程中
							else 
							{
								defining = 1;		//在函数内部，此时是变量定义										
							}
							defVar = 1;	
							type = type + " " + sword;
						}
						else if(defining == -1)
						{
							preWord = sword;	//此时preword记录的是函数名或变量名
						}
						else if(defining == 1 || defining == 4)
						{	
							if(defVar == 1) preWord = sword;
							else if(defVar == 2) val = sword;
						}						
						else if(defining == 2) 
						{
							//	defining = 4;
							// 正在函数中的变量定义
						}
						else if(defining == 4)
						{
						
						}

					}
					count = -1;
					if((ps[i] == ','||ps[i] == ';' || ps[i] == ')') && (defining == 1 || defining == -1 || defining == 4))//在变量定义过程中(可以是任意的变量)
					{
						Var var;
						var.type = type;
						var.name = preWord;
						var.value = val;
						
						if(funflag == 0 && (defining == 1 || defining == -1)) //为全局变量
						{
							int result=checkVar(g,var,0);
							if(result==1)	
							g.gVars.push_back(var);
							else if(result==0)cout<<"第"<<line<<"行：变量'"<<var.name<<"'重复定义；\n";
							else if(result==-1)cout<<"第"<<line<<"行：变量'"<<var.name<<"'与关键字冲突定义；\n";
							else if(result==2)cout<<"第"<<line<<"行：变量'"<<var.name<<"'定义错误；\n";
						}
						else if(defining == 4 && g.fun.size() > 0) //函数参数表
						{
							int result=checkVar(g,var,1);
							if(result==1)			
								g.fun[g.fun.size()-1].vTable.push_back(var);
							else if(result==0)cout<<"第"<<line<<"行：变量'"<<var.name<<"'重复定义；\n";
							else if(result==-1)cout<<"第"<<line<<"行：变量'"<<var.name<<"'与关键字冲突定义；\n";
							else if(result==2)cout<<"第"<<line<<"行：变量'"<<var.name<<"'定义错误；\n";
						}
						else if(g.fun.size() > 0)
						{
							int result=checkVar(g,var,1);
							if(result==1)									
								g.fun[g.fun.size()-1].link->AddVariable(var);								
							else if(result==0)cout<<"第"<<line<<"行：变量'"<<var.name<<"'重复定义；\n";
							else if(result==-1)cout<<"第"<<line<<"行：变量'"<<var.name<<"'与关键字冲突定义；\n";
							else if(result==2)cout<<"第"<<line<<"行：变量'"<<var.name<<"'定义错误；\n";
						}
						preWord = "";
						val = "";
						defVar = 1;
					}
					
					string curDes = ""; 
					int curflag = 0;
					if(g.fun.size()>0)
					{
						curDes = g.fun[g.fun.size()-1].link->GetCurDes();
						curflag = g.fun[g.fun.size()-1].link->GetCurFlag();
					}

					if((curDes == "if" || curDes == "elseif")&& curflag == 1) //if 或else if 的右括号扫描完毕
					{
						if(ps[i] == ')')
						{g.fun[g.fun.size()-1].link->SetCurFlag(2);curflag=2;}
					}
					else if((curDes == "else" && curflag == -1) || 
						(curDes == "if" && curflag == 2) || 
						(curDes == "elseif" && curflag ==2)) {
							if(ps[i] == '{')//if,else if,else,下{...}可能不止一条语句
							{g.fun[g.fun.size()-1].link->SetCurFlag(3);curflag = 3;}
							else if(ps[i] != ' '&& ps[i] != 9)//flag = 4; if,else if,else,下只有一条语句
							{g.fun[g.fun.size()-1].link->SetCurFlag(4);curflag = 4;}
					}
					else if((curDes == "if"||curDes == "elseif"||curDes == "else")&& ps[i] == ';')
					{
						if(curflag ==4 )  //无{}的if,else if,else 遇到;了回退一层
						{
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //树的指针向上走一层
						}
						if(curDes == "else") //若此时属于 else 层,
						{
							curflag = g.fun[g.fun.size()-1].link->GetCurFlag(); //flag == 4,即没有{},则再退一层
							if(curflag == 4 || curflag == 2){
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //树的指针向上走一层
							}
						}
					}


					if(ps[i] == ';') {
						defining = 0;  //一句话结束，定义结束
						type = "";
						defVar = 0;
						if(forflag != -1&&forflag!=4) ++forflag;
						if(forflag==4)
						{
							forflag=-1;
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //树的指针向上走一层
						}
					}
					else if(ps[i] == ',')
					{
						if(defining == 4)type = "";
					}
					else if(ps[i] == '{')
					{
						if(forflag == 4) forflag = -1;
						else if(funflag != 0 && curDes!="if" && curDes!="elseif" && curDes!="else") 
						{
							if(g.fun.size()>0)
							{
								g.fun[g.fun.size()-1].link->AddChild();
							}
						}
						if(defining != 5)++funflag;
						//
					}
					else if(ps[i] == '}')
					{
						if(g.fun.size()>0)
						{
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //树的指针向上走一层
						}
						--funflag;
					}
					else if(ps[i] == '(' && forflag == 0) 
					{
						++forflag;
						g.fun[g.fun.size()-1].link->AddChild(); //添加一子层，并进入子层
					}
					else if(ps[i] == ')' && forflag == 3)
					{
						forflag = 4; //for语句的) 标记forflag = 4 考虑下面的{}
					}
					if(ps[i] == '(' && defining == -1) 
					{
						defining = 2;
						Func function;
						function.type = type;	//函数返回类型
						function.name = preWord; //函数名
						Node<Var> *tVar = new Node<Var>;
						function.link= new Tree<Var>(tVar);
						g.fun.push_back(function); 

						type = "";
						
					}
					else if(ps[i] == ')' && (defining == 4 || defining == 2))
					{
						defining = 5; //函数定义结束准备进函数体	
						type="";
					}
					else if(ps[i] == '{' && defining == 5)
					{
						if(g.fun.size()>0)
						{
							g.fun[g.fun.size()-1].link->AddChild(); //添加一子层，并进入子层
						}
						defining = 0;
						++funflag;
					}
					else if(ps[i] == '=' && (defining == -1 || defining == 1))
					{
						defining = 1;
						defVar = 2;
					}
					else if(ps[i] == '=' && defining == 4)
					{
						defVar = 2;
					}

				}
			}
		}
	}
	in.close();
	display(g);
	
	//writeFile();
	
}