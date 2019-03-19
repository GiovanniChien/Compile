import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Tools {
	public ArrayList<String> pretreat(String local)                                                //Ԥ���?���ж�ȡ                                          
	{
		ArrayList<String> content=new ArrayList<String>();
		try
		{
			FileReader fr=new FileReader(local);
			BufferedReader br=new BufferedReader(fr);
			String line="";
			while((line=br.readLine())!=null)
			{
				content.add(line);
			}
			br.close();
		}
		catch(IOException err)
		{
			System.err.println(err.toString());
		}
		return content;
	}
	
	public boolean isMulitipart(String string)
	{
		for(int i=0;i<string.length();i++)
		{
			if(string.charAt(i)=='|')
			{
				return true;
			}
		}
		return false;
	}
	
	
	

}
