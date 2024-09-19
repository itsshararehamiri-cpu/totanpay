package com.example.totanpay.common;

import java.math.BigInteger;
import java.util.concurrent.*;

public class ECCParallel {
    private final ExecutorService executor;

    public ECCParallel(int numberOfThreads) {
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    public Future<BigInteger[]> generateKeyPairAsync() {
        return executor.submit(ECC::generateKeyPair);
    }

    public Future<ECCPoint> decompressPointAsync(String compressedPoint) {
        return executor.submit(() -> ECCOperations.decompressPoint(compressedPoint));
    }

    // Ensure to shut down the executor when done
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Executor service did not terminate");
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}