package org.iteso.duplicated.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;

public class CloneDetector {

  private ParserRuleContext cstCode1;
  private ParserRuleContext cstCode2;
  private static final double SIMILARITY_THRESHOLD = 0.95;
  private Ast ast;
  private AstBuckets buckets;
  private Ast ast2;
  private AstBuckets buckets2;
  private HashSet<ClonePair> clones;

  public CloneDetector(ParserRuleContext cstCode1, ParserRuleContext cstCode2) {
    this.cstCode1 = cstCode1;
    this.cstCode2 = cstCode2;
    initTrees();
  }

  private void initTrees() {
    ast = new Ast(cstCode1);
    buckets = new AstBuckets(ast.getList(), ast.getRoot());
    ast2 = new Ast(cstCode2);
    buckets2 = new AstBuckets(ast2.getList(), ast2.getRoot());
  }

  public Set<ClonePair> getClones() {
    if (clones == null) {
      clones = new HashSet<>();
      for (int i = 0; i < buckets.getBuckets().size(); i++) {
        detectClonesInBucket(buckets.getBuckets().get(i), buckets2.getBuckets().get(i));
      }
    }
    return clones;
  }

  private void detectClonesInBucket(ArrayList<AstSubTree> leftBuckets,
      ArrayList<AstSubTree> rightBuckets) {
    if (leftBuckets == null || rightBuckets == null) {
      return;
    }
    for (int leftIndex = 0; leftIndex < leftBuckets.size(); leftIndex++) {
      for (int rightIndex = 0; rightIndex < rightBuckets.size(); rightIndex++) {
        if (isParentClone(leftBuckets.get(leftIndex)) || isParentClone(
            rightBuckets.get(rightIndex))) {
          continue;
        }
        double similarity = compareTrees(leftBuckets.get(leftIndex), rightBuckets.get(rightIndex));
        if (similarity > SIMILARITY_THRESHOLD) {
          addClones(leftBuckets.get(leftIndex), rightBuckets.get(rightIndex), similarity);
        }
      }
    }
  }

  private boolean isParentClone(AstSubTree tree) {
    while (tree.getParent() != null) {
      if (tree.getParent().isClone()) {
        return true;
      }
      tree = tree.getParent();
    }
    return false;
  }

  private void addClones(AstSubTree leftTree, AstSubTree rightTree, double similarity) {
    ClonePair pair = new ClonePair(leftTree, rightTree, similarity);
    removeChildren(leftTree);
    removeChildren(rightTree);
    leftTree.setClone(true);
    rightTree.setClone(true);
    clones.add(pair);
  }

  private void removeChildren(AstSubTree tree) {
    for (int i = 0; i < tree.getChildren().size(); i++) {
      AstSubTree child = tree.getChildren().get(i);
      clones.removeIf(pair -> pair.containsTree(child));
      for (int j = 0; j < child.getChildren().size(); j++) {
        removeChildren(child.getChildren().get(j));
      }
    }
  }

  private double compareTrees(AstSubTree left, AstSubTree right) {
    int leftSum = left.getMass();
    int rightSum = right.getMass();

    ArrayList<AstSubTree> leftNodes = new ArrayList<>(leftSum);
    addNodes(left, leftNodes);

    ArrayList<AstSubTree> rightNodes = new ArrayList<>(leftSum);
    addNodes(right, rightNodes);

    intersection(leftNodes, rightNodes);

    //Similarity = 2 x S /  (2 x S + L + R)
    int s = leftSum - leftNodes.size();
    int l = leftSum - s;
    int r = rightSum - s;
    return (2.0 * s) / (2.0 * s + l + r);
  }

  private void intersection(ArrayList<AstSubTree> leftNodes, ArrayList<AstSubTree> rightNodes) {
    for (AstSubTree tree : rightNodes) {
      leftNodes.remove(tree);
    }
  }

  public void addNodes(AstSubTree tree, List<AstSubTree> nodes) {
    nodes.add(tree);
    for (int i = 0; i < tree.getChildren().size(); i++) {
      addNodes(tree.getChildren().get(i), nodes);
    }
  }

  public static class ClonePair {

    private AstSubTree leftPart;
    private AstSubTree rightPart;
    private double similarity;

    public ClonePair(AstSubTree leftPart, AstSubTree rightPart, double similarity) {
      this.leftPart = leftPart;
      this.rightPart = rightPart;
      this.similarity = similarity;
    }

    public AstSubTree getLeftPart() {
      return leftPart;
    }

    public AstSubTree getRightPart() {
      return rightPart;
    }

    public double getSimilarity() {
      return similarity;
    }

    public boolean containsTree(AstSubTree tree) {
      return leftPart.equals(tree) || rightPart.equals(tree);
    }

    @Override
    public int hashCode() {
      return leftPart.hashCode() * 17 * rightPart.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof ClonePair) {
        return ((ClonePair) obj).leftPart.equals(leftPart) || ((ClonePair) obj).rightPart
            .equals(rightPart);
      }
      return false;
    }
  }
}
