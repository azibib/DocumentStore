package random;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Trie<T>{
    private Node root = new Node<>();
    private LoadToDisk disk = new LoadToDisk();
    private Set<T> allUnderNode = new HashSet<>();

    private class Node<T>{
        Node[] links = new Node[256];
        Set<T> value = new HashSet<>();
    }



    public Set<T> put(String key, T element ){
        Node node = put(key, element, 0, root);

        if(node == null){return null;}
        else{return (Set<T>)node.value;}


    }

    private Node put(String key, T element, int height, Node node){
        if(node == null){
            node = new Node<>();
        }
        if(height == key.length()){
            node.value.add(element);
            return node;
        }
        else{
            char c = key.charAt(height);
            node.links[c] = put(key, element, height +1, node.links[c]);
            return node;
        }
        

    }

    public Set<T> get(String key)throws IOException{
        Node node = get(key, root, 0);
        if(node == null){
            try{
                Document doc = disk.deserialize(key);
                if(doc==null){return null;}
                for(String word : doc.getText().split(" ")){
                    this.put(word, (T)doc);
                }

            }catch(IOException e){
                throw e;
            }
            return null;
        }

        return (Set<T>)node.value;
    }

    public Set<T> searchBykeyword(String key){
        return getAllUnderNode(key);
    }

    private Node get(String key, Node node, int height){
        if(node == null){
            return null;
        }
        if(key.length() == height){return node;}
        else{
            char c = key.charAt(height);
            return get(key,node.links[c], height +1);
        }
    }
    public Set<T> delete(String key, T value) throws IOException{
        Node node = get(key, root, 0);

        Set<T> temp = node.value;
        if(temp.isEmpty()){
            return temp;
        }
        for(T doc : temp){
            if(doc!=null){
                if(doc.equals(value)){
                    node.value.remove(doc);
                    break;
                }

            }
        }
        return temp;
    }


    private Set<T> getAllUnderNode(String key){
        
        allUnderNode.clear();
        Node node = get(key, root, 0);
        loopThroughChildNodes(node);
        return allUnderNode;

        
    }
    

    private void loopThroughChildNodes(Node node){
        if(node ==null){return;}
        if(!node.value.isEmpty()){
            
            allUnderNode.add((T) node.value);
        }
        for(Node n : node.links){
            if(n!=null){
                loopThroughChildNodes(n);
            }
        }
    }
}
