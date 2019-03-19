using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Collections;
namespace ffs
{
    public partial class Form1 : Form
    {
        int maxsize = 100;
        clsRules rs;
        char firstchar;
        HashSet<char> first;
        HashSet<char> follow;
        HashSet<char> vnset;
        HashSet<char> vtset;
        HashSet<char> temp;      //存放一轮中已求过的非终结符，避免死循环：E->T->F->E
        char[,] firstarr;        //用来存放所有的first集
        char[,] followarr;	     //用来存放所有的follow集
        string[,] analysis;	     //用来存放所有的分析表 
        public Form1()
        {
            InitializeComponent();
        }

        #region 界面响应

        //获得规则
        private void button_getrules_Click(object sender, EventArgs e)
        {
            rs.sl.Clear();
            //获取文本框内容
            string tmp = richTextBox_rules.Text;
            //正则匹配（用于获取每一行规则）
            string RegexStr = ".+(\n)*";
            MatchCollection mc = Regex.Matches(tmp, RegexStr);
            foreach (Match item in mc)
            {
                string contentwithn = item.ToString();
                string content = contentwithn.Replace("\n", "");
                //把每一行交给下一个函数整理
                dosplit(content);
            }

            //至此构造完毕 下面输出

            //初始化listview
            initListViewshowRules();

            foreach (clsRule re in rs.sl) //ht为一个Hashtable实例
            {
                ListViewItem aItem = new ListViewItem(re.left);
                ListViewItem.ListViewSubItem SubItem = new ListViewItem.ListViewSubItem();//定义第一个子项目        
                SubItem.Text = re.right;
                aItem.SubItems.Add(SubItem);
                listView_showRules.Items.Add(aItem);
            }
        }

        //获取first/folllow集合
        private void button_ff_Click(object sender, EventArgs e)
        {
            button_getrules_Click(sender, e);
            getFirstFollow();
        }

        //获取预测分析表
        private void button_preview_Click(object sender, EventArgs e)
        {
            button_ff_Click(sender, e);
            GetAnalysisForm();
        }

        //一键直达分析表
        private void button_goAnalysis_Click(object sender, EventArgs e)
        {
            button_preview_Click(sender, e);
        }

        //分析句子
        private void button_IdentifySentense_Click(object sender, EventArgs e)
        {

            button_goAnalysis_Click(sender, e);

            string Isentense = textBox_waitSentensive.Text;
            Analysis(Isentense);
        }

        //界面载入
        private void Form1_Load(object sender, EventArgs e)
        {
            //用于把文本框规则存储下来；
            rs = new clsRules();
            vnset = new HashSet<char>();
            vtset = new HashSet<char>();
            temp = new HashSet<char>();
            first = new HashSet<char>();
            follow = new HashSet<char>();
            firstarr = new char[maxsize, maxsize];
            followarr = new char[maxsize, maxsize];
            analysis = new string[maxsize, maxsize]; ;	   //用来存放所有的分析表
            //richTextBox_rules.Text = "E->eTe\ne->+Te\ne->0\nT->Ft\nt->*Ft\nt->0\nF->(E)\nF->i";
            richTextBox_rules.Text = "Z->BA\nA->BZ|d\nB->aA|bZ|c";

        }

        #endregion

        #region listview初始化
        //规则listview初始化
        private void initListViewshowRules()
        {
            listView_showRules.Clear(); listView_showRules.View = View.Details;                                          //listView显示方式
            listView_showRules.GridLines = true;                                             //显示表格线
            listView_showRules.FullRowSelect = true;                                         //是否可以选择行 
            listView_showRules.Scrollable = true;                                            //有滚动条
            listView_showRules.Columns.Add("规则左部", 98, HorizontalAlignment.Center);     //增加项目标题
            listView_showRules.Columns.Add("规则右部", 346 - 101, HorizontalAlignment.Center);     //增加项目标题
        }

        //first集合listview初始化
        private void initListViewshowfirst()
        {
            listView_first.Clear();
            listView_first.View = View.Details;                                          //listView显示方式
            listView_first.GridLines = true;                                             //显示表格线
            listView_first.FullRowSelect = true;                                         //是否可以选择行 
            listView_first.Scrollable = true;                                            //有滚动条
            listView_first.Columns.Add("非终结符", 98, HorizontalAlignment.Center);     //增加项目标题
            listView_first.Columns.Add("first集合", 345 - 101, HorizontalAlignment.Center);     //增加项目标题
        }

