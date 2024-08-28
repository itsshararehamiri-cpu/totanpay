package com.example.totanpay.data.repository.datasource.rki;

import java.math.BigInteger;



public class ECCOperations {
    public static String compressPoint(ECCPoint point) {
        // Determine if y is even or odd
        int flag = point.Y.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO) ? 0x02 : 0x03;

        // Convert x to a hexadecimal string and prepend flag
        return String.format("%02X", flag) + String.format("%064X", point.X);
    }

    public static ECCPoint decompressPoint(String compressedPoint) {
        int flag = Integer.parseInt(compressedPoint.substring(0, 2), 16);
        BigInteger x = new BigInteger(compressedPoint.substring(2), 16);

        // Calculate y^2 = x^3 + 7 (secp256k1)
        BigInteger ySquared = x.pow(3).add(ECC.b).mod(ECC.p);
        BigInteger y = modSqrt(ySquared, ECC.p);

        // Adjust y based on the flag
        if (((flag == 0x02) && (y.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) != 0)) || ((flag == 0x03) && (y.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0))) {
            y = ECC.p.subtract(y);  // y must be even for 0x02, odd for 0x03
        }

        return new ECCPoint(x, y);
    }

    // Simplified modular square root based on p mod 4 = 3
    public static BigInteger modSqrt(BigInteger a, BigInteger p) {
        return a.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4)), p);
    }
}
