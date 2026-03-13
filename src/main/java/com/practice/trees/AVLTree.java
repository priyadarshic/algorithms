package com.practice.trees;

class AVLTree {
    class Node {
        int val, height;
        Node left, right;
        Node(int val) { this.val = val; height = 1; }
    }

    int height(Node n) { return n == null ? 0 : n.height; }
    int bf(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }
    void updateHeight(Node n) { n.height = 1 + Math.max(height(n.left), height(n.right)); }

    // Right Rotation — fixes LL imbalance
    Node rotateRight(Node y) {
        Node x = y.left;   // x becomes new root
        y.left = x.right;  // y adopts x's right child
        x.right = y;       // y becomes x's right child
        updateHeight(y);   // update y first (now lower)
        updateHeight(x);
        return x;
    }

    // Left Rotation — fixes RR imbalance
    Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    Node insert(Node node, int val) {
        // Step 1: Normal BST insert
        if (node == null) return new Node(val);
        if      (val < node.val) node.left  = insert(node.left,  val);
        else if (val > node.val) node.right = insert(node.right, val);
        else return node; // duplicate

        // Step 2: Update height of this node
        updateHeight(node);

        // Step 3: Check balance and rotate if needed
        int balance = bf(node);

        if (balance > 1  && val < node.left.val)   return rotateRight(node);          // LL
        if (balance < -1 && val > node.right.val)  return rotateLeft(node);           // RR
        if (balance > 1  && val > node.left.val)  { node.left  = rotateLeft(node.left);  return rotateRight(node); } // LR
        if (balance < -1 && val < node.right.val) { node.right = rotateRight(node.right); return rotateLeft(node); }  // RL
        return node;
    }
}