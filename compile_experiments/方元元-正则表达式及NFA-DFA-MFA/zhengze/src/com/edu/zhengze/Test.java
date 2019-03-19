package com.edu.zhengze;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		/*TreeSet<Integer> a=new TreeSet<Integer>();
		a.add(5);
		a.add(4);
		a.add(1);
		a.add(4);
		Iterator<Integer> iter=a.iterator();
		while(iter.hasNext())
		{
			int i=iter.next();
		
		System.out.print(i);
		}*/
		ArrayList<Integer> p=new ArrayList<Integer>();
		
		p.add(1);
		p.add(0);
		System.out.println(p.contains(1));
		
		char ch='*';
		if(ch!='*')
			ch='#';

	}
		
		

}
