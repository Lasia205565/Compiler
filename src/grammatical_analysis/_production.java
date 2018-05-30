package grammatical_analysis;

import java.util.ArrayList;

import grammatical_analysis.config.item;
import grammatical_analysis.config.productions;


public class _production {
	config config = new config ();
	private String lookAhead;
	private int dotPos;//点的位置，如果是0则表示在最前面
	private int proIndex;
	private int ItemIndex;

	public boolean equal (_production p2)
	{
		if(dotPos==p2.dotPos&&ItemIndex==p2.ItemIndex&&proIndex==p2.proIndex&&(lookAhead.equals(p2.lookAhead)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public _production(int proIndex,int ItemIndex,String lookAhead,int dotPos)
	{
		this.lookAhead = lookAhead;
		this.dotPos = dotPos;
		this.proIndex = proIndex;
		this.ItemIndex = ItemIndex;
	}
	public String getLookAhead() {
		return lookAhead;
	}
	public void setLookAhead(String lookAhead) {
		this.lookAhead = lookAhead;
	}
	public int getDotPos() {
		return dotPos;
	}
	public void setDotPos(int dotPos) {
		this.dotPos = dotPos;
	}
	public int getProIndex() {
		return proIndex;
	}
	public void setProIndex(int proIndex) {
		this.proIndex = proIndex;
	}
	public int getItemIndex() {
		return ItemIndex;
	}
	public void setItemIndex(int itemIndex) {
		ItemIndex = itemIndex;
	}
	public void backStep()
	{
	   dotPos++;
	}
	public void print()
	{
		System.out.println(getProIndex()+":::"+getItemIndex()+":::"+getDotPos()+":::"+getLookAhead());
	}
	
}


class _proList  //用于存储一个一个状态集
{
	ArrayList<_production> pros ;
	int status;
	boolean Ready;
	public _proList()
	{
		pros = new ArrayList<_production> ();
		Ready = false;
	}

	public int add(_production p)
	{
		if(pros==null)
		{
			pros.add(p);
		}
		else
		{
			int len=pros.size();
			for(int i=0;i<len;i++)
			{
				if(pros.get(i).equal(p))
				{
					return 0;
				}
			}
			pros.add(p);
		}
		return 1;
	}
	public boolean equal(_proList l)
	{
		int len1 = size();
		int len2 = l.size();
		int flag = 0;
		if(len1!=len2)
		{
			return false;
		}
	    for(int i =0;i<len1;i++)
	    {
	    	for(int j =0;j<len2;j++)  		
	    	{
	    		if(get(i).equal(l.get(j)))
	    		{
	    			flag  = flag + 1;
	    		}
	    	}
	    }
	    if(flag==len1)
	    {
	    	return true;
	    }
	    return false;
	}
	public boolean in(_proList l)  //判断一个状态集是否包含于另外一个状态集
	{
		int len1 = size();
		int len2 = l.size();
		int flag = 0;
	    for(int i =0;i<len1;i++)
	    {
	    	for(int j =0;j<len2;j++)  		
	    	{
	    		if(get(i).equal(l.get(j)))
	    		{
	    			flag  = flag + 1;
	    		}
	    	}
	    }
	    if(flag==len1)
	    {
	    	return true;
	    }
	    return false;
	}
	public int size()
	{
		return pros.size();
	}
	public _production get(int n)
	{
		return pros.get(n);
	}
	public void print(productions p)
	{
		int size = pros.size();
		for(int i=0;i<size;i++)
		{
			item items = p.get(pros.get(i).getProIndex()).getRight().get(pros.get(i).getItemIndex());
			String left = p.get(pros.get(i).getProIndex()).getLeft();
			System.out.print(left+" --->");
			items.print_item();
			System.out.println("dotpos:"+pros.get(i).getDotPos()+"    lookahead:"+pros.get(i).getLookAhead());
		}
	}
}

