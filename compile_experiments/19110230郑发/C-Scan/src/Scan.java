import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Scan {
    ArrayList<Content> content=new ArrayList<Content>();
    ArrayList<Content> rContent=new ArrayList<Content>();
    ArrayList<Content> wContent=new ArrayList<Content>();
    ArrayList<Content> function=new ArrayList<Content>();
    ArrayList<Content> rUserFun=new ArrayList<Content>();
    ArrayList<Content> wUserFun=new ArrayList<Content>();
    ArrayList<Content> userVar_Local=new ArrayList<Content>();
    ArrayList<Content> userVar_Global=new ArrayList<Content>();
    ArrayList<String> constant=new ArrayList<String>();
    Struct struct=new Struct();
	Scan(ArrayList<Content> str)
	{
		this.content=str;
	}
	public boolean isVarReserveWord(String str)                                                         //�ж��Ƿ��Ǳ����ͱ�����
	{
	   for(String string:struct.varReserveWord)
	   {
	      if(string.equals(str))
		  {
		    return true;
		  }
	   }
	   return false;
	}
	public boolean isFuncReserveWord(String str)                                                         //�ж��Ƿ��Ǻ����ͱ�����
	{
	   for(String string:struct.functionReserveWord)
	   {
	      if(string.equals(str))
		  {
		    return true;
		  }
	   }
	   return false;
	}
	public boolean noVarReserveWord(String str)                                                       //�ж��Ƿ��ǷǱ����ͱ�����
	{
		for(String string:struct.noVarReserveWord)
		   {
		      if(string.equals(str))
			  {
			    return false;
			  }
		   }
		   return true;
	}
	public boolean isOperationalCharacter1(String str)                                                 //�ж��Ƿ��ǵ�һ������
	{
		//Struct struct=new Struct();
		for(String string:struct.operationalCharacter1)
		{
			if(string.equals(str))
				return true;
		}
		return false;
	}
	public boolean isOperationalCharacter2(String str)                                               //�ж��Ƿ��Ǹ��ϲ�����
	{
		//Struct struct=new Struct();
		for(String string:struct.operationalCharacter2)
		{
			if(string.equals(str))
				return true;
		}
		return false;	
	}
	public boolean isBoundSymbol(String str)                                                          //�ж��Ƿ��Ǳ߽��
	{
		//Struct struct=new Struct();
		for(String string:struct.boundSymbol)
		{
			if(string.equals(str))
				return true;
		}
		return false;
	}
	
	public boolean isRightVar(String str)                                                              //�ж��û�����ı����Ƿ��ϱ�׼
	{
	   String regex = "^[a-zA-Z_]+[a-zA-Z0-9_]*"; 
	   Pattern p = Pattern.compile(regex);  
	   Matcher m = p.matcher(str);  
	   return m.matches();
	}
	public Content  returnContent(ArrayList<Content> content,int i)                                    //��content�����Ƶ�us
	{
		Content us=new Content();
		us.string=content.get(i).string;
		us.line=content.get(i).line;
		us.index=content.get(i).index;
		us.flag=content.get(i).flag;
		return us;
		
	}
	
	public void scanFunction()                                                                       //���Һ���
	{
		
		int small=0,big=0;                                                                           //��¼������С����
		boolean flag=false;
		for (int i = 0; i < content.size(); i++)
		{
			if(isFuncReserveWord(content.get(i).string)&&(content.get(i+2).string.equals("("))&&(content.get(i+3).string).equals(")"))	
			{ 
				flag=true;
				if(isRightVar(content.get(i+1).string))                                               //����û�����ı�����ϱ�׼
				{
					Content us=returnContent(content, i+1);
					rUserFun.add(us);
					//function.add(us);
					Content us1=returnContent(content, i=i+4);
					function.add(us1);
				}
				else
				{
					Content us=returnContent(content, i+1);
					wUserFun.add(us);
					//function.add(us);
					Content us1=returnContent(content, i=i+4);
					function.add(us1);
				}		
			}
			if(content.get(i).string.equals("(")&&flag)
			{small++;}
			if(content.get(i).string.equals(")")&&flag)
			{small--;}
			if(content.get(i).string.equals("{")&&flag)
			{big++;}
			if (content.get(i).string.equals("}")&&flag)
			{big--;}
			if(small==0&&big==0&&flag)
			{
				Content us=returnContent(content, i);
				function.add(us);	
			}
		}
	}
	public void scanWord()                                                                              //�����û�����
	{
	    for(int i=0;i<content.size();i++)
		{
		   if(isVarReserveWord(content.get(i).string)&&!content.get(i+2).string.equals("(")&&!content.get(i+3).string.equals(")"))                                                  //�Ƿ��Ǳ����ͱ�����
		   {
			   Content us=returnContent(content, i+1);
			   if(isRightVar(us.string))                                                                //��ȷ���Ǵ�����û�����
			   {rContent.add(us);}
			   else
			   {
			     wContent.add(us);
			   }
			   for(int j=i+1;j<content.size();j++)                                                     //�жϳ�һ���е����б���
			   {
				   if(content.get(j).string.equals(";"))                                               //������һ�н���
					   break;
				   if(content.get(j).string.equals(",")||content.get(j).string.equals("="))            
				   {
					  if(content.get(j).string.equals("="))                                            //=�ź���Ľ������
					  {
						  j++;
					  }
					  else                                                                             //���ź������û�����
					  {
						   Content us1=returnContent(content, j+1);
						   if(isRightVar(us1.string))
						   {rContent.add(us1);}
						   else
						   {
						     wContent.add(us1);
						   }
					//	   j++;
					  }
				   }	
				   
				}    				 
		   }
		}
	}
 
	public void finduserVar_Local()                                                                 //Ѱ�Ҿֲ�����
	{
		for(Content us:rContent)
		{
			for(int i=0;i<function.size();i=i+2)
			{
				if(us.index>function.get(i).index&&us.index<function.get(i+1).index)
				{
					userVar_Local.add(us);	
				}
			}
		}
	}
	
	public boolean isuserVar_Local(String str)                                                       //�Ƿ��Ǿֲ�����
	{
		for(Content us:userVar_Local)
		{
			if(us.string.equals(str))
				return true;
		}
		return false;
	}
	
	public void finduserVar_Global()                                                                  //Ѱ��ȫ�ֱ���
	{
		for(Content us:rContent)
		{
			if(!isuserVar_Local(us.string))
			   userVar_Global.add(us);
		}
		
	}
}
