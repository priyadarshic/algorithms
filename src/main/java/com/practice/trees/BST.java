package com.practice.trees;

class BST {
    TreeNode root;

    // INSERT: navigate left/right, place where null
    TreeNode insert(TreeNode root, int val) {
        if (root == null) return new TreeNode(val);  // found the spot
        if      (val < root.val) root.left  = insert(root.left,  val);
        else if (val > root.val) root.right = insert(root.right, val);
        // val == root.val → duplicate, ignore
        return root;
    }

    // SEARCH: same direction logic as insert
    boolean search(TreeNode root, int val) {
        if (root == null)     return false;  // not found
        if (val == root.val)  return true;   // found!
        if (val < root.val)   return search(root.left,  val);
        else                  return search(root.right, val);
    }

    // FIND MIN: keep going left until no more left child
    int findMin(TreeNode root) {
        while (root.left != null) root = root.left;
        return root.val;
    }

    // DELETE: three cases — leaf, one child, two children
    TreeNode delete(TreeNode root, int val) {
        if (root == null) return null;
        if      (val < root.val) root.left  = delete(root.left,  val);
        else if (val > root.val) root.right = delete(root.right, val);
        else {
            if (root.left == null)  return root.right; // 0 or 1 child
            if (root.right == null) return root.left;
            // 2 children: replace with in-order successor (min of right subtree)
            root.val   = findMin(root.right);
            root.right = delete(root.right, root.val);
        }
        return root;
    }
}