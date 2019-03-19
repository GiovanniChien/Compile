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
using System.Xml;
using System.IO;
namespace SLR1
{
    class ProLoadGrammar
    {
        public OriginalGrammar OG;
        public string prules;
        public ProLoadGrammar(string s, OriginalGrammar OG)
        {
            this.OG = OG;
            prules = s;
        }

        private void LoadNoTermSymbol()
        {
            //复制一份规则
            string tmp = prules;
            //分开每条规则
            string RegexStrByLine = ".+(\n)*";
            MatchCollection mc1 = Regex.Matches(tmp, RegexStrByLine);
            foreach (Match item1 in mc1)
            {
                string contentwithn = item1.ToString();
                string content = contentwithn.Replace("\n", "");
                //把每一行交给下一个函数整理

                string RegexStr = "(?<left>.+)->(?<right>.+)";
                MatchCollection mc2 = Regex.Matches(content, RegexStr);
                foreach (Match item2 in mc2)
                {
                    string left = item2.Groups["left"].ToString();
                    //第一次出现
                    if (!OG.noTSymbol.Contains(left))
                    {
                        NotTermSymbol nts = new NotTermSymbol(left);
                        OG.noTSymbol.Add(left);
                        OG.dic_sym.Add(left, nts);
                    }
                }
            }
        }

        public void LoadTermSymbol()
        {
            //复制一份规则
            string tmp = prules;
            //分开每条规则
            string RegexStrByLine = ".+(\n)*";
            MatchCollection mc1 = Regex.Matches(tmp, RegexStrByLine);
            foreach (Match item1 in mc1)
            {
                string contentwithn = item1.ToString();
                string content = contentwithn.Replace("\n", "");
                //把每一行交给下一个函数整理

                string RegexStr = "(?<left>.+)->(?<right>.+)";
                MatchCollection mc2 = Regex.Matches(content, RegexStr);
                foreach (Match item2 in mc2)
                {
                    //取右边的部分
                    string right = item2.Groups["right"].ToString();
                    right = right.Replace("\r", "");
                    string[] res = right.Split('|');

                    foreach (string tmpright in res)
                    {
                        for (int i = 0; i < tmpright.Length; i++)
                        {
                            if (!OG.noTSymbol.Contains(tmpright[i]+""))	//且不是非终结符
                            {
                                if (!OG.tSymbol.Contains(tmpright[i] + ""))	//且不是非终结符
                                {
                                    TermSymbol ts = new TermSymbol(tmpright[i] + "");
                                    OG.tSymbol.Add(tmpright[i] + "");
                                    OG.dic_sym.Add(tmpright[i] + "", ts);
                                }
                            }
                        }
                    }
                }
            }
            TermSymbol ts1 = new TermSymbol("#");
            OG.tSymbol.Add("#");
            OG.dic_sym.Add("#", ts1);
        }

        public List<string> StringRightToRule(string s)
        {
            List<string> right = new List<string>();
            if (s == "emp")
            {
                right.Add(s);
                return right;
            }

            for (int i = 0; i < s.Length; i++)
            {
                string now = s.Substring(i, 1);
                if (OG.noTSymbol.Contains(now) || OG.tSymbol.Contains(now))
                    right.Add(now);
            }
            return right;
        }

        public void LoadRule()
        {
            //复制一份规则
            string tmp = prules;
            //分开每条规则
            string RegexStrByLine = ".+(\n)*";
            MatchCollection mc1 = Regex.Matches(tmp, RegexStrByLine);
            foreach (Match item1 in mc1)
            {
                string contentwithn = item1.ToString();
                string content = contentwithn.Replace("\n", "");
                //把每一行交给下一个函数整理

                string RegexStr = "(?<left>.+)->(?<right>.+)";
                MatchCollection mc2 = Regex.Matches(content, RegexStr);
                foreach (Match item2 in mc2)
                {
                     List<string> listRight = new List<string>();

                    //找到左部
                    string left = item2.Groups["left"].ToString();

                    //找到右部
                    string right = item2.Groups["right"].ToString();

                    right = right.Replace("\r", "");
                    string[] res = right.Split('|');

                    foreach (string tmpright in res)
                    {
                         listRight = StringRightToRule(tmpright);
                    Rule myRule = new Rule(left, listRight);
                    OG.pat.Add(myRule);
                    }
                }
            }
        }

        public void LoadStartSymbol()
        {
            foreach (string tmp in OG.noTSymbol)
            {
                OG.startSymbol = tmp;
                break;
            }
        }

        public OriginalGrammar LoadGram()
        {
            //doc.Load(text);
            LoadNoTermSymbol();
            LoadTermSymbol();
            LoadRule();
            LoadStartSymbol();
            return OG;
        }
    }
}
