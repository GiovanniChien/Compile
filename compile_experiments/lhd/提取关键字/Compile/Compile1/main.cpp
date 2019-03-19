#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <map>
#include "struct.h"

using namespace std;

extern void defWordsHash(string s[],int n,const char fileName[]);
extern int defWordsHashSearch(const string defWords[],const int n,const string find);  //hash����
extern void HashLoadKeyWords(string keyWords[],const int n,const char fileName[]);
extern int HashSearch(string keyWords[],const int n,string find);
extern bool Judge(string variable);


string defw[26],keyWords[41];

vector<char> tChar;
vector<vector<int>> state;

bool inWord(char ch,string type)  //����ɨ��һ���ʵĹ�����
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
	if(HashSearch(keyWords,41,newVar.name)!=-1) return -1; //����������ؼ��ֳ�ͻ��

	//if(!checkVariable(tChar,state,newVar.name)) return 2; //�������岻��Ҫ��
	if(!Judge(newVar.name)) return 2; //�������岻��Ҫ��
	if(funflag != 0) //�ں����ڲ�
	{
		if(g.fun.size()>0)
		{

			Node<Var> *curp=g.fun[g.fun.size()-1].link->GetCurrentPointer();			
			for(vector<Var>::const_iterator it = curp->data.begin();it != curp->data.end(); it++)
				if((*it).name==newVar.name) return 0;
			Node<Var> *root=g.fun[g.fun.size()-1].link->GetRoot(); 
			if(root==curp) //�����Ƿ���������еı����ظ�
			{
				for(vector<Var>::const_iterator it=g.fun[g.fun.size()-1].vTable.begin();
					it != g.fun[g.fun.size()-1].vTable.end() ; it++)
					if((*it).name==newVar.name) return 0;
			}

		}
		else //��û�к���
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
	cout<<"ȫ�ֱ�����\n";
	for(int i=0;i<g.gVars.size();++i)
	{
		cout<<g.gVars[i].type+" ";
		cout<<g.gVars[i].name+" ";
		cout<<g.gVars[i].value<<endl;
	}
	cout<<"������أ�\n";
	for(int i=0;i<g.fun.size();++i)
	{
		cout<<"�����������ͣ�"<<g.fun[i].type;
		cout<<" ��������"<<g.fun[i].name;
		for(int j=0;j<g.fun[i].vTable.size();++j)
		{
			cout<<"������";
			cout<<g.fun[i].vTable[j].type<<" ";
			cout<<g.fun[i].vTable[j].name<<" ";
			cout<<g.fun[i].vTable[j].value<<endl;
		}
		cout<<"����\n";
		Node<Var> *r = g.fun[i].link->GetRoot();
		g.fun[i].link->PreOrder(r);
		cout<<"��������\n";
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
		cout<<"�Ҳ����ļ�\'state.txt\'";
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
	bool inNote = false; //��ע����ɨ��ı��
	int defining = 0; //������������=-1,���ڱ�������=1����������=2����������=3�������еı������壽4,�����������׼���������壽5
	string	type;	//���ڵ�һ����
	string  preWord;	//�ղŵ�һ����

	string  val;	//������ֵ

	int defVar = 0;	//���ڶ���ĳһ������������

	int forflag = -1;//ɨ�赽for���ʱ�ı�־,forflag = 1,2,3 �ֱ�ִ��for�еĵ�1��2��3�����
	int line = 1; //ɨ�赽��һ��
	
	int funflag = 0; //���뺯���ı�־

	Global g;
	int count = -1;
	for(string s;getline(in,s);++line)
	{
		const char *ps = s.c_str();
		const int len = s.length();
		
		for(int i = 0;i<len;++i)
		{
			char word[40];
			if(i+1<len && ps[i]=='/' && ps[i+1]=='/') break; //���� // ������һ��
			if(i+1<len && ps[i]=='/' && ps[i+1]=='*') {inNote = true;}	//���� /* ��inNote���Ϊ true
			if(i+1<len && ps[i]=='*' && ps[i+1]=='/') {inNote = false;  i+=2;} //���� */ ��inNote���Ϊ false
			if(inNote == false && i<len)
			{
				if(inWord(ps[i],type))
				{
					word[++count] = ps[i];
				}
				else {		//����ɨ��ʵĹ�����
					if(count>=0) {
						word[++count] = 0;
						string sword(word);
						if(sword != "else" && g.fun.size()>0)
						{
							Node<Var>* curp= g.fun[g.fun.size()-1].link->GetCurrentPointer();
							if(curp->children.size()>0)
							{								 
								Node<Var>* lastChildp = curp->children[curp->children.size()-1]; //��߱������� bug ? �þ����һ�䲻�ܺ�����д
								string lastChildDes = lastChildp->description;
								int lastChidFlag = lastChildp->flag;
								if(lastChildDes == "if" || lastChildDes=="else" || lastChildDes =="elseif" && lastChidFlag!=0)
								{
									string curDes = g.fun[g.fun.size()-1].link->GetCurDes();
									int curflag = g.fun[g.fun.size()-1].link->GetCurFlag();
									if(curflag == 3 || (curDes==""&&curflag==0)) lastChildp->flag = 0; //��ǰ����{}��if,else if,else�㣬�������
									if(curflag == 4)
									{
										Node<Var> *f;
										do{
										f=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
										g.fun[g.fun.size()-1].link->SetCurrentPointer(f); //����ָ��������һ��
										}while(f->flag != 3&&(f->description == "if"||f->description == "else"||f->description == "elseif"));
									}
								}
							}
						}

						if(sword == "for" ) 
							forflag = 0;  //���� for �����
						else if(sword == "if")
						{
							//cout<<line<<" if"<<endl;
							string curDes = g.fun[g.fun.size()-1].link->GetCurDes();
							int curflag = g.fun[g.fun.size()-1].link->GetCurFlag();
							if(curDes == "" && curflag ==0 || curDes == "if" && (curflag ==2||curflag ==3||curflag ==4)) //������һ��if
							{
								g.fun[g.fun.size()-1].link->AddChild(); //��Ӹ�if�㣬�������Ӳ�
								g.fun[g.fun.size()-1].link->SetCurDes("if"); //�� if��� des �� if���� flag �� 1
								g.fun[g.fun.size()-1].link->SetCurFlag(1);
							}

							if(curDes == "else" && curflag == -1)
							{
								g.fun[g.fun.size()-1].link->SetCurDes("elseif"); //else if����� ��des �� elseif���� flag �� 1
								g.fun[g.fun.size()-1].link->SetCurFlag(1);
							}
						}
						else if( sword == "else")
						{
							Node<Var>* curp= g.fun[g.fun.size()-1].link->GetCurrentPointer();
							Node<Var>* lastChildp = curp->children[curp->children.size()-1]; //��߱������� bug ? �þ����һ�䲻�ܺ�����д
							string lastChildDes = lastChildp->description;
							int lastChidFlag = lastChildp->flag;
							if(lastChidFlag == 0 || lastChildDes=="else") cout<<"��"<<line<<"�У�else �� if ƥ��"<<endl;
							
							g.fun[g.fun.size()-1].link->AddChild(); //��Ӹ�if�㣬�������Ӳ�
							g.fun[g.fun.size()-1].link->SetCurDes("else"); //�� if��� des �� if���� flag �� 1
							g.fun[g.fun.size()-1].link->SetCurFlag(-1);

						}
						else if(defWordsHashSearch(defw,26,sword) != -1) //��������Ĺؼ���
						{
							if(defining == 2 || defining == 4) defining = 4; //�ں���
							else if(funflag == 0 && defining == 0) defining = -1;	//�������Ϊ�����������������
							else 
							{
								defining = 1;		//�ں����ڲ�����ʱ�Ǳ�������										
							}
							defVar = 1;	
							type = type + " " + sword;
						}
						else if(defining == -1)
						{
							preWord = sword;	//��ʱpreword��¼���Ǻ������������
						}
						else if(defining == 1 || defining == 4)
						{	
							if(defVar == 1) preWord = sword;
							else if(defVar == 2) val = sword;
						}						
						else if(defining == 2) 
						{
							//	defining = 4;
							// ���ں����еı�������
						}
						else if(defining == 4)
						{
						
						}

					}
					count = -1;
					if((ps[i] == ','||ps[i] == ';' || ps[i] == ')') && (defining == 1 || defining == -1 || defining == 4))//�ڱ������������(����������ı���)
					{
						Var var;
						var.type = type;
						var.name = preWord;
						var.value = val;
						
						if(funflag == 0 && (defining == 1 || defining == -1)) //Ϊȫ�ֱ���
						{
							int result=checkVar(g,var,0);
							if(result==1)	
							g.gVars.push_back(var);
							else if(result==0)cout<<"��"<<line<<"�У�����'"<<var.name<<"'�ظ����壻\n";
							else if(result==-1)cout<<"��"<<line<<"�У�����'"<<var.name<<"'��ؼ��ֳ�ͻ���壻\n";
							else if(result==2)cout<<"��"<<line<<"�У�����'"<<var.name<<"'�������\n";
						}
						else if(defining == 4 && g.fun.size() > 0) //����������
						{
							int result=checkVar(g,var,1);
							if(result==1)			
								g.fun[g.fun.size()-1].vTable.push_back(var);
							else if(result==0)cout<<"��"<<line<<"�У�����'"<<var.name<<"'�ظ����壻\n";
							else if(result==-1)cout<<"��"<<line<<"�У�����'"<<var.name<<"'��ؼ��ֳ�ͻ���壻\n";
							else if(result==2)cout<<"��"<<line<<"�У�����'"<<var.name<<"'�������\n";
						}
						else if(g.fun.size() > 0)
						{
							int result=checkVar(g,var,1);
							if(result==1)									
								g.fun[g.fun.size()-1].link->AddVariable(var);								
							else if(result==0)cout<<"��"<<line<<"�У�����'"<<var.name<<"'�ظ����壻\n";
							else if(result==-1)cout<<"��"<<line<<"�У�����'"<<var.name<<"'��ؼ��ֳ�ͻ���壻\n";
							else if(result==2)cout<<"��"<<line<<"�У�����'"<<var.name<<"'�������\n";
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

					if((curDes == "if" || curDes == "elseif")&& curflag == 1) //if ��else if ��������ɨ�����
					{
						if(ps[i] == ')')
						{g.fun[g.fun.size()-1].link->SetCurFlag(2);curflag=2;}
					}
					else if((curDes == "else" && curflag == -1) || 
						(curDes == "if" && curflag == 2) || 
						(curDes == "elseif" && curflag ==2)) {
							if(ps[i] == '{')//if,else if,else,��{...}���ܲ�ֹһ�����
							{g.fun[g.fun.size()-1].link->SetCurFlag(3);curflag = 3;}
							else if(ps[i] != ' '&& ps[i] != 9)//flag = 4; if,else if,else,��ֻ��һ�����
							{g.fun[g.fun.size()-1].link->SetCurFlag(4);curflag = 4;}
					}
					else if((curDes == "if"||curDes == "elseif"||curDes == "else")&& ps[i] == ';')
					{
						if(curflag ==4 )  //��{}��if,else if,else ����;�˻���һ��
						{
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //����ָ��������һ��
						}
						if(curDes == "else") //����ʱ���� else ��,
						{
							curflag = g.fun[g.fun.size()-1].link->GetCurFlag(); //flag == 4,��û��{},������һ��
							if(curflag == 4 || curflag == 2){
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //����ָ��������һ��
							}
						}
					}


					if(ps[i] == ';') {
						defining = 0;  //һ�仰�������������
						type = "";
						defVar = 0;
						if(forflag != -1&&forflag!=4) ++forflag;
						if(forflag==4)
						{
							forflag=-1;
							Node<Var> *t=g.fun[g.fun.size()-1].link->GetCurrentPointer()->parent;
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //����ָ��������һ��
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
							g.fun[g.fun.size()-1].link->SetCurrentPointer(t); //����ָ��������һ��
						}
						--funflag;
					}
					else if(ps[i] == '(' && forflag == 0) 
					{
						++forflag;
						g.fun[g.fun.size()-1].link->AddChild(); //���һ�Ӳ㣬�������Ӳ�
					}
					else if(ps[i] == ')' && forflag == 3)
					{
						forflag = 4; //for����) ���forflag = 4 ���������{}
					}
					if(ps[i] == '(' && defining == -1) 
					{
						defining = 2;
						Func function;
						function.type = type;	//������������
						function.name = preWord; //������
						Node<Var> *tVar = new Node<Var>;
						function.link= new Tree<Var>(tVar);
						g.fun.push_back(function); 

						type = "";
						
					}
					else if(ps[i] == ')' && (defining == 4 || defining == 2))
					{
						defining = 5; //�����������׼����������	
						type="";
					}
					else if(ps[i] == '{' && defining == 5)
					{
						if(g.fun.size()>0)
						{
							g.fun[g.fun.size()-1].link->AddChild(); //���һ�Ӳ㣬�������Ӳ�
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