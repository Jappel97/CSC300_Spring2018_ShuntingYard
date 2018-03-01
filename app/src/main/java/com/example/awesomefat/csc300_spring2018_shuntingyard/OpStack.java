package com.example.awesomefat.csc300_spring2018_shuntingyard;

/**
 * Created by awesomefat on 2/27/18.
 */

public class OpStack
{
    private OpNode top;

    public OpStack()
    {
        this.top = null;
    }

    public OpNode peek()
    {
        return this.top;
    }

    public OpNode pop()
    {
        OpNode currTop = this.top;
        if(currTop != null)
        {
            this.top = (OpNode)currTop.getNextNode();
            currTop.setNextNode(null);
        }
        return currTop;
    }

    public Boolean push(OpNode OP)
    {
        String bigOps = "*/";
        //this is the logic for whether you can add a OpNode to the stack
        int priority = bigOps.indexOf(OP.getPayload()) > -1 ? 1 : 0;
        OpNode temp = this.peek();
        int priority2 = bigOps.indexOf(temp.getPayload()) > -1 ? 1 : 0;
        if(priority == priority2)
        {
            return false;
        }
        else if(priority > priority2)
        {
            OP.setNextNode(this.top);
            this.top = OP;
            return true;
        }
        else
        {
            return false;
        }
    }
}
