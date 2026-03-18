# Internal Structure of LinkedHashMap

`LinkedHashMap` is a hash table and linked list implementation of the `Map` interface, with predictable iteration order.

## 1. Inheritance and Data Structure

`LinkedHashMap` extends `HashMap`. While `HashMap` provides the core hashing logic and bucket-based storage, `LinkedHashMap` adds a **doubly-linked list** that runs through all of its entries.

### The Entry Class
Internally, it uses a modified entry structure:
```java
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before, after;
    Entry(int hash, K key, V value, Node<K,V> next) {
        super(hash, key, value, next);
    }
}
```
- **`before` and `after`**: These pointers maintain the doubly-linked list, defining the iteration order.
- **`next`**: Inherited from `HashMap.Node`, this pointer is used for collision handling within a bucket (singly-linked list).

## 2. Ordering Modes

`LinkedHashMap` supports two types of ordering, determined by a boolean flag `accessOrder`:

### Insertion Order (Default)
- New entries are appended to the end of the doubly-linked list.
- Re-inserting a key (using `put`) does not change its position in the list.

### Access Order
- Enabled by using the constructor: `public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder)`.
- When an entry is accessed (via `get()`, `put()`, `getOrDefault()`, etc.), it is moved to the **end** of the list.
- This makes the head of the list the **Least Recently Used (LRU)** entry.

## 3. How Order is Maintained

`LinkedHashMap` overrides certain hooks in `HashMap`:
- **`afterNodeInsertion(boolean evict)`**: Called after a new node is inserted. It can be used to remove the eldest entry (useful for LRU caches).
- **`afterNodeAccess(Node<K,V> e)`**: Called when an existing node is accessed. If `accessOrder` is true, it moves the node `e` to the end of the list.
- **`afterNodeRemoval(Node<K,V> e)`**: Called when a node is removed. It updates the `before` and `after` pointers of adjacent nodes to maintain the linked list.

## 4. Performance Characteristics

| Operation | HashMap | LinkedHashMap |
| :--- | :--- | :--- |
| **Put/Get/Remove** | O(1) average | O(1) average (slightly slower due to pointer updates) |
| **Iteration** | O(Capacity + Size) | O(Size) - Iterates through the linked list |
| **Space** | Less | More (2 extra pointers per entry) |

## 5. Use Case: LRU Cache

`LinkedHashMap` is the foundation for building an LRU (Least Recently Used) cache. By overriding `removeEldestEntry(Map.Entry eldest)`, you can automatically remove old items when the map reaches a certain size.

```java
protected boolean removeEldestEntry(Map.Entry eldest) {
    return size() > MAX_ENTRIES;
}
```
