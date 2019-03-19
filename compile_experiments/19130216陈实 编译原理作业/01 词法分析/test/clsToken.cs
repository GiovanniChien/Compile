using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace test
{
    //标记类，所有的类型都基于标记，包括未识别的类型
    class clsToken
    {
        public int tag;
        public int line;
        public clsToken(int t,int line) 
        { 
            tag = t;
            this.line = line;
        }
        public virtual string toString() 
        {
            char tmp = (char)tag;
            if (tmp=='\0')
            {
                return "";
            }
            return "未知: " + tmp; 
        }

        public virtual string myString()
        {
            char tmp = (char)tag;
            if (tmp == '\0')
            {
                return "";
            }
            return tmp + "";
        }
    }
}