        //folllow集合listview初始化
        private void initListViewshowfollow()
        {
            listView_follow.Clear();
            listView_follow.View = View.Details;                                          //listView显示方式
            listView_follow.GridLines = true;                                             //显示表格线
            listView_follow.FullRowSelect = true;                                         //是否可以选择行 
            listView_follow.Scrollable = true;                                            //有滚动条
            listView_follow.Columns.Add("非终结符", 98, HorizontalAlignment.Center);     //增加项目标题
            listView_follow.Columns.Add("follow集合", 345 - 101, HorizontalAlignment.Center);     //增加项目标题
        }

        //预测分析表listview初始化
        private void initListViewshowanalysis()
        {
            listView_analysis.Clear();
            listView_analysis.View = View.Details;                                          //listView显示方式
            listView_analysis.GridLines = true;                                             //显示表格线
            listView_analysis.FullRowSelect = true;                                         //是否可以选择行 
            listView_analysis.Scrollable = true;                                            //有滚动条

            listView_analysis.Columns.Add(" ", 30, HorizontalAlignment.Center);     //增加项目标题

            int liekuan;
            //看终结符中是否有空串，如果有，减少列宽
            if (vtset.Contains('0'))
            {
                liekuan = (550 - 30) / (vtset.Count - 1);
            }
            else
            {
                liekuan = (550 - 30) / (vtset.Count);
            }

            foreach (char vt in vtset)
            {
                if (vt!='0')
                    listView_analysis.Columns.Add(vt + "", liekuan, HorizontalAlignment.Center);     //增加项目标题
            }
        }

        ////识别过程listview初始化
        private void initListViewIdentifySentense()
        {
            listView_IdentifySentense.Clear();
            listView_IdentifySentense.View = View.Details;                                          //listView显示方式
            listView_IdentifySentense.GridLines = true;                                             //显示表格线
            listView_IdentifySentense.FullRowSelect = true;                                         //是否可以选择行 
            listView_IdentifySentense.Scrollable = true;                                            //有滚动条
            listView_IdentifySentense.Columns.Add("序号", 45, HorizontalAlignment.Center);     //增加项目标题
            int liekuan = (550 - 45) / 3;
            listView_IdentifySentense.Columns.Add("栈", liekuan, HorizontalAlignment.Center);     //增加项目标题
            listView_IdentifySentense.Columns.Add("符号串", liekuan, HorizontalAlignment.Center);     //增加项目标题
            listView_IdentifySentense.Columns.Add("规则", liekuan, HorizontalAlignment.Center);     //增加项目标题
        }
        #endregion

        #region 获取规则

        private void dosplit(string content)
        {

            //正则匹配（用于获取每一行规则）
            string RegexStr = "(?<left>.+)->(?<right>.+)";
            MatchCollection mc = Regex.Matches(content, RegexStr);
            foreach (Match item in mc)
            {
                string left = item.Groups["left"].ToString();
                string right = item.Groups["right"].ToString();
                string[] res = right.Split('|');
                for (int i = 0; i < res.Length; i++)
                {
                    rs.sl.Add(new clsRule(left, res[i]));
                }
            }
        }

        #endregion

        #region first and folllow
        //求解first and folllow主函数
        private void getFirstFollow()
        {
            initListViewshowfirst();
            initListViewshowfollow();
            clearCharArray(firstarr);
            clearCharArray(followarr);
            getVN();
            getVT();
            vtset.Add('#');
            int i = 0;
            foreach (char vn in vnset) //ht为一个Hashtable实例
            {
                char c = vn;
                getFirst(c);
                PrintFirstSet(c, i++, first);
                first.Clear();
                temp.Clear();//清空临时集合
            }

            i = 0;
            foreach (char vn in vnset) //ht为一个Hashtable实例
            {
                char c = vn;
                getFollow(c);
                PrintFollowSet(c, i++, follow);
                follow.Clear();
                temp.Clear();//清空临时集合
            }
        }

