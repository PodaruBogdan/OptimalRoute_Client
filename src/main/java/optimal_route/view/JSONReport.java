package optimal_route.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JSONReport {
    private static void flattenTree(Child root){
        root.setProcessed(true);
        for(Child c:root.getChildList()){
            flattenTree(c);
        }
        if(root.hasChilds()) {
            boolean flag=false;
            for(Child c:root.getChildList()){
                if(!c.isProcessed()){
                    flag=true;
                }
            }
            if(flag==false){
                JSONObject object=new JSONObject();
                JSONArray array=new JSONArray();
                for(Child c:root.getChildList()){
                    array.add(c.getObj());
                }
                object.put(root.getInfo(),array);
                root.setObj(object);
            }
        }else{
            JSONObject obj = new JSONObject();
            obj.put(root.getType(),root.getInfo());
            root.setObj(obj);
        }

    }
    public static void writeLine(String fileName, Child root){
        flattenTree(root);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JSONArray result=new JSONArray();
        for(Child c:root.getChildList()){
            result.add(c.getObj());
        }
        try (FileWriter file = new FileWriter("optimal.json")) {

            JsonElement je = jp.parse(result.toJSONString());
            String prettyJsonString = gson.toJson(je);
            file.append(prettyJsonString);
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
