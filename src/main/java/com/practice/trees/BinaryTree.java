package com.practice.trees;

import java.util.LinkedList;
import java.util.Queue;

class BinaryTree {

    // 1. In-Order: L → Root → R  →  gives SORTED sequence in BST
    void inOrder(TreeNode root) {
        if (root == null) return;
        inOrder(root.left);                     // recurse LEFT first
        System.out.print(root.val + " ");       // then print
        inOrder(root.right);                    // then recurse RIGHT
    }

    // 2. Pre-Order: Root → L → R  →  copies tree structure
    void preOrder(TreeNode root) {
        if (root == null) return;
        System.out.print(root.val + " ");    // print FIRST
        preOrder(root.left);
        preOrder(root.right);
    }

    // 3. Post-Order: L → R → Root  →  delete or evaluate
    void postOrder(TreeNode root) {
        if (root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.val + " ");    // print LAST
    }

    // 4. Level-Order (BFS) — uses a Queue
    void levelOrder(TreeNode root) {
        if (root == null) return;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.val + " ");
            if (node.left  != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }
}