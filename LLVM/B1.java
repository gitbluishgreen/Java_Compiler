import syntaxtree.*;
import visitor.*;
import java.util.*;
public class B1
{
	public static void main(String[] arg)
	{
		try
		{
			Node root = new MiniJavaParser(System.in).Goal();
			Integer x = new Integer(0);
			myDFS obj = new myDFS<String,Integer>();
       		root.accept(obj,x); // Your assignment part is invoked here.
            //System.out.println("First pass successful!\n");
			x = new Integer(1);
			root.accept(obj,x);
			//System.out.println("Program type checked successfully");
      	}
      	catch (ParseException e) {
         	System.out.println(e.toString());
      }
	}
}