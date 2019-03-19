//#include <iostream>
#include <stdio.h>
#include <vector>
using namespace std;

#define BUFFERSIZE 2000
#define GRAMMERSIZE 2000

struct rule{
	char h;
	vector<char*> con;
	int num;
};

struct firstsets{
	char *h;
	vector<char> con;
//	int num;
};

struct followsets{
	char h;
	vector<char> con;
	int num;
};

char grammer[GRAMMERSIZE];
char buffer[BUFFERSIZE];
vector<rule> Rule;
vector<firstsets> first;
vector<followsets> follow;
bool state[100];
FILE *fpr;

/*---------------the function to get first and follow sets---------*/
bool isValidRule(char*);
void filesopen();
void filesclose();
void readgrammer();
void showgrammer();
void handlegrammer();
void getFirstSets();
vector<char> getFirstSets(char* a);
int isheadExitsFirst(char *a,vector<firstsets>);//judge whether the element exit in the first sets or the follw sets,if exits,return the number
int isheadExitsFollow(char a,vector<followsets>);//judge whether the element exit in the first sets or the follw sets,if exits,return the number
//int countFirst();//get the number of the first list
void initFirst();
char getFirstElement(char*);
int findFirstElement(char*);
int findRuleElement(char);
bool isFirstAll();
void showfirstsets();
void cleanFirst();

bool isVT(char);//判断一个符号是否是终结符
bool isVN(char);//判断一个符号是否是非终结符
bool cmpString(char* ,char*);
void addVec(vector<char> &,vector<char>&);

/*---------------the function to test function---------------------*/
void testCmpString();
void testCountFirst();
void getfirsthead();
void testfindFirst();
/*------------------------------------------------*/

int main()
{
	
	/*------------realization-------------*/
	filesopen();
	readgrammer();
	showgrammer();
	handlegrammer();
	initFirst();
	getfirsthead();
	getFirstSets();
	filesclose();
	showfirstsets();
	/*------------test--------------------*/
	//testCmpString();
	printf("---------------\n");
//	testfindFirst();
//	initFirst();
//	getfirsthead();
	return 0;
}

bool isValidRule(char* a)
{

	return true;
}

void filesopen()
{
	fpr=fopen("grammer.txt","r");
}

void filesclose()
{
	fclose(fpr);
}

void readgrammer()
{
	fread(buffer,BUFFERSIZE,1,fpr);
}

void showgrammer()
{
	printf("%s\n",buffer);
	int i=0;
	while(buffer[i]!=0)
	{
		printf("%d ",buffer[i]);
		if(buffer[i]==10)
			printf("\n");
		i++;
	}
}

void handlegrammer()
{
	int state=0;
	int i=0;
	while(buffer[i]!=0)
	{
		if(buffer[i]==10)
		{
			i++;
			continue;
		}
		char h;
		int count=0;//表示后面的下标
		vector<char*> temp;
		while(buffer[i]!=10)
		{
			h=buffer[i];
			i++;
			if(buffer[i]==':'&&buffer[i+1]==':'&&buffer[i+2]=='=')
			{
				state=1;
				i=i+3;
				while(buffer[i]!=10)
				{
					if(buffer[i]=='|')
						i++;
					count=0;
					char *con=new char();
					while(buffer[i]!='|'&&buffer[i]!=10)
					{
						con[count]=buffer[i];
						i++;
						count++;
					}
					con[count]=0;
					temp.push_back(con);
				}
			}
		}
		rule a;
		a.h=h;
		a.con=temp;
		a.num=temp.size();
		Rule.push_back(a);
		/*int j;
		for(j=0; j<Rule.size()&&Rule[j].h!=h;j++);
		if(j>=Rule.size())
		{
			rule a;
			a.h=h;
			a.num=1;
			a.con.push_back(con);
			Rule.push_back(a);
		}else
		{
			Rule[j].con.push_back(con);
			Rule[j].num++;;
		}*/
	}
	int m;
	printf("the size of the rule is:%d\n",first.size());
	for(m=0; m<Rule.size(); m++)
	{
		printf("%c ",Rule[m].h);
		int n;
		for(n=0; n<Rule[m].con.size(); n++)
		{
			printf("%s ",Rule[m].con[n]);
		}
		printf("\n");
	}
}

void getFirstSets()
{
	int percent=0;
	printf("first size is:%d \n",first.size());
	int i=0;
	int m=0;
	int n=0;
	for(i=0; i<Rule.size(); i++)
	{
		int j=0;
		char temph[2];
		temph[0]=Rule[i].h;
		temph[1]=0;
		printf("first set of %c:",temph[0]);
		int loci=findFirstElement(temph);
		for(j=0;j<Rule[i].num; j++)
		{
			if(strcmp(Rule[i].con[j],"@")==0)
			{
				//Rule[i].con.push_back("@");
				vector<char> tempnul;
				tempnul.push_back('@');
				addVec(first[loci].con,tempnul);
				j++;
				if(j>=Rule[i].num)
					break;
			}
			int loc=findFirstElement(Rule[i].con[j]);
//		for(j=0; j<Rule[i].num; j++)
//		{
			if(state[loc]==false)
			{
				first[loc].con=getFirstSets(Rule[i].con[j]);
				addVec(first[loci].con,first[loc].con);
			}
			for(n=0; n<first[loc].con.size(); n++)
				printf("%c ",first[loc].con[n]);
			printf("\n");
			//if()
//		}
		}
		state[loci]=true;
//		printf("%s \n",first[loci].h);
//		for(m=0; m<first[loci].con.size(); m++)
//			printf("%c ",first[loci].con[m]);
		printf("\n");
	}
			//first[i]
/*	while(!isFirstAll())
	{
		
	}
	while(percent<first.size())
	{
		if()
	}*/	
}

