package lazycode;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class LzArrayTool {
    public static <T> String toString(T[] obj) {
        return Arrays.toString(obj);
    }

    public static <T> String toString(T[][] objArray) {
        List<String> list = new ArrayList<>();
        for (T[] objects : objArray) {
            list.add(Arrays.toString(objects));
        }
        String str = LzString.joinWith("," + LzString.systemLineSeparator(), list.toArray());
        return LzString.join("[", str, "]");
    }

    public static Map<Object, Object> toMap(Object[][] objArray) {
        // new String[][]{{"RED", "#FF0000"}, {"GREEN", "#00FF00"}}  => {RED=#FF0000, GREEN=#00FF00}
        return ArrayUtils.toMap(objArray);
    }

    public static <T> T[] fill(T[] array, T value) {
        Arrays.fill(array, value);
        return array;
    }

    public static <T> T[] subArray(T[] array, int startIndex, int endIndex) {
        // [ startIndex : endIndex ]
        return ArrayUtils.subarray(array, startIndex, endIndex);
    }

    public static <T> T[] reverse(T[] array, int startIndex, int endIndex) {
        // [ startIndex : endIndex ]
        ArrayUtils.reverse(array, startIndex, endIndex);
        return array;
    }

    public static <T extends Comparable<? super T>> T[] sort(T[] array) {
        // 当数据量达到 1000000 以上时使用多线程排序
        if (array.length < 1000000) {
            Arrays.sort(array);
        } else {
            Arrays.parallelSort(array);
        }
        return array;
    }

    public static <T> T[] sort(T[] array, Comparator<? super T> cmp) {
        if (array.length < 1000000) {
            Arrays.sort(array, cmp);
        } else {
            Arrays.parallelSort(array, cmp);
        }
        return array;
    }

    public static <T extends Comparable<? super T>> T[] sort(T[] array, int startIndex, int endIndex) {
        Arrays.sort(array, startIndex, endIndex);
        return array;
    }

    public static <T> T[] sort(T[] array, int startIndex, int endIndex, Comparator<? super T> cmp) {
        Arrays.sort(array, startIndex, endIndex, cmp);
        return array;
    }

    public static <T> T[] swap(T[] array, int offset1, int offset2) {
        ArrayUtils.swap(array, offset1, offset2);
        return array;
    }

    public static <T> T[] shift(T[] array, int midIndex) {
        // new String[]{"a", "b", "c", "d", "e"}  => [d, e, a, b, c]
        ArrayUtils.shift(array, midIndex);
        return array;
    }

    public static <T> int fistIndexOf(T[] array, T value) {
        return ArrayUtils.indexOf(array, value, 0);
    }

    // -1, 0, 1, 2, ...
    public static <T> int fistIndexOf(T[] array, T value, int startIndex) {
        return ArrayUtils.indexOf(array, value, startIndex);
    }

    // -1, 0, 1, 2, ...
    public static <T> int lastIndexOf(T[] array, T value) {
        return ArrayUtils.lastIndexOf(array, value);
    }

    // -1, 0, 1, 2, ...
    public static <T> int lastIndexOf(T[] array, T value, int startIndex) {
        return ArrayUtils.lastIndexOf(array, value, startIndex);
    }

    public static <T> boolean contains(T[] array, T value) {
        return ArrayUtils.contains(array, value);
    }

    public static <T> boolean isEmpty(T[] array) {
        return ArrayUtils.isEmpty(array);
    }

    @SafeVarargs
    public static <T> T[] addAll(T[] array1, T... array2) {
        return ArrayUtils.addAll(array1, array2);
    }

    @SafeVarargs
    public static <T> T[] addAll(T[] array1, T[]... array2) {
        for (T[] ts : array2) {
            array1 = ArrayUtils.addAll(array1, ts);
        }
        return array1;
    }

    public static <T> T[] removeElement(T[] array, T value) {
        return ArrayUtils.removeElement(array, value);
    }

    public static <T> T[] removeElementByIndex(T[] array, int index) {
        return ArrayUtils.remove(array, index);
    }

    public static <T> T[] removeElementAll(T[] array, T value) {
        return ArrayUtils.removeAllOccurences(array, value);
    }

    public static <T> T[] removeElementAllByIndex(T[] array, int... index) {
        return ArrayUtils.removeAll(array, index);
    }

    @SafeVarargs
    public static <T> T[] insert(T[] array, int index, T... values) {
        return ArrayUtils.insert(index, array, values);
    }

    public static <T> T[] shuffle(T[] array) {
        ArrayUtils.shuffle(array);
        return array;
    }
}
