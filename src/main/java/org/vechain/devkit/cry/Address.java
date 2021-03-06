package org.vechain.devkit.cry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;

/**
 * Address class contains convenient functions to deal with the conversion of
 * addresses.
 */
public class Address {
    /**
     * Check if the public key is 65 bytes, and starts with 4.
     * 
     * @param input
     * @return
     */
    public static boolean isUncompressedPublicKey(byte[] input) {
        if (input.length != 65) {
            throw new IllegalArgumentException("Requires 65 bytes!");
        }

        if (input[0] != 4) {
            throw new IllegalArgumentException("First byte should be 4.");
        }

        return true;
    }

    /**
     * Convert an uncompressed public key to address bytes. (20 bytes)
     * 
     * @param input The uncompressed public key, 65 bytes.
     * @return 20 bytes.
     */
    public static byte[] publicKeyToAddressBytes(byte[] input) {
        isUncompressedPublicKey(input);
        // Get rid of 0x04 (first byte)
        byte[] slice = Utils.remove0x04(input);
        // Hash the slice and get 32 bytes of result.
        byte[] h = Keccak.keccak256(slice);
        // Get the last 20 bytes from the 32 bytes.
        return Arrays.copyOfRange(h, 12, h.length);
    }

    /**
     * Convert a public key to address, in string format.
     * 
     * @param input The uncompressed public key, 65 bytes.
     * @return "0x..." style address string. (42 chars including 0x).
     */
    public static String publicKeyToAddressString(byte[] input) {
        byte[] b = publicKeyToAddressBytes(input);
        return Utils.prepend0x(Utils.bytesToHex(b));
    }

    /**
     * If the input is a roughly correct address.
     * 
     * @param input string of an address.
     * @return true/false.
     */
    public static boolean isAddress(String input) {
        return input.matches("(?i)^0x[0-9a-f]{40}$");
    }

    /**
     * Convert an address string to lower case address string.
     * 
     * @param input address string.
     * @return lower case address string.
     */
    public static String toLowerCaseAddress(String input) {
        if (!isAddress(input)) {
            throw new IllegalArgumentException("address is not valid.");
        }

        return input.toLowerCase();
    }

    /**
     * Convert the address into a checksumed address.
     * 
     * @param input the address string.
     * @return the checksumed address string.
     */
    public static String toChecksumAddress(String input) {
        if (!isAddress(input)) {
            throw new IllegalArgumentException("address illegal.");
        }

        String body = Utils.remove0x(input).toLowerCase();

        byte[] h = Keccak.keccak256(body.getBytes(Charsets.US_ASCII));
        String hash = Utils.bytesToHex(h);

        List<String> parts = new ArrayList<String>();
        parts.add("0x");

        for (int i = 0; i < body.length(); i++) { // loop over body.
            if (Integer.parseInt(hash.substring(i, i + 1), 16) >= 8) {
                parts.add(body.substring(i, i + 1).toUpperCase());
            } else {
                parts.add(body.substring(i, i + 1));
            }
        }

        Joiner j = Joiner.on("");
        return j.join(parts);
    }
}
