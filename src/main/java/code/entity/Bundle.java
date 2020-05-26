package code.entity;

import java.util.HashMap;
import java.util.Map;

public class Bundle {
    private Map<String, Object> bundles = new HashMap<>();

    public Object getBundle(String trackName) {
        Object result = bundles.get(trackName);
        bundles.remove(trackName);
        return result;
    }

    public void setBundle(String trackName, Object bundle) {
        bundles.put(trackName, bundle);
    }

    public boolean hasBundle(String trackName){
        return bundles.containsKey(trackName);
    }

}
