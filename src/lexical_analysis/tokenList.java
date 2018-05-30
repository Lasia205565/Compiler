package lexical_analysis;

import java.util.ArrayList;

/*About the list of token and token/ serve for step1*/
public class tokenList {
    public token getHead() {
		return head;
	}

	public void setHead(token head) {
		this.head = head;
	}

	public token getCurrent() {
		return current;
	}

	public void setCurrent(token current) {
		this.current = current;
	}

	public token head;  
    public token current; 
    public void add(int num,String value,String type){  
      if(head == null) {  
        head= new token(num,value,type);  
        current = head;  
      }  
      else{  
        current.next = new token(num,value,type);  
        current = current.next;  
      }  
    } 
    
    public int getLength(token head) {    
        if(head == null){  
            return 0;  
        }  
        int length = 0;  
        token current = head;  
        while(current != null){  
            length++;  
            current = current.next;  
        }  
        return length;  
    }  
    
    public ArrayList<token> reverse()
    {
        int len = getLength(head);
        ArrayList<token> result = new ArrayList<token> ();
        token current  = head;
        for(int i=0;i<len;i++)
        {
        	result.add(0, current);
        	current=current.next;
        }
        return result;
    }
    
    public void print(token head) {  
        if(head == null){  
            return;  
        }  
        current = head;  
        while(current.next != null) {  
            System.out.println("<"+current.tokenNum+","+current.tokenValue+">"+":"+current.tokentype);     
            current = current.next;  
        }  
        if(current.next==null)
        {
        	System.out.println("<"+current.tokenNum+","+current.tokenValue+">"+":"+current.tokentype);   
        }
    }  
    
	public class token{       /*internal class:token*/
	private int tokenNum;
	private String tokenValue;
	private String tokentype;
	
	public String getType() {
		return tokentype;
	}
	public void setType(String type) {
		this.tokentype = type;
	}
	token next;
	public token(int num,String value,String type)
	{
		tokenNum=num;
		tokenValue=value;
		tokentype =  type;
	}
	public int getTokenNum() {
		return tokenNum;
	}
	public void setTokenNum(int tokenNum) {
		this.tokenNum = tokenNum;
	}
	public String getValue() {
		return tokenValue;
	}
	public void setValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public String toString()
	{
		return "Type number:"+tokenNum+";Type value:"+tokenValue+"\n";
	}
	}
}
