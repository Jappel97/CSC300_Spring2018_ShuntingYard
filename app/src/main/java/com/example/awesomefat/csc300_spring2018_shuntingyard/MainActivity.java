package com.example.awesomefat.csc300_spring2018_shuntingyard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
{
    private Queue q;
    private Queue outQ;
    private OpStack opStack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //"10+3-2" -> turn into a queue of NumNodes and OpNodes
        this.q = new Queue();
        this.outQ = new Queue();
        this.opStack = new OpStack();
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

    private void testQ(Queue cue)
    {
        while(!cue.isEmpty())
        {
            Node n = cue.dequeue();
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
                this.q.enqueue(Integer.parseInt(currNumber));
                currNumber = "";
                this.q.enqueue(s.charAt(i));
            }
        }
        this.q.enqueue(Integer.parseInt(currNumber));
        //this.testQ();
    }

    private void parseStringTok(String s)
    {
        StringTokenizer st = new StringTokenizer(s,"+-*/", true);
        String temp;
        String ops = "+-*/";
        while(st.hasMoreTokens())
        {
            temp = st.nextToken().trim();
            if(ops.indexOf(temp.charAt(0)) == -1)
            {
                this.q.enqueue(Integer.parseInt(temp));
            }
            else
            {
                //"+" -> '+'
                this.q.enqueue(temp.charAt(0));
            }
        }
        this.testQ(this.q);
    }

    public void fillQ()
    {
        Node n;
        while(!this.q.isEmpty())
        {
            n = this.q.dequeue();
            if (n instanceof NumNode)
            {
                this.outQ.enqueue(((NumNode) n).getPayload());
            }
            else
            {
                //try to push to the stack. If you can't, pop the top into the out queue and try again.
                while (!opStack.push((OpNode) n))
                {
                    OpNode temp = opStack.pop();
                    this.outQ.enqueue(temp.getPayload());
                }
            }
        }
        while(this.opStack.peek() != null)
        {
            this.q.enqueue(this.opStack.pop().getPayload());
        }
    }

    public void onClickMeButtonPressed(View v)
    {
        EditText inputET = (EditText)this.findViewById(R.id.inputET);
        String valueWithoutSpaces = this.removeSpaces(inputET.getText().toString());
        this.parseStringTok(inputET.getText().toString());
    }

    public void onFillMeButtonPressed(View v)
    {
        String input = this.findViewById(R.id.inputET).toString();
        this.parseString(this.removeSpaces(input));
        this.fillQ();
        this.testQ(this.outQ);
    }
}
