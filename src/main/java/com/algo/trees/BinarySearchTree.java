package com.algo.trees;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a Binary Search Tree (BST).
 * 
 * @param <T> Type of data stored in the tree, must be Comparable.
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
    protected TreeNode<T> root;

    public void insert(T data) {
        root = insertRecursive(root, data);
    }

    protected TreeNode<T> insertRecursive(TreeNode<T> current, T data) {
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
        System.out.println(result);
        return result;
    }

    private void inOrderRecursive(TreeNode<T> node, List<T> result) {
        if (node != null) {
            inOrderRecursive(node.getLeft(), result);
            result.add(node.getData());
            inOrderRecursive(node.getRight(), result);
        }
    }

    public List<T> preOrderTraversal() {
        List<T> result = new ArrayList<>();
        preOrderRecursive(root, result);
        System.out.println(result);
        return result;
    }

    private void preOrderRecursive(TreeNode<T> node, List<T> result) {
        if (node != null) {
            result.add(node.getData());
            preOrderRecursive(node.getLeft(), result);
            preOrderRecursive(node.getRight(), result);
        }
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    /**
     * Prints the tree structure visually to the console in a horizontal, textbook-style format.
     */
    public void printTree() {
        int height = getHeight(root);
        if (height == 0) {
            System.out.println("[Empty Tree]");
            return;
        }

        List<List<String>> levels = new ArrayList<>();
        List<TreeNode<T>> currentLevel = new ArrayList<>();
        List<TreeNode<T>> nextLevel = new ArrayList<>();

        currentLevel.add(root);
        int nn = 1;

        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;
            for (TreeNode<T> n : currentLevel) {
                if (n == null) {
                    line.add(null);
                    nextLevel.add(null);
                    nextLevel.add(null);
                } else {
                    line.add(String.valueOf(n.getData()));
                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                    nextLevel.add(n.getLeft());
                    nextLevel.add(n.getRight());
                }
            }
            levels.add(line);
            currentLevel = new ArrayList<>(nextLevel);
            nextLevel.clear();
        }

        int perpiece = levels.get(levels.size() - 1).size() * (itemWidth() + 2);
        for (int i = 0; i < levels.size(); i++) {
            List<String> line = levels.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else if (line.get(j) != null) {
                            c = '└';
                        }
                    }
                    System.out.print(c);

                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) System.out.print(" ");
                    } else {
                        for (int k = 0; k < hpw; k++) System.out.print(j % 2 == 0 ? " " : "─");
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) System.out.print(j % 2 == 0 ? "─" : " ");
                    }
                }
                System.out.println();
            }

            for (int j = 0; j < line.size(); j++) {
                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                for (int k = 0; k < gap1; k++) System.out.print(" ");
                System.out.print(f);
                for (int k = 0; k < gap2; k++) System.out.print(" ");
            }
            System.out.println();

            perpiece /= 2;
        }
    }

    private int getHeight(TreeNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }

    private int itemWidth() {
        // Assume maximum width of 2 digits for simple BST practice
        return 2;
    }
}
