package com.java.Test;

import com.java.Utils.FirstAndFollow;

public class TestExample {

    //@��ʾ�մ�	
	//�涨EΪ��ʼ�ַ�
	//,��ʾ'|'
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] source=new String[]{"E:TA","A:+TA,@","T:FB","B:*FB,@","F:(E),i"};
		char[] VN=new char[]{'E','A','T','B','F'};
		char[] VT=new char[]{'+','*','(',')','i'};
		FirstAndFollow faf=new FirstAndFollow(source,VT,VN);
//		faf.GetFirstFollow();  //�����ڲ���������first��follow��
		//����������ս���Ƿ��������ߴ��Ƶ����˳��մ�
		for(int i=0;i<source.length;i++){
			System.out.println(source[i]+"--->>"+faf.IsDeductionToEmpty(source[i]));
		}
		faf.GetFirstFollow();
		System.out.println("first������:");
		faf.DisplayFirst();
		System.out.println("follow������:");
		faf.DisplayFollow();
	}

}
