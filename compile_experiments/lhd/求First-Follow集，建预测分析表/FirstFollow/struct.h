  #include <string>
#include <vector>
#include <set>
using namespace std;

struct Statement{
	string from;          //�ķ���ÿ������::=��-> ��ߵķ�
	vector<string> rule;    //�ķ���ÿ������::=��-> �ұߵ�ÿ������ 
};

struct Grammar
{
	string start;			//�ķ���ʼ��
	vector<Statement> stm;  //�ķ���ÿһ�����
};

struct FirstFollow
{
	string u;           
	set<char> first;   //u ��first ��
	set<char> follow;	 //u ��follow ��
};

struct ForeAnalysisTable
{
	vector<string> NT;
	vector<char> T;
	vector<vector<int>> table;
};