# Threaded Binary Search Tree with In-Order Iterator

## Project Overview

This project extends a Binary Search Tree implementation with two enhancements:

1. **Parent References** – Each node maintains a reference to its parent, making upward traversal possible without a stack.
2. **Threading** – Each node that would otherwise have a null right pointer is given a "thread" pointing directly to its in-order successor. This allows efficient in-order traversal without recursion or a stack.

The in-order iterator was rewritten to use these thread links instead of the original stack-based approach, making iteration simpler and more memory efficient.

The **Identifiers** application uses the threaded BST to parse a Java source file, collect possible identifiers (tokens), store them in sorted order, and print them using the in-order iterator.

---

## Files Included

### Core Classes
| File | Description |
|---|---|
| `BinaryNode.java` | Node class with data, left, right, parent, and thread references |
| `BinaryTree.java` | Base binary tree with threaded in-order iterator |
| `BinarySearchTree.java` | BST extending BinaryTree; add() and removeNode() maintain parent and thread references |
| `Identifiers.java` | Application that parses a Java file and prints possible identifiers in sorted order |

### Test Classes
| File | Description |
|---|---|
| `TestBinaryTree.java` | Tests for BinaryTree and BinaryNode |
| `TestBST.java` | Tests for BinarySearchTree including add and remove |

### Input Files
| File | Description |
|---|---|
| `X.java` | Simple test file; expected identifiers: a b c d e ef f g |
| `Small.java` | Short working Java program; identifiers printed in alphabetical order |

---

## How to Compile

From the project root directory, compile all Java files:

```
javac *.java
```

---

## How to Run

### Identifiers Application
```
java Identifiers
```
When prompted, enter the name of a Java file to parse:
```
Enter filename: X.java
```
The program will print all possible identifiers found in the file in alphabetical order.

**Expected output for X.java:**
```
a b c d e ef f g
```

### Running Tests
```
java TestBinaryTree
java TestBST
```
All tests should pass.

---

## Design Notes

### Parent References
- Added a `parent` field to `BinaryNode` along with `getParent()`, `setParent()`, and `hasParent()` methods.
- Anywhere a left or right child is set in `BinaryTree` or `BinarySearchTree`, the parent reference of the child is updated accordingly.
- The `copy()` method was duplicated — the original sets the copied root's parent to `null` (top of tree), while the overloaded version accepts a parent argument for use on all other nodes during copying.

### Threading
- Added a `thread` field to `BinaryNode` along with `getThread()`, `setThread()`, and `hasThread()` methods.
- `linkSubtreeThreadOut()` threads all rightmost nodes in a left subtree back to the subtree root (their in-order successor).
- `getLeftmostInSubtree()` finds the leftmost node in a right subtree so the root can be threaded to it.
- Both `copy()` methods call these helpers to ensure threads are set correctly when a tree is copied.
- Anywhere a child is set in `BinaryTree` or `BinarySearchTree`, thread references are also updated to keep the threading consistent.

### In-Order Iterator
- The original stack-based `InorderIterator` was copied and then modified. The original is commented out.
- The stack (`nodeStack`) was removed entirely.
- A private helper method `goToLeftmost()` moves `currentNode` to the leftmost node from its current position. This is called in the constructor to initialize the iterator at the first in-order node.
- `hasNext()` returns `true` when `currentNode != null`.
- `next()` saves the current node's data, then advances `currentNode`:
  - If the current node **has a thread**, follow the thread directly to the in-order successor.
  - If the current node **has a real right child**, move right once and then call `goToLeftmost()`.
  - Throws `NoSuchElementException` if called when no elements remain.

### Identifiers Application
- Reads a Java source file line by line using `Scanner`.
- Each line is tokenized using `StringTokenizer` with delimiters that include characters marking the end of an identifier: `+`, `-`, `*`, `/`, `;`, `=`, spaces, `#`, `@`, `!`, `{`, `}`, and other common separators.
- Valid tokens are inserted into the BST (duplicates are naturally ignored by BST insertion).
- The in-order iterator is used to print all identifiers in alphabetical order.

---



