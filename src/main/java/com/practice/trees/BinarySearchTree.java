package com.practice.trees;

public class BinarySearchTree <T extends Comparable<? super T>>
{
    private TreeNode<T> root;

    public TreeNode<T> getRoot() {
        return root;
    }

    public BinarySearchTree() {
        this.root = null;
    }
    public BinarySearchTree(TreeNode<T> root) {
        this.root = root;
    }

    public void insert(T value) {
        root = insertRecursive(root, value);
    }

    private TreeNode<T> insertRecursive(TreeNode<T> node, T data) {
        if (node == null) {
            return new TreeNode<>(data);
        }

        int compare = node.getData().compareTo(data);
        if (compare < 0) {
            node.setLeft(insertRecursive(node.getLeft(), data));
        }
        if (compare > 0) {
            node.setRight(insertRecursive(node.getRight(), data));
        }
        return node;
    }

    public boolean contains(T value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(TreeNode<T> node, T data) {
        if (node == null) {
            return false;
        }
        if(data.equals(node.getData())) {
            return true;
        }
        int compare = node.getData().compareTo(data);
        return compare < 0 ? searchRecursive(node.getLeft(), data) : searchRecursive(node.getRight(), data);

    }


}
