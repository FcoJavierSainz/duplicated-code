package org.iteso.duplicated.model;

import java.util.ArrayList;
import java.util.List;

public class AstBuckets {

  private static final int BUCKETS_NUMBER = 500;
  private static final int MASS_THRESHOLD = 2;
  private List<AstSubTree> list;
  private AstSubTree root;
  private ArrayList<ArrayList<AstSubTree>> buckets = new ArrayList<>(BUCKETS_NUMBER);

  public AstBuckets(List<AstSubTree> list, AstSubTree root) {
    this.list = list;
    this.root = root;

    for (int i = 0; i < BUCKETS_NUMBER; i++) {
      buckets.add(null);
    }
    buildBuckets();
  }

  private void buildBuckets() {
    list.forEach(astSubTree -> {
      System.out.println(astSubTree.getText() + ": " + astSubTree.getMass());
      if (astSubTree.getMass() <= MASS_THRESHOLD) {
        return;
      }
      int hash = Math.abs(astSubTree.hashCode()) % BUCKETS_NUMBER;
      if (buckets.get(hash) == null) {
        buckets.set(hash, new ArrayList<>());
      }
      buckets.get(hash).add(astSubTree);
    });
  }

  public List<AstSubTree> getList() {
    return list;
  }

  public AstSubTree getRoot() {
    return root;
  }

  public ArrayList<ArrayList<AstSubTree>> getBuckets() {
    return buckets;
  }
}
