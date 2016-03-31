
/* *********************************************************************************************
  “I pledge that this assignment has been completed in compliance with the Graduate Honor Code
and that I have neither given nor received any unauthorized aid on this work. Further, I did not
use any source codes from any other unauthorized sources, either modified or unmodified. The
submitted programming assignment is solely done by me and is my original work.”
 
 Name:	ARPIT PARIKH 									Date:	3/26/2016
************************************************************************************************* */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

public class bignum {

	public static void main(String args[])
	{
			//Input fileName should write here
			//keep your file as a reference check the readme for the input
		   String fileName = "C:/Interview/Proj1/src/p1.txt";	
		   String line=null;
		   String string="";
		   
		   //Calculator Class
		   	Rpn rpn = new Rpn();
         
		   try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = 
	                new FileReader(fileName);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);
	            	
	            System.out.println("Info:Program Started!");
	            while((line = bufferedReader.readLine()) != null) {
	         
	            	try
	            	{
	            	if(!line.isEmpty())
	            	{	
	            		System.out.println(line+"=");
	            		System.out.println(rpn.calculate(line)+"\n");
	            	}
	            	}
	            	 catch(IllegalArgumentException argumentException)
	     		   {
	     			   System.out.println("Error: wrong Expression!"+"\n");
	     			   continue;
	     		   }
	                 
	            }   

	            // Always close files.
	            bufferedReader.close();         
	        }
		  
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println(
	                "Error reading file '" 
	                + fileName + "'");                  
	           
	        }
	 
	}	
}
	

class Rpn {

   
    private Deque<Token> mInputStack = new ArrayDeque<Token>();
    private long mCurrentToken = 0;
  
    public Rpn() {

    }
    
 
    public BigInteger calculate(String input) {
        //Check illegal input
        if(input == null)
            throw new NullPointerException("Input cannot be null.");

        //Reset the stack and counter if already present
        if(mInputStack.size() != 0) {
            mInputStack = new ArrayDeque<Token>(input.length());
            mCurrentToken = 0;
        }

        //Tokenize the input String and begin to process the tokens
        StringTokenizer tokenizer = new StringTokenizer(input, " ");
        while(tokenizer.hasMoreTokens()) {
            processToken(tokenizer.nextToken());
            mCurrentToken++;
        }

        //Only one shall remain...
        if(mInputStack.size() != 1)
            throw new IllegalArgumentException("Invalid result size of " + mInputStack.size());

        //Gimme dat!
        return mInputStack.pop().getValue();
    }

   
    private void processToken(String token) {
        //Single char, check its type for correct parsing
        if(token.length() == 1) {
            char temp = token.charAt(0);
            //Digit
            if(Character.isDigit(temp)) {
                mInputStack.push(new ValueToken(token));
                
            }
            //Math symbol such as + - * /
            else if(isMathSymbol(temp)) {
                processOperator(token);
            }
            //Illegal character
            else {
                throw new IllegalArgumentException("Invalid token character " + token + " at position " + mCurrentToken + ".");
            }
        }
        //Has to be a digit if length > 1
        else {
            mInputStack.push(new ValueToken(token));
           
        }
    }

 
    private void processOperator(String operand) {
        if(mInputStack.size() < 2)
            throw new IllegalArgumentException("Does not have two operands for operation " + operand + " at position " + mCurrentToken + ".");

        char operator = operand.charAt(0);
        BigInteger first = mInputStack.pop().getValue(); 
        BigInteger second = mInputStack.pop().getValue();
        ValueToken result = null;

      

        //Check the operator to see which action to perform
        switch(operator) {
            case '+':
                result = new ValueToken(second.add(first));
                mInputStack.push(result);
                break;
            case '-':
                result = new ValueToken(second.subtract(first));
                mInputStack.push(result);
                break;
             
            case '^':
                result = new ValueToken(second.pow((int)first.longValue()));
                mInputStack.push(result);
                break;
                
            case '/':
                if(first.longValue() == 0)
                    throw new IllegalArgumentException("Cannot divide by 0.");

                result = new ValueToken(second.divide(first));
                mInputStack.push(result);
                break;
            case '*':
                result = new ValueToken(second.multiply(first));
                mInputStack.push(result);
                break;
        }

       
    }

  
    private boolean isMathSymbol(char token) {
        switch(token) {
            case '+':
            case '-':
            case '/':
            case '*':
            case '^':
                return true;
            default: return false;
        }
    }

}
 
interface Token {

	    public BigInteger getValue();

	}
 

class ValueToken implements Token {

    private BigInteger value;

    public ValueToken(String input) {
        try {
            value = new BigInteger(input);
          
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException("Illegal number (" + input + ") as input.");
        }
    }

    public ValueToken(BigInteger input) {
        value = input;
    }

    public BigInteger getValue() {
        return value;
    }
}
 
