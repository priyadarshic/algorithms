package com.practice.stacks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListStackTest {

    private LinkedListStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new LinkedListStack<>();
    }

    @Test
    void testPushAndPop() {
        stack.push("apple");
        stack.push("banana");
        stack.push("cherry");

        assertEquals(3, stack.size());
        assertEquals("cherry", stack.pop());
        assertEquals("banana", stack.pop());
        assertEquals(1, stack.size());
        assertEquals("apple", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPeek() {
        stack.push("foo");
        stack.push("bar");

        assertEquals("bar", stack.peek());
        assertEquals(2, stack.size()); // Size should remain unchanged
        assertEquals("bar", stack.pop());
    }

    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push("hello");
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPopOnEmptyStackThrowsException() {
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }

    @Test
    void testPeekOnEmptyStackThrowsException() {
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }
}
