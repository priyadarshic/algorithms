package com.practice.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Test
    void testInsertAndContains() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);

        assertTrue(bst.contains(50), "Should contain the root");
        assertTrue(bst.contains(30), "Should contain the left child");
        assertTrue(bst.contains(70), "Should contain the right child");
        assertFalse(bst.contains(20), "Should not contain a non-existent value");
    }

    @Test
    void testInsertMultipleAndCheckStructure() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);

        assertTrue(bst.contains(20));
        assertTrue(bst.contains(40));
        assertNotNull(bst.getRoot());
        assertEquals(50, bst.getRoot().getData());
    }
}
