package TreePackage;

/**
 * An implementation of the ADT Binary Node.
 */
class BinaryNode<T> {

    private T data;
    private BinaryNode<T> leftChild;
    private BinaryNode<T> rightChild;
    private BinaryNode<T> parent;   // Parent reference
    private BinaryNode<T> thread;   // Thread reference (for threaded binary tree)

    public BinaryNode() {
        this(null);
    }

    public BinaryNode(T dataPortion) {
        this(dataPortion, null, null);
    }

    // Modified to also initialize parent and thread to null
    public BinaryNode(T dataPortion, BinaryNode<T> newLeftChild,
            BinaryNode<T> newRightChild) {
        data = dataPortion;
        leftChild = newLeftChild;
        rightChild = newRightChild;
        parent = null;
        thread = null;
    }

    // Constructor with parent reference
    public BinaryNode(T dataPortion, BinaryNode<T> newLeftChild,
            BinaryNode<T> newRightChild, BinaryNode<T> newParent) {
        this(dataPortion, newLeftChild, newRightChild);
        parent = newParent;
    }

    // Constructor with parent and thread references
    public BinaryNode(T dataPortion, BinaryNode<T> newLeftChild,
            BinaryNode<T> newRightChild, BinaryNode<T> newParent,
            BinaryNode<T> newThread) {
        this(dataPortion, newLeftChild, newRightChild, newParent);
        thread = newThread;
    }

    // Data

    public T getData() {
        return data;
    }

    public void setData(T newData) {
        data = newData;
    }

    // Left Child

    public BinaryNode<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BinaryNode<T> newLeftChild) {
        leftChild = newLeftChild;
    }

    public boolean hasLeftChild() {
        return leftChild != null;
    }

    // Right Child

    public BinaryNode<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(BinaryNode<T> newRightChild) {
        rightChild = newRightChild;
    }

    public boolean hasRightChild() {
        return rightChild != null;
    }

    // Parent

    public BinaryNode<T> getParent() {
        return parent;
    }

    public void setParent(BinaryNode<T> newParent) {
        parent = newParent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    // Thread

    public BinaryNode<T> getThread() {
        return thread;
    }

    public void setThread(BinaryNode<T> newThread) {
        thread = newThread;
    }

    public boolean hasThread() {
        return thread != null;
    }

    // Tree utilities

    public boolean isLeaf() {
        return (leftChild == null) && (rightChild == null);
    }

    public int getHeight() {
        return getHeight(this);
    }

    private int getHeight(BinaryNode<T> node) {
        int height = 0;
        if (node != null) {
            height = 1 + Math.max(getHeight(node.getLeftChild()),
                    getHeight(node.getRightChild()));
        }
        return height;
    }

    public int getNumberOfNodes() {
        int leftNumber = 0;
        int rightNumber = 0;

        if (leftChild != null) {
            leftNumber = leftChild.getNumberOfNodes();
        }
        if (rightChild != null) {
            rightNumber = rightChild.getNumberOfNodes();
        }

        return 1 + leftNumber + rightNumber;
    }

    // Copy (original — no parent links)

    public BinaryNode<T> copy() {
        BinaryNode<T> newRoot = new BinaryNode<>(data);
        if (leftChild != null) {
            newRoot.setLeftChild(leftChild.copy());
        }
        if (rightChild != null) {
            newRoot.setRightChild(rightChild.copy());
        }
        return newRoot;
    }

    // Copy with parent reference

    /**
     * Copies the subtree rooted at this node, wiring each copied node's
     * parent pointer back to its copied parent.
     *
     * @param parentNode The parent of the new root node (null if copying the
     *                   whole tree from the root).
     * @return The root of the copied subtree, with parent links set.
     */
    public BinaryNode<T> copy(BinaryNode<T> parentNode) {
        BinaryNode<T> newRoot = new BinaryNode<>(data, null, null, parentNode);

        if (leftChild != null) {
            newRoot.setLeftChild(leftChild.copy(newRoot));
        }
        if (rightChild != null) {
            newRoot.setRightChild(rightChild.copy(newRoot));
        }

        return newRoot;
    }

} // end BinaryNode