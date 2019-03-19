using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
namespace ffs
{
    class clsRules
    {
        public HashSet<clsRule> sl;
        public clsRules()
        {
            //初始化，防止重复
            sl = new HashSet<clsRule>();
        }

        public void clearall()
        {
            sl.Clear();
        }
    }
}
