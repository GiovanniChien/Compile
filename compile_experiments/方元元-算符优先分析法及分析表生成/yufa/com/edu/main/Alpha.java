package com.edu.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Alpha {

	/**
	 * @param args
	 */
	private static ArrayList<Character>  feiZhong=new ArrayList<Character>();
	private static ArrayList<Character>  operators=new ArrayList<Character>();
	
	public static void setFeiZhong(TreeMap<Character,TreeSet<String> > map)
	{
		 Set<Character> temp=map.keySet();//�������з��ս��
		 feiZhong.addAll(temp);
		 Iterator iter=temp.iterator();//�Ȱ������ս��,���ս������operators��
		 while(iter.hasNext())
		 {
			 Character c=(Character) iter.next();
			 TreeSet<String> temp2=map.get(c);
			 Iterator iter2=temp2.iterator();
			 while(iter2.hasNext())
			 {
				 String s=(String) iter2.next();
				 for(int i=0;i<s.length();++i)
				 {
					 Character  c2=s.charAt(i);
					 if(operators.indexOf(c2)==-1)//û����������ĸ
					 {
						 operators.add(c2);
					 }
				 }//for
			 }//while
		 }//while
		 operators.removeAll(feiZhong); 
	}
	
	public static void display()
	{
		System.out.print("���ս��Ϊ: ");
		for(int i=0;i!=feiZhong.size();++i)
			System.out.print(feiZhong.get(i)+"  ");
		System.out.println();
		System.out.print("���Ϊ: ");
		for(int i=0;i!=operators.size();++i)
			System.out.print(operators.get(i)+"  ");
		System.out.println();
	}
	
	public static ArrayList<Character> getFeiZhong() {
		return feiZhong;
	}

	public static ArrayList<Character> getOperators() {
		return operators;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GuiZe.setMap();
		GuiZe.display();
		TreeMap<Character,TreeSet<String> > map=GuiZe.getMap();
		Alpha.setFeiZhong(map);
		Alpha.display();
	}
}
