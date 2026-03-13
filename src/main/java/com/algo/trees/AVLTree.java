package com.algo.trees;

/**
 * AVL Tree implementation.
 * 
 * <p>An AVL tree is a self-balancing binary search tree (BST) where the height 
 * difference between the left and right subtrees (the balance factor) of any node 
 * is at most 1. This ensures that the tree remains balanced, providing O(log n) 
 * time complexity for search, insertion, and deletion operations.</p>
 * 
 * <p>Self-balancing is achieved through <b>Rotation</b> operations triggered 
 * when the balance factor violates the AVL property after an insertion.</p>
 * 
 * @param <T> Type of data stored in the tree, must be Comparable.
 */
public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {

    /**
     * Overrides the insertion logic to include self-balancing via rotations.
     * 
     * @param node The current node being visited.
     * @param data The data to be inserted into the tree.
     * @return The new root of the subtree after potential rotations.
     */
    @Override
    protected TreeNode<T> insertRecursive(TreeNode<T> node, T data) {
        // 1. Standard BST insertion logic
        if (node == null) {
            return new TreeNode<>(data);
        }

        int cmp = data.compareTo(node.getData());
        if (cmp < 0) {
            node.setLeft(insertRecursive(node.getLeft(), data));
        } else if (cmp > 0) {
            node.setRight(insertRecursive(node.getRight(), data));
        } else {
            // AVL trees typically do not allow duplicate keys
            return node;
        }

        // 2. Update height of this ancestor node after insertion
        updateHeight(node);

        // 3. Calculate balance factor to check for imbalance
        int balance = getBalance(node);

        /* 
         * 4. If the node is unbalanced, perform one of the four rotation cases:
         * 
         * Case 1: Left Left (LL) - Node balance factor > 1 and data < left child's data
         * Case 2: Right Right (RR) - Node balance factor < -1 and data > right child's data
         * Case 3: Left Right (LR) - Node balance factor > 1 and data > left child's data
         * Case 4: Right Left (RL) - Node balance factor < -1 and data < right child's data
         */

        // Left Left Case
        if (balance > 1 && data.compareTo(node.getLeft().getData()) < 0) {
            return rotateRight(node);
        }

        // Right Right Case
        if (balance < -1 && data.compareTo(node.getRight().getData()) > 0) {
            return rotateLeft(node);
        }

        // Left Right Case (Double Rotation)
        if (balance > 1 && data.compareTo(node.getLeft().getData()) > 0) {
            node.setLeft(rotateLeft(node.getLeft())); // Convert to LL case
            return rotateRight(node);
        }

        // Right Left Case (Double Rotation)
        if (balance < -1 && data.compareTo(node.getRight().getData()) < 0) {
            node.setRight(rotateRight(node.getRight())); // Convert to RR case
            return rotateLeft(node);
        }

        // Return the (unchanged) node if it's already balanced
        return node;
    }

    /**
     * Retrieves the height of a given node safely.
     * 
     * @param n The node whose height is requested.
     * @return 0 if the node is null, otherwise its stored height.
     */
    private int height(TreeNode<T> n) {
        return (n == null) ? 0 : n.getHeight();
    }

    /**
     * Updates the height of a node based on the maximum height of its subtrees.
     * 
     * @param n The node to update.
     */
    private void updateHeight(TreeNode<T> n) {
        if (n != null) {
            n.setHeight(1 + Math.max(height(n.getLeft()), height(n.getRight())));
        }
    }

    /**
     * Calculates the balance factor (height diff) of a node.
     * 
     * @param n The node to check.
     * @return Difference between left and right subtree heights.
     */
    private int getBalance(TreeNode<T> n) {
        return (n == null) ? 0 : height(n.getLeft()) - height(n.getRight());
    }

    /**
     * Performs a right rotation around the specified node.
     * <pre>
     *      y           x
     *     / \         / \
     *    x   T3  ->  T1  y
     *   / \             / \
     *  T1  T2          T2  T3
     * </pre>
     * 
     * @param y The node to rotate.
     * @return The new root of the rotated subtree.
     */
    private TreeNode<T> rotateRight(TreeNode<T> y) {
        TreeNode<T> x = y.getLeft();
        TreeNode<T> T2 = x.getRight();

        // Perform rotation
        x.setRight(y);
        y.setLeft(T2);

        // Update heights for affected nodes
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    /**
     * Performs a left rotation around the specified node.
     * <pre>
     *    x               y
     *   / \             / \
     *  T1  y     ->    x   T3
     *     / \         / \
     *    T2  T3      T1  T2
     * </pre>
     * 
     * @param x The node to rotate.
     * @return The new root of the rotated subtree.
     */
    private TreeNode<T> rotateLeft(TreeNode<T> x) {
        TreeNode<T> y = x.getRight();
        TreeNode<T> T2 = y.getLeft();

        // Perform rotation
        y.setLeft(x);
        x.setRight(T2);

        // Update heights for affected nodes
        updateHeight(x);
        updateHeight(y);

        return y;
    }

    /**
     * Main method to demonstrate AVL Tree balancing.
     */
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();

        System.out.println("Inserting elements: 10, 20, 30, 40, 50, 25");
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        //tree.insert(25);

        System.out.println("\nPreorder traversal (Root, Left, Right):");
        tree.preOrderTraversal();
        
        System.out.println("\nTree Structure Visualization:");
        tree.printTree();
    }
}
