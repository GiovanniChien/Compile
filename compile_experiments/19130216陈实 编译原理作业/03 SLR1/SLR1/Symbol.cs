using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    public enum symKind
    {
        term = 0,
        notTerm = 1
    }
    class Symbol
    {
        public string name;
        public symKind kind;
    }
}
