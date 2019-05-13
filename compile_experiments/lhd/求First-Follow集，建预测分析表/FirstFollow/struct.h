  #include <string>
#include <vector>
#include <set>
using namespace std;

struct Statement{
	string from;          //文法的每条语句的::=或-> 左边的符
	vector<string> rule;    //文法的每条语句的::=或-> 右边的每条规则 
};

struct Grammar
{
	string start;			//文法起始符
	vector<Statement> stm;  //文法的每一条语句
};

struct FirstFollow
{
	string u;           
	set<char> first;   //u 的first 集
	set<char> follow;	 //u 的follow 集
};

struct ForeAnalysisTable
{
	vector<string> NT;
	vector<char> T;
	vector<vector<int>> table;
};