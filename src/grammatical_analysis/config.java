package grammatical_analysis;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;



public class config {
public Elements T = new Elements (); //store terminal element
public Elements V = new Elements ();//store non-terminal element
public  productions productions = new productions ();//store productions
Set  firstSet = new Set (); //all the firstSet

public Elements getT() {
	return T;
}
public void setT(Elements t) {
	T = t;
}
public Elements getV() {
	return V;
}
public void setV(Elements v) {
	V = v;
}
public productions getProductions() {
	return productions;
}
public void setProductions(productions productions) {
	this.productions = productions;
}
/*D->DS|A|a is production
 * DS,A,a is item
 * D S A a is element
 * */	
public boolean isSpace(char c)
{
	if(c==' '||c=='\t'||c=='\n'||c=='\r')
	{
		return true;
	}
	return false;
}

void getConfig(String filename) throws Exception
{
	File file = new File(filename);
	@SuppressWarnings("resource")
	BufferedReader br = new BufferedReader(new FileReader(file));
	String line=null;
	int Tnum=0;
	String Tname=null;
	if(((line = br.readLine()))!=null)
	{
		int index=0;
		int len=line.length();
		StringBuffer str = new StringBuffer();
		Element e = new Element(-1,"EMPTY",true);
		T.add(e);
		while(index<len&&(line.charAt(index)!='%'))
		{
			index++;
		}
		if(index<len&&line.charAt(index)=='%')
		{
			index++;
			while(index<len)
			{
				while(isSpace(line.charAt(index))&&(index<len))
				{
					index++;
				}
				while((!isSpace(line.charAt(index)))&&(index<(len-1)))
				{
					str.append(line.charAt(index));	
					index++;
					Tname=str.toString();
				}
				if(isSpace(line.charAt(index))&&(index<(len-1)))
				{
					if(T.InElementList(Tname)==null)
					{
						Element t= new Element(Tnum,Tname,true);
						T.add(t);
					//	System.out.println("T:"+t.name);
						str = new StringBuffer();
					}
					Tname=null;
					Tnum++;
				}
				else if(index==(len-1)&&(!isSpace(line.charAt(index))))
				{
					str.append(line.charAt(index));
					
					if(T.InElementList(str.toString())==null)
					{
						Element t= new Element(Tnum,str.toString(),true);
						T.add(t);
						//System.out.println("T:"+t.name);
						str = new StringBuffer();
					}
					Tname=null;
					index++;
				}
			}
			
		}
	}
	if(((line = br.readLine()))!=null)
	{
		int index=0;
		int len=line.length();
		StringBuffer str = new StringBuffer();
		while(index<len&&(line.charAt(index)!='%'))
		{
			index++;
		}
		if(line.charAt(index)=='%')
		{
			index++;
			while(index<len)
			{
				while((index<len)&&isSpace(line.charAt(index)))
				{
					index++;
				}
				while((index<(len-1))&&(!isSpace(line.charAt(index))))
				{
					str.append(line.charAt(index));	
					index++;
					Tname=str.toString();
				}
				if((index<(len-1))&&isSpace(line.charAt(index)))
				{
					if(V.InElementList(Tname)==null)
					{
						Element t= new Element(Tnum,Tname,false);
						V.add(t);
						//System.out.println("V:"+t.name);
						str = new StringBuffer();
					}
					Tname=null;
					Tnum++;
				}
				else if(index==(len-1)&&(!isSpace(line.charAt(index))))
				{
					str.append(line.charAt(index));
					
					if(V.InElementList(str.toString())==null)
					{
						Element t= new Element(Tnum,str.toString(),false);
						V.add(t);
						str = new StringBuffer();
					}
					Tname=null;
					index++;
				}
			}
			
		}
	}
//	for(int i=0;i<V.size();i++)
//	{
//		System.out.println(V.get(i).name);
//	}
//	for(int i=0;i<T.size();i++)
//	{
//		System.out.println(T.get(i).name);
//	}
//	
//	
	
	int PIndex=0;
	while(((line = br.readLine()))!=null)
	{
		int index=0;
		int len=line.length();
		String left=null;
		StringBuffer rightbuffer = new StringBuffer();
		ArrayList<Element> list=new ArrayList<Element> ();
		ArrayList<item> right=new ArrayList<item> ();
		StringBuffer leftbuffer = new StringBuffer ();
		while(index<len&&line.charAt(index)!=':')
		{
			if(!isSpace(line.charAt(index)))
			{
			    leftbuffer.append(line.charAt(index));
			}
			index++;
		}
		left=leftbuffer.toString();
		
		if(V.InElementList(left)==null)
		{
			System.out.println("The config-file has error:Having non-terminal element not declared in the second line...");
			return;
		}
		index++;
		while(isSpace(line.charAt(index)))
		{
			index++;
		}
		while(index<=len)
		{
			if(index==len)
			{
				item items= new item(list);
				right.add(items);	
				index++;
				list=new ArrayList<Element> ();
				continue;
				
			}
			while((index<len)&&isSpace(line.charAt(index)))
			{
				index++;
			}
				if(index<len&&line.charAt(index)=='|')
				{
					index++;
					item items= new item(list);
					right.add(items);
					list=new ArrayList<Element> ();
				}
				if(index<len)
				{
				rightbuffer.append(line.charAt(index));
				}
				if(V.InElementList(rightbuffer.toString())!=null)
				{
					list.add(V.InElementList(rightbuffer.toString()));
					//InElementList(V,rightbuffer.toString()).printElement();
					rightbuffer = new StringBuffer();
				}
				else if(T.InElementList(rightbuffer.toString())!=null)
				{
					list.add(T.InElementList(rightbuffer.toString()));
					//InElementList(T,rightbuffer.toString()).printElement();
					rightbuffer = new StringBuffer();
				}
				index++;
		}
		if(!productions.HasExisted(left))
		{
		production production = new production (PIndex,left,right);
		productions.add(production);
		PIndex++;
		right=new ArrayList<item> ();	
		}
		else
		{
			productions.addRightItems(left, right);
			right=new ArrayList<item> ();	
		}
	}
	/*for(int m=0;m<productions.GetSize();m++)
	{
		productions.get(m).print_production();
	}*/
}

void getFirst(String filename) throws Exception
{
	Set firstRelation = new Set (); /*all is V ,when the element's firstset changes,the list's firstset all have to change*/
	getConfig(filename);
		for(int j=0;j<productions.GetSize();j++)
		{
		//	System.out.println("ORDER"+j+":");
			//productions.get(j).print_production();
			
			if(productions.get(j).hasEMPTY())
			{
				ArrayList<Element> firsts = new ArrayList<Element> ();
				Element e = new Element(-1,"EMPTY",true);
				firsts.add(e);
				relation first = new relation (productions.get(j).getLeftElement(),firsts,false); 
				firstSet.getSet().add(first);
			}
		}
		for(int j=0;j<T.size();j++)
		{
			ArrayList<Element> firsts = new ArrayList<Element> ();
			firsts.add(T.get(j));
			relation first = new relation (T.get(j),firsts,false); 
			firstSet.getSet().add(first);
		}
	    for(int i=0;i<productions.GetSize();i++)
	    {
	    	productions.get(i).print_production();
	    	Element left = productions.get(i).getLeftElement();
	    	ArrayList<item> right = productions.get(i).getRight();
	    	for(int j=0;j<right.size();j++)
	    	{
	    	    ArrayList<Element> elements=right.get(j).getElements();
	    	    int size=elements.size();
	    	    int num=0;
	    	    while(num<size&&elements.get(num).getIndex()==-1)
	    	    {
	    	    	num++;
	    	    }
	    	    while(num<size&&firstSet.findEMPTY(elements.get(num)))
	    	    {
	    	    	firstSet.copyFirst(left, elements.get(num));
	    	    	firstRelation.addFirstRelation(elements.get(num), left);
	    	    	num++;
	    	    }
	    	    if(num<size)
	    	    {
	    	    	if(!firstSet.findEMPTY(elements.get(num))&&elements.get(num).isT()==false)
	    	    	{
	    	    		firstRelation.addFirstRelation(elements.get(num), left);
	    	    	//	firstRelation.printFirstSet();
	    	    	}
	    	    	firstSet.copyFirst(left, elements.get(num));
	    	    }
	    	    
	    	}
	    	if(firstRelation.findRelation(left)!=null)
	    	{
	    		ArrayList<Element> elements =  firstRelation.findRelation(left).getRelations();
	    		for(int m=0;m<elements.size();m++)
	    		{
	    			firstSet.copyFirst(elements.get(m), left);
	    		}
	    	}
	    }
	   int  change = 1; /*HAVE not change*/
	   while(change!=0)
	   {
		   change=0;
	    for(int j=0;j<firstRelation.set.size();j++)
	    {
	    	ArrayList<Element> elements =  firstRelation.findRelation(firstRelation.set.get(j).getE()).getRelations();
    		for(int m=0;m<elements.size();m++)
    		{
    			change+=firstSet.copyFirst(elements.get(m),firstRelation.set.get(j).getE());
    			//firstSet.printFirstSet();
    		}
	    }
	/*    if(change==0)
	    {
			firstSet.printFirstSet();
			firstRelation.printFirstSet();
	    }*/
	   }
}

Elements getFirstSet(Elements elements,Set firstSet)
{
       Elements result = new Elements ();
       int len = elements.size();
       int index=0;
	   while((index<len)&&firstSet.hasEMPTY(elements.getElement(index).getName()))
	   {
		  result.CopyElements(firstSet.findRelation(elements.getElement(index)).relations);
		  index++;
	   }
	   if(index<len)
	   {
		   result.CopyElements(firstSet.findRelation(elements.getElement(index)).relations);
	   }
	   return result;
}

class production
{
	private String left;
	private int pIndex; //the index of production
	private ArrayList<item> right; //the right of the production
	public production(int index,String Left,ArrayList<item> items)
	{
		left=Left;
		pIndex=index;
		right=items;
	}
	public int getpIndex() {
		return pIndex;
	}
	public void setpIndex(int pIndex) {
		this.pIndex = pIndex;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public ArrayList<item> getRight() {
		return right;
	}
	public void setRight(ArrayList<item> right) {
		this.right = right;
	}
	public void print_production()
	{
		for(int i=0;i<right.size();i++)
		{
			System.out.print(left+"--->");
			right.get(i).print_item();
		}
	}
	public boolean hasEMPTY()
	{
		for(int i=0;i<right.size();i++)
		{
			if(right.get(i).getElements().size()==1&&(right.get(i).getElements().get(0).getIndex()==-1))
			{
				return true;
			}
		}
		return false;
	}
	public Element getLeftElement()
	{
		for(int i=0;i<V.size();i++)
		{
			if(V.get(i).getName().equals(left))
			{
				Element e = new Element(V.get(i).getIndex(),left,false);
				return e;
			}
		}
		return null;
	}
	
}

public class item
{
	private ArrayList<Element> elements;

