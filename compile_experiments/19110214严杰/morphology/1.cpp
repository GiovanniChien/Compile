#include <iostream>
using namespace std;

int main()
{
	int x1,x2,y1,y2;
	int dx,dy;
	cin>>x1>>y1>>x2>>y2;
	
	if (x1==x2 && y1==y2)
	{
		cout<<"CLICK"<<endl;
	}
	else
	{
		if (x1>x2)//LEFT
		{
			dx=x1-x2;
			if (y1>y2)//UP
			{
				dy=y1-y2;
				if (dx>=dy)
					cout<<"LEFT"<<endl;
				else
					cout<<"UP"<<endl;
			}
			else//DOWN
			{
				dy=y2-y1;
				if (dx>=dy)
					cout<<"LEFT"<<endl;
				else
					cout<<"DOWN"<<endl;
			}
		}
		else//RIGHT
		{
			dx=x2-x1;
			if (y1>y2)//UP
			{
				dy=y1-y2;
				if (dx>=dy)
					cout<<"RIGHT"<<endl;
				else
					cout<<"UP"<<endl;
			}
			else//DOWN
			{
				dy=y2-y1;
				if (dx>=dy)
					cout<<"RIGHT"<<endl;
				else
					cout<<"DOWN"<<endl;
			}
		}
	}
	return 0;
}
