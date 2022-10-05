/* Generated by JTB 1.4.4 */
package visitor;

import syntaxtree.*;
import java.util.*;

public class DepthFirstRetVisitor<R> implements IRetVisitor<R> {


  public R visit(final NodeChoice n) {
    final R nRes = n.choice.accept(this);
    return nRes;
  }

  public R visit(final NodeList n) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      final R sRes = e.next().accept(this);
    }
    return nRes;
  }

  public R visit(final NodeListOptional n) {
    if (n.present()) {
      R nRes = null;
      for (final Iterator<INode> e = n.elements(); e.hasNext();) {
        @SuppressWarnings("unused")
        R sRes = e.next().accept(this);
        }
      return nRes;
    } else
      return null;
  }

  public R visit(final NodeOptional n) {
    if (n.present()) {
      final R nRes = n.node.accept(this);
      return nRes;
    } else
    return null;
  }

  public R visit(final NodeSequence n) {
    R nRes = null;
    for (final Iterator<INode> e = n.elements(); e.hasNext();) {
      @SuppressWarnings("unused")
      R subRet = e.next().accept(this);
    }
    return nRes;
  }

  public R visit(final NodeToken n) {
    R nRes = null;
    @SuppressWarnings("unused")
    final String tkIm = n.tokenImage;
    return nRes;
  }

  public R visit(final Goal n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final MainClass n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    n.f7.accept(this);
    n.f8.accept(this);
    n.f9.accept(this);
    n.f10.accept(this);
    n.f11.accept(this);
    n.f12.accept(this);
    n.f13.accept(this);
    n.f14.accept(this);
    n.f15.accept(this);
    n.f16.accept(this);
    return nRes;
  }

  public R visit(final TypeDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final ClassDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    return nRes;
  }

  public R visit(final ClassExtendsDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    n.f7.accept(this);
    return nRes;
  }

  public R visit(final VarDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final MethodDeclaration n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    n.f7.accept(this);
    n.f8.accept(this);
    n.f9.accept(this);
    n.f10.accept(this);
    n.f11.accept(this);
    n.f12.accept(this);
    return nRes;
  }

  public R visit(final FormalParameterList n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final FormalParameter n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final FormalParameterRest n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final Type n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final ArrayType n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final BooleanType n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final IntegerType n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final Statement n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final Block n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final AssignmentStatement n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    return nRes;
  }

  public R visit(final ArrayAssignmentStatement n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    return nRes;
  }

  public R visit(final IfStatement n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final IfthenStatement n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final IfthenElseStatement n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    n.f6.accept(this);
    return nRes;
  }

  public R visit(final WhileStatement n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final PrintStatement n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final Expression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final AndExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final OrExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final CompareExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final neqExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final PlusExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final MinusExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final TimesExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final DivExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final ArrayLookup n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    return nRes;
  }

  public R visit(final ArrayLength n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final MessageSend n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    n.f5.accept(this);
    return nRes;
  }

  public R visit(final ExpressionList n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final ExpressionRest n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final PrimaryExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final IntegerLiteral n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final TrueLiteral n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final FalseLiteral n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final Identifier n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final ThisExpression n) {
    R nRes = null;
    n.f0.accept(this);
    return nRes;
  }

  public R visit(final ArrayAllocationExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    n.f4.accept(this);
    return nRes;
  }

  public R visit(final AllocationExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    n.f3.accept(this);
    return nRes;
  }

  public R visit(final NotExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final BracketExpression n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    n.f2.accept(this);
    return nRes;
  }

  public R visit(final IdentifierList n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

  public R visit(final IdentifierRest n) {
    R nRes = null;
    n.f0.accept(this);
    n.f1.accept(this);
    return nRes;
  }

}