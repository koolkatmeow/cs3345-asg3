# Threaded Binary Search Tree with In-Order Iterator

## Project Overview

This project extends a Binary Search Tree implementation with two enhancements. First, parent references were added so each node maintains a reference to its parent, making upward traversal possible without a stack. Second, threading was added so each node that would otherwise have a null right pointer instead holds a thread pointing directly to its in-order successor, allowing efficient in-order traversal without recursion or a stack.

The in-order iterator was rewritten to use thread links instead of the original stack-based approach, making iteration simpler and more memory efficient.

The Identifiers application uses the threaded BST to parse a Java source file, collect possible identifiers as tokens, store them in sorted order, and print them using the in-order iterator.

---

## Files Included

The core classes are BinaryNode.java, BinaryTree.java, BinarySearchTree.java, and Identifiers.java. The test classes are TestBinaryTree.java and TestBST.java. The input files included are X.java and Small.java.

---

## How to Compile

From the project root directory, compile all Java files:

```
javac *.java
```

---

## How to Run

To run the Identifiers application:

```
java Identifiers
```

When prompted, enter the name of a Java file to parse, for example X.java. The program will print all possible identifiers found in the file in alphabetical order. The expected output for X.java is: a b c d e ef f g.

To run the tests:

```
java TestBinaryTree
java TestBST
```

All tests should pass.

---

## Design Notes

### Parent References

A parent field was added to BinaryNode along with getParent(), setParent(), and hasParent() methods. Anywhere a left or right child is set in BinaryTree or BinarySearchTree, the parent reference of the child is updated accordingly. The copy() method was duplicated so the original sets the copied root's parent to null since it is the top of the tree, while the overloaded version accepts a parent argument used for all other nodes during copying.

### Threading

A thread field was added to BinaryNode along with getThread(), setThread(), and hasThread() methods. linkSubtreeThreadOut() threads all rightmost nodes in a left subtree back to the subtree root, which is their in-order successor. getLeftmostInSubtree() finds the leftmost node in a right subtree so the root can be threaded to it. Both copy() methods call these helpers to ensure threads are set correctly when a tree is copied. Anywhere a child is set in BinaryTree or BinarySearchTree, thread references are also updated to keep threading consistent.

### In-Order Iterator

The original stack-based InorderIterator was copied and modified, and the original is commented out. The stack was removed entirely. A private helper method goToLeftmost() moves currentNode to the leftmost node from its current position and is called in the constructor to initialize the iterator at the first in-order node. hasNext() returns true when currentNode is not null. next() saves the current node's data and then advances currentNode by following the thread if one exists, or by moving right once and calling goToLeftmost() if a real right child exists. It throws NoSuchElementException if called when no elements remain.

### Identifiers Application

The application reads a Java source file line by line using Scanner. Each line is tokenized using StringTokenizer with delimiters that include characters marking the end of an identifier such as +, -, *, /, ;, =, spaces, #, @, !, {, and }. Tokens are inserted into the BST, and duplicates are naturally ignored by BST insertion. The in-order iterator is then used to print all identifiers in alphabetical order.

