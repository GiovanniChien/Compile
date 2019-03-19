#include<iostream> 
#include<fstream>
#include<string> 
using namespace std; 

#define MAX 16            
string key[16]={"begin","end","if","then","else","while","write","do","call","const","char","until","procedure","repeat","int"}; 

int Iskey(string c)
{         //�ؼ����ж�
	int i;  
	for(i=0;i<MAX;i++) 
	{  
		if(key[i].compare(c)==0) 
			return 1; 
	}  
	return 0; 
} 
 
int IsLetter(char c) 
{        //�ж��Ƿ�Ϊ��ĸ
	if(((c<='z')&&(c>='a'))||((c<='Z')&&(c>='A'))) 
		return 1;     
	else 
		return 0; 
}  

int IsDigit(char c)
{          //�ж��Ƿ�Ϊ����     
	if(c>='0'&&c<='9') 
		return 1;      
	else 
		return 0; 
}  

void analyse(string fpin)
{ 
	string arr="";
	char *ch=&fpin[0];
	while(*ch!=NULL) 
	{ 
		arr="";                   
		if(*ch==' '||*ch=='\t'||*ch=='\n'){}                  
		else if(IsLetter(*ch))
		{  
                 while(IsLetter(*ch)||IsDigit(*ch)) 
				 {  
					 if((*ch<='Z')&&(*ch>='A')) 
						 *ch=*ch+32;    
					 arr=arr+(*ch); 
					 ch++; 
				 }  
                 if(Iskey(arr))
					 cout<<arr<<"\t$�ؼ���"<<endl;     
                 else  
					 cout<<arr<<"\t$��ͨ��ʶ��"<<endl;   
		}                     
		else if(IsDigit(*ch))
		{                    
			char*c=ch;
			while(IsDigit(*ch)||*ch=='.'&&IsDigit(*(c++)))
			{ 
				arr=arr+(*ch); 
				ch++; 
			}  
			cout<<arr<<"\t$�޷���ʵ��"<<endl;   
		}         
		else 
			switch(*ch){    
               case'+' : 
               case'-' : 
               case'*' :               
			   case'=' :  
               case'/' :cout<<*ch<<"\t$�����"<<endl;ch++;break; 
               case'(' : 
               case')' : 
               case'[' :  
               case']' :                
               case';' : 
               case'.' : 
               case',' : 
               case'{' :  
               case'}' :cout<<*ch<<"\t$���"<<endl;ch++;break; 
               case':' :{while(*ch!=NULL)
						{	arr=arr+(*ch); ch++; }
				   if(arr==":="||arr==":")
					   cout<<arr<<"\t$�����"<<endl;
				   else
					   cout<<arr<<"\t$�޷�ʶ���ַ�"<<endl;}break;
			   case'>' :{while(*ch!=NULL)
						{	arr=arr+(*ch); ch++; }
				   if(arr==">="||arr==">")
					   cout<<arr<<"\t$�����"<<endl;
				   else if(arr==">>")
					   cout<<arr<<"\t$������Ʒ�"<<endl;
				   else
					   cout<<arr<<"\t$�޷�ʶ���ַ�"<<endl;}break;
               case'<' :{while(*ch!=NULL)
						{	arr=arr+(*ch); ch++; }
				   if(arr=="<="||arr=="<")
					   cout<<arr<<"\t$�����"<<endl;
				   else if(arr=="<<")
					   cout<<arr<<"\t$������Ʒ�"<<endl;
				   else
					   cout<<arr<<"\t$�޷�ʶ���ַ�"<<endl;}break;

			   default :{while(*ch!=NULL) {	arr=arr+(*ch); ch++; }
							cout<<arr<<"\t$�޷�ʶ���ַ�"<<endl;}break;
				   } 				  
	} 
}  
		
int main()
{
	string a[10000];int i=0;
	ifstream infile;
	infile.open("test.txt");
	if(!infile)
	{
		cerr<<"open error!!"<<endl;
		exit(1);
	}
	while(infile)
	{
		infile>>a[i];i++;
	}
	infile.close();
	cout<<"\n********************��������*********************"<<endl;
	for(int j=0;j<=i;j++)
	{
		analyse(a[j]);
	}
	cout<<endl;     
	return 0;
} 
