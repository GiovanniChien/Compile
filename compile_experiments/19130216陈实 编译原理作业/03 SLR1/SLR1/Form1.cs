using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace SLR1
{
    public partial class Form1 : Form
    {
        OriginalGrammar OG;
        ProCreateDFA cDFA;
        ProFillTable ft;
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            OG = new OriginalGrammar();
           
            richTextBox_rules.Text = "S->A\nA->aB|bC\nB->cB|d\nC->cC|e";
        }

        private void button_getrules_Click(object sender, EventArgs e)
        {
            string s = richTextBox_rules.Text;

            ProLoadGrammar lgp = new ProLoadGrammar(s, OG);
            OG = lgp.LoadGram();
            
            
            //输出
            initListViewshowRules();

            foreach(Rule rules in OG.pat)
            {
                ListViewItem aItem = new ListViewItem(rules.left);
                ListViewItem.ListViewSubItem SubItem = new ListViewItem.ListViewSubItem();//定义第一个子项目 

                string re="";

                foreach(string restmp in rules.right)
                {
                    re += restmp;
                }

                SubItem.Text = re;
                aItem.SubItems.Add(SubItem);
                listView_showRules.Items.Add(aItem);
            }
        }

        

        private void button_Ok_Click(object sender, EventArgs e)
        {
            string test = textBox_String.Text;
            ProJudge jd = new ProJudge(OG, cDFA.StateSet);
            bool j1 = jd.Judge(test);
            if (j1)
            {
                textBox_result.Text = "识别成功";
            }
            else
            {
                textBox_result.Text = "识别失败";
            }
            
        }

        private void initListViewshowRules()
        {
            listView_showRules.Clear(); listView_showRules.View = View.Details;                                          //listView显示方式
            listView_showRules.GridLines = true;                                             //显示表格线
            listView_showRules.FullRowSelect = true;                                         //是否可以选择行 
            listView_showRules.Scrollable = true;                                            //有滚动条
            listView_showRules.Columns.Add("规则左部", 98, HorizontalAlignment.Center);     //增加项目标题
            listView_showRules.Columns.Add("规则右部", 346 - 101, HorizontalAlignment.Center);     //增加项目标题
        }

        private void button_createDFA_Click(object sender, EventArgs e)
        {
            cDFA = new ProCreateDFA(OG);
            cDFA.CreateDFA();
        }

        private void button_FillRuleTable_Click(object sender, EventArgs e)
        {
             ft= new ProFillTable(OG, cDFA.StateSet);
            ft.FillChart();
        }

        private void button_All_Click(object sender, EventArgs e)
        {
            button_getrules_Click(sender,e);
            button_createDFA_Click(sender,e);
            button_FillRuleTable_Click(sender,e);
        }
    }
}
