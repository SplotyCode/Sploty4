package me.david.sploty4.util;

import java.util.List;

public final class ListUtil {

    public static <T> void replace(List<T> list, int index, T obj){
        for(int i = list.size()-1;i<index;i--){
            list.set(i+1, list.get(i));
        }
        list.set(index, obj);
    }
}
