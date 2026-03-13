package com.practice.trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {
    private AVLTree avl;
    private AVLTree.Node root;

    @BeforeEach
    void setUp() {
        avl = new AVLTree();
        root = null;
    }

    @Test
    void testLLCase() {
        // Left-Left Case: Single Right Rotation
        //       30
        //      /
        //     20
        //    /
        //   10
        root = avl.insert(root, 30);
        root = avl.insert(root, 20);
        root = avl.insert(root, 10);

        // Should become:
        //     20
        //    /  \
        //   10  30
        assertEquals(20, root.val);
        assertEquals(10, root.left.val);
        assertEquals(30, root.right.val);
        assertEquals(2, root.height);
    }

    @Test
    void testRRCase() {
        // Right-Right Case: Single Left Rotation
        //   10
        //     \
        //      20
        //        \
        //         30
        root = avl.insert(root, 10);
        root = avl.insert(root, 20);
        root = avl.insert(root, 30);

        // Should become:
        //     20
        //    /  \
        //   10  30
        assertEquals(20, root.val);
        assertEquals(10, root.left.val);
        assertEquals(30, root.right.val);
        assertEquals(2, root.height);
    }

    @Test
    void testLRCase() {
        // Left-Right Case: Double Rotation (Left then Right)
        //     30
        //    /
        //   10
        //    \
        //     20
        root = avl.insert(root, 30);
        root = avl.insert(root, 10);
        root = avl.insert(root, 20);

        // Should become:
        //     20
        //    /  \
        //   10  30
        assertEquals(20, root.val);
        assertEquals(10, root.left.val);
        assertEquals(30, root.right.val);
        assertEquals(2, root.height);
    }

    @Test
    void testRLCase() {
        // Right-Left Case: Double Rotation (Right then Left)
        //   10
        //     \
        //      30
        //     /
        //    20
        root = avl.insert(root, 10);
        root = avl.insert(root, 30);
        root = avl.insert(root, 20);

        // Should become:
        //     20
        //    /  \
        //   10  30
        assertEquals(20, root.val);
        assertEquals(10, root.left.val);
        assertEquals(30, root.right.val);
        assertEquals(2, root.height);
    }

    @Test
    void testDuplicateInsertion() {
        root = avl.insert(root, 10);
        root = avl.insert(root, 20);
        root = avl.insert(root, 10); // Duplicate

        assertEquals(10, root.val); // Root should be 10 (first element inserted)
        assertEquals(20, root.right.val);
        
        root = null;
        root = avl.insert(root, 10);
        AVLTree.Node firstRoot = root;
        root = avl.insert(root, 10);
        assertSame(firstRoot, root);
    }

    @Test
    void testComplexInsertion() {
        // 10, 20, 30, 40, 50, 25
        root = avl.insert(root, 10);
        root = avl.insert(root, 20);
        root = avl.insert(root, 30); // RR -> 20 is root
        root = avl.insert(root, 40);
        root = avl.insert(root, 50); // RR -> 40 is right child of 20, but 40 has 30, 50
        root = avl.insert(root, 25); // RL case at some point?
        
        // Let's check root and height
        // After 10, 20, 30: 20(10, 30)
        // After 40: 20(10, 30(null, 40))
        // After 50: 20(10, 40(30, 50))
        // After 25: 
        // 20.right is 40. 25 < 40, so 40.left = insert(30, 25)
        // insert(30, 25) -> 30.left = 25. height of 30 becomes 2. bf(30) is 1.
        // back to 40. height of 40 becomes 3. bf(40) is 2-1 = 1.
        // back to 20. height of 20 becomes 4. bf(20) is 1-3 = -2. 
        // 20.right is 40. 25 < 40. This is RL case at root 20?
        // val(25) < node.right.val(40) -> RL case. 
        // rotateRight(40) -> 30 becomes right child of 20, 30 is root of subtree with 25, 40.
        // Wait, let's verify height and balancing property.
        
        assertTrue(Math.abs(avl.bf(root)) <= 1);
        assertTrue(Math.abs(avl.bf(root.left)) <= 1);
        assertTrue(Math.abs(avl.bf(root.right)) <= 1);
    }
}
