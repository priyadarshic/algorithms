package com.algo.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a Binary Search Tree (BST).
 * 
 * @param <T> Type of data stored in the tree, must be Comparable.
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
    private TreeNode<T> root;

    public void insert(T data) {
        root = insertRecursive(root, data);
    }

    private TreeNode<T> insertRecursive(TreeNode<T> current, T data) {
        if (current == null) {
            return new TreeNode<>(data);
        }

        int cmp = data.compareTo(current.getData());
        if (cmp < 0) {
            current.setLeft(insertRecursive(current.getLeft(), data));
        } else if (cmp > 0) {
            current.setRight(insertRecursive(current.getRight(), data));
        }
        // cmp == 0: Value already exists, we do nothing for standard BST

        return current;
    }

    public boolean search(T data) {
        return searchRecursive(root, data);
    }

    private boolean searchRecursive(TreeNode<T> current, T data) {
        if (current == null) {
            return false;
        }

        if (data.equals(current.getData())) {
            return true;
        }

        int cmp = data.compareTo(current.getData());
        return cmp < 0
                ? searchRecursive(current.getLeft(), data)
                : searchRecursive(current.getRight(), data);
    }

    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(TreeNode<T> node, List<T> result) {
        if (node != null) {
            inOrderRecursive(node.getLeft(), result);
            result.add(node.getData());
            inOrderRecursive(node.getRight(), result);
        }
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    /**
     * Prints the tree structure visually to the console.
     */
    public void printTree() {
        printTreeRecursive(root, "", true);
    }

    private void printTreeRecursive(TreeNode<T> node, String prefix, boolean isTail) {
        if (node == null) return;

        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.getData());

        List<TreeNode<T>> children = new ArrayList<>();
        if (node.getLeft() != null) children.add(node.getLeft());
        if (node.getRight() != null) children.add(node.getRight());

        for (int i = 0; i < children.size(); i++) {
            boolean isLast = (i == children.size() - 1);
            printTreeRecursive(children.get(i), prefix + (isTail ? "    " : "│   "), isLast);
        }
    }
}
