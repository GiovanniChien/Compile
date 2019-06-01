#include <iostream>
#include <fstream>
#include <sstream>
#include <stack>
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

void ExtractTerm(PrecedenceTable& pt)
{
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
						for(int t = 0;t<pt.T.size();t++)
						{
							if(ps[k] == pt.T[t]) {exist = true;break;}
						}
						if(exist == false) pt.T.push_back(ps[k]);
					}
				}	
				else 
				{
					bool exist = false;
					for(int t = 0;t<pt.T.size();t++)
					{
						if(ps[k] == pt.T[t]) {exist = true;break;}
					}
					if(exist == false) pt.T.push_back(ps[k]);
				}
				++k;
			}
		}
	}
	pt.T.push_back('#');//将'#' 也放进去

	for(int i = 0;i<pt.T.size();i++)
	{
		vector<char> v;
		if(i<pt.T.size()-1)
		{
			for(int j=0;j<pt.T.size();j++)
			{
				if( j==pt.T.size()-1 && pt.T[i] != '(')
				v.push_back('>');
				else v.push_back(0);
			}
		}
		else{
			for(int j=0;j<pt.T.size();j++)
			{
				if(pt.T[j] != ')')
				v.push_back('<');
				else v.push_back(0);
			}
		}
		pt.table.push_back(v);		
	}
}

void display(const PrecedenceTable& pt)
{
	cout<<"   ";
	for(int i = 0;i<pt.T.size();i++)
	{
		cout<<pt.T[i]<<"  ";
	}
	cout<<endl;
	for(int i=0;i<pt.table.size();i++)
	{
		cout<<pt.T[i]<<"  ";
		for(int j=0;j<pt.table[i].size();j++)
			cout<<pt.table[i][j]<<"  ";
		cout<<endl;
	}
	cout<<endl;
}

void ConstructPreTable(PrecedenceTable& pt,const FirstLastTerm& flterm)
{
	for(int i = 0;i<gm.stm.size();i++)
	{
		for(int j=0;j<gm.stm[i].rule.size();j++)
		{
			string rule = gm.stm[i].rule[j];			
			const char* ps = rule.c_str();
			const int len = rule.size();
			int pre = -1;  //上一个符号
			int count = 0;
			for(int k=0;k<len;)
			{
				if(ps[k]>='A' && ps[k]<='Z')
				{
					if(count<2) count++;
					else
					{
						pre = -1; 
						count = 0;
					}
					k++;
					if(k<len)					
						if(ps[k] >='0' && ps[k]<='9') k++;						
				}
				else 
				{					
					if(pre>=0 && count<2) //两终结符中间的非终结符个数小于2个
					{
						//cout<<ps[pre]<<"　"<<ps[k]<<endl;
						int a1 = pt.find(ps[pre]);
						int a2 = pt.find(ps[k]);
						pt.table[a1][a2] = '=';
					}
					pre = k;
					k++;
				}
			}
			
		}
	}

	for(int i = 0;i<gm.stm.size();i++)
	{
		for(int j=0;j<gm.stm[i].rule.size();j++)
		{
			
			const char* ps = gm.stm[i].rule[j].c_str();
			const int len = gm.stm[i].rule[j].size();

			for(int k=0;k<len;k++)
			{
				//...........?????????
				if( !(ps[k]>='A' && ps[k]<='Z') ) //是终结符
				{
					char temp[3];					
					if(k>0 && (ps[k-1]>='A' && ps[k-1]<='Z'))
					{
						temp[0] = ps[k-1];
						temp[1] = 0;
						string vn(temp);
						int index;
						for(index = 0;index<flterm.Vn.size(); index++)						
							if(vn == flterm.Vn[index]) break;
						if(index<flterm.Vn.size())
						{
							int col = pt.find(ps[k]);
							for(set<char>::const_iterator it = flterm.lt[index].begin();it != flterm.lt[index].end();it++)
							{
								int row = pt.find(*it);
								pt.table[row][col] = '>';
							}						
						}						
					}
					if(k<len-1 && (ps[k+1]>='A' && ps[k+1]<='Z'))
					{
						temp[0] = ps[k+1];
						temp[1] = 0;
						string vn(temp);
						int index;
						for(index = 0;index<flterm.Vn.size(); index++)						
							if(vn == flterm.Vn[index]) break;
						if(index<flterm.Vn.size())
						{
							int row = pt.find(ps[k]);
							for(set<char>::const_iterator it2 = flterm.ft[index].begin();it2 != flterm.ft[index].end();it2++)
							{
								int col = pt.find(*it2);
								pt.table[row][col] = '<';
							}						
						}						
					
					}				
				}
				//...........?????????
			}
		}
	}
	
	int a = pt.find('1');
	int b = pt.find('.');
	for(int i = a;i<b;i++)
		for(int j = a;j<=b;j++)
		{
			pt.table[i][j] = '<';
		}
	
}

