
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;


public class GramAnalysis_JF {
	/**
	 * @wbp.parser.entryPoint
	 */
	private JFrame gramAnalysis=new JFrame("语法分析");
	private JTable table;
	private JLabel label=new JLabel("预测分析表");
	private FirstAndFollow faf=null;
	private AnalysisTable at=null;
	public GramAnalysis_JF()
	{
		faf=new FirstAndFollow();
		//faf.pretreat();
		faf.scanNS();
		faf.scanTS();
		faf.scanFirst();
		faf.scanFollow();	
	}
	
	public void init()
	{
		at=new AnalysisTable(faf);
		at.cre_AnalysisTable();			
		gramAnalysis.getContentPane().setLayout(null);
		table=new JTable(at.data,at.columnName);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();          //���õ�Ԫ����ݾ�����ʾ
		render.setHorizontalAlignment(SwingConstants.CENTER);
	    for(String s:at.columnName)
	    {
	    	table.getColumn(s).setCellRenderer(render);
	    }
	    table.setBounds(0, 40, 900, 130);
	    table.setCellSelectionEnabled(true);                                      //����һ��ѡ��һ����Ԫ��
	    label.setBounds(420, 10, 100, 20);	      
	    gramAnalysis.getContentPane().add(label);
		gramAnalysis.getContentPane().add(table);
		gramAnalysis.setResizable(false);
		gramAnalysis.setLocation(250, 50);
		gramAnalysis.setSize(900, 700);
		gramAnalysis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gramAnalysis.setVisible(true);
	}

}
