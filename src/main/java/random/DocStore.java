package random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import com.google.gson.internal.sql.SqlTypesSupport;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

//in this project theres something wrong with the input and set count. if i dont set the count then it only leaves space for one document
//if i do set the count then it behaves oddly. keeping to what the count should be but will choose random docs to store on the disk

public class DocStore {
    private Trie<Document> tree = new Trie<>();
    private Stack<Document> stack = new Stack<>();
    private Set<String> docNames = new HashSet<>();
    private Set<String> diskDocs = new HashSet<>();
    private Trie<String> metadataHolder = new Trie<>();
    private AVLTree<String, Document> BITree = new AVLTree<>();
    private LoadToDisk disk = new LoadToDisk();
    private final String location;
    private int docCount = -1;

    public void setDocumentMetaData(String name, String key, String value){
        if(name ==null || key == null){
            throw new IllegalArgumentException("the name and key must not be null");
        }
        Document d = this.get(name);
        if(d==null){
            throw new IllegalArgumentException("this document Doesnt exists");
        }
        d.setMetadata(key, value);
        if(!d.getAllMetadata().isEmpty()){
            for(String k : d.getAllMetadata().keySet()){
                metadataHolder.put(k, d.getName());
            }
        }

    }

    public DocStore(String location){
        if(location==null||location.isEmpty()){
            this.location = "DocStore";
        }
        else{
            this.location = location+"/DocStore";
        }
        


        


    }



    public Document get(String key){
        if(!docNames.contains(key)){
            return null;
        }
        else{
            Document doc;

            doc = BITree.get(key);
            if(doc == null){
                
               try {
                doc =  disk.deserialize(key);
               
                
                if(doc!=null){
                    inputDoc(doc);
                    diskDocs.remove(doc.getName());
                    disk.deleteFromMemory(key);
                    
                }

            } catch (IOException e) {
                return null;
            }
                
            }
            else{
                disk.deleteFromMemory(key);
                doc = removeDocumentFromStack(key);
                doc.setLastUse(System.nanoTime());
                stack.push(doc);
                
            }
            return doc;
        }
        
    }

    



    public void inputDoc(Document document) throws IOException{
        if(document==null){throw new IllegalArgumentException("There is no information on this Doucment");}
        if(this.get(document.getName())!=null){
            removeDocumentFromStack(document.getName());
        }
        document.setLastUse(System.nanoTime());
        for(String word : document.getText().split(" ")){

            tree.put(word, document);
        }
        if(!document.getAllMetadata().isEmpty()){
            for(String key : document.getAllMetadata().keySet()){
                metadataHolder.put(key, document.getName());
            }
        }
        docNames.add(document.getName());
        BITree.insert(document.getName(), document);
        stack.push(document);
        moveToDisk();
        
        
    }

    

    public Document getLeastUsedDoc(){
        Stack<Document> holder = new Stack<>();
        Document doc;
        if(stack.size()==0){
            return null;
        }
        else{
            while(stack.size()>0){
                holder.push(stack.pop());
            }
            doc = holder.pop();
            while(holder.size()!=0){
                stack.push(holder.pop());
            }

        }
        return doc;
    }

    private Document removeDocumentFromStack(String key){
        Stack<Document> holder = new Stack<>();
        Document doc = null;
        if(stack.size()==0){
            return null;
        }
        else{
            while(stack.size()>0){
                holder.push(stack.pop());
                if(holder.peek().getName().equals(key)){
                    doc = holder.pop();
                    break;
                }
            }
            
            while(holder.size()!=0){
                stack.push(holder.pop());
            }

        }
        return doc;
    }

