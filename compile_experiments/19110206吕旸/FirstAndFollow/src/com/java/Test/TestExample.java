package com.java.Test;

import com.java.Utils.FirstAndFollow;

public class TestExample {

    //@表示空串	
	//规定E为开始字符
	//,表示'|'
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] source=new String[]{"E:TA","A:+TA,@","T:FB","B:*FB,@","F:(E),i"};
		char[] VN=new char[]{'E','A','T','B','F'};
		char[] VT=new char[]{'+','*','(',')','i'};
		FirstAndFollow faf=new FirstAndFollow(source,VT,VN);
//		faf.GetFirstFollow();  //调用内部函数，求first、follow集
		//依次输出非终结符是否能在有线次推导后，退出空串
		for(int i=0;i<source.length;i++){
			System.out.println(source[i]+"--->>"+faf.IsDeductionToEmpty(source[i]));
		}
		faf.GetFirstFollow();
		System.out.println("first集如下:");
		faf.DisplayFirst();
		System.out.println("follow集如下:");
		faf.DisplayFollow();
	}

}
