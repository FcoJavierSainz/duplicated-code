package org.iteso.duplicated.model;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class Ast {

  private ParserRuleContext originalNode;
  private AstSubTree root;
  private List<AstSubTree> list;

  public Ast(ParserRuleContext originalNode) {
    this.originalNode = originalNode;
    AstSubTree node = new AstSubTree(originalNode, null);
    list = new ArrayList<>();
    toAst(originalNode, node, 0);
  }

  public ParserRuleContext getOriginalNode() {
    return originalNode;
  }

  public AstSubTree getRoot() {
    return root;
  }

  public List<AstSubTree> getList() {
    return list;
  }

  private void toAst(ParserRuleContext ctx, AstSubTree node, int indentation) {
    boolean toBeIgnored = ctx.getChildCount() == 1 && ctx.getChild(0) instanceof ParserRuleContext;
    if (!toBeIgnored) {
      String ruleName = org.iteso.duplicated.antlr.Java8Parser.ruleNames[ctx.getRuleIndex()];
      for (int i = 0; i < indentation; i++) {
        System.out.print("  ");
      }
      if (root != null) {
        node.getParent().addChild(node);
      } else {
        root = node;
      }
      list.add(node);
      System.out.print(ruleName);
      if (ctx.getChild(0) instanceof TerminalNode) {
        System.out.println(" - " + ctx.getChild(0).getText());
      } else {
        System.out.println();
      }
    }
    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree element = ctx.getChild(i);
      if (element instanceof ParserRuleContext) {
        toAst((ParserRuleContext) element,
            new AstSubTree((ParserRuleContext) element, toBeIgnored ? node.getParent() : node),
            indentation + (toBeIgnored ? 0 : 1));
      }
    }
  }
}
