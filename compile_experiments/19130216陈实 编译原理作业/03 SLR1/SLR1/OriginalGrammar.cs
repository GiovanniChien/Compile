using System;
using System.Collections.Generic;
using System.Text;
namespace SLR1
{
    class OriginalGrammar
    {
        public HashSet<string> noTSymbol;            // 非终结符号集
        public HashSet<string> tSymbol;                // 终结符号组成的字母表
        public List<Rule> pat;                                   // 重写规则集
        public string startSymbol;                               // 开始符号

        public Dictionary<string, Symbol> dic_sym;   //字符串与符号的映射

        public OriginalGrammar()
        {
            this.noTSymbol = new HashSet<string>();
            this.tSymbol = new HashSet<string>();
            this.pat = new List<Rule>();
            this.startSymbol = "";
            this.dic_sym = new Dictionary<string, Symbol>();
        }

        public Symbol StrToSymbol(string name)    //由字符串获得符号实体
        {
            Symbol sym = new Symbol();
            if (dic_sym.TryGetValue(name, out sym))
                return sym;
            return sym;
        }
    }
}
