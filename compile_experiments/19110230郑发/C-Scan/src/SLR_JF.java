
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class SLR_JF {
	private JFrame slr=new JFrame("SLR(1)");
	private JLabel label=new JLabel("SLR(1)分析表");
	private JTable table;
	
	public SLR_JF()
	{
		slr.getContentPane().setLayout(null);
//		table=new JTable(at.data,at.columnName);
//		DefaultTableCellRenderer render = new DefaultTableCellRenderer();          //���õ�Ԫ����ݾ�����ʾ
//		render.setHorizontalAlignment(SwingConstants.CENTER);
//	    for(String s:at.columnName)
//	    {
//	    	table.getColumn(s).setCellRenderer(render);
//	    }
//	    table.setBounds(0, 40, 900, 130);
//	    table.setCellSelectionEnabled(true);                                      //����һ��ѡ��һ����Ԫ��
	    label.setBounds(420, 10, 100, 20);	      
	    slr.getContentPane().add(label);
//		slr.getContentPane().add(table);
		slr.setResizable(false);
		slr.setLocation(250, 50);
		slr.setSize(900, 700);
		slr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		slr.setVisible(true);
	}
	
	

}
