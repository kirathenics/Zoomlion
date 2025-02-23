package org.example.zoomlion.Utils;

import java.util.*;

public class ListUtils {
    @SafeVarargs
    public static <T extends Comparable<T>> List<T> mergeAndSort(List<T>... lists) {
        Set<T> resultSet = new HashSet<>();
        for (List<T> list : lists) {
            resultSet.addAll(list);
        }
        List<T> mergedList = new ArrayList<>(resultSet);
        Collections.sort(mergedList);
        return mergedList;
    }
}
