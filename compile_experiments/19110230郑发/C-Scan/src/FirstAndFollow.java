
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FirstAndFollow {
	private ArrayList<String> content=new ArrayList<String>();
	ArrayList<String> ns=new ArrayList<String>();                                               //���ս��
	ArrayList<String> ts=new ArrayList<String>();                                               //�ս��
	ArrayList<Symbol> nFirst=new ArrayList<Symbol>();
    ArrayList<Symbol> nFollow=new ArrayList<Symbol>();
	private boolean lReplaced=false;
	private boolean gReplaced1=false;
	private boolean gReplaced2=false;
	private ArrayList<String> repStr=new ArrayList<String>();                                                 //first
	private String repStr1="";
//	private ArrayList<String> repStr2=new ArrayList<String>();                                                //follow
	//String repStr3="";
	private Tools t=new Tools();
	
	public FirstAndFollow()
	{
		content=t.pretreat("d:\\2.txt");
	}

	
	public void scanNS()                                                                        //Ѱ�ҷ��ս��
	{
		for(int i=0;i<content.size();i++)
		{
			String []string=new String[2];
			string=content.get(i).split("->");
			ns.add(string[0]);
		}
	}
	
	public boolean isNS(String string)                                                         //�ж��Ƿ��Ƿ��ս��
	{
		for(String str:ns)
		{
			if(string.equals(str))
				return true;
		}
		return false;
	}
	public boolean isTS(String string)                                                         //�ж��Ƿ����ս��
	{
		for(String str:ts)
		{
			if(string.equals(str))
				return true;
		}
		return false;
		
	}
	public boolean isNumber(String str)                                                      //�ж��Ƿ�������
	{
	   String regex = "^[0-9]*"; 
	   Pattern p = Pattern.compile(regex);  
	   Matcher m = p.matcher(str);  
	   return m.matches();
	}
	
	public void scanTS()                                                                        //Ѱ���ս��
	{
		String []string=new String[2];
		for(int i=0;i<content.size();i++)
		{
			string=content.get(i).split("->");
			for(int j=0;j<string[1].length();j++)                                               //�ս��ֻ����String1��
			{
				String str=string[1].charAt(j)+"";	
				if(isNS(str)||str.equals("|"))                                             
				{continue;}
				else
				{
					if(!isTS(str))
					{
						ts.add(str);
					}
				}
			}
		}
	}
	
	public boolean toEmptySymbol(String string)
	{
		for(int i=0;i<content.size();i++)                                                       //�ж��ܷ񵽴�'$'
		{
			String str=content.get(i).charAt(0)+"";                                             //str�ǵ�һ���ַ�
			if(str.equals(string))
			{
				for(int j=0;j<content.get(i).length();j++)
				{
					if(content.get(i).charAt(j)=='$')
					{
						return true;
					}
					
				}
			}
			
		}
		return false;
	}
	
	
	

	
	public void fReplaced(int i,int j)                                                             //�滻����
	{
		for(int m=0;m<nFirst.get(i).first.size();m++)                                               
		{
			boolean flag=false;                                                                   //��¼Դ��ĳһ�����Ŀ�����Ƿ����
			for(int n=0;n<nFirst.get(j).first.size();n++)                                          //�ж�Դ��ĳһ�����Ŀ�����Ƿ����
			{
				if(nFirst.get(j).first.get(n).equals(nFirst.get(i).first.get(m)))
				{
					flag=true;
				}
			}
			if(!flag)
			{
				nFirst.get(j).first.add(nFirst.get(i).first.get(m));
			}
		}
		
	}
	
	public void getFinalFirst(String repStr)                                                       //�����а�����ĵ�first
	{
		int temp3=-1;                                                                               //Դ
		int temp4=-1;                                                                               //Ŀ�ĵ�
		for(int i=0;i<nFirst.size();i++)                                                           //�ҳ�Դ��Ŀ�ĵص��±�
		{
			if(nFirst.get(i).start.equals(repStr))
			{
				temp3=i;
			}
			if(nFirst.get(i).replaced==true&&nFirst.get(i).replacedStr.equals(repStr))
			{
				temp4=i;
				if(temp3!=-1)
				{
					fReplaced(temp3,temp4);                                                                      //�滻
				}
			}
		}
	}
	
	public String getFinalString(String []string)
	{
		String str=string[1].charAt(0)+"";                                          //str��-����ĵ�һ���ַ�
		if(isTS(str))
		{
			return string[1];
		}
		else if(isNS(str))
		{
			for(String str2:content)
			{
				String str4=str2.charAt(0)+"";                                           //str4��->ǰ�ķ��
				if(str4.equals(str))                                                    //��ȴ��str���Ա��滻
				{
					if(toEmptySymbol(str4))                                              //���str4�ܵ���$��֮���ܵ���$�ķ��ս���first������
					{
						for(int i=1;i<string[1].length();i++)
						{
							String str5=string[1].charAt(i)+"";                                 
							if(!toEmptySymbol(str5)&&!isNumber(str5)&&isNS(str5))
							{
							//	string[1]=string[1]+"<"+str5+">";         //�Ƚ����ս����룬֮�����ñȽ�first��
								lReplaced=true;
								gReplaced1=true;
								repStr.add(str5);
								repStr1=str5;
								break;
							}
						}
					}
					String []str3=str2.split("->");
					string[1]=string[1].replace(str, str3[1]);
					getFinalString(string);						
				}
			}
		}
		return string[1];
	}
	
	

	public Symbol first(String []string,Symbol symbol)
	{
		symbol.content=string[0]+"->"+string[1];
		string[1]=getFinalString(string);                                               //�õ���������ַ�
		symbol.start=string[0];
		if(lReplaced)
		{
			symbol.replaced=true;
			symbol.replacedStr=repStr1;                                                //����Ҫ���滻�ַ�
			lReplaced=false;
		}
		if(isTS(string[1].charAt(0)+""))                                             
		{
			symbol.first.add(string[1].charAt(0)+"");
			for(int i=1;i<string[1].length();i++)
			{
				if(string[1].charAt(i)=='|'&&isTS(string[1].charAt(i+1)+""))
				{
					symbol.first.add(string[1].charAt(i+1)+"");
				}
			}
		}
		return symbol;
	}
	
	
	public void scanFirst()
	{	
		for(int i=0;i<content.size();i++)
		{
			Symbol symbol =new Symbol();
			String []string=new String[2];
			string=content.get(i).split("->");
			if(isNS(string[0]))
			{
				symbol=first(string,symbol);
				nFirst.add(symbol);
			}
		}
		if(gReplaced1)                                                                 //�������Ҫ�滻�����
		{
			for(String string:repStr)
			{
				getFinalFirst(string);                                                
			}
		}
		for(String string2:ts)
		{
			Symbol symbol=new Symbol();
			symbol.start=string2;
			symbol.first.add(string2);
			nFirst.add(symbol);
		}
	}
	
	public boolean isStartSymbol(String string)                                      //�ж��Ƿ�����ʼ���
	{
		for(int j=0;j<string.length();j++)
		{
			if(isNS(string.charAt(j)+""))
			{
				return true;
			}
		}
		return false;
	}
	
	public void addFirstToFollow(String string1,String string2)                       //first\follow
	{
		int index=-1;
		for(int j=0;j<nFollow.size();j++)
		{
			if(nFollow.get(j).start.equals(string2))
			{
				index=j;
				break;
			}
		}
		for(int i=0;i<nFirst.size();i++)
		{
			if(nFirst.get(i).start.equals(string1))
			{
				for(int j=0;j<nFirst.get(i).first.size();j++)
				{
					boolean flag=false;                                                                   //��¼Դ��ĳһ�����Ŀ�����Ƿ����
					if(!nFirst.get(i).first.get(j).equals("$"))
					{
						for(int k=0;k<nFollow.get(index).follow.size();k++)
						{
							if(nFirst.get(i).first.get(j).equals(nFollow.get(index).follow.get(k)))
								flag=true;
						}
						if(!flag)                                                                    //��first��follow��û���ֵ���ӵ�follow
						{
							nFollow.get(index).follow.add(nFirst.get(i).first.get(j));
						}
					}
				}
			}
		}
	}
	
	
	public void replacedF(int i,int j)                                                             //�滻����
	{
		for(int m=0;m<nFollow.get(i).follow.size();m++)                                               
		{
			boolean flag=false;                                                                   //��¼Դ��ĳһ�����Ŀ�����Ƿ����
			for(int n=0;n<nFollow.get(j).follow.size();n++)                                          //�ж�Դ��ĳһ�����Ŀ�����Ƿ����
			{
				if(nFollow.get(j).follow.get(n).equals(nFollow.get(i).follow.get(m)))
				{
					flag=true;
				}
			}
			if(!flag)
			{
				nFollow.get(j).follow.add(nFollow.get(i).follow.get(m));
			}
		}
		
	}
	
	
	
	public void getFinalFollow(String repStr)
	{
		int temp3=-1;                                                                               //Դ
		int temp4=-1;                                                                               //Ŀ�ĵ�
		for(int i=0;i<nFollow.size();i++)                                                           //�ҳ�Դ��Ŀ�ĵص��±�
		{
			if(nFollow.get(i).start.equals(repStr))
			{
				temp3=i;
			}
			if(nFollow.get(i).replaced==true&&nFollow.get(i).replacedStr.equals(repStr))
			{
				temp4=i;
				if(temp3!=-1)
				{
					replacedF(temp3,temp4);                                                                      //�滻		
				}
			}
			
		}
	}
	
	
	
	public void getFollow(String start,String string)
	{
		for(int i=0;i<string.length();i++)
		{
			String str=string.charAt(i)+"";                                              
			if(i+1<string.length())
			{
				String str2=string.charAt(i+1)+"";                                          
				if(isNS(str))
				{
					addFirstToFollow(str2,str);		                                        //first���еķ�$Ԫ�ؼ���follow
				}
				if(isNS(str)&&toEmptySymbol(str2))                                              //A->aBb ��b->$
				{
					for(int j=0;j<nFollow.size();j++)
					{
						if(nFollow.get(j).start.equals(str2)&&!nFollow.get(j).start.equals(start))
						{
							nFollow.get(j).replaced=true;
							nFollow.get(j).replacedStr=start;
							repStr.add(start);
							gReplaced2=true;
							break;
							
						}
					}
					
				}
			}
			if(string.length()==2)                                                                              //A->aB���
			{
				String string2=string.charAt(i)+"";
				for(int j=0;j<nFollow.size();j++)
				{
					if(nFollow.get(j).start.equals(string2)&&!nFollow.get(j).start.equals(start))
					{
							nFollow.get(j).replaced=true;
							nFollow.get(j).replacedStr=start;
							repStr.add(start);
							gReplaced2=true;
							break;
					}
				}
			}
		}
	}
	
	public void scanFollow()
	{
		for(int i=0;i<content.size();i++)
		{
			Symbol symbol =new Symbol();
			String []string=new String[2];
			string=content.get(i).split("->");
			if(isStartSymbol(string[1]))
			{
				symbol.start=string[0];
				symbol.follow.add("#");
			}
			else
			{
				if(isNS(string[0]))
				{
					symbol.start=string[0];
				}		
			}
			nFollow.add(symbol);
		}
		for(int i=0;i<content.size();i++)
		{
			String []string=new String[2];
			string=content.get(i).split("->");
			getFollow(string[0],string[1]);				
		}
		
		if(gReplaced2)                                                                 //�������Ҫ�滻�����
		{
			for(String string:repStr)
			{
			    getFinalFollow(string);	                                             
			}
		}
		
	}
}
