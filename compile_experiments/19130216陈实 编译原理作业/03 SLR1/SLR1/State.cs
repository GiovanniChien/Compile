using System;
using System.Collections.Generic;
using System.Text;

namespace SLR1
{
    public enum ActionChoice
    {
        addin,reduct,accept,error
    }

    class ActionNode
    {
        public ActionChoice nowAction;
        public Rule nowRule;
        public int nextState;
        public ActionNode(ActionChoice choice, int n)
        {
            nowAction = choice;
            nextState = n;
            nowRule = new Rule();
        }
        public ActionNode(ActionChoice choice)
        {
            nowAction = choice;
            nowRule = new Rule();
            nextState = -1;
        }
        public ActionNode(ActionChoice choice,Rule r)
        {
            nowAction=choice;
            nowRule=r;
            nextState = -1;
        }
    }
    
    class State
    {
        public int stateNum;
        public List<ExtendRule> ruleSet;
        public Dictionary<string, int> DFAedge;
        public Dictionary<string,ActionNode> myAction;
        public Dictionary<string,int> myGoto;

        public State(int num,List<ExtendRule> ruleSet)
        {
            stateNum=num;
            this.ruleSet=ruleSet;
            myAction=new Dictionary<string,ActionNode>();
            myGoto=new Dictionary<string,int>();
            DFAedge = new Dictionary<string, int>();
        }

        public void SetDFAedge(string strTermSym, int num)
        {
            if (!DFAedge.ContainsKey(strTermSym))
                DFAedge.Add(strTermSym, num);
        }

        public void SetRuleSet(ExtendRule er)
        {
            if(!ruleSet.Contains(er))
                ruleSet.Add(er);
        }

        public void SetAction(string strTermSym,ActionChoice choice,Rule r)
        {
            ActionNode tempNode=new ActionNode(choice,r);
            if(!myAction.ContainsKey(strTermSym))
                myAction.Add(strTermSym,tempNode);
        }

        public void SetGoto(string strNoTermSym,int num)
        {
            if(!myGoto.ContainsKey(strNoTermSym))
                myGoto.Add(strNoTermSym,num);
        }
    }
}
