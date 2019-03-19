using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace test
{
    public partial class Form1 : Form
    {
        public bool tmpFileStatus;//临时文件就绪状态
        public string cur_tmpFileName;//临时文件名
        public StreamReader fileStreamReader;//文件流
        private List<clsToken> myTokenList;//所有关键字的临时存放
        public Form1()
        {
            InitializeComponent();
        }

        //打开文本文件，并显示内容于文本框
        private void button_open_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.ShowDialog();
            string path=ofd.FileName;
            if (path == "") return;
            StreamReader str = new StreamReader(path, Encoding.GetEncoding("gb2312"));
            //Encoding.GetEncoding("gb2312")防止读取文字时出现乱码
            richTextBox_code.Text = str.ReadToEnd();
            saveTmpFile();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            this.richTextBox_code.WordWrap = false;
            tmpFileStatus = false;
            cur_tmpFileName = "";
            Directory.CreateDirectory("./tmpFile");
            myTokenList = new List<clsToken>();
        }

        private void button_split_text_Click(object sender, EventArgs e)
        {
            if (tmpFileStatus==false)
            {
                MessageBox.Show("文件有变化，请先保存");
                return;
            }
            listBox_code.Items.Clear();
            string path = cur_tmpFileName;
            fileStreamReader = new StreamReader(path, Encoding.GetEncoding("gb2312"));
            StreamReader filetmp = new StreamReader(path, Encoding.GetEncoding("gb2312"));
            while (filetmp.Peek() != -1)
            {
                String sb = filetmp.ReadLine();
                //if (sb == "") continue;
                listBox_code.Items.Add(sb);
            }
        }

        private void saveTmpFile()
        {
            cur_tmpFileName = "./tmpFile/" + "my" + DateTime.Now.ToString("HH-mm-ss");
            File.WriteAllText(cur_tmpFileName, this.richTextBox_code.Text, Encoding.UTF8);
            tmpFileStatus = true;
        }

        private void button_saveTmpFile_Click(object sender, EventArgs e)
        {
            cur_tmpFileName = "./tmpFile/"+"my" + DateTime.Now.ToString("HH-mm-ss");
            File.WriteAllText(cur_tmpFileName, this.richTextBox_code.Text, Encoding.UTF8);
            MessageBox.Show("转存成功！");
            tmpFileStatus = true;
        }

        private void richTextBox_code_KeyDown(object sender, KeyEventArgs e)
        {
            tmpFileStatus = false;
        }

        private void Form1_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.S && e.Control)
            {
                e.Handled = true; 
                saveTmpFile();    
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            //fileStreamReader.Close();
            //DirectoryInfo di = new DirectoryInfo("./tmpFile");
            //di.Delete(true);
        }

        private void button_test_Click(object sender, EventArgs e)
        {
            clsLexer lex = new clsLexer(fileStreamReader);
            myTokenList.Clear();
            while (fileStreamReader.EndOfStream==false)
            {
                clsToken ct = lex.scan();
                myTokenList.Add(ct);
            }
            fileStreamReader.Close();
        }

        private void listBox_code_DoubleClick(object sender, EventArgs e)
        {
            int index = listBox_code.SelectedIndex+1;
            string res = "第"+index+"行:\n";
            for (int i = 0; i < myTokenList.Count;i++ )
            {
                clsToken tmp = myTokenList[i];
                string mytmp = tmp.toString();
                if(tmp.line==index)
                {
                    res += tmp.toString();
                    res += "\n";
                }
               
            } 
            MessageBox.Show(res);
        }

        private void button_save_Result_Click(object sender, EventArgs e)
        {
            string path = "result" + DateTime.Now.ToString("HH-mm-ss")+".txt";
            using (StreamWriter sw = File.CreateText(path))
            {
                for (int i = 0; i < myTokenList.Count; i++)
                {
                    clsToken tmp = myTokenList[i];
                    string mytmp = tmp.toString();
                    sw.WriteLine(tmp.toString());
                } 
            }
            MessageBox.Show("分词结果已经存储在" + path + "下！");
        }

        private void button1_test2_Click(object sender, EventArgs e)
        {
            clsTable clst = new clsTable(fileStreamReader);
            clst.work();
        }
    }
}
