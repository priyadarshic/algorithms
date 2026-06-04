package com.practice.hashing;

import java.util.Arrays;

public class BloomFilter {
    private static final boolean[] bloomArray = new boolean[256];
    static {
        Arrays.fill(bloomArray, false);
    }

    public static void setBloomArray(String data)
    {
        // Hash algo 1
        int hash = data.hashCode();
        hash = Math.abs(hash) % bloomArray.length;
        bloomArray[hash] = true;


        // Hash algo 2
        hash = MurmurHash3.hash32(data, 0);
        hash = Math.abs(hash) % bloomArray.length;
        bloomArray[hash] = true;

    }

    public static boolean checkExists(String data)
    {
        int hash = data.hashCode();
        hash = Math.abs(hash) % bloomArray.length;
        boolean bloomOne = bloomArray[hash];

        hash = Math.abs(MurmurHash3.hash32(data, 0)) % bloomArray.length;
        boolean bloomTwo = bloomArray[hash];

        return bloomOne && bloomTwo;
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
            else {
                System.out.println(value + " is NOT available");
            }
        }

    }
}
