#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
using namespace std;

void defWordsHash(string defWords[],const int n,const char fileName[])
{
	
	ifstream in(fileName);
	if(!in)
	{
		cout<<"打不开\'"<<fileName<<"\'文件"<<endl;
		exit(0);
	}
	
	for(string s;getline(in,s);)
	{	
		string word;
		
		for(istringstream sin(s);sin>>word;)
		{
			int target=(word.c_str()[0] - 'a')%26;  //根据hash函数计算目标位置
			if(defWords[target] == "") {defWords[target] = word;}	    //若目标位置为空,则可以直接填入
			else
			{
				while(target=(++target)%26,defWords[target] != ""); //目标位置向后移动,直到空停下
				defWords[target] = word;
			}			
		}		
	}	
}

int defWordsHashSearch(const string words[],const int n,const string find)  //hash查找
{
	if(find.c_str()[0]>'z' || find.c_str()[0]<'a') return -1;
	int target=(find.c_str()[0] - 'a')%26;     //根据find计算目标位置
	while(words[target]!=find&&words[target]!="") target=(++target)%26; //若目标位置的内容不等于find且目标位置内容不为空,则target向后走
	if(words[target]==find) return target;   //查找成功,返回下标
	return -1; //查找不成功
}

void HashLoadKeyWords(string keyWords[],const int n,const char fileName[]) //hash查找载入关键字表;keyWords: 关键字表; fileName: 文件名
{
	ifstream in(fileName);
	if(!in)
	{
		cout<<"打不开\'"<<fileName<<"\'文件"<<endl;
		exit(0);
	}
	for(int i=0;i<n;i++)   
	{
		keyWords[i]="";
	}

	
	for(string s;getline(in,s);)
	{	
		string word;
		for(istringstream sin(s);sin>>word;)
		{
			int target=(word.c_str()[0]*100+word.c_str()[word.size()-1])%41;  //根据hash函数计算目标位置
			if(keyWords[target]=="") {keyWords[target]=word;}	    //若目标位置为空,则可以直接填入
			else
			{
				while(target=(++target)%41,keyWords[target]!=""); //目标位置向后移动,直到空停下
				keyWords[target]=word;
			}
			
		}
	}

}

int HashSearch(string keyWords[],const int n,string find)  //hash查找
{
	int target=(find.c_str()[0]*100+find.c_str()[find.size()-1])%41;   //根据find计算目标位置
	while(keyWords[target]!=find&&keyWords[target]!="") target=(++target)%41; //若目标位置的key不等于find且目标位置key不为空,则target向后走
	if(keyWords[target]==find)   return target;//查找成功
	return -1;
}
