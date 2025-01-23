package random;
import random.Trie;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import random.Document;

public class Main {
    
    public static void main(String[] args) {
        Trie<Integer> tree = new Trie<>();
        DocStore store = new DocStore("random");
        store.setCount(1);
        LoadToDisk disk = new LoadToDisk();

        tree.put("H",1);
        tree.put("He",2);
        tree.put("Hel",3);
        tree.put("Hell",4);
        tree.put("Hello",5);

        
        

        
        
        
        
        Document doc = new Document("doc","this is some text just so this piece of info isnt empty");
        Document doc2 = new Document("doc2","this really really is just so this piece of info isnt empty");
        Document doc3 = new Document("doc3","this is just so this piece of info isnt empty");

        
        try {
            store.setCount(2);
            store.inputDoc(doc);
            store.setDocumentMetaData("doc", "Azi", "Bibbins");
            store.setDocumentMetaData("doc", "Edon", "Bibbins");
            
            //System.out.println(store.searchByPrefix("this"));
            
            store.inputDoc(doc2);
            store.setDocumentMetaData("doc2", "Azi", "Bibbins");
            store.inputDoc(doc3);
            try {
                
                HashMap<String,String> m = new HashMap<>();
                m.put("Azi","Bibbins");
                m.put("Edon","Bibbins");
                //System.out.println(store.deleteAllByKeyword("this"));
                //System.out.println(store.get("doc"));
                
                System.out.println(store.deleteAllByMetadata(m));
                //store.delete("doc");
                
                System.out.println(store.get("doc3"));

                
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            

            
            
            //System.out.println(disk.deserialize(doc3.getName()));
            //disk.deleteFromMemory(doc.getName());
            //System.out.println(store.delete("this"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(tree.get(4));
       

        
        
    }
}