    private void moveToDisk() throws IOException{
        if(this.docCount!=-1){
            while(stack.size()>docCount){
                Document d = getLeastUsedDoc();
                diskDocs.add(d.getName());
                disk.serialize(d);
                BITree.delete(d.getName());
                for(String word : d.getText().split(" ")){
                    tree.delete(word, d);
                }
            }
            
        }
    }


    
    public void setCount(int count){
        if(count<1){throw new IllegalArgumentException("Cant set count smaller than 1 doc");}
        this.docCount = count;
    }
    //this needs work.... it will delete and say its deleted just wont delete off the disk but when 
    //i attemp to delete from disk specificlly in the main class it deleted from disk so idk 
    public Boolean delete(String doc) throws IOException{
        
        Document d = get(doc);
        if(d==null){
            return false;
        }
        
        disk.deleteFromMemory(doc);
        docNames.remove(d.getName());
        BITree.delete(d.getName());
        for(String word : d.getText().split(" ")){
            tree.delete(word, d);
        }
        for(String key : d.getAllMetadata().keySet()){
            metadataHolder.delete(key, doc);
        }
        
        
        if(this.get(doc)!=null){return false;}
        return true;
        
    }
    public Set<Document> searchByPrefix(String key) throws IOException{

        Set<Document> nonDiskDocs =  tree.searchBykeyword(key);
        Set<Document> documentsOnDisk = new HashSet<>();
        
        for(String doc : diskDocs){
            Document d = disk.deserialize(doc);
            for(String word : d.getText().split(" ")){
                if(word.startsWith(key)||word.equals(key)){
                    documentsOnDisk.add(d);
                }
            }
            disk.serialize(d);
        }
        
        
        return nonDiskDocs;

        
    }
    


    public Set<Document> search(String keyword) throws IOException{
        
        Set<Document> nonDiskDocs =  tree.get(keyword);
        Set<Document> diskDocuments =  new HashSet<>();
        
        for(String doc : diskDocs){
            Document d = disk.deserialize(doc);
            if(d.getWordCound(keyword)!=null){
                diskDocuments.add(d);
                
            }
            disk.serialize(d);

        }
        for(Document d : nonDiskDocs){
            diskDocuments.add(d);
        }
        
        return diskDocuments;
    }

    

    public Set<String> getAllDocumentNames(){
        return this.docNames;
    }


    public Set<Document> searchByMetaData(HashMap<String, String> keyvalues) throws IOException{
        Set<Document> documents = new HashSet<>();

        Set<Document> returner = new HashSet<>();
        for(String key : keyvalues.keySet()){
            for(String name : metadataHolder.get(key)){
                Document doc = this.get(name);
                if(doc!=null){
                    documents.add(doc);
                }
            }
        }

        for(Document doc : documents){
            boolean matches = true;
            for(String key : keyvalues.keySet()){
                if(doc.getMetadata(key)==null){
                    matches=false;
                }
                else if(!doc.getMetadata(key).equals(keyvalues.get(key))){
                    matches = false;
                }
            }
            if(matches){
                returner.add(doc);
            }
        }
        return returner;
    }

    public boolean exists(String key){
        return docNames.contains(key);
    }
    
    public Set<Document> deleteAllByKeyword(String key) throws IOException{
        Set<Document> docs = search(key);
        if(docs.isEmpty()){return docs;}

        
        
        for (Document doc : docs) {
            delete(doc.getName());
            
        }
        return docs;

    }
    //i think this one is done
    public Set<Document> deleteAllByMetadata(HashMap<String,String> key) throws IOException{
        Set<Document> docs = searchByMetaData(key);
        if(docs.isEmpty()){return docs;}

        
        for(Document doc : docs){
            delete(doc.getName());
        }
        return docs;

    }

    public Set<Document> searchByMetadataAndKeyword(HashMap<String,String> keysvalues, String key) throws IOException{
        Set<Document> docs = searchByMetaData(keysvalues);
        Set<Document> searched = search(key);
        if(docs.isEmpty()||searched.isEmpty()){
            if(docs.isEmpty()){return docs;}
            return searched;
        }
        Set<Document> returner = new HashSet<>();
        for(Document d : searched){
            for(Document o : docs){
                if(d!=null&&o!=null){
                    if(d.equals(o)){
                        returner.add(d);
                    }
                }
            }
        }
        return returner;
    }


}
