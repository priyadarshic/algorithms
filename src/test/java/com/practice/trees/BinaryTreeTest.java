package com.practice.trees;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinaryTreeTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private BinaryTree tree;
    private TreeNode root;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        tree = new BinaryTree();
        
        // Create sample tree:
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testInOrder() {
        tree.inOrder(root);
        assertEquals("4 2 5 1 3 ", outContent.toString());
    }

    @Test
    void testPreOrder() {
        tree.preOrder(root);
        assertEquals("1 2 4 5 3 ", outContent.toString());
    }

    @Test
    void testPostOrder() {
        tree.postOrder(root);
        assertEquals("4 5 2 3 1 ", outContent.toString());
    }

    @Test
    void testLevelOrder() {
        tree.levelOrder(root);
        assertEquals("1 2 3 4 5 ", outContent.toString());
    }

    @Test
    void testEmptyTree() {
        tree.inOrder(null);
        assertEquals("", outContent.toString());
    }
}
