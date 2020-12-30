package cc.laop.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Auther: pengpeng
 * @Date: create in 2020/8/26 17:16
 * @Description:
 */
public class BitMap {

    private byte[] bitarr = null;
    private int length = 0;
    private static byte[] BIT_VAL = new byte[]{1, 2, 4, 8, 16, 32, 64, -128};


    BitMap() {
        this(Integer.MAX_VALUE);
    }

    BitMap(int length) {
        this.length = length;
        //this.bitarr = new byte[(length / 8) + ((length % 8) > 0 ? 1 : 0)];
        this.bitarr = new byte[(length >> 3) + ((length & 7) > 0 ? 1 : 0)];
    }


    public boolean add(int i) {
        if (i < 0 || i > length) {
            throw new RuntimeException();
        }

        int index = i >> 3;
        int offset = i & 7;

        bitarr[index] = (byte) (bitarr[index] | BIT_VAL[offset]);

        return false;
    }

    public Integer[] toArray() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < bitarr.length; i++) {
            for (int i1 = 0; i1 < BIT_VAL.length; i1++) {
                int ext = bitarr[i] & BIT_VAL[i1];
                if (ext == BIT_VAL[i1]) {
                    int val = (i << 3) + i1;
                    list.add(val);
                }
            }
        }
        return list.toArray(new Integer[list.size()]);
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        int length = 10000000;
        BitMap bitMap = new BitMap(length);
        //int[] intarr = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int val = random.nextInt(length);
            bitMap.add(val);
            //intarr[i] = val;
        }
        System.out.println("------------------");
        //Integer[] arr = bitMap.toArray();
        //for (int j = 0; j < 50; j++) {
        //    System.out.println(arr[j]);
        //}
        System.in.read();
        //System.out.println(intarr[0]);
    }


}
