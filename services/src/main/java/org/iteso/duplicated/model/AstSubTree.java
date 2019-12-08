package org.iteso.duplicated.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;


public class AstSubTree {

  private ParserRuleContext data;
  private String text;
  private int mass;
  private AstSubTree parent;
  private List<AstSubTree> children;
  private boolean clone;

  public AstSubTree(ParserRuleContext data, AstSubTree parent, List<AstSubTree> children) {
    this.data = data;
    this.parent = parent;
    this.children = children;
    String ruleName = org.iteso.duplicated.antlr.Java8Parser.ruleNames[data.getRuleIndex()];
    if (ruleName.equals("literal")) {
      text = data.getChild(0).getText();
    } else {
      text = ruleName;
    }
  }

  public AstSubTree(ParserRuleContext data, AstSubTree parent) {
    this(data, parent, new ArrayList<>());
  }

  public int getMass() {
    if (mass == 0) {
      mass = 1;
      Iterator<AstSubTree> it = this.children.iterator();
      while (it.hasNext()) {
        mass += it.next().getMass();
      }
    }
    return mass;
  }

  public ParserRuleContext getData() {
    return data;
  }

  public AstSubTree getParent() {
    return parent;
  }

  public List<AstSubTree> getChildren() {
    return children;
  }

  public void addChild(AstSubTree child) {
    children.add(child);
  }

  public String getText() {
    return text;
  }

  @Override
  public int hashCode() {
    return text.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof AstSubTree) {
      return text.equals(((AstSubTree) obj).text);
    }
    return false;
  }

  public boolean isClone() {
    return clone;
  }

  public void setClone(boolean clone) {
    this.clone = clone;
  }
}