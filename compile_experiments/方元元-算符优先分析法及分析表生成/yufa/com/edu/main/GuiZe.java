package com.edu.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class GuiZe {

	/**
	 * @param args
	 */
	private static TreeMap<Character,TreeSet<String> > map=new TreeMap<Character,TreeSet<String> >();
	
	public static void setMap()
	{
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			line=in.readLine();
			while(!line.equals("#"))
			{
				TreeSet<String> newTemp;
				String temp[]=line.split("::=");
				if(temp.length!=2||temp[0].length()!=1)//��������3���ķ�
				{
					System.out.println("�����ķ���ʽ����!(S::=1a),�����������������");
					line=in.readLine();
					continue;
				}
				/*if(temp[0].charAt(0)==temp[1].charAt(0))
				{
					System.out.println("�ù�����ݹ�,�����������������");
					line=in.readLine();
					continue;
				}*/
				
				if(map.get(temp[0].charAt(0))==null)//�÷��ս���Ĺ���δ���ֹ�
				 {
					newTemp=new TreeSet<String>();
					newTemp.add(temp[1]);
					map.put(temp[0].charAt(0), newTemp);//������� s 1a
				 }else{//�÷��ս���Ĺ�����ֹ�
					 newTemp=map.get(temp[0].charAt(0));
					 newTemp.add(temp[1]);
					 map.put(temp[0].charAt(0), newTemp);//������� s 1a
				 }
				line=in.readLine();
			}//while
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static TreeMap<Character,TreeSet<String> > getMap()
	{
		return map;
	}
	public static void display()
	{
		for (Map.Entry<Character, TreeSet<String>> word : map.entrySet()) {
			Character key = word.getKey();
			TreeSet<String> value = word.getValue();
			Iterator iter=value.iterator();
			while(iter.hasNext())
			{
				String s=(String) iter.next();
			    System.out.println(key+"::="+s);
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GuiZe.setMap();
		System.out.println();
		GuiZe.display();
	}

}
