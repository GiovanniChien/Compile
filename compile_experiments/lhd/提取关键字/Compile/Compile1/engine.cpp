#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <set>
using namespace std;
struct MySet
{
	string setName;
	set<char> s;
};
struct Martrix
{
	vector<string> setName;
	vector<vector<int>> state;
};

vector<MySet> vSet;
Martrix mt;

bool load(string fileName)
{
	string setFlag="set"; //集合开始标志
	string MFlag="M";	//矩阵开始标志
	string endFlag = "end";
	bool defSet = false;
	bool defM = false;
	bool end = false;

	ifstream indata(fileName.c_str());
	if(!indata)
	{
		cout<<"找不到文件\'"<<fileName<<"\'\n";
		return false;
	}
	
	for(string s;getline(indata,s);)
	{
		string word;
			for(istringstream sin(s);sin>>word;)
			{
				if(end==false && word==setFlag)  defSet=true;
				if(end==false && word==MFlag) defM = true;
				if(word==endFlag) end = true;
				if(end==true && word==setFlag) {defSet=false;end =false;}
				if(end==true && word==MFlag) {defM=false;end=false;}
			}
			if(defSet == true && end == false && word!=setFlag)
			{
				bool nameflag = false;
				bool inSet = false;
				char setName[40];
				char beginChar = 0;
				char endChar = 0;
				MySet ms;
				for(int i=0;i<s.size();i++)
				{
					int j=0;
					if(nameflag==false && (s[i]>='a'&&s[i]<='z'||s[i]>='A'&&s[i]<='Z'))
					{
						while(s[i]>='a'&&s[i]<='z'||s[i]>='A'&&s[i]<='Z')
						{
						setName[j++]=s[i++];
						}
						setName[j]=0;
						nameflag = true;
						ms.setName = setName;
					}
					if(inSet == false && s[i]=='{')
					{
						inSet = true;
					}
					if(inSet == true || s[i]=='}')
					{
						if(s[i]!='{'&&s[i]!='}'&&s[i]!=','&&s[i]!=' ')
						{
							if(beginChar==0) beginChar=s[i];
							else endChar = s[i];
						}
						
						if(s[i]==',' || s[i]=='}')
						{
							if(endChar==0) ms.s.insert(beginChar);
							else
							{
								for(char c=beginChar;c<=endChar;c++)
								{
									ms.s.insert(c);
								}
							}

							beginChar=0;
							endChar=0;
							if(s[i]=='}') inSet = false;
						}
						
					}

				}				
				vSet.push_back(ms);
			}
			if(defM == true && end == false && word!=MFlag)
			{
				if(mt.setName.size()<=0)
				{
					string setName;
					for(istringstream sin(s);sin>>setName;)
					{
						mt.setName.push_back(setName);
					}

				}
				else
				{
					int index;
					vector<int> temp;
					for(istringstream sin(s);sin>>index;)
					{
						temp.push_back(index);
					}
					mt.state.push_back(temp);
				}
			}
		
	}
	
	indata.close();
	return true;
}

void display()
{
	cout<<endl;
	for(vector<MySet>::const_iterator it = vSet.begin();it!=vSet.end();it++)
	{
		cout<<"集合名："<<it->setName<<endl;
		
		for(set<char>::const_iterator it2=(it->s).begin();it2!=(it->s).end();it2++)
			cout<<(*it2);
		cout<<endl;
	}
	cout<<"状态转换："<<endl;
	for(vector<string>::const_iterator it3=mt.setName.begin();it3!=mt.setName.end();it3++)
		cout<<(*it3)<<"  ";
	cout<<endl;
	for(int i=0;i<mt.state.size();i++)
	{
		for(int j=0;j<mt.state[i].size();j++)
		{
			cout<<mt.state[i][j]<<" ";
		}
		cout<<endl;
	}
	cout<<endl;
		
}
bool Judge(string variable)
{
	static bool loaded = false;
	if(loaded == false)
	{
		load("state2.txt");
		loaded = true;
	}
	//display();
	const char* pvar=variable.c_str();
	int S = 0;
	int F = mt.state.size()-1;
	int nextS = S;

	for(int i=0;pvar[i];++i)
	{
		bool found=false;
		string setName="";
		for(vector<MySet>::const_iterator it = vSet.begin();it!=vSet.end();it++) 
		{
			for(set<char>::const_iterator it2=(it->s).begin();it2!=(it->s).end();it2++)
				if((*it2)==pvar[i])
				{
					setName = it->setName;
					break;
				}
			if(setName != "") break;
		}
		if(setName=="") return false;
		int j;
		for(j=0;j<mt.setName.size();j++)
		{
			if(setName == mt.setName[j])
				break;
		}
		if(j>=mt.setName.size()) return false;
		nextS=mt.state[nextS][j];
		if(nextS==-1) return false;
	}
	if(nextS==F) return true; //能到达终止状态
	else return false; //不能到达终止状态
	
}