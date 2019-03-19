#include <vector>
#include <string>
using namespace std;

struct Var				//变量
{
	string type;		//变量类型
	string name;		//变量名
	string value;		//变量值
};


template<class T>
struct Node
{
	vector<T> data; //变量集合
	vector<Node<T>*> children; //子节点
	Node<T> *parent; //父节点
	string description; //节点描述
	int flag;  //节点标记
};

template <class T>
class Tree
{
private:
	Node<T> *root;
	Node<T> *p;
	void Delete(Node<T> *pn);
public:
	Tree(){root = NULL;p = NULL;root->parent=NULL;}
	Tree(Node<T> *root){this->root = root; p=root; this->root->parent = NULL;}
	~Tree();
	void PreOrder(Node<T> *p);
	Node<T>* GetCurrentPointer(){return p;}
	Node<T>* GetRoot(){return root;}
	void AddChild(string des = "",int flag = 0);
	void AddVariable(T data){p->data.push_back(data);}
	void SetCurrentPointer(Node<T> *newCurrentPointer){p = newCurrentPointer;}
	void DisplayVariable();
	string GetCurDes(){ return p->description;}
	void SetCurDes(string des){ p->description = des; }
	int GetCurFlag(){ return p->flag; }
	void SetCurFlag(int flag){ p->flag = flag; }
	
};

template<class T>
Tree<T>::~Tree()
{
	Delete(root);	
}

template<class T>
void Tree<T>::Delete(Node<T> *pn)
{
	for(int i=0;i<pn->children.size();++i)
		Delete(pn->children[i]);
	delete pn;
}

template<class T>
void Tree<T>::AddChild(string des,int flag)
{  
	Node<T> *child = new Node<T>;
	child->parent = p;
	child->description = des;
	child->flag = flag;
	p->children.push_back(child);					
	p = child;
}



template<class T>
void Tree<T>::PreOrder(Node<T> *p)
{
	static int layer = 0;
	for(int i=0;i<p->data.size();++i)
	{
		cout<<layer<<" : "<<p->data[i].type<<"	"<<p->data[i].name<<"  "<<p->data[i].value<<endl;
	}
	++layer;
	for(int i=0;i<p->children.size();++i)
	{
		PreOrder(p->children[i]);
	}
	--layer;
}

template<class T>
void Tree<T>::DisplayVariable()
{
	//for(int i)
}


struct Func				//函数
{
	string type;		//函数返回类型
	string name;		//函数名
	vector<Var> vTable; //函数参数表
	Tree<Var>* link;			//函数中的变量树
};

class Global{
public:
	vector<Var> gVars;	//全局变量列表
	vector<Func> fun;	//函数列表
	~Global();
};

Global::~Global()
{
	for(int i=0;i<fun.size();++i)
	{
		delete fun[i].link;
	}
}