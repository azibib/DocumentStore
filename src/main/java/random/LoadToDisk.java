package random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.internal.sql.SqlTypesSupport;

public class LoadToDisk {
    private Gson gson = new Gson();
    
    private final String location = "DocStore/";
    File file = new File(location);


    public void serialize(Document doc) throws IOException{
        if(doc==null){throw new IllegalArgumentException();}
        File file = new File(location+doc.getName()+".json");
        file.getParentFile().mkdirs();
        file.createNewFile();
        FileWriter writer = new FileWriter(location+doc.getName()+".json");

        String g = gson.toJson(doc);
        writer.write(g);
        writer.close();
        
        
        


    }




    public Document deserialize(String name) throws IOException{
        Document doc = null;
        
        File file = new File(location+name+".json");
        if(!file.exists()){
            return null;
        }
        
        try (FileReader reader = new FileReader(file)) {
                // Deserialize JSON into Document
            doc = gson.fromJson(reader, Document.class);
        
            
        
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File could not be found: " + file.getAbsolutePath(), e);
        } catch (IOException e) {
            throw e; // Log or handle other IO errors
        }
        if (doc != null) {
            deleteFromMemory(doc.getName());
        }
        
        return doc;
        


    }



    public boolean deleteFromMemory(String name){
        File file = new File(location+name+".json");
        if(!file.exists()){
            return false;
        }
        
        else{
            File f = new File(location + name+".json");
            return f.delete();
            
            
        }
    }

    

    
}
