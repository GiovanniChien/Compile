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
		cout<<"�򲻿�\'"<<fileName<<"\'�ļ�"<<endl;
		exit(0);
	}
	
	for(string s;getline(in,s);)
	{	
		string word;
		
		for(istringstream sin(s);sin>>word;)
		{
			int target=(word.c_str()[0] - 'a')%26;  //����hash��������Ŀ��λ��
			if(defWords[target] == "") {defWords[target] = word;}	    //��Ŀ��λ��Ϊ��,�����ֱ������
			else
			{
				while(target=(++target)%26,defWords[target] != ""); //Ŀ��λ������ƶ�,ֱ����ͣ��
				defWords[target] = word;
			}			
		}		
	}	
}

int defWordsHashSearch(const string words[],const int n,const string find)  //hash����
{
	if(find.c_str()[0]>'z' || find.c_str()[0]<'a') return -1;
	int target=(find.c_str()[0] - 'a')%26;     //����find����Ŀ��λ��
	while(words[target]!=find&&words[target]!="") target=(++target)%26; //��Ŀ��λ�õ����ݲ�����find��Ŀ��λ�����ݲ�Ϊ��,��target�����
	if(words[target]==find) return target;   //���ҳɹ�,�����±�
	return -1; //���Ҳ��ɹ�
}

void HashLoadKeyWords(string keyWords[],const int n,const char fileName[]) //hash��������ؼ��ֱ�;keyWords: �ؼ��ֱ�; fileName: �ļ���
{
	ifstream in(fileName);
	if(!in)
	{
		cout<<"�򲻿�\'"<<fileName<<"\'�ļ�"<<endl;
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
			int target=(word.c_str()[0]*100+word.c_str()[word.size()-1])%41;  //����hash��������Ŀ��λ��
			if(keyWords[target]=="") {keyWords[target]=word;}	    //��Ŀ��λ��Ϊ��,�����ֱ������
			else
			{
				while(target=(++target)%41,keyWords[target]!=""); //Ŀ��λ������ƶ�,ֱ����ͣ��
				keyWords[target]=word;
			}
			
		}
	}

}

int HashSearch(string keyWords[],const int n,string find)  //hash����
{
	int target=(find.c_str()[0]*100+find.c_str()[find.size()-1])%41;   //����find����Ŀ��λ��
	while(keyWords[target]!=find&&keyWords[target]!="") target=(++target)%41; //��Ŀ��λ�õ�key������find��Ŀ��λ��key��Ϊ��,��target�����
	if(keyWords[target]==find)   return target;//���ҳɹ�
	return -1;
}
