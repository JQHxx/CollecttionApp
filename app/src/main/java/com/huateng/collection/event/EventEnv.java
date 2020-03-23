package com.huateng.collection.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dengzh
 * @description
 * @time 2016-12-09.
 */
public class EventEnv {

    private Map<String,Object> map=new HashMap<>();

    public String event;

    public EventEnv(String event){
        this.event=event;
    }

    public void put(String key,Object o){
        map.put(key,o);
    }

    public Object get(String key){
        return  map.get(key);
    }

    public String obtainEvent(){
        return event;
    }

    public String MarkEvent(String event){
        return event;
    }
}
