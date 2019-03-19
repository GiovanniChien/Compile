package CompilerPrinciple01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GetCodeFile extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	public static String resultString;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetCodeFile frame = new GetCodeFile();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GetCodeFile() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Toolkit tk = this.getToolkit();
		int width = 542;
		int height = 448;
		Dimension dm = tk.getScreenSize();
		this.setLocation((int)(dm.getWidth()-width)/2,(int)(dm.getHeight()-height)/2);
		setSize(width, height);
		Image a = this.getToolkit().getImage("2.gif");
		this.setIconImage(a);
		setTitle("\u8BCD\u6CD5\u5206\u6790");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 542, 448);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		{
			JPanel panel = new JPanel();
			contentPane.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			{
				textField = new JTextField();
				textField.setBounds(76, 23, 273, 29);
				panel.add(textField);
				textField.setColumns(10);
			}
			{
				JButton button01 = new JButton("\u6D4F\u89C8");
				button01.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
				button01.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						 JFileChooser fileChooser = new JFileChooser("C://");
						 FileNameExtensionFilter filter = new FileNameExtensionFilter("C Code", "c");
						 fileChooser.setFileFilter(filter);

						 int n = fileChooser.showOpenDialog(fileChooser);
						 if (n == fileChooser.APPROVE_OPTION){
						 textField.setToolTipText(fileChooser.getSelectedFile().getPath());
						 textField.setText(fileChooser.getSelectedFile().getPath());
						}
					}
				});
				button01.setBounds(407, 21, 78, 29);
				panel.add(button01);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane.setBounds(10, 77, 496, 261);
				panel.add(scrollPane);
				{
					textArea = new JTextArea();
					scrollPane.setViewportView(textArea);
					textArea.setBackground(Color.white);
					textArea.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN,13));
					textArea.setForeground(Color.BLUE);
				}
			}
			{
				JButton button02 = new JButton("\u6E05\u9664");
				button02.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
				button02.setBounds(39, 348, 149, 36);
				button02.addActionListener(new ActionListener() {
				
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						textArea.setText("");
					}
				});
				panel.add(button02);
			}
			{
				JButton button03 = new JButton("·ÖÎö");
				button03.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 17));
				button03.setBounds(308, 348, 149, 36);
				button03.addActionListener(new ActionListener() {
				
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						LexicalAnalysis lA = new LexicalAnalysis();
						String fileName = textField.getText();
						lA.openCFile(fileName);
						resultString = lA.result;
						textArea.setText(resultString);
					}
				});
				panel.add(button03);
			}
		}
	}
}
