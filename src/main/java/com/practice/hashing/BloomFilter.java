package com.practice.hashing;

import java.util.Arrays;

public class BloomFilter {
    private static final boolean[] bloomArray = new boolean[256];
    static {
        Arrays.fill(bloomArray, false);
    }

    private static int bloomAlgo1(String data)
    {
        // Hash algo 1
        int hash = data.hashCode();
        hash = Math.abs(hash) % bloomArray.length;
        return hash;

    }

    private static int bloomAlgo2(String data)
    {
        // Hash algo 2
        int hash = MurmurHash3.hash32(data, 0);
        hash = Math.abs(hash) % bloomArray.length;
        return hash;

    }

    public static void setBloomArray(String data)
    {
        bloomArray[bloomAlgo1(data)] = true;

        bloomArray[bloomAlgo2(data)] = true;
    }


    public static boolean checkExists(String data)
    {
        return bloomArray[ bloomAlgo1(data)] && bloomArray[ bloomAlgo2(data)];
    }

    public static void main(String[] args) {

        String[] entryArr = {"ID-One", "ID-Two", "ID-TWO"};
        String[] checkArr = {"ID-One", "ID-Two", "ID-TWO", "ID-Four", "ID-Five", "ID-Six", "ID-Seven"};


        for (String value : entryArr) {
            setBloomArray(value);
        }


        for (String value : checkArr) {
            if (!checkExists(value))
            {
                System.out.println(value + " is available");
            }
            else
            {
                System.out.println(value + " is NOT available");
            }
        }

    }
}
