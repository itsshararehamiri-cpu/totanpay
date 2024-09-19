package com.example.totanpay.common;

import java.math.BigInteger;
import java.util.Random;

public class ECC {
    // secp256k1 curve parameters
    public static final BigInteger p = new BigInteger(
            "00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F",
            16);

    public static final BigInteger a = BigInteger.ZERO; // a coefficient
    public static final BigInteger b = new BigInteger("7"); // b coefficient

    public static final BigInteger Gx = new BigInteger(
            "79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798",
            16);

    public static final BigInteger Gy = new BigInteger(
            "483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8",
            16);

    public static BigInteger n = new BigInteger(
            "00FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141",
            16);

    public static BigInteger[] generateKeyPair() {
        BigInteger d = generateRandomPrivateKey();
        ECCPoint Q = multiply(d, new ECCPoint(Gx, Gy));
        return new BigInteger[]{d, Q.X, Q.Y};
    }

    public static BigInteger generateSharedKey(BigInteger privateKey, ECCPoint publicKey) {
        ECCPoint sharedPoint = multiply(privateKey, publicKey);
        return sharedPoint.X;  // The x-coordinate of the shared point is the shared key
    }

    // Encrypts a message using ECC
    public static ECCPoint[] generateKey(ECCPoint publicPoint) {
        BigInteger k = generateRandomPrivateKey();
        ECCPoint R = multiply(k, new ECCPoint(Gx, Gy));  // R = kG
        ECCPoint S = multiply(k, publicPoint); // S = kP
        ECCPoint R1 = multiply(BigInteger.ONE, R);  // R = kG

        // Return the ciphertext point along with the ephemeral public key x-coordinate
        return new ECCPoint[]{R, S};
    }

    public static BigInteger getKeyWithPrivate(ECCPoint cipherPoint, BigInteger privateKeyX) {
        ECCPoint res = multiply(privateKeyX, cipherPoint);
        return res.X;
    }

    // Generates a random private key (k)
    private static BigInteger generateRandomPrivateKey() {
        BigInteger k;
        do {
            k = new BigInteger(n.bitLength(), new Random());
        } while (k.compareTo(n) >= 0 || k.equals(BigInteger.ZERO));

        return k;
    }

    // Performs scalar multiplication (k * P) on the elliptic curve
    private static ECCPoint multiply(BigInteger k, ECCPoint p) {
        ECCPoint r0 = new ECCPoint(BigInteger.ZERO, BigInteger.ZERO);
        ECCPoint r1 = p;

        for (int i = k.bitLength() - 1; i >= 0; i--) {
            if (k.testBit(i)) {
                r0 = add(r0, r1);
                r1 = doublePoint(r1);
            } else {
                r1 = add(r0, r1);
                r0 = doublePoint(r0);
            }
        }
        return r0;
    }

    // Performs point doubling on the elliptic curve
    private static ECCPoint doublePoint(ECCPoint point) {
        BigInteger m = ((point.X.pow(2).multiply(new BigInteger("3")).add(a)).multiply(modInverse(point.Y.multiply(new BigInteger("2")), p))).mod(p);
        if (m.compareTo(BigInteger.ZERO) < 0) m = m.add(p);
        BigInteger x3 = (m.pow(2).subtract(point.X.multiply(new BigInteger("2")))).mod(p);
        if (x3.compareTo(BigInteger.ZERO) < 0) x3 = x3.add(p);
        BigInteger y3 = (m.multiply(point.X.subtract(x3)).subtract(point.Y)).mod(p);
        if (y3.compareTo(BigInteger.ZERO) < 0) y3 = y3.add(p);
        return new ECCPoint(x3, y3);
    }

    // Performs point addition on the elliptic curve
    private static ECCPoint add(ECCPoint p1, ECCPoint p2) {
        BigInteger x1 = p1.X, y1 = p1.Y, x2 = p2.X, y2 = p2.Y;
        if (x1.equals(BigInteger.ZERO) && y1.equals(BigInteger.ZERO)) {
            return new ECCPoint(x2, y2);
        }
        if (x2.equals(BigInteger.ZERO) && y2.equals(BigInteger.ZERO)) {
            return new ECCPoint(x1, y1);
        }
        if (x1.equals(x2) && y1.equals(y2)) {
            return doublePoint(p1);
        }
        if (x1.equals(x2) && !y1.equals(y2)) {
            return new ECCPoint(BigInteger.ZERO, BigInteger.ZERO);
        }

        BigInteger slope = (y2.subtract(y1)).multiply(modInverse(x2.subtract(x1), p)).mod(p);

        BigInteger xResult = (slope.pow(2).subtract(x1).subtract(x2)).mod(p);
        if (xResult.compareTo(BigInteger.ZERO) < 0) xResult = xResult.add(p);
        BigInteger yResult = (slope.multiply(x1.subtract(xResult)).subtract(y1)).mod(p);
        if (yResult.compareTo(BigInteger.ZERO) < 0) yResult = yResult.add(p);
        return new ECCPoint(xResult, yResult);
    }

    // Computes the modular inverse using the Extended Euclidean Algorithm
    private static BigInteger modInverse(BigInteger a, BigInteger mod) {
        return a.modPow(p.subtract(BigInteger.valueOf(2)), p);
    }
}

