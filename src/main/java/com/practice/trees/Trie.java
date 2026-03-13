package com.practice.trees;

class Trie {
    private TrieNode root = new TrieNode();

    static class TrieNode {
        TrieNode[] children = new TrieNode[26]; // one slot per letter a-z
        boolean isEndOfWord = false;
    }

    // INSERT — O(L) where L = word length
    public void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';          // map: 'a'→0, 'b'→1, ..., 'z'→25
            if (curr.children[idx] == null)
                curr.children[idx] = new TrieNode();
            curr = curr.children[idx]; // walk down
        }
        curr.isEndOfWord = true;       // mark end
    }

    // SEARCH exact word — O(L)
    public boolean search(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) return false; // path broken
            curr = curr.children[idx];
        }
        return curr.isEndOfWord; // must reach an end marker
    }

    // PREFIX CHECK — O(L): does any word start with this prefix?
    public boolean startsWith(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) return false;
            curr = curr.children[idx];
        }
        return true; // prefix path exists — don't need isEndOfWord!
    }
}