package util;

import console.Log;
import java.util.*;

/**
 * Class for handling of lists. E.g. making deep copies of lists. The objects in
 * the list to copy need a copy constructor.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 * @param <T> the type of the objects in the array that shall be operated on
 */
public class ListUtil<T> {

    public List<T> makeDeepCopy(List<T> list) {
        List<T> retList = new ArrayList<>();
        if (list.isEmpty()) {
            return retList;
        }
        try {
            for (T element : list) {
                Class c = element.getClass();
                T newElement = (T) c.getDeclaredConstructor(c).newInstance(element);
                retList.add(newElement);
            }
        } catch (Exception e) {
            Log.error(ListUtil.class, e.getMessage());
        }
        return retList;
    }

    public void addAllNoDuplicates(List<T> listToAppendTo, List<T> objectsToAdd) {
        for (T obj : objectsToAdd) {
            if (!listToAppendTo.contains(obj)) {
                listToAppendTo.add(obj);
            }
        }
    }

    public void addNoDuplicates(List<T> listToAppendTo, T objectToAdd) {
        if (!listToAppendTo.contains(objectToAdd)) {
            listToAppendTo.add(objectToAdd);
        }
    }

    public List<T> returnOnlyDuplicates(List<T> list1, List<T> list2) {
        List<T> retList = new ArrayList<>();
        for (T obj : list1) {
            if (list2.contains(obj)) {
                addNoDuplicates(retList, obj);
            }
        }
        for (T obj : list2) {
            if (list1.contains(obj)) {
                addNoDuplicates(retList, obj);
            }
        }
        return retList;
    }

    public List<T> returnOnlyDuplicates(List<T> list) {
        List<T> retList = new ArrayList<>();
        Set<T> unique = new HashSet<>();
        for (T obj : list) {
            if (!unique.add(obj)) {
                if (!retList.contains(obj)) {
                    retList.add(obj);
                }
            }
        }
        return retList;
    }
}
