/**
 * An exception used to indicate an attempt to access an empty tree.
 * 
 */

package TreePackage;

public final class EmptyTreeException extends RuntimeException {

    public EmptyTreeException(String s) {
        super(s);
    }
    
}
