package compile;
import java.io.IOException;
import java.util.Scanner;

import grammatical_analysis.Step2;
import lexical_analysis.Step1;
import lexical_analysis.tokenList;



public class Complie {
    public static void main(String[] args) throws Exception {
    	@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
    	System.out.println("Please enter the file path that you want to do the lexical analysis:");
    	String filename = scan.nextLine();
    	Step1 step1= new Step1();
        tokenList tokenlist = new tokenList();
        try {
			tokenlist=step1.getToken(filename);
			if(tokenlist==null)
			{
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        tokenlist.print(tokenlist.getHead());
//    	System.out.println("Please enter the file path of your config-file:");
//        String  filename = scan.nextLine();
        Step2 step2= new Step2();
        step2._analyse(tokenlist);
    }
}
