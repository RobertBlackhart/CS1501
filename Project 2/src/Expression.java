import java.util.EmptyStackException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Name:  Robert McDermot
 * Email: rom66@pitt.edu
 * ID #:  ***2800
 * Date:  9/11/13
 */
public class Expression
{
	private String expressionString;
	static Map<String, Boolean> atomMap = new LinkedHashMap<String, Boolean>();
	Node root = null;

	public Expression(String s) throws ParseError
	{
		expressionString = s;
		parse();
		TreeDisplay display = new TreeDisplay(s);
		display.setRoot(root);
	}

	public Expression(String s, boolean displayTree) throws ParseError
	{
		expressionString = s;
		parse();
	}

	public Node parse() throws ParseError
	{
		String expression = expressionString;
		Stack <Character> operatorStack = new Stack<Character>();
		Stack<Node> operandStack = new Stack<Node>();

		for(char c : expression.toCharArray())
		{
			if(c == ' ')
				continue;
			else if(c == '(')
			{
				operatorStack.push(c);
			}
			else if(isAtom(c))
			{
				Node node = new Node(String.valueOf(c), null, null);
				operandStack.push(node);
			}
			else if(isUnaryOp(c) || isBinaryOp(c))
			{
				if(operatorStack.empty() || operatorStack.peek() == '(')
				{
					operatorStack.push(c);
				}
				else // clear operator stack and push new one onto it
				{
					do
					{
						char operator = operatorStack.pop();
						Node right = operandStack.pop();
						Node left = null;
						if(isBinaryOp(operator))
						{
							try
							{
								left = operandStack.pop();
							}
							catch(EmptyStackException ex)
							{
								throw new ParseError("Error:\n"+expressionString + " Illegal character : A non-atom must begin with '('");
							}
						}
						Node node = new Node(String.valueOf(operator), left, right);
						operandStack.push(node);
					} while(!operatorStack.empty() && operatorStack.peek() != '(');

					operatorStack.push(c);
				}
			}
			else if(c == ')') //clear operatorStack back to the '('
			{
				while(operatorStack.size() != 0)
				{
					char operator = operatorStack.pop();
					if(operator == '(')
					{
						break;
					}
					if(isUnaryOp(operator))
					{
						Node node = new Node(String.valueOf(operator), null, operandStack.pop());
						operandStack.push(node);
					}
					if(isBinaryOp(operator))
					{
						try
						{
							Node right = operandStack.pop();
							Node left = operandStack.pop();
							Node node = new Node(String.valueOf(operator), left, right);
							operandStack.push(node);
						}
						catch(EmptyStackException ex)
						{
							throw new ParseError("Error:\n" + expressionString + " Binary operator must have 2 operands");
						}
					}
				}
			}
		}

		/*while(!operatorStack.empty())
		{
			char operator = operatorStack.pop();
			if(operator == '(')
				continue;
			if(isUnaryOp(operator))
			{
				Node node = new Node(String.valueOf(operator), null, operandStack.pop());
				operandStack.push(node);
			}
			if(isBinaryOp(operator))
			{
				try
				{
					Node right = operandStack.pop();
					Node left = operandStack.pop();
					Node node = new Node(String.valueOf(operator), left, right);
					operandStack.push(node);
				}
				catch(EmptyStackException ex)
				{
					throw new ParseError("Error:\n" + expressionString + " Binary operator must have 2 operands");
				}
			}
		}*/

		if(operatorStack.size() > 0)
			throw new ParseError("Error:\n" + expressionString + " Missing right parenthesis");
		if(operandStack.size() > 1)
			throw new ParseError("Error:\n" + expressionString + " Illegal binary operator");
		root = operandStack.pop();
		return root;
	}

	public static void setAtom(String atom, String value)
	{
		atomMap.put(atom, Boolean.valueOf(value));
	}

	public boolean evaluate() throws ParseError
	{
		if(root == null)
			parse();
		return evaluate(root);
	}

	private boolean evaluate(Node node)
	{
		if(node != null)
		{
			if(node.left == null && node.right == null)
				return atomMap.get(node.symbol);
			else
			{
				if(isUnaryOp(node.symbol.charAt(0)))
					return !evaluate(node.right);
				else
				{
					if(node.symbol.charAt(0) == '^')
						return evaluate(node.right) && evaluate(node.left);
					else
						return evaluate(node.right) || evaluate(node.left);
				}
			}
		}
		else
			return false;
	}

	public Expression copy() throws ParseError
	{
		return new Expression(expressionString,false);
	}

	public void normalize()
	{
		normalize(root);
	}

	private void normalize(Node node)
	{
		if(node != null)
		{
			if(isAtom(node.symbol.charAt(0)))
				return;
			if(isUnaryOp(node.symbol.charAt(0)) && isUnaryOp(node.right.symbol.charAt(0)))
			{
				node.symbol = node.right.right.symbol;
				node.left = node.right.right.left;
				node.right = node.right.right.right;
				normalize(node);
			}
			if(isUnaryOp(node.symbol.charAt(0)) && isBinaryOp(node.right.symbol.charAt(0)))
			{
				char binaryOp = node.right.symbol.charAt(0);
				char newBinaryOp = '^';
				if(binaryOp == '^')
					newBinaryOp = 'v';
				Node negLeftNode = new Node("!",null,node.right.left);
				Node negRightNode = new Node("!",null,node.right.right);
				Node newNode = new Node(String.valueOf(newBinaryOp),negLeftNode,negRightNode);
				node.symbol = newNode.symbol;
				node.left = newNode.left;
				node.right = newNode.right;
				normalize(node);
			}
			if(isBinaryOp(node.symbol.charAt(0)))
			{
				if(node.symbol.charAt(0) == '^')
				{
					if(node.left.symbol.charAt(0) == '^')
						normalize(node.left);
					if(node.right.symbol.charAt(0) == '^')
						normalize(node.right);

					if(node.left.symbol.charAt(0) == 'v')
					{
						Node newAnd1 = new Node("^",node.left.left,node.right);
						Node newAnd2 = new Node("^",node.left.right,node.right);
						node.symbol = "v";
						node.left = newAnd1;
						node.right = newAnd2;
					}
					if(node.right.symbol.charAt(0) == 'v')
					{
						Node newAnd1 = new Node("^",node.right.left,node.left);
						Node newAnd2 = new Node("^",node.right.right,node.left);
						node.symbol = "v";
						node.left = newAnd1;
						node.right = newAnd2;
					}
				}

				normalize(node.left);
				normalize(node.right);
			}
		}
	}

	public void displayNormalized()
	{
		if(root == null)
		{
			try
			{
				parse();
			}
			catch(ParseError parseError)
			{
				parseError.printStackTrace();
			}
		}
		normalize();
		TreeDisplay display = new TreeDisplay("normalized " + expressionString);
		display.setRoot(root);
	}

	@Override
	public String toString()
	{
		//returns the print form of this expression
		return expressionString;
	}

	public static boolean isAtomAssignment(String s)
	{
		String[] elements = s.split(" ");
		if(!elements[0].substring(0, 1).equals("(") && elements.length == 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isAtom(char c)
	{
		return (Character.isLetter(c) && c != 'v');
	}

	public boolean isUnaryOp(char c)
	{
		return c == '!';
	}

	public boolean isBinaryOp(char c)
	{
		return (c == '^' || c == 'v');
	}
}
