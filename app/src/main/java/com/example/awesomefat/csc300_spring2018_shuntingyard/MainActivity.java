package com.example.awesomefat.csc300_spring2018_shuntingyard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
{
    private Queue inputQ;
    private OpStack opStack;
    private Queue outputQ;
    private Stack solStack;
    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answer = this.findViewById(R.id.Answer);

        //"10+3-2" -> turn into a queue of NumNodes and OpNodes
        this.inputQ = new Queue();
        this.outputQ = new Queue();
        this.opStack = new OpStack();
        this.solStack = new Stack();
    }

    private String removeSpaces(String s)
    {
        String answer = "";
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) != ' ')
            {
                answer = answer + s.charAt(i);
            }
        }
        return answer;
    }

    private void testQ(Queue q)
    {
        while(!q.isEmpty())
        {
            Node n = q.dequeue();
            if(n instanceof NumNode)
            {
                NumNode temp = (NumNode)n;
                System.out.println(temp.getPayload());
            }
            else
            {
                OpNode temp = (OpNode)n;
                System.out.println(temp.getPayload());
            }
        }
    }

    private void parseString(String s)
    {
        String currNumber = "";
        String digits = "0123456789";
        for(int i = 0; i < s.length(); i++)
        {
            if(digits.indexOf(s.charAt(i)) != -1)
            {
                currNumber = currNumber + s.charAt(i);
            }
            else
            {
                this.inputQ.enqueue(Integer.parseInt(currNumber));
                currNumber = "";
                this.inputQ.enqueue(s.charAt(i));
            }
        }
        this.inputQ.enqueue(Integer.parseInt(currNumber));
        this.testQ(this.inputQ);
    }

    private void fillInputQ(String s)
    {
        StringTokenizer st = new StringTokenizer(s,"+-*/", true);
        String temp;
        String ops = "+-*/";
        while(st.hasMoreTokens())
        {
            temp = st.nextToken().trim();
            if(ops.indexOf(temp.charAt(0)) == -1)
            {
                this.inputQ.enqueue(Integer.parseInt(temp));
            }
            else
            {
                //"+" -> '+'
                this.inputQ.enqueue(temp.charAt(0));
            }
        }
        //this.testQ(this.inputQ);
    }

    private void processInputQ()
    {
        Node currNode;
        while(!this.inputQ.isEmpty())
        {
            currNode = this.inputQ.dequeue();
            if(currNode instanceof NumNode)
            {
                this.outputQ.enqueue((NumNode)currNode);
            }
            else
            {
                this.opStack.push((OpNode)currNode, this.outputQ);
            }
        }
        this.opStack.clearOpStack(this.outputQ);
    }

    private void processOutputQueue()
    {
        //ultimately show the answer on the screen
        while(!outputQ.isEmpty())
        {
            Node temp = outputQ.dequeue();
            if(temp instanceof NumNode)
            {
                solStack.push((NumNode) temp, solStack);
            }
            else if(temp instanceof OpNode)
            {
                NumNode thing = new NumNode(doMath((OpNode) temp, solStack.pop(solStack), solStack.pop(solStack)));
                solStack.push(thing, solStack);
            }
        }
        answer.setText(solStack.getTop());
    }
    public void onClickMeButtonPressed(View v)
    {
        EditText inputET = (EditText)this.findViewById(R.id.inputET);
        String valueWithoutSpaces = this.removeSpaces(inputET.getText().toString());
        this.fillInputQ(inputET.getText().toString());
        this.processInputQ();
        this.testQ(this.outputQ);
    }

    private int doMath(OpNode op, NumNode R1, NumNode R2)
    {
        char op1 = op.getPayload();
        int Reg1 = R1.getPayload();
        int Reg2 = R2.getPayload();
        if(op1 == '+')
            return Reg1 + Reg2;
        else if(op1 == '-')
            return Reg2 - Reg1;
        else if(op1 == '*')
            return Reg1*Reg2;
        else if(op1 == '/')
            return Reg2/Reg1;
        else
            return -1;
    }
}