void InitFirstLastTerm(FirstLastTerm& flterm)
{
	for(int i = 0;i<gm.stm.size();i++)
	{
		flterm.Vn.push_back(gm.stm[i].from);
		set<char> t;
		flterm.ft.push_back(t);
		flterm.lt.push_back(t);
	}
}
void display(FirstLastTerm& flterm)
{
	for(int i=0;i<flterm.Vn.size();i++)
	{
		cout<<flterm.Vn[i]<<":\n";
		cout<<"firstTerm"<<endl;
		for(set<char>::const_iterator it = flterm.ft[i].begin();it!=flterm.ft[i].end();it++)
			cout<<*it<<"  ";
		cout<<endl<<"lastTerm:\n";
		for(set<char>::const_iterator it2 = flterm.lt[i].begin();it2!=flterm.lt[i].end();it2++)
			cout<<*it2<<"  ";
		cout<<endl;
	
	}

}

void FindFirstLastTerm(FirstLastTerm& flterm)
{
	// 找 firstTerm
	for(int i=0;i<gm.stm.size();i++)
	{
		for(int j=0;j<gm.stm[i].rule.size();j++)
		{
			const char* ps = gm.stm[i].rule[j].c_str();		
			const int len = gm.stm[i].rule[j].size();
			if( !(ps[0]>='A'&&ps[0]<='Z') ) //第一个不是非终结符，是终结符
			{
				flterm.ft[i].insert(ps[0]);
			}
			else
			{
				if(len>=2 && ps[1]>='0' && ps[1]<='9')
				{
					if(len>=3 && !(ps[2]>='A'&&ps[2]<='Z') )
						flterm.ft[i].insert(ps[2]);
				}
				else
				{
					if(len>=2 && !(ps[1]>='A'&&ps[1]<='Z'))
					flterm.ft[i].insert(ps[1]);
				}
			}
		
		}	
	}

	bool changed;

	do
	{
		changed = false;
		for(int i=0;i<gm.stm.size();i++)
		{
			for(int j=0;j<gm.stm[i].rule.size();j++)
			{
				const char* ps = gm.stm[i].rule[j].c_str();		
				const int len = gm.stm[i].rule[j].size();
				
				if( ps[0]>='A'&&ps[0]<='Z' ) //是非终结符
				{
					char temp[3];
					temp[0] = ps[0];
					if(len>=2 && ps[1]>='0' && ps[1]<='9')
					{
						temp[1] = ps[1];
						temp[2] = 0;
					}
					else temp[1] = 0;
					string vn(temp);
					if(vn != flterm.Vn[i])
					{
						int k;
						for(k = 0;k<flterm.Vn.size();k++)
						{
							if(i == k) continue;
							if(vn == flterm.Vn[k])
								break;
						}
						if( k<flterm.Vn.size() ) 
						{
							int size0 = flterm.ft[i].size();
							for(set<char>::const_iterator it = flterm.ft[k].begin();it!=flterm.ft[k].end();it++)
								flterm.ft[i].insert(*it);
							int size1 = flterm.ft[i].size();
							if(size0 != size1) changed = true;
						}
					}

				}
			}
		}
	}while(changed == true);

	//找lastTerm
	for(int i=0;i<gm.stm.size();i++)
	{
		for(int j=0;j<gm.stm[i].rule.size();j++)
		{
			const char* ps = gm.stm[i].rule[j].c_str();		
			const int len = gm.stm[i].rule[j].size();
			if(len>0 && !(ps[len-1]>='A'&&ps[len-1]<='Z') ) 
			{
				if( !(ps[len-1]>='0'&&ps[len-1]<='9') )
				flterm.lt[i].insert(ps[len-1]);
				else //最后一个是 0-9 之间的数
				{
					if(len>=2)
					{
						if(!(ps[len-2]>='A'&&ps[len-2]<='Z')) //倒数第二个
						flterm.lt[i].insert(ps[len-1]);	
						else 
						{
							if(len>=3)
							{
								if(!(ps[len-3]>='A'&&ps[len-3]<='Z')) //倒数第三个
								{
									flterm.lt[i].insert(ps[len-3]);	//....???
								}
							}
						}
					}
					else flterm.lt[i].insert(ps[len-1]);	
				}
			}
			else //最后一个是非终结符
			{
				if(len>1 && !(ps[len-2]>='A'&&ps[len-2]<='Z')) 
				{
					flterm.lt[i].insert(ps[len-2]);	//....???
				}
			}
		
		}	
	}

	do
	{
		changed = false;
		for(int i=0;i<gm.stm.size();i++)
		{
			for(int j=0;j<gm.stm[i].rule.size();j++)
			{
				const char* ps = gm.stm[i].rule[j].c_str();		
				const int len = gm.stm[i].rule[j].size();
				
				if( (len>=1 && ps[len-1]>='A'&&ps[len-1]<='Z') 
					|| (len>=2 && ps[len-2]>='A'&&ps[len-2]<='Z' && ps[len-1]>='0'&&ps[len-1]<='9') ) //最后是非终结符
				{
					char temp[3];
					if(ps[len-1]>='A'&&ps[len-1]<='Z')
					{
						temp[0] = ps[len-1];
						temp[1] = 0;
					}
					else 
					{
						temp[0] = ps[len-2];
						temp[1] = ps[len-1];
						temp[2] = 0;
					}

					string vn(temp);
					if(vn != flterm.Vn[i])
					{
						int k=-1;
						for(k = 0;k<flterm.Vn.size();k++)
						{
							if(i == k) continue;
							if(vn == flterm.Vn[k])
								break;
						}
						if(k != -1 && k<flterm.Vn.size()) 
						{
							int size0 = flterm.lt[i].size();
							for(set<char>::const_iterator it2 = flterm.lt[k].begin();it2!=flterm.lt[k].end();it2++)
								flterm.lt[i].insert(*it2);
							int size1 = flterm.lt[i].size();
							if(size0 != size1) changed = true;
						}
					}

				}
			}
		}
	}while(changed == true);
}

