import java.io.*;
import java.util.ArrayList;
public class Pretreat {
	private String text="";
	private int line=1;
	private int index=0;
	ArrayList<Content> content=new ArrayList<Content>();
	ArrayList<Content> key=new ArrayList<Content>();
	Struct struct=new Struct();
	public int isOperationalCharacter1(int temp)                                                 //�ж��Ƿ��ǵ�һ������
	{	
		String string=(char)temp+"";
		for(int i=0;i<struct.operationalCharacter1.length;i++)
		{
			if(struct.operationalCharacter1[i].equals(string))
				return 200+i;
		}
		return 0;	
	}
	public int isOperationalCharacter2(String string)                                           //�ж��Ƿ��Ǹ��ϲ�����
	{
		//Struct struct=new Struct();
		for(int i=0;i<struct.operationalCharacter2.length;i++)
		{
			if(struct.operationalCharacter2[i].equals(string))
				return 250+i;
		}
		return 0;	
	}
	public int isBoundSymbol(int temp)                                                          //�ж��Ƿ��Ǳ߽��
	{
		//Struct struct=new Struct();
		String string=(char)temp+"";
		for(int i=0;i<struct.boundSymbol.length;i++)
		{
			if(struct.boundSymbol[i].equals(string))
				return 290+i;
		}
		return 0;
	}
	public Boolean addReserveWord(String string,int line,int index)                                 //����Ǳ����ּ��뱣���ֱ�
	{
		//Struct struct=new Struct();
		Content ks=new Content();
		for(int i=0;i<struct.reserveWord.length;i++)
		{
			if(struct.reserveWord[i].equals(string))
			{
				ks.string=string;
				ks.line=line;
				ks.flag=1+i;
				key.add(ks);
				addContent(text, line,index++,ks.flag);
				return true;
			}	
		}
		return false;
	}
	public String getString(int a,int b)                                                            //�ϲ��ַ�
	{
		String str1=(char)a+"";
		String str2=(char)b+"";
		String str=str1+str2;
		return str;
	}
	public void addContent(String string,int line,int index,int flag)                            //���ַ���ӵ�����
	{
		Content content1=new Content();
		content1.string=string;
		content1.line=line;
		content1.index=index;
		content1.flag=flag;
		content.add(content1);
	}
	public int returnFlag(int flag,int flag2)
	{
		int flagt=0;
		if(flag!=0) flagt=flag;
		else flagt=flag2;
		return flagt;
	}
	public void getInclude(Reader r,String string1,int flagt) throws IOException                 //��include�е�������ȡ
	{
		int temp=0;                                                             
		addContent(string1, line,index++,flagt);
		String str2="";
		while((temp=r.read())!=-1)
		{
			String str1=(char)temp+"";
			if(!str1.equals(">"))
				str2+=str1;
			else 
			{
				addContent(str2, line,index++,flagt);
				addContent(str1, line,index++,flagt);		
				return;
			}		
		}
		
	}
	public void readScan()
	{
		File file=new File("d:\\1.txt");
		Reader r=null;
		try{
			r=new InputStreamReader(new FileInputStream(file));
			int temp,flag=0,flag1=0,flag2=0,flagu=300;                                                //flag�ǵ��ַ�flag1�Ǹ����ַ�flag2�Ǳ߽��
			label1:
			while((temp=r.read())!=-1)
			{
				
				label2:
				while(temp!=9&&temp!=10&&temp!=13)
				{
					if(temp!=9&&temp!=10&&temp!=13)                                                      //�ж��Ƿ��ǻس������С��Ʊ��
					{
						if((flag2=isBoundSymbol(temp))!=0||(flag=isOperationalCharacter1(temp))!=0)                     //�������ͽ��޷�  
						{
							if(text!="")                                                                 //���text���վ�add
							{
							    if(!addReserveWord(text,line,index))                                   //����Ǳ����ּ��뱣���ֱ�
							    	addContent(text, line,index++,flagu++);	
							    text ="";
							}
							String string1=(char)temp+"";
							int flagt=returnFlag(flag, flag2);
							if(string1.equals("<")&&content.get(index-1).string.equals("#include"))
							{
								getInclude(r, string1, flagt);                                           //��include�е�������ȡ
							    continue label1;
							}
							else
							{
								int temp1=r.read();                                                     //ȡ��һ���ַ��ж�
								if(isBoundSymbol(temp1)!=0||isOperationalCharacter1(temp1)!=0)
								{
									String str=getString(temp, temp1);                                  //�ϲ������ַ�
									if((flag1=isOperationalCharacter2(str))!=0||str.equals("//")||str.equals("/*"))         //�жϸ��ϲ�����
									{
										if(str.equals("//"))                                           //����ע��//
										{
											int temp2;
											while((temp2=r.read())!=-1)
											{
												if(temp2==10)
												{
													temp=temp2;
													continue label2;
												}												
											}	
										}
										else if (str.equals("/*")) 
										{                                                                       //����ע��/* */       
											int temp3=0,temp4=0;
											boolean first=true;
											while(true)
											{
												if(first)                                                     //����ѭ��ƴ�ӱȶ�
												{
													temp3=r.read();
													temp4=r.read();
													first=false;
												}
												else {
													temp3=temp4;
													temp4=r.read();	
												}	
												String str3=getString(temp3, temp4);                          //�ϲ������ַ�
												if(str3.equals("*/"))
													continue label1;												
											}	
										}
										else {
											addContent(str, line,index++,flag1);
											continue label1;
										}
									}
									else 
									{                                                                       //���Ǿͽ���һ���ַ�add								
										int flagt1=returnFlag(flag, flag2);
										String string2=(char)temp+"";      
										addContent(string2, line,index++,flagt1);
										temp=temp1;                                                        //����ȡ���ַ�����     
										continue label2;                                                  //�����жϵڶ����ַ�
									}
								}
								else
								{                                                                        //�ڶ����ַ���                         
									int flagt1=returnFlag(flag, flag2);
									String string2=(char)temp+"";                                        //�����һ���ַ�
									addContent(string2, line,index++,flagt1);
									temp=temp1;
								}
								
							}
						}						
						if(temp==32)                                                                //������ǿո�֮ǰ�õ����ַ�add
						{
							if(text!="")
							{
								 if(!addReserveWord(text,line,index))                                   //����Ǳ����ּ��뱣���ֱ�
								    	addContent(text, line,index++,flagu++);	
							     text="";
							}   
							 continue label1;
						}
						
						if(temp!=32&&isBoundSymbol(temp)==0&&isOperationalCharacter1(temp)==0&&temp!=9&&temp!=10&&temp!=13)    						                                                                            
						{                                                                           //�ж��Ƿ��ǿո�������ͽ��޷� 
							if(temp==34)                                                            //����������м������
							{
								String string=(char)temp+"";
								while((temp=r.read())!=62)
								{								
									if(temp==34)
									{
										String string3=(char)temp+"";
										string+=string3;
										addContent(string, line,index++,flagu++);
										continue label1;
									}	
									String string1=(char)temp+"";
									string+=string1;
								}
							}
							if(temp==39)                                                           //����˫����м������
							{
								String string=(char)temp+"";
								while((temp=r.read())!=-1)
								{	
									if(temp==39)
									{
										String string3=(char)temp+"";
										string+=string3;
										addContent(string, line,index++,flagu++);
										continue label1;
									}
									String string1=(char)temp+"";
									string+=string1;
								}
							}						
							text+=(char)temp;
							continue label1;
						}
						
					}	
					
				}	
			if(temp==10)                                                                             //��¼����
			{line++;}
			}
		
	
			r.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
