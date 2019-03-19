using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    class stackNode
    {
        public int stateNum;
        public string sym;
        public stackNode(int stateNum, string sym)
        {
            this.stateNum = stateNum;
            this.sym = sym;
        }
    }
    class ProJudge
    {
        public OriginalGrammar OG;
        public List<State> StateSet;
        public Stack<stackNode> judgeStack;

        public ProJudge(OriginalGrammar OG, List<State> StateSet)
        {
            this.OG = OG;
            this.StateSet = StateSet;
            judgeStack = new Stack<stackNode>();
        }

        public bool Judge(string test)
        {
            stackNode snode = new stackNode(0, "#");
            judgeStack.Push(snode);
            for (int i = 0; i < test.Length; )
            {
                string tmp = test[i].ToString();
                ActionNode node = new ActionNode(ActionChoice.error);
                int sNo = judgeStack.Peek().stateNum;
                if (StateSet[sNo].myAction.TryGetValue(tmp, out node))
                {
                    if (node.nowAction == ActionChoice.accept) return true;
                    if (node.nowAction == ActionChoice.addin)
                    {
                        stackNode nextN = new stackNode(node.nextState, tmp);
                        judgeStack.Push(nextN);
                        i++;
                        continue;
                    }
                    if (node.nowAction == ActionChoice.reduct)
                    {
                        Rule r = node.nowRule;
                        int nextSta = -1;
                        stackNode tempNode = new stackNode(0, "#");
                        for (int j = 0; j < r.right.Count; j++)
                            tempNode = judgeStack.Pop();
                        tempNode = judgeStack.Peek();
                        if (StateSet[tempNode.stateNum].myGoto.TryGetValue(r.left, out nextSta))
                        {
                            if (nextSta == -1) return false;
                            stackNode nextN = new stackNode(nextSta, r.left);
                            judgeStack.Push(nextN);
                            continue;
                        }
                    }
                }
                return false;
            }
            return true;
        }
    }
}
