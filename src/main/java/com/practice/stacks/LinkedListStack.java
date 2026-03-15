package com.practice.stacks;

import java.util.EmptyStackException;

/**
 * An implementation of a Stack using a Linked List.
 * This class represents a generic Last-In-First-Out (LIFO) stack of objects.
 *
 * @param <T> the type of elements in this stack
 */
public class LinkedListStack<T> {

    private static class Node<T> {
        final T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top;
    private int size;

    /**
     * Constructs a new empty LinkedListStack.
     */
    public LinkedListStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Pushes an item onto the top of this stack.
     *
     * @param item the item to be pushed onto this stack.
     */
    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
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
        T item = top.data;
        top = top.next;
        size--;
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
        return top.data;
    }

    /**
     * Tests if this stack is empty.
     *
     * @return true if and only if this stack contains no items; false otherwise.
     */
    public boolean isEmpty() {
        return top == null; // or size == 0
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return the number of elements in the stack.
     */
    public int size() {
        return size;
    }
}
