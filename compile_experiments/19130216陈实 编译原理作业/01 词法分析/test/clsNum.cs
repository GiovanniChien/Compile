using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace test
{
    class clsNum : clsToken
    {
        public int value;
        public clsNum(int v,int line) :base(Tag.NUM,line)
        { 
            value = v; 
        }
        public override string toString() 
        {
            return "整数:" + value;
        }
        public override string myString()
        {
            return value + "";
        }
    }
}
