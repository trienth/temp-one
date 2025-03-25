package com.tbb.payment.util;

import java.util.Random;

public final class RandomUtils {
    public static String randomResult() {
        Random random = new Random();
        // Generate a random boolean (true or false)
        boolean isSuccess = random.nextBoolean();
        return isSuccess ? "SUCCESS" : "ERROR";
    }
}
