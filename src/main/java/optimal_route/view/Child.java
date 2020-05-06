package optimal_route.view;

import java.util.ArrayList;
import java.util.List;

public class Child {
    private String type;
    private String info;
    private List<Child> childList;
    boolean processed;
    private Object obj;
    public Child(String info){
        this.info=info;
        this.processed=false;
        childList=new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type=type;
    }
    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public int getNumOfChilds(){
        return childList.size();
    }
    public boolean hasChilds(){
        return !childList.isEmpty();
    }
    public void addChild(Child c){
        childList.add(c);
    }
    public String getInfo() {
        return info;
    }

    public List<Child> getChildList() {
        return childList;
    }
}
