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
   class FunctionTable
   {
      public int args;
      public ArrayList<Pair<String,String>> arguments;
      public Hashtable<String,String> local_variables;//names and their type
      public String return_type;
      public FunctionTable()
      {
         arguments = new ArrayList<Pair<String,String>>();
         local_variables = new Hashtable<String,String>();
         args = 0;
         return_type = "int";
      }
   }
   class ClassTable
   {
      public Hashtable<String,FunctionTable> functions;
      public Hashtable<String,String> class_fields;
      public String parent;
      public ClassTable()
      {
         functions =  new Hashtable<String,FunctionTable>();
         class_fields = new Hashtable<String,String>();
         parent = null;
      }
   }
   class ObjectTable
   {
      public ArrayList<Pair<Pair<String,String>,String>> functions;
      public ArrayList<Pair<String,String>> fields;
      public ObjectTable()
      {
         functions = new ArrayList<Pair<Pair<String,String>,String>>();
         fields = new ArrayList<Pair<String,String>>();
      }
   }
   ClassTable class_object;
   String current_class;
   String current_function;
   String original_type_of_expr;
   FunctionTable function_object;
   ArrayList<Pair<String,String>> function_arguments = new ArrayList<Pair<String,String>>();
   Hashtable<String,ClassTable> classes = new Hashtable<String,ClassTable>();
   Hashtable<String,ObjectTable> vtables = new Hashtable<String,ObjectTable>();
   String type_of_expr;
   PrintWriter pw = new PrintWriter(System.out,true);
   int label_count = 0;
   int temp_count = 1;
   int sizeof(String type)
   {
      if(type.equals("boolean"))
         return 1;
      else
         return 4; 
   }
   String get_llvm_type(String a)
   {
      if(a.equals("int"))
         return "i32";
      else if(a.equals("boolean"))
         return "i8";
      else if(a.equals("int[]"))
         return "i32*";
      else if(a.equals("String[]"))
         return "i8**";
      else 
         return "i8*";//object pointer
   }
   ObjectTable get_vtable(String class_type)
   {
      if(vtables.get(class_type) == null)
      {
         String x = class_type;
         ObjectTable ot = new ObjectTable();
         while(class_type != null)
         {
            ClassTable ct = classes.get(class_type);
            Iterator<String> it1 = ct.class_fields.keySet().iterator();
            while(it1.hasNext())
            {
               String s = it1.next();
               ot.fields.add(new Pair<String,String>(class_type + "_" + s,ct.class_fields.get(s)));
            }
            Iterator<String> it = ct.functions.keySet().iterator();
            while(it.hasNext())
            {
               String s = it.next();
               FunctionTable ft = ct.functions.get(s);
               String ac = get_llvm_type(ft.return_type) + " (i8*";
               for(Pair<String,String> arg : ft.arguments)
               {
                  String ty = get_llvm_type(arg.second);
                  ac += "," + ty;
               } 
               ac += ")*";
               ot.functions.add(new Pair<Pair<String,String>,String>((new Pair<String,String>(class_type + "_" + s,ac)),ft.return_type));
            }
            class_type = ct.parent;
         }
         vtables.put(x,ot);
         return ot;
      }
      else
         return vtables.get(class_type);
   }
   Pair<Integer,Pair<String,String>> get_function(String fname,String obj_type)
   {
      ObjectTable ot = get_vtable(obj_type);
      int i = 0;
      for(Pair<Pair<String,String>,String> n: ot.functions)
      {
         if(n.first.first.endsWith(fname))
            return new Pair<Integer,Pair<String,String>>(i,new Pair<String,String>(n.first.second,n.second));//index in vtable
         i++; 
      }
      return null;
   }

   String get_mem_loc(String var)
   {
      String x = function_object.local_variables.get(var);
      if(x != null)
      {
         type_of_expr = get_llvm_type(x);
         original_type_of_expr = x;
         return String.format("%%%s",var);//local variables
      }
      for(Pair<String,String> it : function_object.arguments)
      {
         if(it.first.equals(var))
         {
            type_of_expr = get_llvm_type(it.second);
            original_type_of_expr = it.second;
            return String.format("%%%s",var);//arguments
         }
      }
      ObjectTable ot = get_vtable(current_class);
      int ind = 4;//earmark 4 bytes for the vtable
      String checker = "_" + var;
      for(Pair<String,String> it: ot.fields)
      {
         if(it.first.endsWith(checker))//return an appropriate pointer over here
         {
            int rg1 = temp_count++;
            int rg2 = temp_count++;
            pw.printf("%%%d = getelementptr i8,i8* %%this,i32 %d\n",rg1,ind);
            pw.printf("%%%d = bitcast i8* %%%d to %s*\n",rg2,rg1,get_llvm_type(it.second));
            type_of_expr = get_llvm_type(it.second);
            original_type_of_expr = it.second;
            return String.format("%%%d",rg2);
         }
         ind += sizeof(it.second);//the size of the type
      }
      return null;//does not exist in memory.

   }
   String generate_object(String object_type)
   {
      ObjectTable ot = get_vtable(object_type);
      int r1 = temp_count++;
      int sz = 4;//4 vbytes for vtable address
      for(Pair<String,String> it : ot.fields)
         sz += sizeof(it.second);
      char c = '%';
      pw.printf("%c%d = call i8* @calloc(i32 %d, i32 1)\n",c,r1,sz);
      int r2 = temp_count++;
      int r3 = temp_count++;
      int r4 = temp_count++;
      pw.printf("%c%d = bitcast i8* %c%d to i8***\n",c,r2,c,r1);//pointer to the vtable is now in vt_temp
      pw.printf("%c%d = call i8* @calloc(i32 4,i32 %d)\n",c,r3,ot.functions.size());//pointer to heap space for vt
      pw.printf("%c%d = bitcast i8* %c%d to i8**\n",c,r4,c,r3);
      pw.printf("store i8** %%%d, i8*** %%%d\n",r4,r2);//stored vtable successfully, now have to update vtable details themselves 
      int f_cnt = 0;
      for(Pair<Pair<String,String>,String> fn : ot.functions)
      {
         int r5 = temp_count++;
         pw.printf("%c%d = getelementptr i8*,i8** %c%d,i32 %d\n",c,r5,c,r4,f_cnt);
         pw.printf("store i8* bitcast (%s @%s to i8*),i8** %c%d\n",fn.first.second,fn.first.first,c,r5);
         f_cnt++;   
      }
      return String.format("%%%d",r1);//register to the object address
   }
   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public R visit(Goal n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 0)
      {
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         n.f2.accept(this, argu);
      }
      else if(ar == 1)
      {
         pw.println("@_fint = constant [4 x i8] c\"%d\\0a\\00\"");
         pw.println("declare i8* @calloc(i32,i32)");
         pw.println("declare i32 @printf(i8*,...)");
         pw.println("declare void @exit(i32)");
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         n.f2.accept(this, argu);
      }
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> PrintStatement()
    * f15 -> "}"
    * f16 -> "}"
    */
   public R visit(MainClass n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 0)
      {
         n.f0.accept(this, argu);
         current_class = (String)n.f1.accept(this, argu);
         class_object = new ClassTable();
         function_object = new FunctionTable();
         function_object.args = 1;
         n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         n.f6.accept(this, argu);
         n.f7.accept(this, argu);
         n.f8.accept(this, argu);
         n.f9.accept(this, argu);
         n.f10.accept(this, argu);
         String arg_to_main = (String)n.f11.accept(this, argu);
         n.f12.accept(this, argu);
         n.f13.accept(this, argu);
         n.f14.accept(this, argu);
         n.f15.accept(this, argu);
         n.f16.accept(this, argu);
         function_object.arguments.add(new Pair<String,String>(arg_to_main,"String[]"));
         class_object.functions.put("main",function_object);
         classes.put(current_class,class_object);
         class_object = null;
         function_object = null;
      }
      else if(ar == 1)
      {
         n.f0.accept(this,argu);
         current_class = (String)n.f1.accept(this,argu);
         n.f2.accept(this,argu);
         n.f3.accept(this,argu);
         n.f4.accept(this,argu);
         n.f5.accept(this,argu);
         n.f6.accept(this,argu);
         n.f7.accept(this,argu);
         n.f8.accept(this,argu);
         n.f9.accept(this,argu);
         n.f10.accept(this,argu);
         String param = (String)n.f11.accept(this,argu);
         n.f12.accept(this,argu);
         pw.printf("define void @main(i8* %%%s){\n",param);
         n.f13.accept(this,argu);
         n.f14.accept(this,argu);
         n.f15.accept(this,argu);
         n.f16.accept(this,argu);
         pw.printf("ret void\n}\n");

      }
      return _ret;
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public R visit(TypeDeclaration n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public R visit(ClassDeclaration n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 0)
      {
         class_object = new ClassTable();
         n.f0.accept(this, argu);
         current_class = (String)n.f1.accept(this, argu);
         n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         classes.put(current_class,class_object);
         class_object = null;
      }
      else if(ar == 1)
      {
         n.f0.accept(this,argu);
         current_class = (String)n.f1.accept(this, argu);
         class_object = classes.get(current_class);
         n.f2.accept(this,argu);
         n.f3.accept(this,argu);
         n.f4.accept(this,argu);
         n.f5.accept(this,argu);
         current_class = null;
         class_object = null;
      }
      return _ret;
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public R visit(ClassExtendsDeclaration n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 0)
      {
         n.f0.accept(this, argu);
         current_class = (String)n.f1.accept(this, argu);
         n.f2.accept(this, argu);
         String pname = (String)n.f3.accept(this, argu);
         class_object = new ClassTable();
         class_object.parent = pname;
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         n.f6.accept(this, argu);
         n.f7.accept(this, argu);
         classes.put(current_class,class_object);
         current_class = null;
         class_object = null;
      }
      else if(ar == 1)
      {
         n.f0.accept(this, argu);
         current_class = (String)n.f1.accept(this, argu);
         class_object = classes.get(current_class);
         n.f2.accept(this, argu);
         String pname = (String)n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         n.f6.accept(this, argu);
         n.f7.accept(this, argu);
         current_class = null;
         class_object = null;
      }
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public R visit(VarDeclaration n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 0)
      {
         String t = (String)n.f0.accept(this, argu);
         String nm = (String)n.f1.accept(this, argu);
         if(function_object == null)
            class_object.class_fields.put(nm,t);
         else
            function_object.local_variables.put(nm,t);
         n.f2.accept(this, argu);
      }
      else if(ar == 1)
      {
         String t = (String)n.f0.accept(this, argu);
         String nm = (String)n.f1.accept(this, argu);
         if(function_object != null)
         {
            //allocate memory on the stack directly for the same.
            pw.printf("%%%s = alloca %s\n",nm,get_llvm_type(t));
         }
      }
      return _ret;
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public R visit(MethodDeclaration n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 0)
      {
         n.f0.accept(this, argu);
         String t = (String)n.f1.accept(this, argu);
         String fname = (String)n.f2.accept(this, argu);
         function_object = new FunctionTable();
         function_object.return_type = t;
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         n.f6.accept(this, argu);
         n.f7.accept(this, argu);
         n.f8.accept(this, argu);
         n.f9.accept(this, argu);
         n.f10.accept(this, argu);
         n.f11.accept(this, argu);
         n.f12.accept(this, argu);
         class_object.functions.put(fname,function_object);
         function_object = null;
      }
      else if(ar == 1)
      {
         temp_count = 1;//each function has it's own special local variables
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         current_function = (String)n.f2.accept(this, argu);
         function_object = class_object.functions.get(current_function);
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         n.f6.accept(this, argu);
         pw.printf("define %s @%s_%s (i8* %%this",get_llvm_type(function_object.return_type),current_class,current_function);
         int cnt = 0;
         for(Pair<String,String> it : function_object.arguments)
         {
            pw.printf(",%s %%.%s",get_llvm_type(it.second),it.first);
         }
         pw.println("){");
         for(Pair<String,String> it : function_object.arguments)
         {
            pw.printf("%%%s = alloca %s\n",it.first,get_llvm_type(it.second));//allocate stack space to be modified
            pw.printf("store %s %%.%s,%s* %%%s\n",get_llvm_type(it.second),it.first,get_llvm_type(it.second),it.first);
         }
         n.f7.accept(this, argu);
         n.f8.accept(this, argu);
         n.f9.accept(this, argu);
         String rv = (String)n.f10.accept(this, argu);
         n.f11.accept(this, argu);
         n.f12.accept(this, argu);
         pw.printf("ret %s %s\n}\n",get_llvm_type(function_object.return_type),rv);
         function_object = null;
         current_function = null;
      }
      return _ret;
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public R visit(FormalParameterList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public R visit(FormalParameter n, A argu) {
      R _ret=null;
      int x = Integer.valueOf((Integer)argu);
      if(x == 0)
      {
         String ty = (String)n.f0.accept(this, argu);
         String nm = (String)n.f1.accept(this, argu);
         function_object.arguments.add(new Pair<String,String>(nm,ty));
      }
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public R visit(FormalParameterRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public R visit(Type n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public R visit(ArrayType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return (R)"int[]";
   }

   /**
    * f0 -> "boolean"
    */
   public R visit(BooleanType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)"boolean";
   }

   /**
    * f0 -> "int"
    */
   public R visit(IntegerType n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return (R)"int";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public R visit(Statement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public R visit(Block n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      n.f2.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public R visit(AssignmentStatement n, A argu) {
      R _ret=null;
      int x = Integer.valueOf((Integer)argu);
      if(x == 1)
      {
         String id_name = (String)n.f0.accept(this,argu);
         String id = get_mem_loc(id_name);
         String lhs_type = type_of_expr;
         n.f1.accept(this, argu);
         String a2 = (String)n.f2.accept(this, argu);//expressions always return a register directly or a value as appropriate
         pw.printf("store %s %s,%s* %s\n",lhs_type,a2,lhs_type,id);
         n.f3.accept(this, argu);
      }
      return _ret;
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public R visit(ArrayAssignmentStatement n, A argu) {
      R _ret=null;
      int x = Integer.valueOf((Integer)argu);
      if(x == 1)
      {
         String id = get_mem_loc((String)n.f0.accept(this, argu));//arrays are always of integer type as per grammar
         n.f1.accept(this, argu);
         String ind = (String)n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         String val = (String)n.f5.accept(this, argu);
         n.f6.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = getelementptr %s,%s* %s,i32 %s\n",rg,type_of_expr,type_of_expr,id,ind);
         pw.printf("store %s %s,%s* %%%d\n",type_of_expr,val,type_of_expr,rg);
      }
      return _ret;
   }

   /**
    * f0 -> IfthenElseStatement()
    *       | IfthenStatement()
    */
   public R visit(IfStatement n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(IfthenStatement n, A argu) {
      R _ret=null;
      int x = Integer.valueOf((Integer)argu);
      if(x == 1)
      {
         int lb1 = label_count++;
         int lb2 = label_count++;
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String id = (String)n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         int rgf = temp_count++;
         pw.printf("%%%d = trunc i8 %s to i1\n",rgf,id);
         pw.printf("br i1 %%%d,label %%L%d,label %%L%d\n",rgf,lb1,lb2);
         pw.printf("L%d:\n",lb1);
         n.f4.accept(this, argu);
         pw.printf("br label %%L%d\n",lb2);
         pw.printf("L%d:\n%%%d = add i32 0,0\n",lb2,temp_count++);//added a dummy instruction
      }
      return _ret;
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public R visit(IfthenElseStatement n, A argu) {
      R _ret=null;
      int x = Integer.valueOf((Integer)argu);
      if(x == 1)
      {
         int lb1 = label_count++;
         int lb2 = label_count++;
         int lb3 = label_count++;//final label to jump to
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String id = (String)n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         int rgf = temp_count++;
         pw.printf("%%%d = trunc i8 %s to i1\n",rgf,id);
         pw.printf("br i1 %%%d, label %%L%d,label %%L%d\n",rgf,lb1,lb2);
         pw.printf("L%d:\n",lb1);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         pw.printf("br label %%L%d\nL%d:\n",lb3,lb2);
         n.f6.accept(this, argu);
         pw.printf("br label %%L%d\n",lb3);
         pw.printf("L%d:\n%%%d = add i32 0,0\n",lb3,temp_count++);
      }
      return _ret;
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public R visit(WhileStatement n, A argu) {
      R _ret=null;
      int x = Integer.valueOf((Integer)argu);
      if(x == 1)
      {
         int lb1 = label_count++;
         int lb2 = label_count++;
         int lb3 = label_count++;
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         pw.printf("br label %%L%d\n",lb1);
         pw.printf("L%d:\n",lb1);
         String id = (String)n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         int rgf = temp_count++;
         pw.printf("%%%d = trunc i8 %s to i1\n",rgf,id);
         pw.printf("br i1 %%%d, label %%L%d, label %%L%d\n",rgf,lb2,lb3);
         pw.printf("L%d:\n",lb2);
         n.f4.accept(this, argu);
         pw.printf("br label %%L%d\n",lb1);
         pw.printf("L%d:\n%%%d = add i32 0,0\n",lb3,temp_count++);
      }
      return _ret;
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public R visit(PrintStatement n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String x = (String)n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = bitcast [4 x i8]* @_fint to i8*\n",rg);
         pw.printf("%%%d = call i32 (i8*,...) @printf(i8* %%%d,%s %s)\n",temp_count++,rg,type_of_expr,x);
      }
      return _ret;
   }

   /**
    * f0 -> OrExpression()
    *       | AndExpression()
    *       | CompareExpression()
    *       | neqExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | DivExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public R visit(Expression n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public R visit(AndExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = and i8 %s,%s\n",rg,x,y);
         type_of_expr = "i8";
         original_type_of_expr = "boolean";
         return (R)String.format("%%%d",rg);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "||"
    * f2 -> PrimaryExpression()
    */
   public R visit(OrExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = or i8 %s,%s\n",rg,x,y);
         type_of_expr = "i8";
         original_type_of_expr = "boolean";
         return (R)String.format("%%%d",rg);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<="
    * f2 -> PrimaryExpression()
    */
   public R visit(CompareExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         int rg1 = temp_count++;
         pw.printf("%%%d = icmp sle i32 %s,%s\n",rg,x,y);
         pw.printf("%%%d = zext i1 %%%d to i8\n",rg1,rg);
         type_of_expr = "i8";
         original_type_of_expr = "boolean";
         return (R)String.format("%%%d",rg1);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "!="
    * f2 -> PrimaryExpression()
    */
   public R visit(neqExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         String type_used = type_of_expr;
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         int rg1 = temp_count++;
         pw.printf("%%%d = icmp ne %s %s,%s\n",rg,type_used,x,y);
         pw.printf("%%%d = zext i1 %%%d to i8\n",rg1,rg);
         type_of_expr = "i8";
         original_type_of_expr = "boolean";
         return (R)String.format("%%%d",rg1);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public R visit(PlusExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = add i32 %s,%s\n",rg,x,y);
         type_of_expr = "i32";
         original_type_of_expr = "int";
         return (R)String.format("%%%d",rg);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public R visit(MinusExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = sub i32 %s,%s\n",rg,x,y);
         type_of_expr = "i32";
         original_type_of_expr = "int";
         return (R)String.format("%%%d",rg);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public R visit(TimesExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = mul i32 %s,%s\n",rg,x,y);
         type_of_expr = "i32";
         original_type_of_expr = "int";
         return (R)String.format("%%%d",rg);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "/"
    * f2 -> PrimaryExpression()
    */
   public R visit(DivExpression n, A argu) {
      int ar = Integer.valueOf((Integer)argu);
      R _ret = null;
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String y = (String)n.f2.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = sdiv i32 %s,%s\n",rg,x,y);
         type_of_expr = "i32";
         original_type_of_expr = "int";
         return (R)String.format("%%%d",rg);
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public R visit(ArrayLookup n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         String id = (String)n.f0.accept(this, argu);//direct reference
         String arr_type = type_of_expr;
         String arr_orig_type = original_type_of_expr;
         n.f1.accept(this, argu);
         String ind = (String)n.f2.accept(this, argu);//add one to the index to access
         int rg = temp_count++;
         pw.printf("%%%d = add i32 %s,1\n",rg,ind);
         int mem = temp_count++;
         pw.printf("%%%d = getelementptr i32,i32* %s,i32 %%%d\n",mem,id,rg);
         int fin = temp_count++;
         pw.printf("%%%d = load i32,i32* %%%d\n",fin,mem);
         n.f3.accept(this, argu);
         type_of_expr = "i32";
         original_type_of_expr = "int";
         return (R)(String.format("%%%d",fin));
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public R visit(ArrayLength n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         String s = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         n.f2.accept(this, argu);
         String arr_type = "i32";
         int an = temp_count++;
         pw.printf("%%%d = load %s,%s* %s\n",an,arr_type,arr_type,s);
         type_of_expr = "i32";
         original_type_of_expr = "int";
         return (R)(String.format("%%%d",an));
      }
      return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public R visit(MessageSend n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         String s = (String)n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         String obj_type = original_type_of_expr;
         String fn_name = (String)n.f2.accept(this, argu);
         Pair<Integer,Pair<String,String>> fn_details = get_function(fn_name,obj_type);
         int t1 = temp_count++;
         int t2 = temp_count++;
         int t3 = temp_count++;
         int t4 = temp_count++;
         int t5 = temp_count++;
         pw.printf("%%%d = bitcast i8* %s to i8***\n",t1,s);
         pw.printf("%%%d = load i8**,i8*** %%%d\n",t2,t1);
         pw.printf("%%%d = getelementptr i8*,i8** %%%d,i32 %d\n",t3,t2,fn_details.first);
         pw.printf("%%%d = load i8*,i8** %%%d\n",t4,t3);
         pw.printf("%%%d = bitcast i8* %%%d to %s\n",t5,t4,fn_details.second.first);
         int old_size = function_arguments.size();
         n.f3.accept(this, argu);
         n.f4.accept(this, argu);
         n.f5.accept(this, argu);
         int new_size = function_arguments.size();
         int res = temp_count++;
         pw.printf("%%%d = call %s %%%d(i8* %s",res,get_llvm_type(fn_details.second.second),t5,s);
         for(int i = old_size; i < new_size;i++)
         {
            Pair<String,String> it = function_arguments.get(i);
            pw.printf(",%s %s",it.first,it.second);
         }
         pw.printf(")\n");//no arguments to the function
         new_size--;
         while(function_arguments.size() != old_size)
         {
            function_arguments.remove(new_size);
            new_size--;
         }
         original_type_of_expr = fn_details.second.second;
         type_of_expr = get_llvm_type(original_type_of_expr);
         return (R)(String.format("%%%d",res));
      }
      return _ret;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public R visit(ExpressionList n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         function_arguments.add(new Pair<String,String>(type_of_expr,x));
         n.f1.accept(this, argu);
      }
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public R visit(ExpressionRest n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         n.f0.accept(this, argu);
         String x = (String)n.f1.accept(this, argu);
         function_arguments.add(new Pair<String,String>(type_of_expr,x));
      }
      return _ret;
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public R visit(PrimaryExpression n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         String x = (String)n.f0.accept(this, argu);
         if(n.f0.which == 3)
         {
            //identifier which has to be returned as part of a register
            String loc = get_mem_loc(x);
            int rg = temp_count++;
            pw.printf("%%%d = load %s,%s* %s\n",rg,type_of_expr,type_of_expr,loc);
            return (R)(String.format("%%%d",rg));
         }
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
      type_of_expr = "i32";
      original_type_of_expr = "int";
      return _ret;
   }

   /**
    * f0 -> "true"
    */
   public R visit(TrueLiteral n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      type_of_expr = "i8";
      original_type_of_expr = "boolean";
      return (R)"1";
   }

   /**
    * f0 -> "false"
    */
   public R visit(FalseLiteral n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      _ret = n.f0.accept(this, argu);
      type_of_expr = "i8";
      original_type_of_expr = "boolean";
      return (R)"0";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Identifier n, A argu) {
      R _ret=null;
      _ret = n.f0.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> "this"
    */
   public R visit(ThisExpression n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      type_of_expr = "i8*";
      original_type_of_expr = current_class;
      return (R)"%this";//currently active object
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public R visit(ArrayAllocationExpression n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         n.f0.accept(this, argu);
         n.f1.accept(this, argu);
         n.f2.accept(this, argu);
         String num = (String)n.f3.accept(this, argu);//allocate on the heap using calloc
         n.f4.accept(this, argu);
         int rg = temp_count++;
         int rg1 = temp_count++;
         pw.printf("%%%d = add i32 %s,1\n",rg,num);
         pw.printf("%%%d = mul i32 %%%d,4\n",rg1,rg);
         int cl_rg = temp_count++;
         pw.printf("%%%d = call i8* @calloc(i32 1,i32 %%%d)\n",cl_rg,rg1);
         int arr_rg = temp_count++;
         pw.printf("%%%d = bitcast i8* %%%d to i32*\n",arr_rg,cl_rg);
         pw.printf("store i32 %s,i32* %%%d\n",num,arr_rg);//0th index should contain the array size
         type_of_expr = "i32*";
         original_type_of_expr = "i8*";
         return (R)(String.format("%%%d",arr_rg));
      }
      return _ret;
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public R visit(AllocationExpression n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         n.f0.accept(this, argu);
         String object_type = (String)n.f1.accept(this, argu);
         n.f2.accept(this, argu);
         n.f3.accept(this, argu);
         type_of_expr = "i8*";
         original_type_of_expr = object_type;
         return (R)generate_object(object_type);
      }
      return _ret;
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public R visit(NotExpression n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         n.f0.accept(this, argu);
         String x = (String)n.f1.accept(this, argu);
         int rg = temp_count++;
         pw.printf("%%%d = sub i8 1,%s\n",rg,x);
         type_of_expr = "i8";
         original_type_of_expr = "boolean";
         return (R)(String.format("%%%d",rg));
      }
      return _ret;
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public R visit(BracketExpression n, A argu) {
      R _ret=null;
      int ar = Integer.valueOf((Integer)argu);
      if(ar == 1)
      {
         n.f0.accept(this, argu);
         String an = (String)n.f1.accept(this, argu);
         n.f2.accept(this, argu);
         return (R)an;
      }
      return _ret;
   }
   //these projections are never taken
   /**
    * f0 -> Identifier()
    * f1 -> ( IdentifierRest() )*
    */
   public R visit(IdentifierList n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }

   /**
    * f0 -> ","
    * f1 -> Identifier()
    */
   public R visit(IdentifierRest n, A argu) {
      R _ret=null;
      n.f0.accept(this, argu);
      n.f1.accept(this, argu);
      return _ret;
   }
}
