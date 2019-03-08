package com.ayaz.instamart.sdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayaz on 16.11.2016.
 */

public class RequestParams {
    private List<Pair<String, String>> pairList = new ArrayList<Pair<String, String>>();

    public void add(String key, String value){
        pairList.add(new Pair<String, String>(key, value));
    }

    public void remove(int location){
        pairList.remove(location);
    }

    public void clear(){
        pairList.clear();
    }

    public Pair<String, String> get(int location){
        return pairList.get(location);
    }

    public int size(){
        return pairList.size();
    }

    public class Pair<Key, Value> {
        private Key key;
        private Value value;

        Pair(Key k, Value v){
            this.key = k;
            this.value = v;
        }

        public Key getKey(){return key;}
        public Value getValue(){return value;}
        public void setKey(Key k){this.key = k;}
        public void setValue(Value v){this.value = v;}
    }
}
