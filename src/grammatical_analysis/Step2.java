package grammatical_analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


import grammatical_analysis.config.productions;
import lexical_analysis.tokenList;
import lexical_analysis.tokenList.token;

import grammatical_analysis.config.production;


public class Step2 {
	 table Action = new table ();
	 table Goto = new table ();
	 productions productions;
	public void GetTable()
	{
		String filename="File/table.txt";
		File file = new File (filename);
		int choice =1;
		@SuppressWarnings("resource")
		Scanner scan = new Scanner (System.in);
		if(file.exists())
		{
			System.out.println("有已生成分析表文件，是否直接使用该文件进行语法分析（输入0重新设置文法,输入任意健将按照以存在的文件进行分析）：");
			choice = scan.nextInt();
		}
		if(!file.exists()||choice==0)
		{
			if(choice==0)
			{
				System.out.println("请输入配置文件路径，生成文件分析表文件（请按照固定格式设置该文件）：");
			}
			else
			{
			System.out.println("暂无已生成的分析表文件，请输入文件路径，生成文件分析表文件：");
			}
			String configfile=scan.next();
			@SuppressWarnings("unused")
			CreateTable c = new CreateTable(configfile);
			Action  = c.getAction();
			Goto = c.getGoto();
			productions = c.getProductions();
		}
		else {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line=null;
			analyse a = new analyse();
			     line = br.readLine();
				if(line.startsWith("Table Action"))
				{
				   line = br.readLine();
				   Action.add(a.CreateAnalyse(line));
				}
				line = br.readLine();
				while(!line.startsWith("Table Goto"))
				{
					Action.add(a.CreateAnalyse(line));
					 line = br.readLine();
				}
				line = br.readLine();
				while(line!=null)
				{
					Goto.add(a.CreateAnalyse(line));
					line = br.readLine();
				}
				config config = new config();
				config.getConfig("G:/config-test.txt");
				productions = config.getProductions();
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	public void _analyse(tokenList tokenlist)
	{
		GetTable();
		Stack <String> inputbuffer = new Stack <String> ();
		Stack <Integer> status = new Stack<Integer> ();
		Stack <String> analyse = new Stack<String> ();
		int flag= 0;
		ArrayList<token> list = tokenlist.reverse();
		int len1 = list.size();
		inputbuffer.push("#");
		for(int i=0;i<len1;i++)
		{
			inputbuffer.push(list.get(i).getType());
		}
		status.push(0);
		analyse.push("#");
		int s = status.peek();
		String a=inputbuffer.pop();
		while(analyse.size()>0)
		{
			analyse an = Action.find(s, a);
			if(an==null)
			{
				System.out.println("ERROR:There is not transfer from"+s+"by"+a);
				flag++;
				if(flag>2)
				{
					return;
				}
			}
			else if(an.getMethod().equals("S"))
			{
				status.push(an.getJ());
				s= an.getJ();
				a= inputbuffer.pop();
				analyse.push(an.getA());
				System.out.println("MOVE IN..."+an.getK()+"->"+an.getMethod()+an.getJ()+an.getJ2()+"("+an.getA()+")");
			}
			
			
			else if(an.getMethod().equals("R"))
			{
				int j = an.getJ();
				int j2 = an.getJ2();
				production p = productions.get(j);
				int size = p.getRight().get(j2).getElements().size();
				for(int m=0;m<size;m++)
				{
					analyse.pop();
					status.pop();
				}
				System.out.print(analyse);
				String left = p.getLeft();
				analyse.push(left);
				s=status.peek();
				analyse an1 = Goto.find(s, left); 
				status.push(an1.getJ());
				s= an1.getJ();
				System.out.println("Reduce..."+an1.getK()+"->"+an1.getMethod()+an1.getJ()+an1.getJ2()+"("+an1.getA()+")");
			}
			else if(an.getMethod().equals("acc"))
			{
				System.out.println("Accepted...");
				return;
			}
		}
	}
	
	}

    
	

