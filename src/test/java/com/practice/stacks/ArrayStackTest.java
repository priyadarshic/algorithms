package com.practice.stacks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStackTest {

    private ArrayStack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new ArrayStack<>(5);
    }

    @Test
    void testPushAndPop() {
        stack.push(10);
        stack.push(20);
        stack.push(30);

        assertEquals(3, stack.size());
        assertEquals(30, stack.pop());
        assertEquals(20, stack.pop());
        assertEquals(1, stack.size());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void testPeek() {
        stack.push(100);
        stack.push(200);

        assertEquals(200, stack.peek());
        assertEquals(2, stack.size()); // Size should remain unchanged
        assertEquals(200, stack.pop());
    }

    @Test
    void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push(5);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testIsFull() {
        assertFalse(stack.isFull());
        for (int i = 0; i < 5; i++) {
            stack.push(i);
        }
        assertTrue(stack.isFull());
    }

    @Test
    void testPushOnFullStackThrowsException() {
        for (int i = 0; i < 5; i++) {
            stack.push(i);
        }

        Exception exception = assertThrows(IllegalStateException.class, () -> stack.push(6));
        assertEquals("Stack is full", exception.getMessage());
    }

    @Test
    void testPopOnEmptyStackThrowsException() {
        assertThrows(EmptyStackException.class, () -> stack.pop());
    }

    @Test
    void testPeekOnEmptyStackThrowsException() {
        assertThrows(EmptyStackException.class, () -> stack.peek());
    }

    @Test
    void testInvalidCapacity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new ArrayStack<>(0));
        assertEquals("Capacity must be greater than 0", exception.getMessage());

        assertThrows(IllegalArgumentException.class, () -> new ArrayStack<>(-5));
    }
}
