#include <vector>
#include <string>
#include "tree.h"
struct Var				//����
{
	string type;		//��������
	string name;		//������
	string value;		//����ֵ
};

struct Func				//����
{
	string type;		//������������
	string name;		//������
	vector<Var> vTable; //����������
	Tree<string>* link;			//�����еı�����
};

struct Global{
	vector<Var> gVars;	//ȫ�ֱ����б�
	vector<Func> fun;	//�����б�
};