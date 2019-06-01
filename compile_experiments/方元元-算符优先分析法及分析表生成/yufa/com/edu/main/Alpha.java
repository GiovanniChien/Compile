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
		 Set<Character> temp=map.keySet();//加入所有非终结符
		 feiZhong.addAll(temp);
		 Iterator iter=temp.iterator();//先把所有终结符,非终结符加入operators中
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
					 if(operators.indexOf(c2)==-1)//没遇到过该字母
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
		System.out.print("非终结符为: ");
		for(int i=0;i!=feiZhong.size();++i)
			System.out.print(feiZhong.get(i)+"  ");
		System.out.println();
		System.out.print("算符为: ");
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
