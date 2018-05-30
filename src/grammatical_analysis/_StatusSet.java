package grammatical_analysis;

import java.util.ArrayList;

import grammatical_analysis.config.Elements;
import grammatical_analysis.config.productions;

public class _StatusSet {  //一个状态集 包含多个产生式
	ArrayList<_proList> pros;
	public _StatusSet()
	{
		pros = new ArrayList<_proList> ();
	}
	public _proList get (int n)
	{
		return pros.get(n);
	}
	public int size()
	{
		return pros.size();
	}
	public boolean HaveNotReady()
	{
		int len = pros.size();
		for(int i=0;i<len;i++)
		{
			if(!pros.get(i).Ready)
			{
				return true;
			}
		}
		return false;
	}
	public _proList getNotReady()
	{
		int len = pros.size();
		for(int i=0;i<len;i++)
		{
			if(!pros.get(i).Ready)
			{
				return pros.get(i);
			}
		}
		return null;
	}
	public void ChangeByStatus(int status )
	{
		int len = pros.size();
		for(int i=0;i<len;i++)
		{
			if(pros.get(i).status==status)
			{
				pros.get(i).Ready=true;
			}
		}
	}
	public void print(productions p)
	{
		int len = pros.size();
		for(int i=0;i<len;i++)
		{
			System.out.println("状态集"+i+"：");
			pros.get(i).print(p);
		}
	}
	
	public void add(_proList p )
	{

	    pros.add(p);
	}
	public int HaveExist(_proList p)
	{
		int len = pros.size();
		for(int i=0;i<len;i++)
		{
			if(p.equal(pros.get(i)))
			{
				return i;
			}
		}
		return -1;
	}
}


class table
{
	ArrayList<analyse> t ;
	public table ()
	{
		t= new ArrayList<analyse>  ();
	}
	public int size()
	{
		return t.size();
	}
	public analyse get (int n)
	{
		return t.get(n);
	}
	public void add(analyse a)
	{
		int len = t.size();
		for(int i=0;i<len;i++)
		{
			if(t.get(i).equal(a))
			{
				return;
			}
		}
		t.add(a);
	}
	public analyse find(int s,String a)
	{
		int len = t.size();
		for(int i =0;i<len;i++)
		{
			if(t.get(i).getK()==s&&t.get(i).getA().equals(a))
			{
				return t.get(i);
			}
		}
		return null;
	}
	
}


class analyse
{
	private int k;
	private int j;
	private String a;
	private String method;
	private int j2;
	
	public int getJ2() {
		return j2;
	}
	public void setJ2(int j2) {
		this.j2 = j2;
	}
	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public analyse()
	{
		
	}
	public analyse CreateAnalyse(String line)
	{
		   String [] info = line.split(" ");
		   analyse a =new  analyse(Integer.parseInt(info[0]) ,Integer.parseInt(info[1]) , info[2] ,info[3],Integer.parseInt(info[4]));
		   return a ;
	}
	public analyse(int k ,int j , String a ,String method,int j2)
	{
		this.k=k;
		this.j=j;
		this.a =a;
		this.method= method;
		this.j2=j2;
	}
	public analyse(int k ,int j,String a ,String method)  //action,S
	{
		this.k=k;
		this.j=j;
		this.a =a;
		this.method= method;
		this.j2=-1;
	}
	public analyse (int k,int j,String a ) //goto
	{
		this.k=k;
		this.j=j;
		this.a=a;
		this.j2=-1;
		this.method="G";
	}
	public analyse(int k,int j ,int j2,String a ) //action.R
	{
		this.k=k;
		this.j=j;
		this.a =a ;
		this.method = "R";
		this.j2 = j2;
	}
	public String toString()
	{
		return(k+" "+j+" "+a+" "+method+" "+j2+"\n");
	}
	public analyse(int k ) //action.acc
	{
		this.k=k;
		this.j=-1;
		this.a ="#" ;
		this.method = "acc";
		this.j2 = -1;
	}
	public boolean equal(analyse analyse)
	{
		if(k==analyse.k&&j==analyse.j&&j2==analyse.j2&&a.equals(analyse.a)&&method.equals(analyse.method))
		{
			return true;
		}
		return false;
	}
}


class transfer
{
	private int sou;
	private int des;
	private String  e;
	public int getSou() {
		return sou;
	}
	public void setSou(int sou) {
		this.sou = sou;
	}
	public int getDes() {
		return des;
	}
	public void setDes(int des) {
		this.des = des;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	public transfer(int sou,int des,String e)
	{
		this.sou = sou;
		this.des= des;
		this.e = e;
	}
	public transfer ()
	{
		
	}
	public int find (ArrayList<transfer> table,int k,String e)
	{
		 int len = table.size();
		 for(int i=0;i<len;i++)
		 {
			 transfer  t = table.get(i);
			 if(t.sou==k&&t.e.equals(e))
			 {
				 return t.des;
			 }
		 }
		 return -1;
	}
	
	public boolean isV(Elements V,String name)
	{
		int len = V.getLength();
		for(int i=0;i<len;i++)
		{
			if(V.get(i).getName().equals(name))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void print()
	{
		System.out.println(sou+"->"+des+"("+e+")");
	}
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         