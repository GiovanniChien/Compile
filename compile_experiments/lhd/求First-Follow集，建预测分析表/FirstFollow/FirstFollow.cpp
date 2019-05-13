   #include <iostream>
#include <fstream>
#include <sstream>
#include "struct.h"
using namespace std;

const int NONE = 0xffffffff;
vector<FirstFollow> ff;
Grammar gm;

bool Load(Grammar& gm,const char* fileName)
{
	ifstream in(fileName);
	if(!in) 
	{
		cout<<"cannot open file\n";
		return 0;
	}
	int line=0;
	for(string s;getline(in,s);)
	{
		++line;
		if(line == 1) gm.start = s;
		else
		{
			Statement st;
			string tos;

			int res=-2;
			const char* ps = s.c_str();
			const int len = s.size();
			char temp[20];
			res = s.find("::=");
			int i;
			for(i = 0;i<res;i++)
				temp[i]=ps[i];
			temp[i]=0;
			st.from = temp;

			int pre = res+3;
			while(res!=NONE)
			{
				res = s.find("|",res+1);
				if( res == NONE ) break;
				for(i=pre;i<res;i++)
					temp[i-pre] = ps[i];
				temp[i-pre] = 0;
				tos = temp;
				st.rule.push_back(tos);
				pre = res+1;
			}
			for(i=pre;i<len;i++)
					temp[i-pre] = ps[i];
			temp[i-pre] = 0;
			tos = temp;
			st.rule.push_back(tos);
			gm.stm.push_back(st);
		}
		
	}
	return 1;
}

void display(const Grammar& gm)
{
		cout<<gm.start<<endl;
		for(int i=0;i<gm.stm.size();i++)
		{
			cout<<gm.stm[i].from<<"->";
			for(int j = 0;j<gm.stm[i].rule.size();j++)
			{				
				cout<<gm.stm[i].rule[j];
				if(j<gm.stm[i].rule.size()-1) cout<<"|";
			}
			cout<<endl;
		}
}

void initFirstFollow(const Grammar& gm,vector<FirstFollow>& ff)
{
	for(int i=0;i<gm.stm.size();i++)   //ff[i].u 与 gm.stm[i].from 一一对应
	{
		FirstFollow tff;
		tff.u = gm.stm[i].from;
		ff.push_back(tff);
	}
}

void display(const vector<FirstFollow>& ff)
{
	for(int i = 0;i<ff.size();i++)
	{
		cout<<ff[i].u<<"\tfirst 集  ";
		for(set<char>::const_iterator it =ff[i].first.begin();it!=ff[i].first.end();it++)
			cout<<(*it)<<"  ";
		cout<<"\t\tfollow 集  ";
		for(set<char>::const_iterator it2 =ff[i].follow.begin();it2!=ff[i].follow.end();it2++)
			cout<<(*it2)<<"  ";
		cout<<endl;
	}
}

