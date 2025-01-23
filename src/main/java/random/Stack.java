package random;

public class Stack<T> {
    private Node current = new Node();
    private int size = 0;




    private class Node{
        Node next;
        T value;
    }



    public void push(T element){
        if(element == null){
            throw new IllegalArgumentException();

        }

        Node node = new Node();
        node.value = (T)element;
        node.next = current;
        current = node;
        size++;
    }

    public T pop(){
        if(size==0){
            throw new IllegalStateException("There are no Documents in Cache Memory");
        }
        Node node = current;
        current = current.next;
        size--;
        return node.value;
    }

    public int size(){
        return size;
    }

    public T peek(){
        return current.value;
    }


}
