using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace test
{
    class clsRealNum : clsToken
    {
        public float value;
        public clsRealNum(float v,int line)
            : base(Tag.REAL,line)
        {
            value = v;
        }
        public override string toString() 
        {
            return "实数:" + value;
        }
        public override string myString()
        {
            return value + "";
        }
    }
}
