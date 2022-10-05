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
   PrintWriter pw  = new PrintWriter(System.out,true);
   int arg_cnt = 0;
   int passed_args = 0;
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
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( SpillInfo() )?
    * f13 -> ( Procedure() )*
    * f14 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String sl = (String)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      pw.printf(".text\n.globl main\nmain:\n");
      int y = Integer.parseInt(sl);
      pw.println("sw $fp,0($sp)");
      pw.println("sw $ra,-4($sp)");
      pw.println("move $fp $sp");
      pw.printf("sub $sp,$sp,%d\n",4*y+8);//move the stack pointer to the appropriate location
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      pw.printf("lw $ra,-4($fp)\n");
      pw.printf("lw $fp,0($fp)\n");
      pw.printf("add $sp,$sp,%d\n",4*y+8);//2 slots for return address and frame pointer
      pw.printf("j $ra\n");
      n.f13.accept(this, argu);
      n.f14.accept(this, argu);
      pw.printf(".data\n.globl str_er\n.align 0\nstr_er: .asciiz \"ERROR: Abnormal Termination\\n\"\n");
      pw.printf(".data\n.globl new_l\n.align 0\nnew_l: .asciiz \"\\n\"");
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( SpillInfo() )?
    */
   public R visit(Procedure n, A argu) {
      R _ret=null;
      String fname = (String)n.f0.accept(this, (A)new Integer(1));
      n.f1.accept(this, argu);
      arg_cnt =  Integer.parseInt((String)n.f2.accept(this, argu));
      n.f3.accept(this, argu);
      n.f4.accept(this, argu);
      String sl = (String)n.f5.accept(this, argu);
      n.f6.accept(this, argu);
      n.f7.accept(this, argu);
      n.f8.accept(this, argu);
      n.f9.accept(this, argu);
      int y = Integer.parseInt(sl);
      pw.printf(".text\n.globl   %s\n%s:\n",fname,fname);
      pw.printf("sw $fp,0($sp)\n");
      pw.printf("sw $ra,-4($sp)\n");
      pw.printf("move $fp,$sp\n");
      pw.printf("sub $sp,$sp,%d\n",4*y+8);
      n.f10.accept(this, argu);
      n.f11.accept(this, argu);
      n.f12.accept(this, argu);
      pw.printf("lw $ra,-4($fp)\n");
      pw.printf("lw $fp,0($fp)\n");
      pw.printf("add $sp,$sp,%d\n",4*y+8);//restore the stack pointer to wherever we began from, and the frame pointer as well
      pw.printf("j $ra\n");
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
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
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
      pw.println("nop");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      pw.printf("la $a0,str_er\nadd $v0,$0,4\nsyscall\n");
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String l = (String)n.f2.accept(this, (A)new Integer(1));
      pw.printf("beqz $%s,%s\n",x,l);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, (A)new Integer(1));
      pw.printf("b %s\n",x);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String v = (String)n.f2.accept(this, argu);
      String y = (String)n.f3.accept(this, argu);
      pw.printf("sw $%s,%s($%s)\n",y,v,x);
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String y = (String)n.f2.accept(this, argu);
      String v = (String)n.f3.accept(this, argu);
      pw.printf("lw $%s,%s($%s)\n",x,v,y);
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String y = (String)n.f2.accept(this, (A)new Integer(1));//in v0 by default
      pw.printf("move $%s $%s\n",x,y);
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, (A)new Integer(1));
      pw.printf("move $a0 $%s\nadd $v0,$0,1\nsyscall\n",x);//print only integers as per minijava grammar
      pw.printf("la $a0,new_l\nli $v0,4\nsyscall\n");
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
   public R visit(ALoadStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String y = (String)n.f2.accept(this, argu);
      //read from the existing frame pointer properly
      int y1 = Integer.parseInt(y);
      int z = Math.max(arg_cnt-4,0);
      if(y1 < z)
         pw.printf("lw $%s,%d($fp)\n",x,4*(z-y1));
      else
         pw.printf("lw $%s,%d($fp)\n",x,-1*(8+4*(y1-z)));//load from the appropriate stack slot
      return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   public R visit(AStoreStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String y = (String)n.f2.accept(this, argu);
      int x1 = Integer.parseInt(x);
      int z  = Math.max(arg_cnt-4,0);
      if(x1 < z)
         pw.printf("sw $%s,%d($fp)\n",y,4*(z-x1));
      else
         pw.printf("sw $%s,%d($fp)\n",y,-1*(8+4*(x1-z)));//store into the appropriate stack slot 
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
   public R visit(PassArgStmt n, A argu) {
      R _ret=null;
      passed_args++;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String y = (String)n.f2.accept(this, argu);
      int x1 = Integer.parseInt(x);
      pw.printf("sw $%s,%d($sp)\n",y,-4*(x1-1));
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   public R visit(CallStmt n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, (A)new Integer(1));
      if(passed_args > 0)
         pw.printf("sub $sp,$sp,%d\n",4*passed_args);
      pw.printf("jalr $%s\n",x);
      passed_args = 0;//start again for a new function call
      return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, (A)new Integer(1));
      pw.printf("move $a0 $%s\nli $v0, 9\nsyscall\n",x);
      return (R)"v0";
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n, A argu) {
      R _ret=null;
      String op = (String)n.f0.accept(this, argu);
      String x = (String)n.f1.accept(this, argu);
      String y = (String)n.f2.accept(this, (A)new Integer(1));
      switch(op)
      {
         case "LE": {pw.printf("sle $v0,$%s,$%s\n",x,y);return (R)"v0";}
         case "NE": {pw.printf("sne $v0,$%s,$%s\n",x,y);return (R)"v0";}
         case "PLUS":{pw.printf("add $v0,$%s,$%s\n",x,y);return (R)"v0";}
         case "MINUS":{pw.printf("sub $v0,$%s,$%s\n",x,y);return (R)"v0";}
         case "TIMES":{pw.printf("mul $v0,$%s,$%s\n",x,y);return (R)"v0";}
         case "DIV":{pw.printf("div $v0,$%s,$%s\n",x,y);return (R)"v0";}
         default:;break;
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
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      _ret = n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      if(n.f0.which == 1)
      {
         pw.printf("li $v0, %s\n",(String)_ret);
         _ret = (R)"v0";
      }
      else if(n.f0.which == 2)
      {
         pw.printf("la $v0, %s\n",(String)_ret);
         _ret = (R)"v0";
      }
      return _ret;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n, A argu) {
      R _ret=null;
      int y = Integer.valueOf((Integer)argu);
      _ret = n.f0.accept(this, argu);
      if(y == 0)
         pw.printf("%s:\n",(String)_ret);
      return _ret;
   }

   /**
    * f0 -> "//"
    * f1 -> SpillStatus()
    */
   public R visit(SpillInfo n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> <SPILLED>
    *       | <NOTSPILLED>
    */
   public R visit(SpillStatus n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }
}
