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
	
	private int num=2;//  1�ǿ�ʼ״̬�����ǽ���״̬
	
	Zhengze(String s)
	{
		expression=s;
		temp=new ArrayList<Data>();
		result=new ArrayList<Data>();
	}
	
	private void change(int from ,String s,int to)
	{
		int flag_kh=0;
		int i=0;//���Ƿ���������� '|'  
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
		
		int j=0;//���Ƿ���������� '.'  
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
		
		int k=0;//���Ƿ���������� '*'  
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
		
		if(i!=s.length())//��������� '|'  
		{
			String s1=s.substring(0, i);
			if(s1.length()==1)   //һ���ַ����ʹ浽�������
				result.add(new Data(from,s1,to));
			else
				temp.add(new Data(from,s1,to));
			
			String s2=s.substring(i+1);
			if(s2.length()==1)   //һ���ַ����ʹ浽�������
				result.add(new Data(from,s2,to));
			else
				temp.add(new Data(from,s2,to));
		}
		
		else if(j!=s.length())//��������� '.'  
		{
			String s1=s.substring(0, j);
			if(s1.length()==1)   //һ���ַ����ʹ浽�������
				result.add(new Data(from,s1,++num));
			else
				temp.add(new Data(from,s1,++num));
			
			String s2=s.substring(j+1);
			if(s2.length()==1)   //һ���ַ����ʹ浽�������
				result.add(new Data(num,s2,to));
			else
				temp.add(new Data(num,s2,to));
		}
		
		
		else if(k!=s.length())//��������� '*'  
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
						flag1++;   //')'�ĸ���
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
						flag2++;   //'('�ĸ���
						pos++;
					}
					else 
						break;
				}
				if(flag1<flag2)  //pos���ڡ�'��'������'��'������С��ֵ
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
		
		else if(s.length()>=1&&s.charAt(0)=='('&&s.charAt(s.length()-1)==')')//������û����������������ȥ��
		{
			String s1=s.substring(1, s.length()-1);
			if(s1.length()==1)//'('��')'�м�ֻ��һ���ַ�
				result.add(new Data(from ,s1,to));
			else
				temp.add(new Data(from ,s1,to));
		}
		
		else{
			System.out.println("error");//��������ȷ��ʽ��������ʽ
			errorExpression=true;
		}
		
	}
	
	public void nfa()
	{
		if(!errorExpression)
		{
		if(expression=="")
			System.out.print("������ʽ�ǿռ�");
		else if(expression.length()==1)
			result.add(new Data(1,expression,2));
		else {
			temp.add(new Data(1,expression,2));
			while(!temp.isEmpty())//��temp����ʱ
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
		System.out.println("nfaΪ��");
		System.out.println("��ʼ״̬�ǣ�������״̬�ǣ���");
		for(int i=0;i!=result.size();i++)
		{
			System.out.println(""+result.get(i));
		}
		}
	}
	/*************************************************************/
	private void suan_empty()//empty��¼���еĿձ�
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
			if(alpha.indexOf(expression.charAt(i))==-1)//�ַ�����û�и��ַ�
			      alpha=alpha+expression.charAt(i);  //�����
		}
	}
	
    //	��֮ǰҪ��suan_empty();
	private void add_empty_state(TreeSet<Integer> state,int index)//�ݹ��
	{
		for(int i=0;i!=empty.size();i++)
		{
			if(empty.get(i).match_1_2(index, ""))//���index�����ձߵ���ĳ״̬p
			{
				state.add(empty.get(i).getIndex2());//���p����״̬��
				add_empty_state(state,empty.get(i).getIndex2());//�ٿ���p�Ƿ񾭹��ձߵ���ĳ״̬
			}
		}
	}
	
	private TreeSet<Integer> find(int index,String s)//��״̬index����s���ܴﵽ������״̬�ļ���
	{
		TreeSet<Integer> state=new TreeSet<Integer>();
		for(int i=0;i!=result.size();i++)
		{
			if(result.get(i).match_1_2(index, "") )//��״̬�пձ�  *****************
			{
				int index2=result.get(i).getIndex2();
				state.addAll(find(index2,s));
			}                                       //***************
				
			if(result.get(i).match_1_2(index,s))
			{
				state.add(result.get(i).getIndex2());
				add_empty_state(state,result.get(i).getIndex2());//��Ϊ��e�߱հ�
			}
			
		}
		return state;
	}
	
	private TreeSet<Integer> findall(TreeSet<Integer> t,String s)//״̬��t����s�õ���״̬��
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
 	
	private int is_in_states_dfa(TreeSet<Integer> t)//�쿴״̬��t�Ƿ��Ѿ�������states_dfa
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
		suan_empty();//�õ����пձߵļ���
		suan_alpha();//�õ��ַ���
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
				index2=is_in_states_dfa(newstate);//�����״̬���ֹ�����õ�����״̬���е��±�
				if (newstate.size()!=0 && index2==-1)//��״̬�����ǿռ������ҡ���״̬û���ֹ�
				{
					states_dfa.add(newstate);
					temp.add(newstate);
					index2=states_dfa.size()-1;//��Ϊ��״̬�Ǵ�״̬��ĩβ����ģ����������±���size()-1
				}
				if(newstate.size()!=0)//��״̬�����ǿռ�
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
		System.out.print("dfaΪ��");
		System.out.print("��ʼ״̬Ϊ��,������״̬Ϊ ");
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
		temp_mfa.add(temp1);//���ǽ���״̬��
		ArrayList<Integer> temp2=new ArrayList<Integer>();
		for(int i=0;i!=states_dfa.size();i++)
		{
			if(!temp1.contains(i))
				temp2.add(i);
		}
		temp_mfa.add(temp2);//���Ƿǽ���״̬��	
		/*System.out.println("init_mfa()");
		for(int j=0;j!=temp_mfa.size();j++)
		{
			ArrayList<Integer> q=temp_mfa.get(j);
			for(int k=0;k!=q.size();k++)
				System.out.print(q.get(k));
			System.out.println();
		}*/
			
		
	}
	
	private ArrayList<Integer> find_dfa(int index)//��dfa��result_dfa������״̬index���������ַ����е�Ԫ�أ��ܴﵽ��״̬����
	{                                                     //���ɵ�dfa�����пձ�,��ͨ��ĳ�ַ��ܵ����״̬���һ��
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
	
	private void suan_temp_mfa()//ȷ��״̬С��Ļ���
	{
		end_mfa.add(0);
		int flag=1;
		while(true)
		{
		  int count=temp_mfa.size();
		  flag=1;
		  for(int i=0;i!=count;++i)
		 {
			  ArrayList<Integer> p=temp_mfa.get(i);//ĳ��״̬�ķ���
			  if(p.size()!=1)//��״̬�����в�ֹһ��״̬ʱ���迼����
			  {
			  for(int j=p.size()-1;j!=-1;--j)//��״̬�����е�ÿ��״̬,�����һ��״̬��ʼ������ɾ��
			  {
				  if(p.size()>1)
				  {
				   ArrayList<Integer> temp=find_dfa(p.get(j));//״̬p.get(j)����һ���ַ����ܴﵽ��״̬����
				   for(int k=0;k!=temp.size();k++)
				   {
					   if(!p.contains(temp.get(k)))//״̬p.get(j)�����״̬û�����������ڵ�״̬����С����
					   {
						   ArrayList<Integer> new_state_collecton=new ArrayList<Integer>();
						   new_state_collecton.add(p.get(j));
						   temp_mfa.get(i).remove(j);//ԭ״̬С����p.get(j)״̬ɾ��
						   temp_mfa.add(new_state_collecton);//��״̬������Ϊһ��С��
						   flag=0;//˵��״̬С��䶯��
						   if(end_mfa.contains(i))//�����״̬ԭ�����ڽ���״̬С�飬����������Ҳ�ǽ���״̬С��
							   end_mfa.add(temp_mfa.size()-1);
						   break;
					   }//if
				   }//for
				  }
			  }//for
			  }
		  }//for
		  if(flag==1)
			  break;//״̬С���ڿ����һ�ֺ�û�з����仯����״̬С��ȷ��
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
	
	private int search(int index)//��ԭ��dfa�е�״̬�ţ���Ϊ״̬���
	{
		for(int i=0;i!=temp_mfa.size();++i)
		{
			for(int j=0;j!=temp_mfa.get(i).size();++j)
			{
				if(temp_mfa.get(i).get(j)==index)
					return i;//�������
			}
		}
		return -1;//û�ҵ�������-1
		
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
		    int index1=search(result_dfa.get(i).getIndex1());//��ԭ����״̬�ű�Ϊ���
			int index2=search(result_dfa.get(i).getIndex2());
			String s=result_dfa.get(i).getInfo();
			Data new_data=new Data(index1,s,index2);
			if(!result_mfa.contains(new_data))//��û�и����ߣ��ͼ���
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
			System.out.println("mfaΪ��");
			System.out.print("��ʼ״̬Ϊ:");
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
			System.out.print("����״̬Ϊ: ");
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
		// TODO �Զ����ɷ������
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
		
		public boolean match_1_2(int index,String s)//index״̬����s�ܱ�Ϊ���״̬
		{
			if(index==index1&&s.equals(info))
				return true;
			return false;
		}
		
		public boolean match_2(String s)//ĳ״̬����s�ܱ�Ϊ���״̬
		{
			if(s.equals(info))
				return true;
			return false;
		}
		@Override
		public String toString() {
			// TODO �Զ����ɷ������
			return ""+index1+"--"+info+"-->"+index2;
		}
		
		
	}

}