        //生成终结符表
        private void getVT()
        {
            vtset.Clear();
            foreach (clsRule rules in rs.sl) //ht为一个Hashtable实例
            {
                for (int i = 0; i < rules.right.Length; i++)
                {
                    if (!vnset.Contains(rules.right[i]))	//且不是非终结符
                    {
                        vtset.Add(rules.right[i]);
                    }
                }
            }
        }

        //生成非终结符表
        void getVN()
        {
            vnset.Clear();
            int cnt = 0;
            foreach (clsRule rules in rs.sl) //ht为一个Hashtable实例
            {
                if (cnt++ == 0)
                {
                    firstchar = rules.left[0];
                }
                vnset.Add(rules.left[0]);
            }
        }

        //打印first集合
        private void PrintFirstSet(char c, int index, HashSet<char> myset)
        {
            //注意编号从1开始
            int i = 1;
            firstarr[index, 0] = c;
            ListViewItem aItem = new ListViewItem(c + "");
            ListViewItem.ListViewSubItem SubItem = new ListViewItem.ListViewSubItem();//定义第一个子项目                  
            string result = "{";
            foreach (char vn in myset)
            {
                firstarr[index, i] = vn;
                i++;
                result += vn + ",";
            }
            result = result.Remove(result.LastIndexOf(","), 1);
            result += "}";
            SubItem.Text = result;
            aItem.SubItems.Add(SubItem);
            listView_first.Items.Add(aItem);
        }

        //打印folllow集合
        private void PrintFollowSet(char c, int index, HashSet<char> myset)
        {
            int i = 1;
            followarr[index, 0] = c;

            ListViewItem aItem = new ListViewItem(c + "");
            ListViewItem.ListViewSubItem SubItem = new ListViewItem.ListViewSubItem();//定义第一个子项目                  
            string result = "{";
            foreach (char vn in myset)
            {
                followarr[index, i] = vn;
                i++;
                result += vn + ",";
            }
            result = result.Remove(result.LastIndexOf(","), 1);
            result += "}";
            SubItem.Text = result;
            aItem.SubItems.Add(SubItem);
            listView_follow.Items.Add(aItem);

        }

        //生成first集合
        private void getFirst(char cc)	//	生成first集
        {
            int j, k;
            //if (temp.Contains(cc))
            //{
            //    //已经求过，直接返回
            //    return;
            //}
            temp.Add(cc);
            foreach (clsRule rules in rs.sl)
            {
                //如果该条规则左部匹配，那边看右部，否则直接退出
                if (rules.left[0] == cc)
                {
                    //处理X->终结符....和X->ε的情况
                    //如果最后是终结符或者是ε
                    if (vtset.Contains(rules.right[0]) || rules.right[0] == '0')
                    {
                        first.Add(rules.right[0]);
                        continue;
                    }
                    //如果最右边是终结符，那就递归进去找
                    if (vnset.Contains(rules.right[0]))
                    {
                        getFirst(rules.right[0]);
                    }
                    bool flag = true;
                    bool flag2 = true;
                    for (j = 1; j < rules.right.Length; j++)
                    {
                        for (k = 0; k < j; k++)
                        {
                            if (vtset.Contains(rules.right[k]))
                            {
                                flag = false;	//存在终结符，则不满足条件，退出
                            }
                        }
                        if (flag == true)		//如果都是非终结符，那么继续判断有没有ε
                        {
                            for (k = 0; k < j; k++)
                            {
                                if (!getkong(rules.right[k]))
                                {
                                    flag2 = false;
                                }
                            }
                            if (flag2 == true)	//若能推出ε
                            {
                                getFirst(rules.right[j]);
                            }
                        }
                    }
                }
            }
        }

