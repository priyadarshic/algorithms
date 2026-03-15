package com.practice.stacks;

import java.util.EmptyStackException;

/**
 * An implementation of a Stack using an Array.
 * This class represents a generic Last-In-First-Out (LIFO) stack of objects.
 *
 * @param <T> the type of elements in this stack
 */
public class ArrayStack<T> {

    private final T[] array;
    private int top;
    private final int capacity;

    /**
     * Constructs a new ArrayStack with the specified capacity.
     *
     * @param capacity the maximum number of elements the stack can hold
     * @throws IllegalArgumentException if capacity is less than or equal to 0
     */
    @SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.array = (T[]) new Object[capacity];
        this.top = -1;
    }

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param item the item to be pushed onto this stack.
     * @throws IllegalStateException if the stack is full
     */
    public void push(T item) {
        if (isFull()) {
            throw new IllegalStateException("Stack is full");
        }
        array[++top] = item;
    }

    /**
     * Removes the object at the top of this stack and returns that
     * object as the value of this function.
     *
     * @return The object at the top of this stack.
     * @throws EmptyStackException if this stack is empty.
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T item = array[top];
        array[top] = null; // Help garbage collection
        top--;
        return item;
    }

    /**
     * Looks at the object at the top of this stack without removing it
     * from the stack.
     *
     * @return the object at the top of this stack.
     * @throws EmptyStackException if this stack is empty.
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return array[top];
    }

    /**
     * Tests if this stack is empty.
     *
     * @return true if and only if this stack contains no items; false otherwise.
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Tests if this stack is full.
     *
     * @return true if and only if this stack capacity has been reached; false otherwise.
     */
    public boolean isFull() {
        return top == capacity - 1;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return the number of elements in the stack.
     */
    public int size() {
        return top + 1;
    }
}
