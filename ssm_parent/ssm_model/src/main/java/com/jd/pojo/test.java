package com.jd.pojo;

import java.util.Arrays;

/**
 * @Auther lxy
 * @Date
 */
public class test {
    public static void main(String[] args) {
        test01();
        test02();
    }

    private static void test02() {
        int i = 3;
        int num = 8;
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        if (i < 0 || i > arr.length) {
            System.out.println("it no exist");
        } else {
            for (int j = arr.length-1; j <=i ; j--) {
                arr[j + 1] = arr[j];
                arr[j] = num;
            }
        }
        System.out.println(Arrays.toString(arr));
    }

    private static void test01() {
        int i = 3;
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        if (i > arr.length  || i < 0 || arr.length == 0) {
            System.out.println("it no exist");
        } else {
            for (int j = i; j < arr.length-1; j++) {
                arr[j] = arr[j + 1];
//                arr.length -= 1;
            }
            arr[arr.length - 1] = 0;
        }
        System.out.println(Arrays.toString(arr));
    }
}