	public ArrayList<Element> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
	}
	public item(ArrayList<Element> ele)
	{
		elements=ele;
	}
	public void print_item()
	{

		for(int i=0;i<elements.size();i++)
		{
			System.out.print(elements.get(i).name+" ");
		}
		System.out.print("\n");
	}
}
class Element // T 
{
	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	private int index;/* the index of the terminal */
	private String name;
	private boolean isT;
	public Element( int tindex, String tname,boolean ist) 
	{
		name = tname;
		index = tindex;
		setT(ist);
	}


	public void printElement()
	{
				System.out.print("element-"+"index:"+index+"name:"+name+"  "+isT+"\n");
	}


	public boolean isT() {
		return isT;
	}


	public void setT(boolean isT) {
		this.isT = isT;
	}

}
class relation
{
	private Element e;
	private ArrayList<Element> relations;
	boolean isNull;
	public ArrayList<Element> getRelations() {
		return relations;
	}
	public void setRelations(ArrayList<Element> relations) {
		this.relations = relations;
	}
	public Element getE() {
		return e;
	}
	public void setE(Element e) {
		this.e = e;
	}
	public ArrayList<Element> getFirst() {
		return relations;
	}
	public void setFirst(ArrayList<Element> first) {
		this.relations = first;
	}
	public relation (Element element,ArrayList<Element> first,boolean isnull)
	{
		e=element;
		relations=first;
		isNull=isnull;
	}
	public void addFirst(Element e2)
	{
		relations.add(e2);
	}
	public boolean exitsEMPTY()
	{
		for(int i=0;i<relations.size();i++)
		{
			if(relations.get(i).index==-1)
			{
				return true;
			}
		}
		return false;
	}
	public void print_Relation ()
	{
		for(int i=0;i<relations.size();i++)
			
		{
			System.out.println(relations.get(i).getName());
		}
	}

}

class Set
{
	
