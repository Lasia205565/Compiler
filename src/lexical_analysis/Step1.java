package lexical_analysis;
/*to get the token list
 * can't support a line with nothing but space
 * */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Step1 {
	private String [] keyword=
		{
				"int","char","float","long","double","short","signed","break","continue","case","do","while","else",
				"default","enum","extern","for","if","return","sizeof","static","struct","switch","typedef","union",
				"unsigned","void","bool","include" /*for the token_num 0-28*/
		};
	@SuppressWarnings("unused")
	private String [] operator=
		{
			"+","-","*","/","=","-=","+=","*=","/=",">>","<<","%","<",">","<=",">=","!","~","|","&","||","&&","++","--","==","!="
		};  /*for the token_num 41-80*/
	@SuppressWarnings("unused")
	private String [] bound=
		{
			"{","}","[","]","(",")","\'","\"",":",";"
		};  /*for the token_num 81-90*/
	@SuppressWarnings("unused")
	private String [] others=
		{
				"?",",",".","\\","\\*","*\\","\\\\"
		}; /*for the token_num 91-100*/
	
	/*identifier for 101, float for 102,integer for 103,string for 104,char for 105*/
	
	public int isKeyword(String word)
	{
		for(int i=0;i<keyword.length;i++)
		{
			if(keyword[i].equals(word))
				return i;
		}
		return -1;
	}
	
	
	public boolean isSpace(char c)
	{
		if(c==' '||c=='\t'||c=='\n'||c=='\r')
		{
			return true;
		}
		return false;
	}
	
	public boolean isAlpha(char c)
	{
		if((c>='A'&&c<='Z')||(c>='a'&&c<='z'))
		{
			return true;
		}
		return false;
	}
	
	public boolean isNumber(char c)
	{
		if(c>='0'&&c<='9')
		{
			return true;
		}
		return false;
	}
	
	public String copy_token(String line,int first,int forward)
	{
	    StringBuffer word = new StringBuffer();
	    for(int m=first;m<=forward;m++)
	    {
	       word.append(line.charAt(m));
	    }
	    return word.toString();
	}
	
	public tokenList getToken(String filename) throws IOException
	{
		File file = new File(filename);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(file));
	    tokenList tokenlist = new tokenList();
		String line = null; 
		boolean inNote=false;
		boolean inStr=false;
		if(!file.exists())
		{
			System.out.println("This file does not exist...");
			return null;
		}
        while(((line = br.readLine()))!=null)
        {
        	String tokens=null;
        	int len=line.length();
            int i= 0;
            int first=0;
            int forward=0;
            while(i<len)
            {
                if(inStr)
                {
                	StringBuffer str= new StringBuffer();
                    while((line.charAt(i)!='\"')&&(i<len-1))
                    {
                    	str.append(line.charAt(i));
                        i++;
                    }
                    if(line.charAt(i)=='\"'&&(i<len))
                    {
                    	inStr=false;
                    	tokenlist.add(104, str.toString(),"STRING_LITERAL");
                        i++;
                        continue;
                    }
                }
                
                while(isSpace(line.charAt(i))&&(i<len))
                {
                    first++;
                    i=i+1;
                }
                if(inNote&&(i<len))
                {
                    i++;
                    while((line.charAt(i-1)!='*'||line.charAt(i)!='/')&&(i<len))
                    {
                        i++;
                    }
                    if(i==len)
                    {
                    	continue;
                    }
                    else
                    {
                    i++;
                    inNote=false;
                    continue;
                    }
                }
                if((i<len)&&((isAlpha(line.charAt(i)))||(line.charAt(i)=='_')))
                {
                    first=i;
                    i=i+1;
                    while((i<len)&&((isAlpha(line.charAt(i)))||(line.charAt(i)=='_')||(isNumber(line.charAt(i)))))
                    {
                        i=i+1;
                    }
                    forward=i-1;
                    if(forward-first>31)
                    {
                        System.out.println("Identifier length is too long!Please modify it.\n");
                        continue;
                    }
                    tokens =copy_token(line,first,forward);
                    if(isKeyword(tokens)==-1)
                    {
                    	tokenlist.add(101, tokens,"IDENTIFIER");
                    }
                    else
                    {
                    	tokenlist.add(isKeyword(tokens),"0",tokens.toUpperCase());
                    }
                    
                }
                if((i<len)&&isNumber(line.charAt(i)))
                {
                    first=i;
                    i=i+1;
                    while(isNumber(line.charAt(i)))
                    {
                        i=i+1;
                    }
                    if(line.charAt(i)=='.')
                    {
                        i=i+1;
                        while(isNumber(line.charAt(i)))
                        {
                            i=i+1;
                        }
                        forward=i-1;
                        tokens=copy_token(line,first,forward);
                        tokenlist.add(102,tokens,"CONSTANT");
                    }
                    else
                    {
                        forward=i-1;
                        tokens=copy_token(line,first,forward);
                        tokenlist.add(103,tokens,"CONSTANT");
                    }
                }
                switch(line.charAt(i))
                {
                    case '+':
                    	if(i<len) {
                        i++;}
                    	else
                    		continue;
                        if(line.charAt(i)=='+')
                        {
                        	tokenlist.add(63,"-","++");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else if(line.charAt(i)=='=')
                        {
                        	tokenlist.add(47,"-","+=");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else
                        {
                        	tokenlist.add(41,"-","+");
                        }
                        break;
                    case '-':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='-')
                        {
                        	tokenlist.add(64,"-","--");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else if(line.charAt(i)=='=')
                        {
                        	tokenlist.add(46,"-","-=");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else
                        {
                        	tokenlist.add(42,"-","-");
                        }
                        break;
                    case '*':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(43,"-","*");
                        break;
                    case '\\':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(94,"-","LineComment");
                        break;
                    case '=':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='=')
                        {
                        	tokenlist.add(65,"-","EQ_OP");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else 
                        {
                        	tokenlist.add(45,"-","=");
                        }
                        break;
                    case '%':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(52,"-","%");
                        break;
                    case '!':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='=')
                        {
                        	tokenlist.add(66,"-","NE_OP");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else 
                        {
                        	tokenlist.add(57,"-","!");
                        }
                        break;
                    case '>':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='=')
                        {
                        	tokenlist.add(56,"-","GE_OP");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else if(line.charAt(i)=='>')
                        {
                        	tokenlist.add(50,"-","RIGHT_MOVE");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else
                        {
                        	tokenlist.add(54,"-",">");
                        }
                        break;
                    case '<':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='=')
                        {
                        	tokenlist.add(55,"-","LE_OP");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else if(line.charAt(i)=='<')
                        {
                        	tokenlist.add(51,"-","LEFT_MOVE");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else
                        {
                        	tokenlist.add(53,"-","<");
                        }
                        break;
                    case '\"':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        inStr=true;
                        break;
                    case '\'':
                    	if(line.charAt(i)!='\''&&line.charAt(i+1)=='\'')
                    	{
                    		char c=line.charAt(i);
                    		tokenlist.add(105,String.valueOf(c),"CHAR_LITERAL");
                    		if(i<len-1) {
                                i=i+2;}
                            	else
                            		continue;
                    	}
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(87,"-","\'");
                        break;
                    case ':':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(89,"-",":");
                        break;
                    case '{':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(81,"-","{");
                        break;
                    case '}':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(82,"-","}");
                        break;
                    case '[':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(83,"-","[");
                        break;
                    case ']':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(84,"-","]");
                        break;
                    case ',':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(92,"-",",");
                        break;
                    case ';':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(90,"-",";");
                        break;
                    case '(':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(85,"-","(");
                        break;
                    case ')':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(86,"-",")");
                        break;
                    case '.':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(93,"-",".");
                        break;
                    case '&':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='&')
                        {
                        	tokenlist.add(62,"-","AND_OP");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else
                        {
                        	tokenlist.add(60,"-","AND_SONGLE_OP");
                        }
                        break;
                    case '|':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='|')
                        {
                        	tokenlist.add(61,"-","OR_OP");
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                        }
                        else
                        {
                        	tokenlist.add(59,"-","OR_SINGLE_OP");
                        }
                        break;
                    case '?':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        tokenlist.add(91,"-","?");
                        break;
                    case '/':
                    	if(i<len) {
                            i++;}
                        	else
                        		continue;
                        if(line.charAt(i)=='/')
                        {
                        	i=len;
                        	break;
               
                        }
                        else if(line.charAt(i)=='*')
                        {
                        	if(i<len) {
                                i++;}
                            	else
                            		continue;
                            inNote=true;
                            continue;
                        }
                        else
                        {
                        	tokenlist.add(44,"-","/");
                        }
                        break;
                }
            }
        }
        return tokenlist;
	}
}