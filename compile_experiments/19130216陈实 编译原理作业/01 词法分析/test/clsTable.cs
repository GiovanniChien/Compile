using System;
using System.Collections;
using System.IO;
using System.Linq;
using System.Text;

namespace test
{
    class clsTable
    {
        string dangqianzibiaoshumu = "curzibiaonum";
        string zibiaobianhao = "zibiaobianhao";
        clsLexer lex;
        StreamReader fileStreamReader;
        public Hashtable GlobalTable;//全局变量表,既可以加string，也可以继续加hashtable
        public Stack cur_Weizhi;//表明我当前所在的层数位置
        public Hashtable curtable;

        public clsTable(StreamReader fileStreamReader)
        {
            this.fileStreamReader = fileStreamReader;
            lex = new clsLexer(fileStreamReader);
            GlobalTable = new Hashtable();
            //一开始curtable指向Global
            cur_Weizhi = new Stack();
            cur_Weizhi.Push(GlobalTable);
            curtable = (Hashtable)cur_Weizhi.Peek();
            //定义开始层中的子表个数,为0
            GlobalTable.Add(dangqianzibiaoshumu, 0);
        }

        public void work()
        {
            while (fileStreamReader.EndOfStream == false)
            {
                clsToken ct = lex.scan();
                string x = ct.myString();

                if (isZuokuohao(x))
                {
                    //是左括号的话，说明进入了一个新的定义层，
                    //我们新建一个新的Hashtable，加入当前层中，并加入stack，并更新当前层，
                    //定义一个新的hashtable
                    Hashtable tmp = new Hashtable();
                    //定义其中的初始数目
                    tmp.Add(dangqianzibiaoshumu, 0);
                    //定义完毕后，要加入到当前层
                    //首先获得当前层的子表的个数
                    int cnt = (int)curtable[dangqianzibiaoshumu];
                    //构造标准化子表名称：字标编号+cnt
                    string tmpname = zibiaobianhao + cnt;
                    //更新计数器
                    curtable[dangqianzibiaoshumu] = cnt + 1;
                    //将这个心的hashtable加入当前表，关键字是标准化命名，值就是hashtable
                    curtable.Add(tmpname, tmp);
                    //加入stack
                    cur_Weizhi.Push(tmp);
                    //更新当前层
                    curtable = (Hashtable)cur_Weizhi.Peek();
                }

                if (isYoukuohao(x))
                {
                    //遇到右括号，代表着当前子空间已经结束了。
                    //如果遇到右括号，弹出栈，更新当前层
                    cur_Weizhi.Pop();
                    curtable = (Hashtable)cur_Weizhi.Peek();
                }

                //如果形如int啊  我们称之为定义符号，并进入定义声明阶段
                if (IsDingYiFu(x))
                {
                    while (true)
                    { //如果读到了定义符，我们接着读下一个词法单元
                        clsToken mytmpct = lex.scan();
                        string tmpx = mytmpct.myString();

                        if (isBiaozhifu(mytmpct))
                        {
                            //如果读取的词是标识符
                            //先在本层判重
                            if (curtable.Contains(tmpx))
                            {
                                //已经有了 拒绝定义，本版本直接报错。
                                Console.Write("error");
                            }
                            else
                            {
                                //否则不重复，加入其中
                                curtable.Add(tmpx, mytmpct);
                            }

                        }

                        //如果读到的是分号，推出变量声明阶段
                        if (IsFenhao(tmpx))
                        {
                            break; ;
                        }
                    }
                   
                }
            }
            fileStreamReader.Close();
        }

        private bool isYoukuohao(string x)
        {
            return x.Equals(")") || x.Equals("}");
        }

        private bool IsDingYiFu(string x)
        {
            if (x.Equals("int"))
            {
                return true;
            }
            return false;
        }

        private bool IsFenhao(string tmpx)
        {
            return tmpx.Equals(";");
        }

        private bool isBiaozhifu(clsToken mytmpct)
        {
            return mytmpct.tag == Tag.ID;
        }

        private bool isZuokuohao(string x)
        {
            return x.Equals("(") || x.Equals("{");
        }

    }
}
