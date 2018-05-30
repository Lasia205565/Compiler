package grammatical_analysis;

import java.util.ArrayList;

import grammatical_analysis.config.Element;
import grammatical_analysis.config.Elements;
import grammatical_analysis.config.Set;
import grammatical_analysis.config.item;
import grammatical_analysis.config.production;
import grammatical_analysis.config.productions;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateTable {
    static Elements T ; //store terminal element
	static Elements V ;//store non-terminal element
	static productions productions ;//store productions
	static Set  firstSet ; //all the firstSet
	static Elements TV;
	ArrayList<transfer> table = new ArrayList<transfer> ();
	 table Action = new table ();
	 table Goto = new table ();

	public productions getProductions() {
		return productions;
	}

	public static void setProductions(productions productions) {
		CreateTable.productions = productions;
	}

	public table getAction() {
		return Action;
	}

	public void setAction(table action) {
		Action = action;
	}

	public table getGoto() {
		return Goto;
	}

	public void setGoto(table goto1) {
		Goto = goto1;
	}

	public CreateTable ()
	{
		
	}
	_StatusSet set = new _StatusSet ();
	public CreateTable(String filename)
	{
        config config = new config();
        try {
			config.getFirst(filename);
			T = config.getT();
			V = config.getV();
			productions  = config.getProductions();
			firstSet = config.firstSet;
		    TV = new Elements ();
			int l1 = T.size();
			for(int i=0;i<l1;i++)
			{
				TV.add(T.get(i));
			}
			int l2 = V.size();
			for(int i=0;i<l2;i++)
			{
				TV.add(V.get(i));
			}
			G();
			createTable();
			SaveTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Element findByName(String name) //找到T，V中的
	{
		int Tlen = T.size();
		int Vlen = V.size();
		for(int i=0;i<Tlen;i++)
		{
			if(T.get(i).getName().equals(name))
			{
				return T.get(i);
			}
		}
		for(int i=0;i<Vlen;i++)
		{
			if(V.get(i).getName().equals(name))
			{
				return V.get(i);
			}
		}
		return null;
		
	}
	
	public static _proList get_closure( _proList _pros) //主要需要计算不同状态的展望符
	{
		 _proList result = _pros;
		int change=1;
        while(change!=0)
        {
        	change=0;
        	int size = result.size();
        	for(int i=0;i<size;i++)
        	{
        		_production _temp = result.get(i);
        		int dotpos = _temp.getDotPos();
        		int proIndex = _temp.getProIndex();
        		int itemIndex= _temp.getItemIndex();
        		String lookahead=_temp.getLookAhead();
        		item item =productions.get(proIndex).getRight().get(itemIndex); //是给出的列表中的右部
        		int length = item.getElements().size();
        		if(dotpos==length) //点的位置在产生式的最后面
        		{
        			continue;
        		}
        		else
        		{
        			if(item.getElements().get(dotpos).isT())//如果点后面的元素是终结符
        			{
        				continue;
        			}
        			else if (length-dotpos==1)//β是空，则不用再求first集了
        			{
        				Element e = item.getElements().get(dotpos);
        				if(productions.FindByLeft(e.getName())==null)
        				{
        					continue;
        				}
        				else
        				{
        					production c = productions.FindByLeft(e.getName());
        					int cindex= c.getpIndex();
        					int clen = c.getRight().size();
        					for(int m =0;m<clen;m++)
        					{
        						_production p  = new _production(cindex,m,lookahead,0);
        						change+=result.add(p);
        					}
        				}
        			}
        			else   //β不是空，需要求first集啦
        			{
        				Element e1 = item.getElements().get(dotpos);
        				Elements e3 = new Elements ();
        				for(int o=dotpos+1;o<length;o++)
        				{
        					Element e2 = item.getElements().get(dotpos+1);
        					e3.add(e2);
        				}
        				e3.add(findByName(lookahead));
        				config config = new config();
        				Elements firstset=config.getFirstSet(e3,firstSet);
        				int flen = firstset.size();
        				for(int w=0;w<flen;w++)
        				{
        					String e = firstset.get(w).getName();
        					if(productions.FindByLeft(e1.getName())==null)
            				{
            					continue;
            				}
            				else
            				{
            					production c = productions.FindByLeft(e1.getName());
            					int cindex= c.getpIndex();
            					int clen = c.getRight().size();
            					for(int m =0;m<clen;m++)
            					{
            						_production p  = new _production(cindex,m,e,0);
            						_production p1 = new _production(cindex,m,lookahead,0);
            						change+=result.add(p1);
            						change+=result.add(p);
            					}
            				}
        				}
        			}
        		}
        	}
        }
		return result;
	}
	
	public _proList Goto(final _proList I,String X)
	{
		 _proList J = new  _proList ();
		 _proList K = new _proList ();
		 K = I;
		 int len =K.size();
		 int flag = 0;
		 _production temp = null ;
		 for(int i=0;i<len;i++)
		 {
			 temp = new _production(K.get(i).getProIndex(),K.get(i).getItemIndex(),K.get(i).getLookAhead(),K.get(i).getDotPos());
			 item item =productions.get(temp.getProIndex()).getRight().get(temp.getItemIndex());
			 if(temp.getDotPos()==item.getElements().size())
			 {
				 continue;
			 }
			 if(X.equals(item.getElements().get(temp.getDotPos()).getName()))
			 {
				 _production t = temp;
				 t.backStep();
				 flag +=J.add(t);
			 }
		 }
		 if(flag!=0)
		 {
			 return get_closure(J);
		 }
		 else
		 {
			 return null;
		 }
	}
	
	
	public void G()  {
		_proList p1= new _proList ();
		_production p2 = new _production (0,0,"#",0);
		p1.add(p2);
		p1= get_closure(p1);
		int index=0;
	    p1.status=index;
	    set.add(p1);
	    index ++;
	    int length = TV.size();
	    _proList temp =  new _proList();
	    temp = p1;
	    _proList temp1 = new _proList ();
	    int pindex =index;
	    int index1=0;
	    for(int i=0;i<length;i++)
	    {
	    	if((temp1=Goto(p1,TV.get(i).getName()))!=null)
	    	{
	    		if(temp1.in(temp))
	    		{
	    			 transfer t1 = new transfer(0,0,TV.get(i).getName());
			    	 table.add(t1);
	    		}
	    		else if((index1 = set.HaveExist(temp1))!=-1)
	    		{
	    			transfer t1 = new transfer(0,index1,TV.get(i).getName());
			    	 table.add(t1);
	    		}
	    		else 
	    		{
	    			temp1.status=pindex;
		    		set.add(temp1);
		    		pindex++;
		    		transfer t1 = new transfer(0,temp1.status,TV.get(i).getName());
		    	    table.add(t1);
	    		}
	    	}
	    }
	    set.ChangeByStatus(p1.status);
	    while(set.HaveNotReady())
	    {
	    	 index = pindex;
	    	 temp =  set.getNotReady();
	    	 int s = temp.status;
	    	
	    	 for(int i=0;i<length;i++)
	    	 {
	    		 if((temp1=Goto(temp,TV.get(i).getName()))!=null)
		    	{
		    		if(temp1.in(temp))
		    		{
		    			 transfer t1 = new transfer(s,s,TV.get(i).getName());
				    	 table.add(t1);
		    		}
		    		else if((index1 = set.HaveExist(temp1))!=-1)
		    		{
		    			transfer t1 = new transfer(s,index1,TV.get(i).getName());
				    	 table.add(t1);
		    		}
		    		else 
		    		{
		    			temp1.status=pindex;
			    		set.add(temp1);
			    		pindex++;
			    		transfer t1 = new transfer(s,temp1.status,TV.get(i).getName());
			    	    table.add(t1);
		    		}
		    	}
	    	 }
	    	 set.ChangeByStatus(temp.status);
	    }
		
	}
	
	public void createTable() //创建分析表
	{
		int k = set.size();
		int u;
		transfer temp =new transfer (); 
		for(int i=0;i<k;i++)
		{
			_proList p = set.get(i);
			int len = p.size();
			int s =p.status;
			for(int j =0;j<len;j++)
			{
				_production pro = p.get(j);
				int proindex = pro.getProIndex();
				int itemindex = pro.getItemIndex();
				int dotpos = pro.getDotPos();
				String lookahead = pro.getLookAhead();
				ArrayList<Element> elements = productions.get(proindex).getRight().get(itemindex).getElements();
				int itemlen = elements.size();
				if (proindex==0&&itemindex==0&&dotpos==1&&lookahead=="#")
				{
					analyse a = new analyse (s); //action.acc
					Action.add(a);
				}
				else if(dotpos==itemlen)
				{
					analyse a = new  analyse(s ,proindex,itemindex,lookahead);
					Action.add(a);
				}
				else
				{
				Element element = elements.get(dotpos);
				String name =element.getName();
				u=temp.find(table, s, name);
				 if(element.isT()&&(u!=-1))
				{
					analyse analyse = new analyse(s,u,name,"S");
					Action.add(analyse);
				}
			}
			}
			int Glen  = table.size();
			for(int m=0;m<Glen;m++)
			{
				transfer t2 = table.get(m);
				if(t2.getSou()==i&&t2.isV(V, t2.getE()))
				{
					u=temp.find(table, s, t2.getE());
					analyse analyse = new  analyse (s,u,t2.getE());
					Goto.add(analyse);
				}
			}
			
		}
		
	}
	public void SaveTable()
	{
		try  
	    {     
	     String savepath="File/table.txt";
	      File file = new File(savepath);  
	      if(!file.exists())
	      {
	    	  file.createNewFile();
	      }
	      else
	      {
	    	  file.delete();
	    	  file.createNewFile();
	      }
	      FileWriter fileWriter = new FileWriter(file);   
	      fileWriter.write("Table Action:\n");  
	      int size = Action.size();
	      for(int i=0;i<size;i++)
	      {
	    	  fileWriter.write(Action.get(i).toString());
	      }
	      fileWriter.write("Table Goto:\n");  
	      size = Goto.size();
	      for(int i=0;i<size;i++)
	      {
	    	  fileWriter.write(Goto.get(i).toString());
	      }
	      fileWriter.close();  
	    }  
	    catch (IOException e)  
	    {  
	      e.printStackTrace();  
	    }  
	}
}
