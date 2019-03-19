using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace test
{
    class clsDelimiter:clsToken
    {
        //界限符数组
         public static List<clsDelimiter> myDelimiters;

        //界限符
         static clsDelimiter()
         {
             myDelimiters = new List<clsDelimiter>();
             myDelimiters.Add(new clsDelimiter("{", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter("}", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter("(", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter(")", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter(",", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter(";", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter(".", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter("[", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter("<", Tag.delimiter, -1));
             myDelimiters.Add(new clsDelimiter(">", Tag.delimiter, -1));
         }

        //界限符内容
        public string sign;

        public clsDelimiter(): base(0,-1)
        {
            line = -1;
            sign = "";
        }

        public clsDelimiter(string s, int t, int line)
            : base(t,line)
        {
            sign = s;
        }
        public override string toString() 
        {
            return "界限符:" + sign;
        }

        public override string myString()
        {
            return sign;
        }
    }

}
