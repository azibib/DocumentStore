package random;

public class AVLTree<K extends Comparable<K>,T> {
    private int height = 0;

    private Node root;
    private class Node<K,T>{
        Node left,right;
        T value;
        K key;
        int height;
    }

    public void insert(K key, T v){
        if(v == null){
            throw new IllegalArgumentException();
        }
        root = insert(root, v, key);


        
        
        
    }

    private Node insert(Node node, T value, K key){
        if(node == null){
            node = new Node<>();
            node.value = value;
            node.key = key;
            return node;
        }
        if(node.key == null&& node.value==null){
            node.value = value;
            node.key = key;
            return node;

        }
        if(node.key.equals(key)){
            node.value = value;
            return node;
        }
        if (key.compareTo((K) node.key) < 0) {
            node.left = insert(node.left, value, key); // Insert in left subtree
        } else if (key.compareTo((K) node.key) > 0) {
            node.right = insert(node.right, value, key); // Insert in right subtree
        }
        else{
            return node; // this means that this is the exact value that you are trying to put inside the tree
        }

        updateHeight(node);

        int balance = getbalance(node);
        if (balance > 1 && key.compareTo((K) node.left.key) < 0) {
            return rotateRight(node); // Left-Left case
        }
        if (balance < -1 && key.compareTo((K) node.right.key) > 0) {
            return rotateLeft(node); // Right-Right case
        }
        
        if (balance > 1 && key.compareTo((K) node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
    
        // Right Left Case
        if (balance < -1 && key.compareTo((K) node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
        

    }

    private int getbalance(Node node){
        if(node == null){
            return 0;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        return leftHeight - rightHeight;


    }

    private int height(Node node){
        return (node==null) ? 0 : node.height;
    }

    private int updateHeight(Node node){
    
        if(node ==null){
            return 0;
        }

        return 1+ Math.max(height(node.right), height(node.left));
    }

    private Node rotateRight(Node node){
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        updateHeight(node);
        updateHeight(temp);
        return temp;
        
    }

    private Node rotateLeft(Node node){
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        updateHeight(node);
        updateHeight(temp);
        return temp;
        
    }


    public T get(K key){
        if(key == null){
            throw new IllegalArgumentException();
        }
        Node node=  get(key, root);
        if(node==null){
            return null;
        }
        return (T)node.value;


    }

    

    private Node get(K key, Node node){
        if(node == null){
            return null;
        }
        if(((Comparable<K>) key).compareTo((K) node.key)>0){
            return get(key, node.right);

        }
        if(((Comparable<K>) key).compareTo((K) node.key)<0){
            return get(key, node.left);
        }
        return node;
    }

    
    public T delete(K key){
        root= delete(root, key);
        
        return get(key);
    }

    private Node delete(Node node, K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (node == null) {
            return null;
        }
        if (key.compareTo((K) node.key) < 0) {
            node.left = delete(node.left, key);
        } else if (key.compareTo((K) node.key) > 0) {
            node.right = delete(node.right, key);
        } else {
            // Node with only one child or no child
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
    
            // Node with two children: Get the inorder successor (smallest in the right subtree)
            Node min = node.right;
            while (min.left != null) {
                min = min.left;
            }
    
            // Copy the inorder successor's data to this node and delete the successor
            node.key = min.key;
            node.value = min.value;
            node.right = delete(node.right, (K) min.key);
        }
    
        // Update the height of the current node
        updateHeight(node);
    
        // Rebalance the node if necessary
        return rotations(node);
    }
    
    // Rotations and balancing
    private Node rotations(Node node) {
        int balance = getbalance(node);
    
        // Left-Left Case
        if (balance > 1 && getbalance(node.left) >= 0) {
            return rotateRight(node);
        }
    
        // Left-Right Case
        if (balance > 1 && getbalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
    
        // Right-Right Case
        if (balance < -1 && getbalance(node.right) <= 0) {
            return rotateLeft(node);
        }
    
        // Right-Left Case
        if (balance < -1 && getbalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
    
        return node;
    }



}