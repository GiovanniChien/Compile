#include <stdio.h>
int a,re=6;
char c='7',d,f;
float pi=3.14;

int fun(int b,char c='p')
{
	int a = 5;
	a = 3;
	{
		int b = 9;
	}
}

int ab(){}
void main2()
{
	int aa = 1,3a=734;
	unsigned short 8b = 4;
	
	printf("in layer 1 a = %d \n",a);
	{
		int a,b;
		printf("in layer 2 a = %d\n",a);
		{
			int a=3;
			printf("in layer 3 a = %d\n",a);	
			{
				printf("in layer 4 a = %d\n",a);	
				{
					int a = 5;
					printf("in layer 5 a = %d\n",a);	
				}
			}
		}		
		printf("in layer 2 a = %d\n",a);
	}	
	
	int i=2;
	printf("%d",i);
	for(int i = 1;i<5;++i)
	
		//int i = 2;
		printf("%d",i);
	
	if(a == 1)
	{
		int a = 3;
		printf("in layer 2 a = %d\n",a);
	}
	else a=5;
	

	if(a == 1)
   	   if(a == 2)
		if(a == 3)
		{
			if(a == 4) a = 1;
			else if(a == 5) a = 2;
			else if(a == 6) a = 3;
			else a=4;
			a = 1;
			else a=3;
			a = 2;
			a = 3;
		}
		else a=3;
	 else a=9;
	else a =10;
	else a=11;

	int a=3;
	if(a==4) a=3;
	printf("in layer 1 a = %d\n ",a);
}

int b=9;