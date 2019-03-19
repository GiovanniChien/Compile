import java.awt.Panel;
import java.awt.TextArea;
import javax.swing.JFrame;


public class ScanWord_JF {
	/**
	 * @wbp.parser.entryPoint
	 */
	private JFrame scanWord=new JFrame("词法分析");
	
	private TextArea contentResult=new TextArea("全部内容:");
	private TextArea keyResult=new TextArea("关键字:");
	private TextArea rVarResult=new TextArea("正确的用户变量:");
	private TextArea wVarResult=new TextArea("错误的用户变量:");
	private TextArea rFunResult=new TextArea("正确的函数符号:");
	private TextArea wFunResult=new TextArea("错误的函数符号:");
	private TextArea gVarResult=new TextArea("全局变量:");
	private TextArea lVarResult=new TextArea("局部变量:");
	private Panel scanWordResultPanel=new Panel();
	public ScanWord_JF()
	{	
		
		scanWord.getContentPane().setLayout(null);
		scanWordResultPanel.setLayout(null);
		scanWordResultPanel.setBounds(0, 0, 900, 550);
		
		contentResult.setBounds(0, 0, 450, 250);
		scanWordResultPanel.add(contentResult);	
		
		keyResult.setBounds(450, 0, 450, 250);
		scanWordResultPanel.add(keyResult);	
		
		rVarResult.setBounds(0,250, 450, 100);
		scanWordResultPanel.add(rVarResult);	
		
		wVarResult.setBounds(450, 250, 450, 100);
		scanWordResultPanel.add(wVarResult);	
		
		rFunResult.setBounds(0, 350, 450, 100);
		scanWordResultPanel.add(rFunResult);	
		
		wFunResult.setBounds(450, 350, 450, 100);
		scanWordResultPanel.add(wFunResult);	
		
		gVarResult.setBounds(0, 450, 450, 100);
		scanWordResultPanel.add(gVarResult);	
		
		lVarResult.setBounds(450, 450, 450, 100);
		scanWordResultPanel.add(lVarResult);
		
		scanWord.getContentPane().add(scanWordResultPanel);
		
		scanWord.setResizable(false);
		scanWord.setLocation(250,50);
		scanWord.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scanWord.setSize(900, 700);
		scanWord.setVisible(true);
		
		
		
	}
	public void init()
	{
		Pretreat pre=new Pretreat();
		pre.readScan();
		String key="",allContent="",rVar="",wVar="",rFun="",wFun="",gVar="",lVar="";
		for(Content content:pre.content)
		{
			allContent+=content.string+" "+"行数："+content.line+" "+"类别标识："+content.flag+"\r\n";
		}
		for(Content ks:pre.key)
		{
			key+=ks.string+" "+"行数："+ks.line+" "+"类别标识："+ks.flag+"\r\n";
		}
		Scan scan=new Scan(pre.content);
		scan.scanWord();
		for(Content us:scan.rContent)
		{
			rVar+=us.string+" "+"行数："+us.line+" "+"类别标识："+us.flag+"\r\n";
		}
		for(Content us:scan.wContent)
		{
			wVar+=us.string+" "+"行数："+us.line+" "+"类别标识："+us.flag+"\r\n";
		}
		scan.scanFunction();
		for(Content us:scan.rUserFun)
		{
			rFun+=us.string+" "+"行数："+us.line+" "+"类别标识："+us.flag+"\r\n";
		}
		for(Content us:scan.wUserFun)
		{
			wFun+=us.string+" "+"行数："+us.line+" "+"类别标识："+us.flag+"\r\n";
		}
		scan.finduserVar_Local();
		for(Content us:scan.userVar_Local)
		{
			lVar+=us.string+" "+"行数："+us.line+" "+"类别标识："+us.flag+"\r\n";
		}
		scan.finduserVar_Global();
		for(Content us:scan.userVar_Global)
		{
			gVar+=us.string+" "+"行数："+us.line+" "+"类别标识："+us.flag+"\r\n";
		}
		
		contentResult.append(allContent);
		keyResult.append(key);
		rVarResult.append(rVar);
		wVarResult.append(wVar);
		rFunResult.append(rFun);
		wFunResult.append(wFun);
		gVarResult.append(gVar);
		lVarResult.append(lVar);
	}

}