vector<char> getFirstSets(char* a)
{
	vector<char> temp;
	int i=0;
	if(isVT(a[i]))
	{
		temp.push_back(a[i]);
		return temp;
	}
	if(cmpString(a,"@"))
	{
		temp.push_back('@');
		return temp;
	}
	if(isVN(a[i]))
	{
		/*int num=0;
		if((num=isheadExitsFirst(&a[i],first)))
			return first[num].con;*/
		char b[2];
		b[0]=a[i];
		b[1]=0;
		int loc=findFirstElement(b);
		int locRule=findRuleElement(a[i]);
		if(state[loc]==false)
		{
			int j=0;
			for(j=0; j<Rule[locRule].num; j++)
			{
				char f[2];
				f[0]=a[0];
				f[1]=0;
				addVec(first[loc].con,getFirstSets(Rule[locRule].con[j]));
			}
//			first[loc].con=getFirstSets(b);
		}
		state[loc]=true;
		return first[loc].con;
/*		int num=0;
		char b=a[i];
		num=isheadExitsFirst(&b,first);
		if(num!=0)
			return first[num].con;
		else
		{

		}*/
	}
	return temp;
}

int isheadExitsFirst(char *a,vector<firstsets> b)
{
	printf("%s \n",a);
	int i;
	for(i=0; i<b.size(); i++)
		if(cmpString(a,b[i].h))
			return i;
	return -1;
}

int isheadExitsFollow(char a,vector<followsets> b)
{
	int i;
	for(i=0; i<b.size(); i++)
		if(b[i].h==a)
			return i;
	return -1;
}

/*
int countFirst()
{
	int count=0;
	int i;
	for(i=0; i<Rule.size(); i++)
		count=count+1+Rule[i].num;
	return count;
}
*/

void initFirst()
{
	int i;
	for(i=0; i<Rule.size(); i++)
	{
		char temp[2];
		temp[0]=Rule[i].h;
		temp[1]=0;
		if(isheadExitsFirst(temp,first)<0&&Rule[i].h!='@')
		{
			char *a=&(Rule[i].h);
			a[1]=0;
			firstsets temp;
			temp.h=a;
			first.push_back(temp);
		}
		int j;
		for(j=0; j<Rule[i].num; j++)
		{
			if(isheadExitsFirst(Rule[i].con[j],first)<0&&!cmpString(Rule[i].con[j],"@"))
			{
				char *a=Rule[i].con[j];
				firstsets temp;
				temp.h=a;
				first.push_back(temp);
			}
		}
	}
	for(i=0; i<first.size(); i++)
		state[i]=false;
}

bool isVT(char a)
{
	if(a>='a'&&a<='z'||a=='+'||a=='-'||a=='*'||a=='/'||a=='('||a==')'||a=='{'||a=='}')
		return true;
	return false;
}

bool isVN(char a)
{
	if(a>='A'&&a<='Z')
		return true;
	return false;
}

bool cmpString(char* a,char* b)
{
	int i=0;
	int nula=strcmp(a,"");
	int nulb=strcmp(b,"");
	if(nula==0&&nulb!=0)
		return false;
	if(nula!=0&&nulb==0)
		return false;
	if(nula==0&&nulb==0)
		return true;
	while(a[i]!=0&&b[i]!=0&&a[i]==b[i])
		i++;
	if(a[i]!=0||b[i]!=0)
		return false;
	return true;
}

/*------------------the function to test function-------------------------*/
void testCmpString()
{
	char *a="abc";
	char *b="abc";
	char *c="abcd";
	char *d="abce";
	printf("ans is 1,result is %d \n",cmpString(a,b));
	printf("ans is 0,result is %d \n",cmpString(a,c));
	printf("ans is 0,result is %d \n",cmpString(c,d));
	printf("ans is 0,result is %d \n",cmpString(b,d));
}

/*
void testCountFirst()
{
	printf("%d \n",countFirst());
}
*/

void testfindFirst()
{
	printf("%d \n",findFirstElement("{M}"));
}

void getfirsthead()
{
	int i;
	int j;
//	printf("first size is: %d\n",first.size());
	for(i=0; i<first.size(); i++)
	{
		printf("%s \n",first[i].h);
/*		for(j=0; j<first[i].con.size(); j++)
			printf("%c",first[i].con[j]);
		printf("\n");*/
	}
}

char getFirstElement(char* a)
{
	return *a;
}

int findFirstElement(char* a)
{
	int i=0;
	for(i=0; i<first.size(); i++)
		if(cmpString(first[i].h,a))
			break;
	return i;
}

bool isFirstAll()
{
	int i=0; 
	for(i=0; i<first.size(); i++)
		if(state[i]==false)
			break;
	if(i==first.size())
		return true;
	return false;
}

void showfirstsets()
{
	int i=0;
	int j=0;
	for(i=0; i<first.size(); i++)
	{
		printf("first{%s} = {",first[i].h);
		for(j=0; j<first[i].con.size(); j++)
		{
			printf("%c ",first[i].con[j]);
		}
		printf("}\n");
	}
}

void addVec(vector<char> &a,vector<char> &b)
{
	int i=0;
	for(i=0; i<b.size(); i++)
		a.push_back(b[i]);
}

int findRuleElement(char a)
{
	int i=0;
	for(i=0; i<Rule.size(); i++)
		if(a==Rule[i].h)
			break;
	return i;
}
