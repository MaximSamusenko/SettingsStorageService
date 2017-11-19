import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public final class SettingsJSONHelper {

    public static String Apply(String base, String override) throws JSONException {
        JSONObject baseObject = new JSONObject(base);
        JSONObject overrideObject = new JSONObject(override);

        Apply(baseObject, overrideObject);

        return baseObject.toString();
    }

    private static void Apply(JSONObject base, JSONObject override) throws JSONException {
        for (Iterator it = override.keys();
             it.hasNext();) {
            String childKey = (String)it.next();
            Object child = override.get(childKey);

            if(base.has(childKey)){
                if(child instanceof JSONObject){
                    Apply(base.getJSONObject(childKey), (JSONObject)child);
                } else if(child instanceof JSONArray){
                    JSONArray overrideArr = (JSONArray)child;
                    JSONArray baseArr = base.getJSONArray(childKey);
                    for(int i = 0; i < overrideArr.length(); i ++){
                        baseArr.put(overrideArr.get(i));
                    }
                }else{
                    base.put(childKey, child);
                }
            }else {
                // if there is no such child in first object - adding
                base.put(childKey, child);
            }
        }
    }

    public static String Subtract(String from, String what) throws JSONException {
        JSONObject fromObject = new JSONObject(from);
        JSONObject whatObject = new JSONObject(what);

        String result = "";

        if(!SubstractJSONAndCheckEquality(fromObject, whatObject))
        {
            result = fromObject.toString();
        }

        return result;
    }

    private static boolean SubstractJSONAndCheckEquality(JSONObject from, JSONObject what) throws JSONException {
        boolean result = true;
        List<String> keysToRemove = new LinkedList<String>();
        for (Iterator it = from.keys();
             it.hasNext();) {
            String childKey = (String)it.next();
            if(what.has(childKey)) {
                if(SubstractJSONAndCheckEquality(from.get(childKey), what.get(childKey))) {
                    keysToRemove.add(childKey);
                }else {
                    result = false;
                }
            }
        }
        for (String key: keysToRemove) {
            from.remove(key);
        }
        return result;
    }

    private static boolean SubstractJSONAndCheckEquality(Object from, Object what) throws JSONException {
        boolean result;
        if(from instanceof JSONObject){
            result = SubstractJSONAndCheckEquality((JSONObject) from, (JSONObject)what);
        } else if(from instanceof JSONArray){
            result = SubstractJSONAndCheckEquality((JSONArray) from, (JSONArray) what);
        }else{
            result = from.equals(what);
        }
        return result;
    }

    private static boolean SubstractJSONAndCheckEquality(JSONArray from, JSONArray what) throws JSONException {
        HashSet<String> whatArrItemsHash = new HashSet<String>(what.length());
        JSONArray newFromArray = new JSONArray();
        for (int i = 0; i < what.length(); i ++){
            whatArrItemsHash.add(what.get(i).toString());
        }
        for(int i = 0; i < from.length(); i ++){
            if(!whatArrItemsHash.contains(from.get(i).toString())){
                newFromArray.put(from.get(i));
            }
        }
        from = newFromArray;
        return from.length() == 0;
    }

}
