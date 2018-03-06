package com.example.awesomefat.csc300_spring2018_shuntingyard;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by Josh on 3/6/2018.
 */

public class Stack {
    //create a bottom up design.
    private NumNode root;
    protected NumNode top;
    private NumNode prev;

    public Stack()
    {
        this.root = null;
        this.top = null;
        this.prev = null;
    }

    public void push(NumNode r1, Stack stack)
    {
        //moves a disk from the register onto the top of the stack
        //if the stack is empty, we put the disk on top of the base and label it as the top
        if(stack.root == null)
        {
            stack.root.setNextNode(r1);
            r1 = null;
            stack.top = (NumNode) stack.root.getNextNode();
        }
        //else, if there is a top, we point it to the new disk, and label the new disk as top.
        else
        {
            stack.top.setNextNode(r1);
            r1 = null;
            stack.prev = stack.top;
            stack.top = (NumNode) stack.top.getNextNode();
        }
    }

    public NumNode peek(Stack stack)
    {
        return stack.top;
    }

    public NumNode pop(Stack stack)
    {
        //removes the top disk of the stack and puts it in the register
        //fills the register with null if the stack is empty.
        NumNode r1 = stack.top;
        stack.top = stack.prev;
        stack.top.setNextNode(null);
        return r1;
    }

    public int getTop()
    {
        return this.top.getPayload();
    }
}