int findFirst(const string& from)
{
	int i;
	for(i = 0;i<gm.stm.size();i++)
	{
		if(gm.stm[i].from == from)
			break;
	}

	if(ff[i].first.size()>0) return i;   //ff[i] 的first集已经求得
	for(int j=0;j<gm.stm[i].rule.size();j++)
	{
		string rule = gm.stm[i].rule[j];
		const char* prule = rule.c_str();
		const int len = rule.size();
		if(!(prule[0]>='A' && prule[0]<='Z')) //首个符号是终结符号,或$(空)
		{
			ff[i].first.insert(prule[0]);
			continue;
		}
		else 
		{
			int k = 0;
			while(k<len)
			{
				char temp[3];
				temp[0] = prule[k];
				if(k<len-1 && prule[k+1]>='0' && prule[k+1]<='9')
				{
					temp[1] = prule[k+1];
					temp[2] = 0;
					k++;
				}
				else temp[1] = 0;
				k++;
				int tempIndex = findFirst(temp);
				bool containNull = false;
				for(set<char>::const_iterator it =ff[tempIndex].first.begin();it!=ff[tempIndex].first.end(); it++)
				{
					if(*it == '$') containNull = true; //包含空
					else ff[i].first.insert(*it);					
				}
				if(containNull == false) break; 
				if(k >= len)  ff[i].first.insert('$');  //X::=X1X2...Xi-1,  X1X2...Xi-1=>*...空
			}
		
		}
	}
	return i;
}
int findFollow(const string& from)
{
	int i;
	for(i = 0;i<gm.stm.size();i++)
	{
		if(gm.stm[i].from == from)
			break;
	}
	if(!(ff[i].follow.size()==0 ||(ff[i].follow.size()==1 && (*ff[i].follow.begin())=='#'))) return i;   //ff[i] 的follow集已经求得
	for(int sn = 0;sn<gm.stm.size();sn++)
	{
				for(int j=0;j<gm.stm[sn].rule.size();j++)
				{
					string rule = gm.stm[sn].rule[j];
					int res = rule.find(from);
					const int len = from.size();

					while(res != NONE)
					{
						if(from.size() == 1 && res<rule.size()-1 && rule.c_str()[res+1]>='0' && rule.c_str()[res+1]<='9') //找到的不是后面还有数字
						{
							res = rule.find(from,res+1);
							continue;
						}

						char temp[3];
						temp[0] = from[0];
						if(len>1) 
						{
							temp[1] = from[1];
							temp[2] = 0;
						}
						else temp[1] = 0;

						if(res+len-1 < rule.size()-1 )
						{
							if(rule.c_str()[res+len] >='A' && rule.c_str()[res+len]<='Z')  //后面一个是非终结符
							{
								int k = res+len;
								while(k<rule.size() && rule.c_str()[k] >='A' && rule.c_str()[k]<='Z')
								{
									char cur[3];
									cur[0] = rule.c_str()[k];
									if(k<rule.size()-1 && rule.c_str()[k+1]>='0' && rule.c_str()[k+1]<='9')
									{
										cur[1] = rule.c_str()[++k];
										cur[2] = 0;
									}
									else cur[1] = 0;									
									string scur(cur);
									bool containNull = false;
									int f;
									for(f = 0;f<ff.size(); f++)
										if(ff[f].u == scur) break;
									for(set<char>::const_iterator it = ff[f].first.begin();it!=ff[f].first.end();it++)
									{
										if(*it == '$') containNull = true;
										else ff[i].follow.insert(*it);
									}
									if(containNull == false) break;
									k++;
								}
								if(k>=rule.size())  //后面的非终结符都可以取空
								{
									int nexti = findFollow(gm.stm[sn].from);
									for(set<char>::const_iterator it = ff[nexti].follow.begin();it!=ff[nexti].follow.end();it++)
										ff[i].follow.insert(*it);
								}
							}
							else ff[i].follow.insert(rule.c_str()[res+len]);        //后面一个是终结符
						}
						else {
							string next(temp);
							if(gm.stm[sn].from == next) break;
							else 
							{
								int nexti = findFollow(gm.stm[sn].from);
								for(set<char>::const_iterator it = ff[nexti].follow.begin();it!=ff[nexti].follow.end();it++)
									ff[i].follow.insert(*it);
							}
						}
						res = rule.find(from,res+1);
					}

				}
	}
	return i;
}

void initFAT(ForeAnalysisTable& fat)  //取出文法中的终结符和非终结符
{
	for(int i = 0;i<gm.stm.size();i++)
	{
		fat.NT.push_back(gm.stm[i].from);  //fat.NT[i] = gm.stm[i].from 一一对应
	}
	for(int i = 0;i<gm.stm.size();i++)
	{
		for(int j=0;j<gm.stm[i].rule.size();j++)
		{
			string rule = gm.stm[i].rule[j];
			const char* ps = rule.c_str();
			const int len = rule.size();
			int k = 0;
			while(ps[k])
			{
				if(ps[k] == '$') {++k;break;}   // 
				if(ps[k]>='A' && ps[k] <='Z')
				{									
				}
				else if(ps[k]>='0' && ps[k] <='9')
				{
					if(!(k>0 &&(ps[k-1]>='A' && ps[k-1] <='Z')))  //
					{
						bool exist = false;
						for(int t = 0;t<fat.T.size();t++)
						{
							if(ps[k] == fat.T[t]) {exist = true;break;}
						}
						if(exist == false) fat.T.push_back(ps[k]);
					}
				}	
				else 
				{
					bool exist = false;
					for(int t = 0;t<fat.T.size();t++)
					{
						if(ps[k] == fat.T[t]) {exist = true;break;}
					}
					if(exist == false) fat.T.push_back(ps[k]);
				}
				++k;
			}
		}
	}
	fat.T.push_back('#');

	for(int i=0;i<fat.NT.size();i++)
	{
		vector<int> v;
		for(int j=0;j<fat.T.size();j++)
		v.push_back(-1);
		fat.table.push_back(v);
	}
}

