using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class NotTermSymbol : Symbol
    {
        public NotTermSymbol()
        {
            this.name = "";
            this.kind = symKind.notTerm;
        }

        public NotTermSymbol(string name)
        {
            this.name = name;
            this.kind = symKind.notTerm;
        }
    }
}
