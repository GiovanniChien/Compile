using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class ProFillTable
    {
        public OriginalGrammar OG;
        public List<State> StateSet;

        public ProFillTable(OriginalGrammar OG, List<State> StateSet)
        {
            this.OG = OG;
            this.StateSet = StateSet;
        }

        private void FillAction(State tempState, ExtendRule tempER)
        {
            if (tempER.PointRight.Count == 0)
            {
                if (tempER.leftRule == OG.startSymbol)
                {
                    ActionNode node = new ActionNode(ActionChoice.accept);
                    tempState.myAction.Add("#", node);
                    return;
                }
                else
                {
                    ActionNode node = new ActionNode(ActionChoice.reduct, tempER.OriginRule);
                    foreach(string tmp in OG.tSymbol)
                    {
                        tempState.myAction.Add(tmp,node);
                    }
                    return;
                }
            }
            else
            {
                if (OG.StrToSymbol(tempER.PointRight[0]).kind == symKind.term)
                {
                    int next = -1;
                    if (tempState.DFAedge.TryGetValue(tempER.PointRight[0], out next))
                    {
                        ActionNode node = new ActionNode(ActionChoice.addin, next);
                        tempState.myAction.Add(tempER.PointRight[0], node);
                    }
                }
            }
        }

        private void FillGoto(State tempState)
        {
            foreach (KeyValuePair<string, int> temp in tempState.DFAedge)
            {
                if (OG.StrToSymbol(temp.Key).kind == symKind.notTerm)
                    tempState.myGoto.Add(temp.Key, temp.Value);
            }
        }

        public void FillChart()
        {
            for (int i = 0; i < StateSet.Count; i++)
            {
                for (int j = 0; j < StateSet[i].ruleSet.Count; j++)
                    FillAction(StateSet[i], StateSet[i].ruleSet[j]);
                FillGoto(StateSet[i]);
            }
        }
    }
}
