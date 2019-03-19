using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class ProCreateDFA
    {
        public OriginalGrammar OG;
        public List<State> StateSet;
        private List<ExtendRule> ERSet;

        public ProCreateDFA(OriginalGrammar OG)
        {
            this.OG = OG;
            StateSet = new List<State>();
            ERSet = new List<ExtendRule>();
        }

        private int FindERinSet(ExtendRule tempER)
        {
            for (int i = 0; i < ERSet.Count; i++)
                if (ERSet[i].leftRule == tempER.leftRule
                    && ERSet[i].PointLeft == tempER.PointLeft
                    && ERSet[i].PointRight == tempER.PointRight)
                    return i;
            return -1;
        }

        private void SetStateSet(State nowS)
        {
            if (!StateSet.Contains(nowS))
                StateSet.Add(nowS);
        }

        private void SetRuleBelong(List<ExtendRule> stateER, int stateNum)
        {
            for (int i = 0; i < stateER.Count; i++)
                stateER[i].BelongState.Add(stateNum);
        }

        private int FindNext(string pointFirst, int start)
        {
            for (int i = start; i < ERSet.Count; i++)
                if (ERSet[i].leftRule == pointFirst && ERSet[i].PointLeft.Count == 0)
                    return i;
            return -1;
        }

        private void SameStateRule(int erNum, List<ExtendRule> stateER)
        {
            ExtendRule er = ERSet[erNum];
            int nextIndex = -1;
            stateER.Add(er);
            if (er.PointRight.Count != 0)
            {
                if (OG.StrToSymbol(er.PointRight[0]).kind == symKind.notTerm)
                {
                    do
                    {
                        nextIndex = FindNext(er.PointRight[0], nextIndex + 1);
                        if (nextIndex != -1)
                            SameStateRule(nextIndex, stateER);
                    } while (nextIndex != -1);
                }
            }
        }

        private int CalNextState(ExtendRule nowER)
        {
            if (nowER.PointRight.Count == 0) return -1;
            return ERSet[nowER.nextRuleNum].BelongState[0];
        }

        public void CreateERSet()
        {
            for (int i = 0; i < OG.pat.Count; i++)
            {
                Rule tempR = OG.pat[i];
                for (int j = 0; j <= tempR.right.Count; j++)
                {
                    ExtendRule er = new ExtendRule(tempR, j);
                    er.OriginRule = i;
                    if (j < tempR.right.Count) er.nextRuleNum = ERSet.Count + 1;
                    else er.nextRuleNum = -1;
                    ERSet.Add(er);
                }
            }
        }

        public void CreateState()
        {
            int stateNo = 0;
            for (int i = 0; i < ERSet.Count; i++)
            {
                List<ExtendRule> stateER = new List<ExtendRule>();
                if (ERSet[i].PointRight.Count == 0 ||
                       OG.StrToSymbol(ERSet[i].PointRight[0]).kind == symKind.notTerm)
                {
                    SameStateRule(i, stateER);
                    State newState = new State(stateNo++, stateER);
                    SetRuleBelong(stateER, newState.stateNum);
                    SetStateSet(newState);
                }
            }
        }

        public void ConnetState()
        {
            for (int i = 0; i < StateSet.Count; i++)
            {
                List<ExtendRule> ERList = StateSet[i].ruleSet;
                int nextS = -1;
                for (int j = 0; j < ERList.Count; j++)
                {
                    nextS = CalNextState(ERList[j]);
                    if (nextS != -1)
                        StateSet[i].DFAedge.Add(ERList[j].PointRight[0], nextS);
                }
            }
        }

        public void CreateDFA()
        {
            CreateERSet();
            CreateState();
            ConnetState();
        }
    }
}
