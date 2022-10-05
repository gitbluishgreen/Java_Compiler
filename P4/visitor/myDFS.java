//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;
import java.io.PrintWriter;
/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class myDFS<R,A> implements GJVisitor<R,A> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
    PrintWriter pw = new PrintWriter(System.out,true);
   int current_register = -1;
   ArrayList<String> arguments = new ArrayList<String>();
   int pl = 0;
   public R visit(NodeList n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n, A argu) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this,argu);
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n, A argu) {
      if ( n.present() )
         return n.node.accept(this,argu);
      else
         return null;
   }

   public R visit(NodeSequence n, A argu) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this,argu);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n, A argu) { return (R)n.tokenImage; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
      }
      else
      {
        current_register++;//first available reg
        n.f0.accept(this, argu);
        pw.println("MAIN");
        n.f1.accept(this,(A)new Integer(2));
        n.f2.accept(this,argu);
        pw.println("END");
        n.f3.accept(this,argu);
        n.f4.accept(this,argu);
      }
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) != 0)
        n.f0.accept(this, (A)new Integer(2));//always print labels
      else
        n.f0.accept(this,argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
   public R visit(Procedure n, A argu) 
   {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        String x = (String)n.f2.accept(this, argu);
        int y = Integer.parseInt(x);
        current_register = Math.max(current_register,y-1);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
      }
      else
      {
        n.f0.accept(this,(A)new Integer(2));
        n.f1.accept(this,argu);//to be printed out
        String y = (String)n.f2.accept(this,argu);
        n.f3.accept(this,argu);
        pw.printf("[%s]\n",y);
        n.f4.accept(this,(A)new Integer(2));
      }
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
   public R visit(Stmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      if(Integer.valueOf((Integer)argu) != 0)
        pw.println("NOOP");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      if(Integer.valueOf((Integer)argu) != 0)
        pw.println("ERROR");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Exp()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
      }
      else
      {
        Integer t = new Integer(1);
        n.f0.accept(this, argu);
        int rg = current_register++;
        String x = (String)n.f1.accept(this, (A)t);
        pw.printf("MOVE TEMP %d %s\n",rg,x);
        pw.printf("CJUMP TEMP %d ",rg);
        String y = (String)n.f2.accept(this, (A)t);//it is 1 now
        pw.println(y);
        //current_register--;
      }
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
      }
      else
      {
        n.f0.accept(this, argu);
        String x = (String)n.f1.accept(this, (A)new Integer(1));
        pw.printf("JUMP %s\n",x);
      }
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Exp()
    * f2 -> IntegerLiteral()
    * f3 -> Exp()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
      }
      else
      {
        n.f0.accept(this, argu);
        Integer t = new Integer(1);
        int rg1 = current_register++;
        int rg2 = current_register++;
        String a = (String)n.f1.accept(this, (A)t);
        String v = (String)n.f2.accept(this, (A)t);
        String b = (String)n.f3.accept(this, (A)t);
        pw.printf("MOVE TEMP %d %s\nMOVE TEMP %d %s\n",rg1,a,rg2,b);
        pw.printf("HSTORE TEMP %d %s TEMP %d\n",rg1,v,rg2);
        //current_register -= 2;//these are now free
      }
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Exp()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        String x = (String)n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
      }
      else
      {
        n.f0.accept(this,argu);
        Integer t = new Integer(1);
        String a = (String)n.f1.accept(this,(A)t);
        int rg = current_register++;
        String v = (String)n.f2.accept(this,(A)t);
        String b = (String)n.f3.accept(this,(A)t);
        pw.printf("MOVE TEMP %d %s\n",rg,v); 
        pw.printf("HLOAD %s TEMP %d %s\n",a,rg,b);
        //current_register--;
      }
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        String x1 = (String)n.f1.accept(this, argu);
        n.f2.accept(this, argu);
      }
      else
      {
        n.f0.accept(this,argu);
        Integer t = new Integer(1);
        String a = (String)n.f1.accept(this,(A)t);
        String b = (String)n.f2.accept(this,(A)t);
        pw.printf("MOVE %s %s\n",a,b);
      }
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> Exp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      if(Integer.valueOf((Integer)argu) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
      }
      else
      {
        n.f0.accept(this,argu);
        String a = (String)n.f1.accept(this,(A)new Integer(1));
        pw.printf("PRINT %s\n",a);
      }
      return _ret;
   }

   /**
    * f0 -> StmtExp()
    *       | Call()
    *       | HAllocate()
    *       | BinOp()
    *       | Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> Exp()
    * f4 -> "END"
    */
   public R visit(StmtExp n, A argu) {
      R _ret=null;
      Integer y = (Integer)argu;
      int x1 = Integer.valueOf(y);
      if(x1 == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
      }
      else if(x1 == 1)
      {
        n.f0.accept(this,argu);
        n.f1.accept(this,(A)new Integer(2));
        n.f2.accept(this,argu);
        int rg = current_register++;
        
        String x = (String)n.f3.accept(this,argu);
        pw.printf("MOVE TEMP %d %s\n",rg,x);
        n.f4.accept(this,argu);
        return (R)("TEMP " + Integer.toString(rg));
      }
      else if(x1 == 2)
      {
        y = new Integer(1);
        n.f0.accept(this,(A)y);
        pw.printf("BEGIN\n");
        n.f1.accept(this,(A)new Integer(2));
        n.f2.accept(this,(A)y);
        int rg = current_register++;
        String x = (String)n.f3.accept(this,(A)y);
        pw.printf("MOVE TEMP %d %s\n",rg,x);
        n.f4.accept(this,argu);
        pw.printf("RETURN TEMP %d\nEND\n",rg);
        return (R)("TEMP " + Integer.toString(rg));
      }
      else//part of a function call
      {
        y = new Integer(1);
        n.f0.accept(this,(A)y);
        n.f1.accept(this,(A)new Integer(2));
        n.f2.accept(this,(A)y);
        int rg = current_register++;
        String x = (String)n.f3.accept(this,(A)y);
        pw.printf("MOVE TEMP %d %s\n",rg,x);
        n.f4.accept(this,argu);
        String x2 = "TEMP " + Integer.toString(rg);
        arguments.add(x2);
        return (R)(x2);
      }
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> Exp()
    * f2 -> "("
    * f3 -> ( Exp() )*
    * f4 -> ")"
    */
   public R visit(Call n, A argu) {
      R _ret=null;
      Integer val = (Integer)argu;
      if(Integer.valueOf(val) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
      }
      else if((Integer.valueOf(val) == 1) || (Integer.valueOf(val) == 2))
      {
        Integer te = new Integer(1);
        n.f0.accept(this, (A)te);
        int rg0 = current_register++;
        String a = (String)n.f1.accept(this,(A)te);
        pw.printf("MOVE TEMP %d %s\n",rg0,a);
        n.f2.accept(this,(A)te);
        int old =arguments.size();
        n.f3.accept(this,(A)new Integer(3));//denotes a function call
        n.f4.accept(this,(A)te);
        int rg = current_register++;
        pw.printf("MOVE TEMP %d CALL TEMP %d (",rg,rg0);
        int x = arguments.size();
        int i;
        for(i = old;i < x;i++)
        {
          pw.printf("%s ",arguments.get(i));
        }
        pw.printf(")\n");
        while(arguments.size() != old)
        {
          arguments.remove(arguments.size()-1);
        }
        return (R)("TEMP " + Integer.toString(rg));
      }
      else//case 3, nested function call..
      {
        Integer t = new Integer(1);
        n.f0.accept(this, (A)t);
        int rg0 = current_register++;
        String a = (String)n.f1.accept(this,(A)t);
        pw.printf("MOVE TEMP %d %s\n",rg0,a);
        n.f2.accept(this,(A)t);
        int old = arguments.size();
        n.f3.accept(this,(A)new Integer(3));//denotes a function call
        n.f4.accept(this,(A)t);
        int rg = current_register++;
        pw.printf("MOVE TEMP %d CALL TEMP %d (",rg,rg0);
        int x = arguments.size();
        int i;
        for(i = old;i < x;i++)
        {
          pw.printf("%s ",arguments.get(i));
        }
        pw.printf(")\n");
        while(arguments.size() != old)
        {
          arguments.remove(arguments.size()-1);
        }
        arguments.add("TEMP " + Integer.toString(rg));
        return (R)("TEMP " + Integer.toString(rg));
      }
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> Exp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      Integer val = (Integer)argu;
      if(Integer.valueOf(val) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
      }
      else if((Integer.valueOf(val) == 1) || (Integer.valueOf(val) == 2))
      {
        Integer te = new Integer(1);
        n.f0.accept(this,(A)te);
        int rg = current_register++;
        int rg1 = current_register++;
        String x = (String)n.f1.accept(this,(A)te);
        pw.printf("MOVE TEMP %d %s\n",rg,x);
        pw.printf("MOVE TEMP %d HALLOCATE TEMP %d\n",rg1,rg);
        String t = "TEMP " + Integer.toString(rg1);
        return (R)t;
      }
      else
      {
        Integer te = new Integer(1);
        n.f0.accept(this,(A)te);
        int rg = current_register++;
        int rg1 = current_register++;
        String x = (String)n.f1.accept(this,(A)te);
        pw.printf("MOVE TEMP %d %s\n",rg,x);
        pw.printf("MOVE TEMP %d HALLOCATE TEMP %d\n",rg1,rg);
        String t = "TEMP " + Integer.toString(rg1);
        arguments.add(t);
        return (R)t;
      }
      return _ret;
   }

   /**
    * f0 -> Operator()
    * f1 -> Exp()
    * f2 -> Exp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      Integer val = (Integer)argu;
      if(Integer.valueOf(val) == 0)
      {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
      }
      else if((Integer.valueOf(val) == 1) || (Integer.valueOf(val) == 2))
      {
        Integer t1 = new Integer(1);
        String a = (String)n.f0.accept(this,(A)t1);
        int rg0 = current_register++;
        int rg1 = current_register++;
        String b = (String)n.f1.accept(this,(A)t1);
        String c = (String)n.f2.accept(this,(A)t1);
        pw.printf("MOVE TEMP %d %s\nMOVE TEMP %d %s\n",rg0,b,rg1,c);
        int rg2 = current_register++;
        pw.printf("MOVE TEMP %d %s TEMP %d TEMP %d\n",rg2,a,rg0,rg1);
        String t = "TEMP " + Integer.toString(rg2);
        return (R)t;
      }
      else
      {
        Integer t1 = new Integer(1);
        String a = (String)n.f0.accept(this,(A)t1);
        int rg0 = current_register++;
        int rg1 = current_register++;
        String b = (String)n.f1.accept(this,(A)t1);
        String c = (String)n.f2.accept(this,(A)t1);
        pw.printf("MOVE TEMP %d %s\nMOVE TEMP %d %s\n",rg0,b,rg1,c);
        int rg2 = current_register++;
        pw.printf("MOVE TEMP %d %s TEMP %d TEMP %d\n",rg2,a,rg0,rg1);
        String t = "TEMP " + Integer.toString(rg2);
        arguments.add(t);
        return (R)t;
      }
      return _ret;
   }

   /**
    * f0 -> "LE"
    *       | "NE"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "DIV"
    */
   public R visit(Operator n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
   public R visit(Temp n, A argu) {
      R _ret=null;
      Integer val = (Integer)argu;
      if(Integer.valueOf(val) == 0)
      {
        n.f0.accept(this, argu);
        String x = (String)n.f1.accept(this, (A)new Integer(1));
        int y = Integer.parseInt(x);
        current_register = Math.max(current_register,y);  
      }
      else
      {
        n.f0.accept(this, argu);
        String x = (String)n.f1.accept(this, (A)new Integer(1));
        x = "TEMP " + x;
        if(Integer.valueOf(val) == 3)
          arguments.add(x);
        return (R)x;
      }
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      if(Integer.valueOf((Integer)argu) == 3)
      {
        int rg = current_register++;
        pw.printf("MOVE TEMP %d %s\n",rg,(String)_ret);
        arguments.add("TEMP " + Integer.toString(rg));
      }
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      Integer y = (Integer)argu;
      if(Integer.valueOf(y) == 2)
      {
        pw.println((String)_ret);//label name
        return _ret;
      }
      else if(Integer.valueOf(y) == 3)
      {
        int rg = current_register++;//function arguments
        pw.printf("MOVE TEMP %d %s\n",rg,(String)_ret);
        arguments.add("TEMP " + Integer.toString(rg));
      }
      return _ret;
   }
}
