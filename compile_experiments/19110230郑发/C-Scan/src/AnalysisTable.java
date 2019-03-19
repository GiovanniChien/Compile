
import java.util.ArrayList;


public class AnalysisTable {
	FirstAndFollow faf=new FirstAndFollow();
	ArrayList<ArrayList<String>> analyTable=new ArrayList<ArrayList<String>>();
	String []columnName;
	String [][]data;
	private Tools tool=new Tools();
	public AnalysisTable(FirstAndFollow faf){
		// TODO Auto-generated constructor stub
		this.faf=faf;
		data=new String [faf.ns.size()+1][faf.ts.size()+1];
	}
/*	public void getStart(String string)
	{
		String strNS="";
		for(int i=0;i<faf.nFirst.size();i++)
		{
			if(faf.nFirst.get(i).start.equals(string))
			{
				strNS+=string+" ";
			}
			
		}
		
	}
	*/
	public boolean isFirst(String string,String start)                                            //string �Ƿ�����start��first��
	{
		for(Symbol symbol:faf.nFirst)
		{
			if(symbol.start.equals(start))
			{
				for(String str:symbol.first)
				{
					if(string.equals(str))
						return true;
				}			
			}
			
		}
		return false;
	}
	public boolean isFollow(String string,String start)                                          //string �Ƿ�����start��follow��
	{
		for(Symbol symbol:faf.nFollow)
		{
			if(symbol.start.equals(start))
			{
				for(String str:symbol.follow)
				{
					if(string.equals(str))
						return true;
				}			
			}
			
		}
		return false;
	}
	
	public boolean toEmptySymbol(String string)
	{
		for(int i=0;i<string.length();i++)                                                       //�ж��ܷ񵽴�'$'
		{
			if(string.charAt(i)=='$')
			{
				return true;
			}
		}
		return false;
	}
	public String delEmptySymbol(String string)                                                //ɾ��'$'
	{
		String string2="";
		for(int i=0;i<string.length();i++)  
		{
			string2+=string.charAt(i);
			if(i+2<string.length()&&string.charAt(i+1)=='|'&&string.charAt(i+2)=='$')
			{
				i=i+2;
				continue;
			}
		}
		return string2;
	}
	
	
	public String getFinalContent(String string,String ts)
	{
		String temp=string.replace('|', '@');
		String []arr=temp.split("->");
		String []arr1=arr[1].split("@");
		if(arr1[0].charAt(0)==ts.charAt(0))
			return arr1[0];
		return arr1[1];
	}
	
	public void cre_AnalysisTable()
	{
		ArrayList<String> row= new ArrayList<String>();
		String temp="";
		String start="";
		for(String str:faf.ts)
		{
			temp+=str+"@";
		}
		row.add("ns/ts"+"@"+temp);
		columnName=row.get(0).split("@");
		data[0]=columnName;
		//analyTable.add(row);
		for(int i=0;i<faf.ns.size();i++)
		{
			start=faf.nFirst.get(i).start;
			data[i+1][0]=start;
			for(int j=0;j<faf.ts.size();j++)
			{
				if(isFirst(faf.ts.get(j),faf.ns.get(i)))                                  //�ս�������ķ���ʼ��ŵ�first��
				{
					if(toEmptySymbol(faf.nFirst.get(i).content))
					{
						String string=delEmptySymbol(faf.nFirst.get(i).content);
						if(tool.isMulitipart(string))
						{
							data[i+1][j+1]=getFinalContent(string,faf.ts.get(j));						
						}
						else
						{
							data[i+1][j+1]=string;
						}
					}
					else
					{
						if(tool.isMulitipart(faf.nFirst.get(i).content))
						{
							data[i+1][j+1]=start+"->"+getFinalContent(faf.nFirst.get(i).content,faf.ts.get(j));						
						}
						else
						{
							data[i+1][j+1]=start+"->"+faf.nFirst.get(i).content;
						}
						
					}
					//temp1+=faf.nFirst.get(i).content+"@";
				}
				//�ķ���ʼ����ܵ���$�����ս�������ķ���ʼ��ŵ�follow��
				else if (toEmptySymbol(faf.nFirst.get(i).content)&&isFollow(faf.ts.get(j),faf.ns.get(i)))     
				{
					if(data[i+1][j+1]!=null)
					{
						data[i+1][j+1]+=","+start+"->"+"$";
					}
					else
					{
						data[i+1][j+1]=start+"->"+"$";
					}
					
				}
			}
								
		}
		analyTable.add(row);	
	}

}
