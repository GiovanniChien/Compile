package com.edu.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Term {
	private static ArrayList<Character>  feiZhong;
	private static ArrayList<Character>  operators;
	private static TreeMap<Character,TreeSet<String> > wenFa;
	
    /*static {
	    GuiZe.setMap();
	    wenFa=GuiZe.getMap();
	    Alpha.setFeiZhong(wenFa);
	    feiZhong=Alpha.getFeiZhong();
	    operators=Alpha.getOperators();
    }*/
	
	public Term(ArrayList<Character>  _feiZhong,ArrayList<Character>  _operators,TreeMap<Character,TreeSet<String> > _wenFa)
	{
		feiZhong=_feiZhong;
		operators=_operators;
		wenFa=_wenFa;
	}
    public static void firstTerm(Character v,ArrayList<Character> ft)//算firstTerm(v),结果放在ft中
    {
    	Set<String> temp=wenFa.get(v);
    	Iterator iter=temp.iterator();
    	while(iter.hasNext())
    	{
    		String s=(String) iter.next();
    		if(operators.indexOf(s.charAt(0))!=-1)//以算符开头 如F::=i
    		{
    			if(ft.indexOf(s.charAt(0))==-1)//没加过
    				ft.add(s.charAt(0));
    		}	
    		else if(s.length()>=2)//如E::=E+T
    		{
    			if(operators.indexOf(s.charAt(1))!=-1)//以非终结符开头,第二个是算符
    			{
    				if(ft.indexOf(s.charAt(1))==-1)//没加过
    				   ft.add(s.charAt(1));
    				if(s.charAt(0)!=v)
    				  firstTerm(s.charAt(0),ft);//递归
    			}
    		}
    		else //右边就一个非终结符 如E::=T
    		{
    			if(s.charAt(0)!=v)
    				 firstTerm(s.charAt(0),ft);//递归	
    		}
    	}
    }
    
    public static void lastTerm(Character v,ArrayList<Character> lt)//算lastTerm(v),结果放在lt中
    {
    	Set<String> temp=wenFa.get(v);
    	Iterator iter=temp.iterator();
    	while(iter.hasNext())
    	{
    		String s=(String) iter.next();
    		if(operators.indexOf(s.charAt(s.length()-1))!=-1)//以算符结尾
    		{
    			if(lt.indexOf(s.charAt(s.length()-1))==-1)//没加过
    			     lt.add(s.charAt(s.length()-1));
    		}
    		else if(s.length()>=2)
    		{
    			if(operators.indexOf(s.charAt(s.length()-2))!=-1)//以非终结符结尾,倒数第二个是算符
    			{
    				if(lt.indexOf(s.charAt(s.length()-2))==-1)//没加过
    				    lt.add(s.charAt(s.length()-2));
    				if(s.charAt(s.length()-1)!=v)
    				       lastTerm(s.charAt(s.length()-1),lt);//递归
    			}
    		}
    		else //右边就一个非终结符 如E::=T
    		{
    			if(s.charAt(s.length()-1)!=v)
    				 lastTerm(s.charAt(s.length()-1),lt);//递归	
    		}
    	}
    }
    
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*ArrayList<Character> result=new ArrayList<Character>();
		Term.lastTerm('E', result);
		for(int i=0;i!=result.size();++i)
			System.out.print(result.get(i)+"  ");
		System.out.println();*/
	}
}
