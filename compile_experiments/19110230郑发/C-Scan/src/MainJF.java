
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainJF{
	/**
	 * @param args
	 */
    private Dimension screenSize=null;
	
	public MainJF()
	{
		screenSize=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	}
	public void cre_MainFrame()
	{
		int height=300;
		int width=300;
		JFrame mainFrame=new JFrame("编译原理");
		mainFrame.getContentPane().setLayout(null);
		JButton scanWord=new JButton("词法分析");
		JButton gramAnalysis = new JButton("语法分析");
		JButton SLR=new JButton("SLR(1)");
		scanWord.setBounds(87, 36, 120, 30);
		gramAnalysis.setBounds(87, 89, 120, 30);
		SLR.setBounds(87,142,120,30);
		mainFrame.getContentPane().add(scanWord);
		mainFrame.getContentPane().add(gramAnalysis);
		mainFrame.getContentPane().add(SLR);
		
		//添加监听
		scanWord.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ScanWord_JF scan=new ScanWord_JF();
				scan.init();
			}
		});		
		gramAnalysis.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
					GramAnalysis_JF analysis=new GramAnalysis_JF();
					analysis.init();
			}
		});
		
		SLR.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
					SLR_JF slr=new SLR_JF();
			}
		});
	
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(width,height);
		mainFrame.setLocation((screenSize.width-width)/2,(screenSize.height-height)/2);      //��������Ļ�м�
		mainFrame.setVisible(true);
		
		
	}	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainJF main=new MainJF();
				main.cre_MainFrame();
			}
		});
		
	}
}



