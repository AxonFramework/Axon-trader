/*
 * Copyright (c) 2010-2012. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.samples.trader.users.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;

public abstract class DigestUtils {

    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * Calculate the SHA1 hash for a given string.
     *
     * @param text the given text to hash to a SHA1
     * @return the SHA1 Hash
     */
    public static String sha1(String text) {
        notNull(text);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return hex(md.digest(text.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "Unable to calculate hash. No SHA1 'hasher' available in this Java implementation",
                    ex
            );
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(
                    "Unable to calculate hash. UTF-8 encoding is not available in this Java implementation",
                    ex
            );
        }
    }

    /**
     * Calculate the MD5 hash for a given string.
     *
     * @param text The string to calculate the hash for
     * @return The hex representation of the hash value
     */
    public static String md5(String text) {
        notNull(text);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(text.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "Unable to calculate hash. No MD5 hasher available in this Java implementation",
                    ex
            );
        }
    }

    /**
     * Converts the byte array to a string containing its Hex representation.
     *
     * @param data the byte array of data
     * @return the hex representation of the String
     */
    public static String hex(byte[] data) {
        notNull(data);
        return hex(data, 0, data.length);
    }

    /**
     * Converts the byte array to a string containing its Hex representation.
     *
     * @param data   the byte array of data
     * @param offset An offset in the array where the encoding should start
     * @param length Indicates how many bytes should be encoded.
     * @return the hex representation of the String
     */
    public static String hex(byte[] data, int offset, int length) {
        notNull(data);
        state(offset >= 0, "The offset must be positive");
        state(offset < data.length, "The offset must be lower than the length of the data");
        state(length >= 0, "The requested length must be positive");
        state(length <= data.length, "The requested length must be equal to or lower than the length of the data");

        StringBuilder buf = new StringBuilder(length * 2);
        for (int i = offset; i < (offset + length); i++) {
            byte b = data[i];
            // look up high nibble char
            buf.append(HEX_CHARS[(b & 0xf0) >>> 4]);
            // look up low nibble char
            buf.append(HEX_CHARS[b & 0x0f]);
        }
        return buf.toString();
    }

    private DigestUtils() {
        // Prevent instantiation as this is a utility class
    }
}
