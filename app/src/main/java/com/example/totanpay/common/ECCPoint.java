package com.example.totanpay.common;

import androidx.annotation.NonNull;

import java.math.BigInteger;

public class ECCPoint {
    public BigInteger X;
    public BigInteger Y;

    public ECCPoint(BigInteger x, BigInteger y) {
        X = x;
        Y = y;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + X.toString(16).toUpperCase() + ", " + Y.toString(16).toUpperCase() + ")";
    }
}