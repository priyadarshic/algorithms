# LinkedHashMap Internal Structure Explained

I've completed the research and demonstration of the `LinkedHashMap` internal structure.

## Overview of Changes

### 1. Research Artifact
I've created a detailed research document [research.md](file:///c:/JavaProjects/GraphsTrees/docs/LinkedHashMap/research.md) that explains:
- **Inheritance**: How it extends `HashMap`.
- **Entry Structure**: The doubly-linked list nodes (`before` and `after` pointers).
- **Ordering Modes**: Insertion-order (default) and Access-order.
- **Hook Methods**: `afterNodeAccess`, `afterNodeInsertion`, and `afterNodeRemoval`.

### 2. Demonstration Code
I've implemented [LinkedHashMapDemo.java](file:///c:/JavaProjects/GraphsTrees/src/main/java/com/practice/datastructures/LinkedHashMapDemo.java) to practically show how these ordering modes work.

## Verification Results

I compiled and ran the demo using Maven. The output confirmed the following behaviors:

### Insertion Order
- The map maintained the order: `One, Two, Three`.

### Access Order
- Accessing an element moved it to the end of the list, making it the "Most Recently Used" (MRU) element.
- This is key for implementing LRU caches.

### Command Line Output:
```text
--- Insertion Order (Default) ---
Map elements: {One=1, Two=2, Three=3}

--- Access Order ---
Before access: {Apple=100, Banana=200, Cherry=300}
After accessing 'Apple': {Banana=200, Cherry=300, Apple=100}
After accessing 'Banana': {Cherry=300, Apple=100, Banana=200}

--- LRU Cache behavior ---
After adding 'Date': {Cherry=300, Apple=100, Banana=200, Date=400}
```

The results align perfectly with the theoretical internal structure of `LinkedHashMap`.
