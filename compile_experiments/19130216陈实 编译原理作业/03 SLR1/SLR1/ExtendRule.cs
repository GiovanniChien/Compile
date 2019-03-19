using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class ExtendRule
    {
        public string leftRule;
        public int OriginRule;
        public int nextRuleNum;
        public List<string> PointLeft;
        public List<string> PointRight;
        public List<int> BelongState;


        public ExtendRule()
        {
            leftRule = "";
            OriginRule = -1;
            nextRuleNum = -1;
            PointLeft = new List<string>();
            PointRight = new List<string>();
            BelongState = new List<int>();
        }

        public ExtendRule(Rule ori, int i)
        {
            leftRule = ori.left;
            OriginRule = -1;
            nextRuleNum = -1;
            PointLeft = new List<string>();
            PointRight = new List<string>();
            BelongState = new List<int>();
            for (int j = 0; j < ori.right.Count; j++)
            {
                if (j < i) PointLeft.Add(ori.right[j]);
                else PointRight.Add(ori.right[j]);
            }
        }
    }
}