	private ArrayList<relation> set=new ArrayList<relation> ();

	public ArrayList<relation> getSet() {
		return set;
	}

	public void setSet(ArrayList<relation> set) {
		this.set = set;
	}
	public boolean hasEMPTY(String name)
	{
		for(int i=0;i<set.size();i++)
		{
			if(set.get(i).e.getName().equals(name))
			{
				if(set.get(i).exitsEMPTY())
				{
					return true;
				}
			}
		}
		return false;
	}
	public relation findRelation(Element e)
	{
		//System.out.println("SIZE:"+set.size());
		for(int i=0;i<set.size();i++)
		{
			if(set.get(i).e.index==e.index&set.get(i).e.isT==e.isT)
			{
				relation relation= new relation(e,set.get(i).relations,false);
				return relation;
			}
		}
		return null;
	}
	public boolean findEMPTY(Element e)
	{
		if(findRelation(e)==null)
		{
			return false;
		}
		else
		{
			relation relation = findRelation(e);
			if(relation.exitsEMPTY())
			{
				return true;
			}
		}
		return false;
	}
	
	public void addFirstRelation(Element e1,Element e2)  //e1 changes then e2 changes
	{
		if(e1.isT==true||e2.isT==true)
		{
			return ;
		}
		if(findRelation(e1)==null)
		{
			ArrayList<Element> elements = new ArrayList<Element> ();
			elements.add(e2);
			relation relation = new relation(e1,elements,false);
			set.add(relation);
		}
		else
		{
			findRelation(e1).getRelations().add(e2);
		}
	}
	public int copyFirst (Element e1,Element e2) /*add f2's firstSet to f1's firstset*/
	{
		int flag=0;
		int result=0;
		if(findRelation(e2)==null)
		{
			result =0;
		}
		else if(findRelation(e1)==null)
		{
			ArrayList<Element> list = new ArrayList<Element> ();
			for(int i=0;i<findRelation(e2).getRelations().size();i++)
			{
				list.add(findRelation(e2).getRelations().get(i));
			}
			relation relation1 = new relation(e1,list,false);
			set.add(relation1);
			result =1;
		}
		else
		   
		{
			for(int i=0;i<findRelation(e2).relations.size();i++)
			{
				flag=0;
			for(int j=0;j<findRelation(e1).relations.size();j++)
			{
				if(findRelation(e2).relations.get(i).getIndex()==findRelation(e1).relations.get(j).getIndex()||findRelation(e2).relations.get(i).getIndex()==-1)
				{
					flag=1;
					continue;
				}
			}
			if(flag==0)
			{
				findRelation(e1).addFirst(findRelation(e2).relations.get(i));
				result =1;
			}
			
		}}
		return result;
	}
	public void printFirstSet()
	{
		for(int i=0;i<set.size();i++)
		{
			int num = set.get(i).getRelations().size();
			for(int j=0;j<num;j++)
			{
				System.out.println(j+":"+set.get(i).getRelations().get(j).getName());
			}
		}
	}
}

static class Elements
{
	private ArrayList<Element> elements;
	public Elements()
	{
		this.elements =  new ArrayList<Element> ();
	}
	public void addElement(Element e)
	{
		elements.add(e);
	}
	public void print_Elements()
	{
		System.out.println("The List of Elements:");
		
		for(int i=0;i<elements.size();i++)
		{
			System.out.println("Element "+i+elements.get(i).getName());
		}
	}
	public void add(Element e)
	{
		elements.add(e);
	}
	public int getLength()
	{
		return elements.size();
	}
	public Element getElement (int n)
	{
		return elements.get(n);
	}
	public void CopyElements(ArrayList<Element> relations)
	{
		int length = relations.size();
		for(int i=0;i<length;i++)
		{
			if(relations.get(i).index!=-1)
			{
			elements.add(relations.get(i));
			}
		}
	}
	public Element get(int n )
	{
		return elements.get(n);
	}
	public Element findByName(String name)
	{
		int len = elements.size();
		for(int i=0;i<len;i++)
		{
			if(name.equals(elements.get(i).name))
			{
				return elements.get(i);
			}
		}
		return null;
	}
	public Element InElementList(String word)
	{
		for(int i=0;i<elements.size();i++)
		{
			if(word.equals(elements.get(i).getName()))
			{
				return elements.get(i);
			}
		}
		return null;
	}
	public int size()
	{
		return elements.size();
	}
}

static class productions
{
	static ArrayList<production> productions = new ArrayList<production> ();
	public boolean HasExisted(String left)
	{
		if(productions==null)
		{
			return false;
		}
		int len = productions.size();
		for(int i=0;i<len;i++)
		{
			if(productions.get(i).left.equals(left))
			{
				return true;
			}
		}
		return false;
	}
	public void add(production p)
	{
		productions.add(p);
	}
	public  int  GetSize()
	{
		return productions.size();
	}
	public production get(int m)
	{
		return productions.get(m);
	}
	public void addRightItems(String left,ArrayList<item> items)
	{
		
		int length = productions.size();
		for(int i=0;i<length;i++)
		{
			if(productions.get(i).left.equals(left))
			{
				int len = items.size();
				for(int j=0;j<len;j++)
				{
					productions.get(i).right.add(items.get(j));
				}
				return;
			}
		}
	}
	public production FindByLeft(String left)
	{
		int len = productions.size();
		for(int i=0;i<len;i++)
		{
			if(productions.get(i).getLeft().equals(left))
			{
				return productions.get(i);
			}
		}
		return null;
	}
}


}


