package com.github.hackerwin7.libjava.exec;

import java.io.IOException;
import java.util.Random;

public class SIMDExec {
    public static void main(String[] args) throws Exception {
        SIMDExec exec = new SIMDExec();
        exec.test();
    }

    public void test() throws IOException {
        Random rand = new Random();
        long[] tmp = new long[14];
        long[] longs = new long[64];
        for (int i = 0; i < 14; i++) {
            tmp[i] = rand.nextLong();
        }
        for (int i = 0; i < 64; i++) {
            longs[i] = rand.nextLong();
        }
        decode7(tmp, longs);
    }

    private static final long MASK8_1 = mask8(1);
    private static final long MASK8_7 = mask8(7);

    private static long expandMask32(long mask32) {
        return mask32 | (mask32 << 32);
    }

    private static long expandMask16(long mask16) {
        return expandMask32(mask16 | (mask16 << 16));
    }

    private static long expandMask8(long mask8) {
        return expandMask16(mask8 | (mask8 << 8));
    }

    private static long mask8(int bitsPerValue) {
        return expandMask8((1L << bitsPerValue) - 1);
    }

    /**
     * The pattern that this shiftLongs method applies is recognized by the C2
     * compiler, which generates SIMD instructions for it in order to shift
     * multiple longs at once.
     */
    private static void shiftLongs(long[] a, int count, long[] b, int bi, int shift, long mask) {
        for (int i = 0; i < count; ++i) {
            b[bi+i] = (a[i] >>> shift) & mask;
        }
    }

    private static void decode7(long[] tmp, long[] longs) throws IOException {
        shiftLongs(tmp, 14, longs, 0, 1, MASK8_7);
        shiftLongs(tmp, 14, tmp, 0, 0, MASK8_1);
        for (int iter = 0, tmpIdx = 0, longsIdx = 14; iter < 2; ++iter, tmpIdx += 7, longsIdx += 1) {
            long l0 = tmp[tmpIdx+0] << 6;
            l0 |= tmp[tmpIdx+1] << 5;
            l0 |= tmp[tmpIdx+2] << 4;
            l0 |= tmp[tmpIdx+3] << 3;
            l0 |= tmp[tmpIdx+4] << 2;
            l0 |= tmp[tmpIdx+5] << 1;
            l0 |= tmp[tmpIdx+6] << 0;
            longs[longsIdx+0] = l0;
        }
    }
}
