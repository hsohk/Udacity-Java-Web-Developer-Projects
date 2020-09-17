package com.example.demo;

import java.lang.reflect.Field;

public class TestUnits {
    public static void injectObjects(Object target, String fieldName, Object toInject){
        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if(!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target,toInject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
