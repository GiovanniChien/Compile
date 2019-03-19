import java.io.IOException;
import java.util.ArrayList;


public class SLR {
	private ArrayList<String> content=new ArrayList<String>();
	private ArrayList<State> stateArr=new ArrayList<State>();
	private Tools t=new Tools();
	private State state;
	private Tools tool=new Tools();
	private int num=0;                      //通过可接收字符能到达的下一个项目集序号
	public SLR() throws IOException
	{
		content=t.pretreat("d:\\3.txt");	
	}
	
	//判断产生式能否到达空串
	public boolean toEmptySymbol(String str)
	{
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='$')
			{
				return true;
			}
		}	
		return false;
	}
	
	 //对每一个产生式规则添加.
	public void slrPre()                              
	{
		for(int i=0;i<content.size();i++)
		{			
			if(!toEmptySymbol(content.get(0)))
			{
				String string=content.get(0).substring(0, 3)+"."+content.get(0).substring(3, content.get(0).length());
				content.add(string);
				content.remove(0);
			}
			else
			{
				String string=content.get(0).substring(0, 3)+".";
				content.add(string);
				content.remove(0);
			}
		}
	}
	
	//得到初始状态的项目集
	public void setStartState()
	{
		state=new State();
		state.state=getAllStart();
		getAccChar(state);
		stateArr.add(state);
		num++;
	}
	
	//判断最后一位是否为.
	public boolean isEnd(String str)
	{
		if(str.charAt(str.length()-1)=='.')
			return true;
		return false;
	}
	
	//判断是否到达Acc
	public boolean isAcc(String str)
	{
		if(str.charAt(0)==content.get(0).charAt(0))
			return true;
		return false;
	}
	
	//得到.的下标
	public int getPointIndex(String string)
	{
		for(int i=0;i<string.length();i++)
		{
			if(string.charAt(i)=='.')
			{
				return i;
			}
		}
		return -1;
	}
	
	// 寻找初始项目集中所有的状态
	public ArrayList<String> getAllStart()
	{
		ArrayList<String> tempState=new ArrayList<String>();
		for(String str:content)
		{
			if(tool.isMulitipart(str))
			{
				String temp=str.replace('|', '@');
				String strTemp[]=temp.split("@");
				strTemp[1]=""+str.charAt(0)+str.charAt(1)+str.charAt(2)+"."+strTemp[1];
				tempState.add(strTemp[0]);
				tempState.add(strTemp[1]);
			}
			else
			{
				tempState.add(str);
			}					
				
		}	
		return tempState;
	}
	
	//寻找可接收的字符
	public void getAccChar(State s)
	{
		for(String str:s.state)
		{
			int index=getPointIndex(str);
			if(index==str.length()-1)
			{
				s.accChar.add('*');
				s.isR=true;
				continue;
			}
			s.accChar.add(str.charAt(index+1));
			s.isS=true;
		}
	}
	
	//得到从上一项目集扩展出来的所有项目集
	
	public void getAllState(State s)
	{
		for(char ch:s.accChar)
		{
			if(ch=='*')
			{
				s.arrNum.add(-1);
			}
			else
			{
				state=new State();
				for(String str:s.state)
				{
					for(int i=0;i<str.length()-1;i++)
					{
						if(str.charAt(i)=='.'&&str.charAt(i+1)==ch)
						{
							StringBuffer sb=new StringBuffer(str);
							sb.setCharAt(i, str.charAt(i+1));
							sb.setCharAt(i+1, '.');
							String string=sb.toString();
							state.state=addToState(string);
						}
					}	
				}	
				int stateIndex=isExist(state);
				if(stateIndex==-1)
				{
					getAccChar(state);
					stateArr.add(state);			
					s.arrNum.add(num);
					num++;
				}
				else
					s.arrNum.add(stateIndex);
			}
			
		}
		
	}
	
	
	//得到扩展项目集的所有状态
	public ArrayList<String> addToState(String str)
	{
		ArrayList<String> stateTmp=new ArrayList<String>();
		stateTmp.add(str);
		int index=getPointIndex(str);
		if(index==str.length()-1)
		{
			return stateTmp;
		}
		String temp=str.replace('.', '@');
		String strTmp[]=temp.split("@");
		for(String s:stateArr.get(0).state)
		{
			if(strTmp[1].charAt(0)==s.charAt(0))
			{
				stateTmp.add(s);
			}
		}
		return stateTmp;
	}
	
	
	//查找新生成的项目集是否已存在
	public int isExist(State s)
	{	
		for(int index=0;index<stateArr.size();index++)
		{
			int flag=0;
			for(int i=0;i<s.state.size();i++)
			{
			   for(int j=0;j<stateArr.get(index).state.size();j++)
			   {
				   if(s.state.get(i).equals(stateArr.get(index).state.get(j)))
				   {
					   flag++;
				   }
			   }
			}
			if(flag==s.state.size())
				return index;
		}
		return -1;
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SLR slr=new SLR();
		slr.slrPre();
		slr.setStartState();
		for(int i=0;i<slr.stateArr.size();i++)
		{
			slr.getAllState(slr.stateArr.get(i));
		}		
	}
	
}
