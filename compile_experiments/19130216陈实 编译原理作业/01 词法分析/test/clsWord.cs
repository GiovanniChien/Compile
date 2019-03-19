using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace test
{
    class clsWord : clsToken
    {
        public string lexeme;

        public clsWord()
            : base(0,-1)
        {
            line = -1;
            lexeme = "";
        }

        public clsWord(string s, int t,int line)
            : base(t,line)
        {
            lexeme = s;
        }
        public override string toString()
        {
            if (tag == Tag.ID)
                return "标识符:" + lexeme;
            else if (tag == Tag.error)
                return "错误标识符" + lexeme;
            else
                return "保留字:" + lexeme;
        }

        public override string myString()
        {
            return lexeme;
        }
    }
}
