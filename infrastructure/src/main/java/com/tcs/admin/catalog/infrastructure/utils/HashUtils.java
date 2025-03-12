package com.tcs.admin.catalog.infrastructure.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public final class HashUtils {

    private static final HashFunction CHECKSUM = Hashing.crc32c();

    private HashUtils() {
    }

    public static String checksum(final byte[] content) {
        return CHECKSUM.hashBytes(content).toString();
    }
}
