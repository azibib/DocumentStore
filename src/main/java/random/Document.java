package random;

import java.util.HashMap;




public class Document implements Comparable{
    private HashMap<String,Integer> wordcount = new HashMap<>();
    private String text = null;
    private long lastUse;
    private String name;
    private HashMap<String, String> metadata = new HashMap<>();
   

    


    public Document(String name, String text){
        
        
        if(text ==null|| text.isEmpty()){
            throw new IllegalArgumentException("The Text for the intended Document can be neither Null or Empty");
        }
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("The Name for the intended Document can be neither Null or Empty");
        }
        this.text = text;
        this.name = name;
        wordCount();
        setLastUse(System.nanoTime());


    }
    

    public void setMetadata(String key, String value){
        if(key==null){
            throw new IllegalArgumentException();

        }
        metadata.put(key, value);
    }
    public String getMetadata(String key){
        if(key ==null){throw new IllegalArgumentException();}
        return metadata.get(key);
    }
    public HashMap<String,String> getAllMetadata(){
        return metadata;
    }




    public String getText(){
        return text;
    }
    public void updateText(String text){
        this.text = text;
        wordcount = new HashMap<>();
        wordCount();
    }

    private void wordCount(){
        String[] words = text.split(" ");
        for(String word : words){
            if(wordcount.get(word)==null){
                wordcount.put(word, 0);
            }
            wordcount.put(word, wordcount.get(word)+1);
        }
    }

    public void setLastUse(long nano){
        lastUse = nano;
    }

    public long getLastUse(){
        return lastUse;
    }

    public Integer getWordCound(String key){
        return wordcount.get(key);
    }


    public String getName(){
        return this.name;
    }
    @Override
    public int compareTo(Object o) {
        Document doc;
        if(o==null){
            throw new IllegalArgumentException();
        }
        try{
            doc = (Document)o;
        }catch(ClassCastException e){
            throw new IllegalArgumentException("The comparable object is not a Document");
        }
        if(this.getLastUse()>doc.getLastUse()){
            return 1;
        }
        if(this.getLastUse()<doc.getLastUse()){
            return -1;
        }
        else{return 0;}
    }
    
    
}
