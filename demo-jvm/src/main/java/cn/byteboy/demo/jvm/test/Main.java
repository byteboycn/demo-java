package cn.byteboy.demo.jvm.test;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hongshaochuan
 */
public class Main {

    public static void main(String[] args) {
        List<String> board = new LinkedList<>();
        Collections.fill(board, String.join("", Collections.nCopies(5, ".")));
//        backtrack(borad, row);
    }
}
