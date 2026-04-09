package TreePackage;

/**
 * An implementation of the ADT Binary Tree.
 */
import java.util.*;

public class BinaryTree<T> implements BinaryTreeInterface<T>
{
    private BinaryNode<T> root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(T rootData) {
        root = new BinaryNode<T>(rootData);
    }

    public BinaryTree(T rootData, BinaryTree<T> leftTree,
            BinaryTree<T> rightTree) {
        privateSetTree(rootData, leftTree, rightTree);
    }

    public void setTree(T rootData) {
        root = new BinaryNode<T>(rootData);
    }

    public void setTree(T rootData, BinaryTreeInterface<T> leftTree,
            BinaryTreeInterface<T> rightTree) {
        privateSetTree(rootData, (BinaryTree<T>) leftTree,
                                 (BinaryTree<T>) rightTree);
    }

    private void privateSetTree(T rootData,
            BinaryTree<T> leftTree, BinaryTree<T> rightTree) {

        root = new BinaryNode<T>(rootData);

        if ((leftTree != null) && !leftTree.isEmpty()) {
            root.setLeftChild(leftTree.root);

            // Set parent of the left subtree root back to this root
            leftTree.root.setParent(root);

            // Thread: the in-order successor of the rightmost node in the
            // left subtree is the root itself.  Walk to that rightmost node
            // and point its thread at root.
            BinaryNode<T> rightmost = leftTree.root;
            while (rightmost.hasRightChild()) {
                rightmost = rightmost.getRightChild();
            }
            rightmost.setThread(root);
        }

        if ((rightTree != null) && !rightTree.isEmpty()) {
            if (rightTree != leftTree) {
                root.setRightChild(rightTree.root);
            } else {
                root.setRightChild(rightTree.root.copy());
            }

            // Set parent of the right subtree root back to this root
            root.getRightChild().setParent(root);

            // Thread out of root: root's in-order successor is the leftmost
            // node of the right subtree.
            BinaryNode<T> leftmost = root.getRightChild();
            while (leftmost.hasLeftChild()) {
                leftmost = leftmost.getLeftChild();
            }
            root.setThread(leftmost);
        }

        if ((leftTree != null) && (this != leftTree)) {
            leftTree.clear();
        }

        if ((rightTree != null) && (this != rightTree)) {
            rightTree.clear();
        }
    }

    // Core accessors

    public T getRootData() {
        if (isEmpty()) {
            throw new EmptyTreeException("Empty tree for operation getRootData");
        }
        return root.getData();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }

    protected void setRootData(T rootData) {
        root.setData(rootData);
    }

    protected void setRootNode(BinaryNode<T> rootNode) {
        root = rootNode;
    }

    protected BinaryNode<T> getRootNode() {
        return root;
    }

    // Tree metric methods

    public int getHeight() {
        if (root == null) return 0;
        return root.getHeight();
    }

    public int getNumberOfNodes() {
        if (root == null) return 0;
        return root.getNumberOfNodes();
    }

    // Traversal methods

    public void inorderTraverse() {
        inorderTraverse(root);
    }

    private void inorderTraverse(BinaryNode<T> node) {
        if (node != null) {
            inorderTraverse(node.getLeftChild());
            System.out.println(node.getData());
            inorderTraverse(node.getRightChild());
        }
    }

    // Additional tree-access methods

    /**
     * Returns the parent of the node containing the given data,
     * or null if the data is not found or is at the root.
     */
    public T getParentData(T data) {
        BinaryNode<T> node = findNode(root, data);
        if (node == null || node.getParent() == null) return null;
        return node.getParent().getData();
    }

    /**
     * Returns true if the tree contains a node with the given data.
     */
    public boolean contains(T data) {
        return findNode(root, data) != null;
    }

    /**
     * Returns the in-order successor data of the node holding the given data
     * using the thread pointer, or null if there is no successor.
     */
    public T getInorderSuccessor(T data) {
        BinaryNode<T> node = findNode(root, data);
        if (node == null) return null;

        // If the node has a right child, the successor is the leftmost
        // node in the right subtree (standard BST rule).
        if (node.hasRightChild()) {
            BinaryNode<T> leftmost = node.getRightChild();
            while (leftmost.hasLeftChild()) {
                leftmost = leftmost.getLeftChild();
            }
            return leftmost.getData();
        }

        // Otherwise use the thread pointer directly.
        if (node.hasThread()) {
            return node.getThread().getData();
        }

        return null; // node is the last in inorder
    }

    /**
     * Copies this tree and returns the new tree's root as a BinaryTree.
     */
    public BinaryTree<T> copy() {
        BinaryTree<T> newTree = new BinaryTree<>();
        if (!isEmpty()) {
            newTree.root = root.copy(null); // copy with parent links
        }
        return newTree;
    }

    /**
     * Prints every node's data together with its parent and thread
     * successor (if any) — useful for debugging parent/thread wiring.
     */
    public void printStructure() {
        printStructure(root);
    }

    private void printStructure(BinaryNode<T> node) {
        if (node == null) return;
        printStructure(node.getLeftChild());

        String parentStr  = node.hasParent() ? node.getParent().getData().toString() : "null";
        String threadStr  = node.hasThread() ? node.getThread().getData().toString() : "null";
        System.out.printf("Node: %-10s  Parent: %-10s  Thread→: %s%n",
                node.getData(), parentStr, threadStr);

        printStructure(node.getRightChild());
    }

    // Private helper

    /** Finds the first node whose data equals the target (pre-order search). */
    private BinaryNode<T> findNode(BinaryNode<T> node, T target) {
        if (node == null) return null;
        if (node.getData().equals(target)) return node;
        BinaryNode<T> found = findNode(node.getLeftChild(), target);
        if (found != null) return found;
        return findNode(node.getRightChild(), target);
    }

    // Iterator support

    private class InorderIterator implements Iterator<T> {

        private Stack<BinaryNode<T>> nodeStack;
        private BinaryNode<T> currentNode;

        public InorderIterator() {
            nodeStack = new Stack<>();
            currentNode = root;
        }

        public boolean hasNext() {
            return !nodeStack.isEmpty() || (currentNode != null);
        }

        public T next() {
            BinaryNode<T> nextNode = null;

            while (currentNode != null) {
                nodeStack.push(currentNode);
                currentNode = currentNode.getLeftChild();
            }

            if (!nodeStack.isEmpty()) {
                nextNode = nodeStack.pop();
                currentNode = nextNode.getRightChild();
            } else {
                throw new NoSuchElementException();
            }

            return nextNode.getData();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<T> getInorderIterator() {
        return new InorderIterator();
    }

    public Iterator<T> getPreorderIterator() {
        throw new RuntimeException("Pre order iterators not yet supported by this class");
    }

    public Iterator<T> getPostorderIterator() {
        throw new RuntimeException("Post order iterators not yet supported by this class");
    }

    public Iterator<T> getLevelOrderIterator() {
        throw new RuntimeException("Level order iterators not yet supported by this class");
    }

} // end BinaryTree