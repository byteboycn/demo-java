package cn.byteboy.demo.jvm.test;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongshaochuan
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        T t1 = new T(1);
        T t2 = new T(2);

        Stream.of(t2, t1)
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);

    }
}


class T implements Comparable<T> {

    public int sort;

    public T(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(T o) {
        return o.sort - this.sort;
//        return this.sort - o.sort;
    }

    @Override
    public String toString() {
        return "T{" +
                "sort=" + sort +
                '}';
    }
}
