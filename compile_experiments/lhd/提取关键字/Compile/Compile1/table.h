#include <vector>
#include <string>
#include "tree.h"
struct Var				//变量
{
	string type;		//变量类型
	string name;		//变量名
	string value;		//变量值
};

struct Func				//函数
{
	string type;		//函数返回类型
	string name;		//函数名
	vector<Var> vTable; //函数参数表
	Tree<string>* link;			//函数中的变量树
};

struct Global{
	vector<Var> gVars;	//全局变量列表
	vector<Func> fun;	//函数列表
};