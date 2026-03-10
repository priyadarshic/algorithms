package com.algo.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Test
    void testInsertAndInOrderTraversal() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(20);
        bst.insert(40);
        bst.insert(70);
        bst.insert(60);
        bst.insert(80);

        List<Integer> expected = List.of(20, 30, 40, 50, 60, 70, 80);
        assertEquals(expected, bst.inOrderTraversal(), "In-order traversal should return sorted elements.");
    }

    @Test
    void testSearchExistingValue() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        assertTrue(bst.search(10), "Should find the root.");
        assertTrue(bst.search(5), "Should find a leaf on the left.");
        assertTrue(bst.search(15), "Should find a leaf on the right.");
    }

    @Test
    void testSearchNonExistingValue() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);

        assertFalse(bst.search(100), "Should not find a value that hasn't been inserted.");
        assertFalse(bst.search(7), "Should not find a value that hasn't been inserted.");
    }

    @Test
    void testEmptyTree() {
        assertTrue(bst.inOrderTraversal().isEmpty(), "In-order traversal of an empty tree should be empty.");
        assertFalse(bst.search(10), "Searching in an empty tree should return false.");
    }
}