double Compute(double a,char op,double b)
{
	double result;
	switch(op)
	{
	case '+':result = a+b;break;
	case '-':result = a-b;break;
	case '*':result = a*b;break;
	case '/':result = a/b;break;
	default: result = 0x7fffffff;break;
	}
	return result;
}

double tran(const vector<char>& cval)
{
	double val;
	int i;
	for(i=0;i<cval.size();i++)
		if(cval[i] == '.') break;

	double intp = 0;
	double decp = 0;
	double decv = 0.1;
	int intv = 1;
	if(i>=0 && i<cval.size())
	{
		for(int j = i+1;j<cval.size();j++)
		{
			intp += (cval[j]-'0')*intv;
			intv *= 10;
		}
		for(int j=i-1;j>=0;j--)
		{
			decp += (cval[j]-'0')*decv;
			decv *= 0.1;
		}
	}
	else
	{
		for(int j = 0;j<cval.size();j++)
		{
			intp += (cval[j]-'0')*intv;
			intv *= 10;
		}
	}

	val = intp + decp;
	return val;
}

void Calculate(PrecedenceTable& pt,const char* fileName)
{
	ifstream in(fileName);
	stack<char> st;
	st.push('#');
	stack<double> dst;

	int line = 0;
	for(string s;getline(in,s);line++)
	{
		line++;
		const char* ps=s.c_str();
		const int len = s.size();
		for(int i=0;i<=len;)
		{
			if(ps[i]==' '||ps[i] == 9) {i++;continue;}
			
			char c1 = st.top();
			char c2;
			if(i<len) c2 = ps[i];
			else c2 = '#';
			int row = pt.find(c1);
			int col = pt.find(c2);
			if(pt.table[row][col] == '=')
			{
				st.pop(); //弹栈
				i++;
				continue; //读下一个字符
			}			
			else if(pt.table[row][col] == '<')
			{
				st.push(c2);
				i++;
				continue;
			}
			else if(pt.table[row][col] == '>')
			{
				if(c1>='0' && c1<='9')
				{
					vector<char> cval;
					while(st.top()>='0' && st.top()<='9'||st.top() == '.')
					{						
						cval.push_back(st.top());	
						st.pop();
					}
					double val = tran(cval);
					dst.push(val);
				}
				else
				{					
					double b = dst.top();
					dst.pop();
					double a = dst.top();
					dst.pop();
					st.pop();
					dst.push(Compute(a,c1,b));
				}				
			}
			else if(pt.table[row][col] == 0)
			{
				cout<<"line "<<line<<" column "<<i++<<"  error"<<endl;
			}
		}
	}
	cout<<dst.top()<<endl;
}

void main()
{
	Load(gm,"grammar.txt");
	display(gm);
	PrecedenceTable pt;
	ExtractTerm(pt);	//提取终结符
	
	FirstLastTerm flterm;
	InitFirstLastTerm(flterm);
	FindFirstLastTerm(flterm);
	display(flterm);

	ConstructPreTable(pt,flterm);
	display(pt);
	
	Calculate(pt,"in.txt");

}