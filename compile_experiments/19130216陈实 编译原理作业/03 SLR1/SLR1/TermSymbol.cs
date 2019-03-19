using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class TermSymbol : Symbol
    {
        public TermSymbol()
        {
            this.name = "";
            this.kind = symKind.term;
        }

        public TermSymbol(string name)
        {
            this.name = name;
            this.kind = symKind.term;
        }
    }
}