        //生成folllow集合
        private void getFollow(char cc)
        {
            if (!temp.Contains(cc))
            {
                temp.Add(cc);
                int j = 1;
                List<int> tmpindex;
                if (cc == firstchar)	//如果是文法的起始符，就把‘#’加到Follow
                {
                    int t = getIndex(cc);
                    follow.Add('#');
                }
                foreach (clsRule rules in rs.sl)
                {
                    int[] test = getIndexfromString(cc, rules.right);
                    if (test != null)
                    {
                        tmpindex = new List<int>(getIndexfromString(cc, rules.right));//获取一批下标
                        foreach (int tempindex in tmpindex)
                        {
                            //对每个下标单独计算
                            int length = rules.right.Length - 1;

                            //如果获取到的tempindex就是最后一位，那么无法计算这个if
                            if (tempindex + 1 < rules.right.Length)
                            {
                                //否则满足条件，先求跟在后面的first集合，分非终结和终结
                                if (rules.right[tempindex] == cc && length > tempindex)//形如A->aBb...且求folllow（B）
                                {
                                    //如果b是非终结符
                                    if (vnset.Contains(rules.right[tempindex + 1]))
                                    {

                                        int index = getIndex(rules.right[tempindex + 1]);	//那么就在非终结符中把它找到，返回下标
                                        while (firstarr[index, j] != '\0' && firstarr[index, j] != '0')
                                        {
                                            follow.Add(firstarr[index, j]);			//把b的first集中的内容给B
                                            j++;
                                        }
                                    }

                                    //如果b是终结符
                                    if ((tempindex + 1 < rules.right.Length) && vtset.Contains(rules.right[tempindex + 1]))
                                    {
                                        follow.Add(rules.right[tempindex + 1]);//把b的first集中的内容给B(终结符的First集就是它本身)
                                    }
                                }//A->aBb...
                            }

                            //如果B后面是非终结符，那么把后面所有符号看下能不能推导出ε,
                            // 如果到结尾之前，有一个不能推倒，那么放弃，
                            // 否则左部的folllow必定加入Folllow（B）
                            if (rules.right[tempindex] == cc)
                            {
                                int startindex = tempindex + 1;
                                bool flag = true;
                                while (startindex < rules.right.Length)
                                {
                                    if (getkong(rules.right[startindex]) == false)
                                    {
                                        flag = false;
                                    }
                                    startindex++;
                                }

                                if (flag)
                                {
                                    getFollow(rules.left[0]);
                                }
                            }

                            //B后面没东西了，左部的folllow必定加入Folllow（B）
                            if (rules.right[tempindex] == cc)
                            {
                                if (length == tempindex) //A->aB
                                {
                                    getFollow(rules.left[0]);
                                }
                            }
                        }
                    }
                }
            }
        }


        private int[] getIndexfromString(char c, string s)
        {
            int[] result = new int[s.Length];
            int cnt = 0;
            for (int i = 0; i < s.Length; i++)
            {
                if (s[i] == c)
                {
                    result[cnt++] = i;
                }
            }

            if (cnt == 0)
            {
                //没有找到，返回空
                return null;
            }

            int[] retu = new int[cnt];
            for (int i = 0; i < cnt; i++)
            {
                retu[i] = result[i];
            }
            return retu;
        }


