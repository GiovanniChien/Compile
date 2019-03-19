package com.edu.zhengze;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class Zhengze {

	/**
	 * @param args
	 */
	private String  expression;
	private String alpha=new String();
	private ArrayList<Data> temp;
	private ArrayList<Data> result;
	private ArrayList<Data> result_dfa=new ArrayList<Data>();
	private ArrayList<Data> result_mfa=new ArrayList<Data>();
	private ArrayList<TreeSet<Integer> > states_dfa=new ArrayList<TreeSet<Integer> >();
	private ArrayList<Integer>  end_mfa=new ArrayList<Integer>();
	private ArrayList<Data> empty;
	private boolean errorExpression=false;
	
	private int num=2;//  1是开始状态，２是结束状态
	
	Zhengze(String s)
	{
		expression=s;
		temp=new ArrayList<Data>();
		result=new ArrayList<Data>();
	}
	
	private void change(int from ,String s,int to)
	{
		int flag_kh=0;
		int i=0;//看是否有括号外的 '|'  
		while(i<s.length())
		{
			if(s.charAt(i)=='(')
				flag_kh++;
			if(s.charAt(i)==')')
				flag_kh--;
			if(s.charAt(i)=='|' && flag_kh==0)
				break;
			++i;
		}
		
		int j=0;//看是否有括号外的 '.'  
		flag_kh=0;
		while(j<s.length())
		{
			if(s.charAt(j)=='(')
				flag_kh++;
			if(s.charAt(j)==')')
				flag_kh--;
			if(s.charAt(j)=='.' && flag_kh==0)
				break;
			j++;
		}
		
		int k=0;//看是否有括号外的 '*'  
		flag_kh=0;
		while(k<s.length())
		{
			if(s.charAt(k)=='(')
				flag_kh++;
			if(s.charAt(k)==')')
				flag_kh--;
			if(s.charAt(k)=='*' && flag_kh==0)
				break;
			k++;
		}
		
		if(i!=s.length())//有括号外的 '|'  
		{
			String s1=s.substring(0, i);
			if(s1.length()==1)   //一个字符，就存到结果集中
				result.add(new Data(from,s1,to));
			else
				temp.add(new Data(from,s1,to));
			
			String s2=s.substring(i+1);
			if(s2.length()==1)   //一个字符，就存到结果集中
				result.add(new Data(from,s2,to));
			else
				temp.add(new Data(from,s2,to));
		}
		
		else if(j!=s.length())//有括号外的 '.'  
		{
			String s1=s.substring(0, j);
			if(s1.length()==1)   //一个字符，就存到结果集中
				result.add(new Data(from,s1,++num));
			else
				temp.add(new Data(from,s1,++num));
			
			String s2=s.substring(j+1);
			if(s2.length()==1)   //一个字符，就存到结果集中
				result.add(new Data(num,s2,to));
			else
				temp.add(new Data(num,s2,to));
		}
		
		
		else if(k!=s.length())//有括号外的 '*'  
		{
			int flag1=0;
			int flag2=0;
			String s1;
			if(s.charAt(k-1)!=')')
				s1=""+s.charAt(k-1);
			else {
				int pos=k-1;
				while(pos>=0)
				{
					if(s.charAt(pos)==')')
					{	
						flag1++;   //')'的个数
					    pos--;
					}
					else 
						break;
				}
				pos=0;
				while(pos<=k-1)
				{
					if(s.charAt(pos)=='(')
					{
						flag2++;   //'('的个数
						pos++;
					}
					else 
						break;
				}
				if(flag1<flag2)  //pos等于　'（'个数和'）'个数中小的值
					pos=flag1;
				else
					pos=flag2;
				s1=s.substring(pos, k-pos);
			}
			result.add(new Data(from,"",++num));
			result.add(new Data(num,"",to));
		    if(s1.length()==1)
		    {
		    	result.add(new Data(num,s1,num));
		    }else{
		    	temp.add(new Data(num,s1,num));
		    }		
		}
		
		else if(s.length()>=1&&s.charAt(0)=='('&&s.charAt(s.length()-1)==')')//括号外没有运算符　则把括号去了
		{
			String s1=s.substring(1, s.length()-1);
			if(s1.length()==1)//'('和')'中间只有一个字符
				result.add(new Data(from ,s1,to));
			else
				temp.add(new Data(from ,s1,to));
		}
		
		else{
			System.out.println("error");//否则不是正确形式的正则表达式
			errorExpression=true;
		}
		
	}
	
	public void nfa()
	{
		if(!errorExpression)
		{
		if(expression=="")
			System.out.print("该正则式是空集");
		else if(expression.length()==1)
			result.add(new Data(1,expression,2));
		else {
			temp.add(new Data(1,expression,2));
			while(!temp.isEmpty())//当temp不空时
			{
				Data temp2=temp.get(0);
				temp.remove(0);
				change(temp2.getIndex1(),temp2.getInfo(),temp2.getIndex2());
			}
		}
		}
	}
	
	public void display_nfa()
	{ 
		if(!errorExpression)
		{
		System.out.println("nfa为：");
		System.out.println("开始状态是１，结束状态是２：");
		for(int i=0;i!=result.size();i++)
		{
			System.out.println(""+result.get(i));
		}
		}
	}
	/*************************************************************/
	private void suan_empty()//empty记录所有的空边
	{
		empty=new ArrayList<Data>();
		for(int i=0;i!=result.size();i++)
		{
			if(result.get(i).match_2(""))
				empty.add(result.get(i));
		}
	}
	private void suan_alpha()
	{
		for(int i=0;i!=expression.length();i++)
		{
			String s=""+expression.charAt(i);
			if(s.equals(".")||s.equals("*")||s.equals("|"))
				continue;
			if(alpha.indexOf(expression.charAt(i))==-1)//字符集中没有该字符
			      alpha=alpha+expression.charAt(i);  //则加入
		}
	}
	
    //	用之前要用suan_empty();
	private void add_empty_state(TreeSet<Integer> state,int index)//递归的
	{
		for(int i=0;i!=empty.size();i++)
		{
			if(empty.get(i).match_1_2(index, ""))//如果index经过空边到达某状态p
			{
				state.add(empty.get(i).getIndex2());//则把p加入状态集
				add_empty_state(state,empty.get(i).getIndex2());//再考察p是否经过空边到达某状态
			}
		}
	}
	
	private TreeSet<Integer> find(int index,String s)//由状态index经过s，能达到的所有状态的集合
	{
		TreeSet<Integer> state=new TreeSet<Integer>();
		for(int i=0;i!=result.size();i++)
		{
			if(result.get(i).match_1_2(index, "") )//该状态有空边  *****************
			{
				int index2=result.get(i).getIndex2();
				state.addAll(find(index2,s));
			}                                       //***************
				
			if(result.get(i).match_1_2(index,s))
			{
				state.add(result.get(i).getIndex2());
				add_empty_state(state,result.get(i).getIndex2());//因为是e边闭包
			}
			
		}
		return state;
	}
	
	private TreeSet<Integer> findall(TreeSet<Integer> t,String s)//状态集t经过s得到的状态集
	{
		TreeSet<Integer> state=new TreeSet<Integer>();
		Iterator<Integer> iter=t.iterator();
		while(iter.hasNext())
		{
			int index=iter.next();
			TreeSet<Integer> temp=find(index,s);
			state.addAll(temp);
			/*System.out.println("index="+index+" s="+s);
			Iterator iter2=temp.iterator();
			while(iter2.hasNext())
				System.out.println(iter2.next());
			System.out.println();*/
		}
		return state;
	}
 	
	private int is_in_states_dfa(TreeSet<Integer> t)//察看状态集t是否已经存在于states_dfa
	{
		for(int i=0;i!=states_dfa.size();i++)
		{
			if(states_dfa.get(i).equals(t))
				return i;
		}
		return -1;
	}
	
	public void dfa()//####################
	{
		if(!errorExpression)
		{
		suan_empty();//得到所有空边的集合
		suan_alpha();//得到字符集
		ArrayList<TreeSet<Integer> > temp=new ArrayList<TreeSet<Integer> >();
		TreeSet<Integer> start=new TreeSet<Integer>();
		start.add(1);
		add_empty_state(start,1);
		/*Iterator iter=empty.iterator();
		System.out.println();
		while(iter.hasNext())
			System.out.print(iter.next());
		System.out.println();*/
		states_dfa.add(start);
		/*Iterator iter=start.iterator();
		System.out.println("start");
		while(iter.hasNext())
			System.out.println(iter.next());
		System.out.println();*/
		temp.add(start);
		while(!temp.isEmpty())
		{
			TreeSet<Integer> t=temp.get(0);
			temp.remove(0);
			int index1=is_in_states_dfa(t);
			for(int i=0;i!=alpha.length();i++)
			{
				TreeSet<Integer> newstate=findall(t,""+alpha.charAt(i));
				int index2;
				index2=is_in_states_dfa(newstate);//如果该状态出现过，则得到它在状态表中的下标
				if (newstate.size()!=0 && index2==-1)//该状态集不是空集　并且　该状态没出现过
				{
					states_dfa.add(newstate);
					temp.add(newstate);
					index2=states_dfa.size()-1;//因为新状态是从状态表末尾加入的，所以它的下标是size()-1
				}
				if(newstate.size()!=0)//该状态集不是空集
				    result_dfa.add(new Data(index1,""+alpha.charAt(i),index2) );	
			}
		}	
		}
	}
	
	public void display_dfa()
	{
		if(!errorExpression)
		{
		ArrayList<Integer> end=endNode();
		System.out.print("dfa为：");
		System.out.print("开始状态为０,　结束状态为 ");
		for(int j=0;j!=end.size();j++)
			System.out.print(end.get(j)+" ,");
		System.out.println();
		for(int i=0;i!=result_dfa.size();i++)
		{
			System.out.println(""+result_dfa.get(i));
		}
		}
	}
	
	private ArrayList<Integer> endNode()
	{
		ArrayList<Integer> end=new ArrayList<Integer>();
		for(int i=0;i!=states_dfa.size();i++)
		{
			Iterator<Integer> iter=states_dfa.get(i).iterator();
			while(iter.hasNext())
			{
				if(iter.next()==2)
				{
					end.add(i);
					break;
				}
			}
		}
		return end;
	}
	/****************************************************************/
	private ArrayList<ArrayList<Integer> > temp_mfa=new ArrayList<ArrayList<Integer> >();
	
	private void init_mfa()
	{
		ArrayList<Integer> temp1=endNode();
		temp_mfa.add(temp1);//这是结束状态集
		ArrayList<Integer> temp2=new ArrayList<Integer>();
		for(int i=0;i!=states_dfa.size();i++)
		{
			if(!temp1.contains(i))
				temp2.add(i);
		}
		temp_mfa.add(temp2);//这是非结束状态集	
		/*System.out.println("init_mfa()");
		for(int j=0;j!=temp_mfa.size();j++)
		{
			ArrayList<Integer> q=temp_mfa.get(j);
			for(int k=0;k!=q.size();k++)
				System.out.print(q.get(k));
			System.out.println();
		}*/
			
		
	}
	
	private ArrayList<Integer> find_dfa(int index)//在dfa的result_dfa中找由状态index经过所有字符集中的元素，能达到的状态集合
	{                                                     //生成的dfa不会有空边,且通过某字符能到达的状态最多一个
		ArrayList<Integer> state=new ArrayList<Integer>();
		for(int j=0;j!=alpha.length();++j)
		{
			String s=""+alpha.charAt(j);
		    for(int i=0;i!=result_dfa.size();i++)
		   {
			   if(result_dfa.get(i).match_1_2(index,s))
				   state.add(result_dfa.get(i).getIndex2());	
		   }
		}
		/*System.out.println("find_dfa("+index+")");
		for(int p=0;p!=state.size();p++)
			System.out.print(state.get(p));
		System.out.println();*/
		return state;
	}
	
	private void suan_temp_mfa()//确定状态小组的划分
	{
		end_mfa.add(0);
		int flag=1;
		while(true)
		{
		  int count=temp_mfa.size();
		  flag=1;
		  for(int i=0;i!=count;++i)
		 {
			  ArrayList<Integer> p=temp_mfa.get(i);//某个状态的分组
			  if(p.size()!=1)//当状态分组中不止一个状态时，需考察下
			  {
			  for(int j=p.size()-1;j!=-1;--j)//该状态分组中的每个状态,从最后一个状态开始，方便删除
			  {
				  if(p.size()>1)
				  {
				   ArrayList<Integer> temp=find_dfa(p.get(j));//状态p.get(j)经过一个字符所能达到的状态集合
				   for(int k=0;k!=temp.size();k++)
				   {
					   if(!p.contains(temp.get(k)))//状态p.get(j)到达的状态没有落在它所在的状态集合小组中
					   {
						   ArrayList<Integer> new_state_collecton=new ArrayList<Integer>();
						   new_state_collecton.add(p.get(j));
						   temp_mfa.get(i).remove(j);//原状态小组中p.get(j)状态删除
						   temp_mfa.add(new_state_collecton);//该状态独立成为一个小组
						   flag=0;//说明状态小组变动过
						   if(end_mfa.contains(i))//如果该状态原来属于结束状态小组，那它独立后也是结束状态小组
							   end_mfa.add(temp_mfa.size()-1);
						   break;
					   }//if
				   }//for
				  }
			  }//for
			  }
		  }//for
		  if(flag==1)
			  break;//状态小组在考察过一轮后，没有发生变化，则状态小组确定
		}//while
		/*System.out.println("suan_temp_mfa()");
		for(int j=0;j!=temp_mfa.size();j++)
		{
			ArrayList<Integer> q=temp_mfa.get(j);
			for(int k=0;k!=q.size();k++)
				System.out.print(q.get(k));
			System.out.println();
		}*/
	}
	
	private int search(int index)//把原来dfa中的状态号，变为状态组号
	{
		for(int i=0;i!=temp_mfa.size();++i)
		{
			for(int j=0;j!=temp_mfa.get(i).size();++j)
			{
				if(temp_mfa.get(i).get(j)==index)
					return i;//返回组号
			}
		}
		return -1;//没找到，返回-1
		
	}
	public void mfa()
    {
		if(!errorExpression)
		{
		init_mfa();
		suan_temp_mfa();
		//result_mfa.addAll(result_dfa);
		for(int i=0;i!=result_dfa.size();i++)
		{
		    int index1=search(result_dfa.get(i).getIndex1());//把原来的状态号变为组号
			int index2=search(result_dfa.get(i).getIndex2());
			String s=result_dfa.get(i).getInfo();
			Data new_data=new Data(index1,s,index2);
			if(!result_mfa.contains(new_data))//若没有该条边，就加入
			{
				result_mfa.add(new_data);
			}
		}
		}
	}
	
	public void display_mfc()
	{
		if(!errorExpression)
		{
			System.out.println("mfa为：");
			System.out.print("开始状态为:");
		l1:	for(int i1=0;i1!=temp_mfa.size();++i1)
			{
				for(int j1=0;j1!=temp_mfa.get(i1).size();++j1)
				{
					if(temp_mfa.get(i1).get(j1)==0)
					{
						System.out.print(i1);
						break l1;
					}
					
				}
			}
			System.out.print("结束状态为: ");
			for(int j=0;j!=end_mfa.size();j++)
				System.out.print(end_mfa.get(j)+" ,");
			System.out.println();
			for(int i=0;i!=result_mfa.size();i++)
			{
				System.out.println(""+result_mfa.get(i));
			}
		}
		
	}
	
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		Scanner in=new Scanner(System.in);
		String s=in.next();
		Zhengze a=new Zhengze(s);
		a.nfa();
		a.display_nfa();
		System.out.println();
		a.dfa();
		a.display_dfa();
		System.out.println();
		a.mfa();
		a.display_mfc();
	}
	/**************************************************************/
	class Data{
		private int index1;
		private String info;
		private int index2;
		
		Data(int _index1,String _info,int _index2)
		{
			index1=_index1;
			info=_info;
			index2=_index2;
		}
		public int getIndex1() {
			return index1;
		}
		public void setIndex1(int index1) {
			this.index1 = index1;
		}
		@Override
		public boolean equals(Object obj) {
			Data a=(Data)obj;
			if(this.toString().equals(obj.toString()))
			    return true;
			return false;
		}
		public int getIndex2() {
			return index2;
		}
		public void setIndex2(int index2) {
			this.index2 = index2;
		}
		public String getInfo() {
			return info;
		}
		public void setInfo(String info) {
			this.info = info;
		}
		
		public boolean match_1_2(int index,String s)//index状态经过s能变为别的状态
		{
			if(index==index1&&s.equals(info))
				return true;
			return false;
		}
		
		public boolean match_2(String s)//某状态经过s能变为别的状态
		{
			if(s.equals(info))
				return true;
			return false;
		}
		@Override
		public String toString() {
			// TODO 自动生成方法存根
			return ""+index1+"--"+info+"-->"+index2;
		}
		
		
	}

}
