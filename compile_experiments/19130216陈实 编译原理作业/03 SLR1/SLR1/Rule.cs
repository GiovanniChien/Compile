using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class Rule
    {
        public string left;
        public List<string> right;

        public Rule()
        {
            this.left = "";
            this.right = new List<string>();
        }

        public Rule(string left, List<string> right)
        {
            this.left = left;
            this.right = right;
        }
    }
}