void ForeAnalysisM(ForeAnalysisTable& fat)
{
	for(int i = 0;i<fat.NT.size();i++)
	{
		for(int rindex = 0;rindex < gm.stm[i].rule.size();rindex++)
		{
			string rule = gm.stm[i].rule[rindex];
			cout<<"rule = "<<rule<<endl;
			char temp[3];

			if(rule.c_str()[0]>='A' && rule.c_str()[0]<= 'Z')
			{
				int k = 0;
				bool nextNT;
				do{
					if( !(rule.c_str()[k]>='A' && rule.c_str()[k]<= 'Z') )
					{
						int tindex;
						for(tindex = 0;tindex<fat.T.size();tindex++) if(rule.c_str()[k] == fat.T[tindex]) break;
						fat.table[i][tindex] = rindex;
						break;
					}
						temp[0] = rule.c_str()[k];
						if(rule.size()>k+1)
						{
							if(rule.c_str()[k+1]>='0' && rule.c_str()[k+1]<='9')
							{temp[1] = rule.c_str()[k+1]; temp[2] = 0; k+=2;}
							else {temp[1] = 0;k++;}
						}
						cout<<temp<<endl;
						nextNT = false;
						for(int findex = 0;findex<ff.size();findex++)
						{
							if(ff[findex].u == temp)
							{
								for(set<char>::iterator it = ff[findex].first.begin();it != ff[findex].first.end(); it++)
								{
									if(*it == '$') { nextNT = true;continue;}
									int tindex;
									for(tindex = 0;tindex<fat.T.size();tindex++) if(*it == fat.T[tindex]) break;
									fat.table[i][tindex] = rindex;
								}
								break;
							}
						}
				}while(nextNT && rule.size()>k);
				if(k>=rule.size() && nextNT)
				{
					for(int findex = 0;findex<ff.size();findex++)
					{
						if(ff[findex].u == fat.NT[i])
						{
							for(set<char>::iterator it = ff[findex].follow.begin();it != ff[findex].follow.end(); it++)
							{
								int tindex;
								for(tindex = 0;tindex<fat.T.size();tindex++) if(*it == fat.T[tindex]) break;
								fat.table[i][tindex] = rindex;
							}
							break;
						}
					}
				}
			}
			else if(rule.c_str()[0] == '$')
			{
				for(int findex = 0;findex<ff.size();findex++)
				{
					if(ff[findex].u == fat.NT[i])
					{
						for(set<char>::iterator it = ff[findex].follow.begin();it != ff[findex].follow.end(); it++)
						{
							int tindex;
							for(tindex = 0;tindex<fat.T.size();tindex++) if(*it == fat.T[tindex]) break;
							fat.table[i][tindex] = rindex;
						}
						break;
					}
				}
			}
			else {
				int tindex;
				for(tindex = 0;tindex<fat.T.size();tindex++) if(rule.c_str()[0] == fat.T[tindex]) break;
				fat.table[i][tindex] = rindex;
			}

		}

	}

}

void displayFAT(const ForeAnalysisTable& fat)
{
	for(int i = 0;i<fat.NT.size();i++)
		cout<<i<<":"<<fat.NT[i]<<"\t";
	cout<<endl;

	for(int i = 0;i<fat.T.size();i++)
		cout<<i<<":"<<fat.T[i]<<"\t";
	cout<<endl;

	for(int i=0;i<fat.table.size();i++)
	{
		for(int j = 0;j<fat.table[i].size();j++)
			cout<<fat.table[i][j]<<"\t";
		cout<<endl;
	}
}


void main()
{	
	Load(gm,"grammar.txt");
	display(gm);
	
	initFirstFollow(gm,ff);	
	for(int i = 0;i<gm.stm.size();i++)
	{
		findFirst(gm.stm[i].from);
	}
	ff[0].follow.insert('#');
	for(int i = 0;i<gm.stm.size();i++)
	{		
		findFollow(gm.stm[i].from);
	}
	display(ff);	
	ForeAnalysisTable fat;
	initFAT(fat);
	cout<<endl;
	cout<<"NT:"<<endl;
	for(int i=0;i<fat.NT.size();i++)
		cout<<fat.NT[i]<<endl;
	cout<<"T:"<<endl;
	for(int i=0; i<fat.T.size();i++)
		cout<<fat.T[i]<<endl;
	displayFAT(fat);
	ForeAnalysisM(fat);
	displayFAT(fat);
	

}