        //判断能不能推出ε
        private bool getkong(char c)
        {
            foreach (clsRule rules in rs.sl)
            {
                if (rules.left[0] == c)
                {
                    for (int j = 0; j < rules.right.Length; j++)
                    {
                        if (rules.right[j] == '0')
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        //获取在firstset中的下标
        private int getIndex(char cc)
        {
            for (int i = 0; i < rs.sl.Count; i++)
            {
                if (cc == firstarr[i, 0])
                {
                    return i;
                }
            }
            return -1;
        }
        private void clearCharArray(char[,] array)
        {
            for (int i = 0; i < maxsize; i++)
            {
                for (int j = 0; j < maxsize; j++)
                {
                    array[i, j] = '\0';
                }
            }
        }
        private void clearStringArray(string[,] array)
        {
            for (int i = 0; i < maxsize; i++)
            {
                for (int j = 0; j < maxsize; j++)
                {
                    array[i, j] = "";
                }
            }
        }




        #endregion

        #region  预测分析表

        //初始化预测分析表
        private void initial_analysis(string[,] s)
        {
            s[0, 0] = " ";
            int i = 1;
            foreach (char vt in vtset)
            {
                s[0, i] = vt + "";
                i++;
            }

            i = 1;
            foreach (char vn in vnset)
            {
                s[i, 0] = vn + "";
                i++;
            }
        }

        //用来判断a∈First(A)是否成立
        private bool isBelongFirst(char A, char a)
        {
            int index = getIndex(A);//获取A在firstset中的下标
            if (-1 == index)
            {
                return false;
            }
            int i = 0;
            if (vtset.Contains(A) && A == a)
            {
                return true;
            }
            while (firstarr[index, i] != '\0')
            {
                if (firstarr[index, i] == a)
                {
                    return true;
                }
                i++;
            }
            return false;
        }

        //获取某字符在某表中的下标
        private int getsetindex(char c, HashSet<char> myset)
        {
            int i = 0;
            foreach (char setchar in myset)
            {
                if (c == setchar)
                {
                    return i;
                }
                i++;
            }
            return -1;
        }

        //填充每一个格子
        private void getAnalysis_one(char Vn, char Vt)
        {
            //Vn 非终结符
            //Vt 终结符
            // 如果VT是#，
            // 那么我们需要查看当前VN是否能够推出空串，
            // 如果不能，直接略过
            // 如果能，将推出空串的规则，写入表
            // 


            if (Vt=='#')
            {
                bool flag = false;
                //情况1：右部直接匹配
                foreach (clsRule rules in rs.sl)
                {
                    //左部有的规则，看右部是否有0（空串）
                    if (rules.left[0] == Vn)
                    {
                        if (rules.right[0]=='0')
                        {
                            flag=true;
                            break;
                        }
                    }


                }

                if (flag==true)
                {

                }
            }
            else
            {
                foreach (clsRule rules in rs.sl)
                {
                    if (rules.left[0] == Vn)
                    {
                        if (vnset.Contains(rules.right[0]))	//A->α
                        {
                            if (isBelongFirst(rules.right[0], Vt))	//若α为非终结符且a∈First(α)
                            {
                                int indexa = getsetindex(Vt, vtset) + 1;//获取a在终结符表中的下标+1
                                int indexA = getsetindex(Vn, vnset) + 1;//获取α在非终结符表中的下标+1
                                analysis[indexA, indexa] = rules.left + "->" + rules.right;
                            }
                        }
                        else if (vtset.Contains(rules.right[0]))//α为终结符
                        {
                            if (Vt == rules.right[0])
                            {
                                int indexa = getsetindex(Vt, vtset) + 1;//获取a在终结符表中的下标+1
                                int indexA = getsetindex(Vn, vnset) + 1;//获取α在非终结符表中的下标+1
                                analysis[indexA, indexa] = rules.left + "->" + rules.right;
                            }
                        }
                        if (isBelongFirst(rules.right[0], '0'))	//若空串a∈First(α)
                        {
                            int tempvnindex = getsetindex(Vn, vnset);
                            for (int j = 1; j < vtset.Count + 1; j++)
                            {
                                int tempvtindex = getsetindex(followarr[tempvnindex, j], vtset);
                                int tempAindex = getsetindex(Vn, vnset);
                                if (tempvtindex != -1 && tempAindex != -1)
                                {
                                    analysis[tempAindex + 1, tempvtindex + 1] = rules.left + "->" + rules.right;
                                }
                            }
                        }
                    }
                }
            }

           
        }

        //调用填充预测表
        private void GetAnalysisForm()
        {
            //清空初始清空
            clearStringArray(analysis);

            //初始化分析表            
            initial_analysis(analysis);

            //vtset.Remove('0');

            //遍历获取表格内容
            foreach (char vn in vnset)
            {
                foreach (char vt in vtset)
                {
                    //如果是空串，不录入
                    //if (vt=='0')
                    //{
                    //    continue;
                    //}
                    getAnalysis_one(vn, vt);
                }
            }

            //初始化listview
            initListViewshowanalysis();

            for (int i = 1; i <= vnset.Count; i++)
            {
                ListViewItem aItem = new ListViewItem(analysis[i, 0]);

                //从2开始，略过0（空串）
                for (int j = 2; j <= vtset.Count; j++)
                {
                    ListViewItem.ListViewSubItem SubItem = new ListViewItem.ListViewSubItem();
                    SubItem.Text = analysis[i, j];
                    aItem.SubItems.Add(SubItem);

                }
                listView_analysis.Items.Add(aItem);
            }
        }


        #endregion

        #region 句子识别

        //识别句子
        private void Analysis(string sentense)
        {
            //建立listview
            initListViewIdentifySentense();

            //建立栈，用于存放字符
            Stack<char> st = new Stack<char>();

            //初始入栈#与首字符
            st.Push('#');
            st.Push(firstchar);

            char cur_stack = ' ';
            char cur_char = ' ';


            //指针计数器，指向当前需要识别的字符
            int cntS = 0;

            //用于记录当前步骤数目
            int cntB = 1;

            //当栈不空，一直做
            while (st.Count != 0)
            {
                //记录步骤数
                ListViewItem aItem = new ListViewItem(cntB++ + "");

                //输出栈
                addSubItem(aItem, getStck(st));

                //输出子串
                addSubItem(aItem, getRemainString(sentense, cntS));

                cur_char = sentense[cntS];

                cur_stack = st.Pop();

                string res = getRules(cur_char, cur_stack);

                if (res == null)
                {
                    addSubItem(aItem, "未找到合适条件，识别失败！");
                    listView_IdentifySentense.Items.Add(aItem);
                    return;
                }
                else if (res.Equals("100"))
                {
                    //终结符相等，匹配成功
                    addSubItem(aItem, cur_char + "=" + cur_stack + ",匹配成功");
                    cntS++;
                }
                else if (res.Equals("200"))
                {
                    //句子识别成功
                    addSubItem(aItem, "句子识别成功！");
                }
                else
                {
                    //是非终结符，将右部倒序入栈
                    addSubItem(aItem, res);


                    string[] result = res.Split('>');

                    for (int i = result[1].Length - 1; i >= 0; i--)
                    {
                        st.Push(result[1][i]);
                    }
                }
                listView_IdentifySentense.Items.Add(aItem);
            }
        }

        //往listview增项
        private void addSubItem(ListViewItem aItem, string content)
        {
            ListViewItem.ListViewSubItem SubItem = new ListViewItem.ListViewSubItem();
            SubItem.Text = content;
            aItem.SubItems.Add(SubItem);
        }

        //倒序输出栈
        private string getStck(Stack<char> a)
        {
            string result = "";
            foreach (char y in a)
            {
                result = result.Insert(0, y + "");
            }
            return result;
        }

        //正序输出当前剩余串
        private string getRemainString(string s, int index)
        {
            string result = "";
            for (int i = index; i < s.Length; i++)
            {
                result += s[i];
            }
            return result;
        }

        //用来获取下一步干什么
        private string getRules(char cur_char, char cur_stack)
        {
            //1.cur_stack是终结符，那么看cur_char和cur_stack是不是相等，相等，返回100，检测到1输出相等的符号，并且输出匹配成功，
            // 若不相等，则出错返回空
            if (vtset.Contains(cur_stack))
            {
                if (cur_char == cur_stack)
                {
                    return "100";
                }
                else
                {
                    return null;
                }
            }
            //2.cur_stack若不是终结符，那么看是否是#，若是，看cur_stack是否等于cur_char=#,若是，返回200，句子识别成功
            //若不是#，那么查预测分析表，若查到，返回分析表内容（形式为S->abc）
            // 若查不到，返回nulll
            else
            {
                if (cur_stack == '#')
                {
                    if (cur_char == '#')
                    {
                        return "200";
                        //但是由于#作为终结符了，所以，不识别，这边作废吧
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    string rules = getFromAnalysis(cur_char, cur_stack);
                    if (rules == "")
                    {
                        return null;
                    }
                    else
                    {
                        return rules;
                    }
                }
            }
        }

        //取出分析表中的东西
        private string getFromAnalysis(char cur_char, char cur_stack)
        {
            //能够到这里的，栈顶元素肯定是非终结符，相信我
            //接下来要去定位分析表位置
            //所以要求行列号

            int colsindex = getsetindex(cur_stack, vnset) + 1;//获取栈顶元素在分析表中的行下标
            int rowsindex = getsetindex(cur_char, vtset) + 1;//获取当前读入字符在分析表中的行下标

            //如果没找到，返回空
            if (colsindex == 0 || rowsindex == 0)
            {
                return "";
            }
            string res = analysis[colsindex, rowsindex];
            return res;
        }

        #endregion

        //帮助，使用说明
        private void button_Help_Click(object sender, EventArgs e)
        {
            string help = "注意事项：\n①文法需要没有左递归\n②文法需要正确\n③由于ε不属于ASCII码，这边采用0代替，若产生式规则本身含有0，会导致识别错误\n④下面的文法仅作示范，可以更改";
            MessageBox.Show(help);
        }


    }
}
