package com.edu.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReSultTable {

	/**
	 * @param args
	 */
	private static ArrayList<Character>  feiZhong;
	private static ArrayList<Character>  operators;
	private static TreeMap<Character,TreeSet<String> > wenFa;
	private static Character resultTable[][];
	
	static {
		    GuiZe.setMap();
		    wenFa=GuiZe.getMap();
		    Alpha.setFeiZhong(wenFa);
		    feiZhong=Alpha.getFeiZhong();
		    operators=Alpha.getOperators();
		    resultTable=new Character[operators.size()][operators.size()];
		    for(int i=0;i!=operators.size();i++)//resultTableÿһ���ʼ��Ϊ' '
		    	for(int j=0;j!=operators.size();j++)
		    	{
		    		resultTable[i][j]=' ';
		    	}
	    }
	
	public static void creatTable()
	{
		for (Map.Entry<Character, TreeSet<String>> word : wenFa.entrySet()) {
			Character key = word.getKey();
			TreeSet<String> value = word.getValue();
			Iterator iter=value.iterator();
			while(iter.hasNext())
			{
				String s=(String) iter.next();//ȡ��һ��������Ҳ�
				int i;
				for( i=0;i!=s.length()-1;i++)
				{
					Character c1=s.charAt(i);
					Character c2=s.charAt(i+1);
					Character c3;int index3=-1;
					if(i<s.length()-2)
					{
						c3=s.charAt(i+2);
						index3=operators.indexOf(c3);
					}
					int index1=operators.indexOf(c1);
					int index2=operators.indexOf(c2);
					
					
					if(index1!=-1&&index2!=-1)//c1c2�����ŵ����
						resultTable[index1][index2]='=';
					if(index1!=-1&&index2==-1)//c1�����,c2�Ƿ��ս��
					{//��F::=(E) '('һ����,��firstTerm(E)���������λ����'<'
						ArrayList<Character> firstTerm=new ArrayList<Character>();
						Term a=new Term(feiZhong,operators,wenFa);
						a.firstTerm(c2, firstTerm);	
						for(int ii=0;ii!=firstTerm.size();++ii)
							resultTable[index1][operators.indexOf(firstTerm.get(ii))]='<';
						if(i!=s.length()-1&&index3!=-1)//c1�����,c2�Ƿ��ս��,c3���
							resultTable[index1][index3]='=';
					}
					if(index1==-1&&index2!=-1)//c1�Ƿ��ս��,c2�����
					{//��F::=(E) ')'һ����,��lastTerm(E)���������λ����'>'
						ArrayList<Character> lastTerm=new ArrayList<Character>();
						Term a=new Term(feiZhong,operators,wenFa);
						a.lastTerm(c1, lastTerm);
						for(int ii=0;ii!=lastTerm.size();++ii)
							resultTable[operators.indexOf(lastTerm.get(ii))][index2]='>';
					}
				}//for
			}
		}
	}
	
	public static void display()
	{
		System.out.print("   ");
		for(int i=0;i!=operators.size();i++)
		{
			System.out.print(operators.get(i)+"  ");
		}
		System.out.println();
		for(int p=0;p!=operators.size();p++)
		{
			System.out.print(operators.get(p)+"  ");
			for(int q=0;q!=operators.size();q++)
			{
				System.out.print(resultTable[p][q]+"  ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReSultTable.creatTable();
		ReSultTable.display();
	}

}
