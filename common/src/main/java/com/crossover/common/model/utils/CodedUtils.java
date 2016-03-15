package com.crossover.common.model.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This utils class offers coded methods
 *
 * @author <a href="mailto:aajn88@gmail.com">Antonio A. Jimenez N.</a>
 */
public final class CodedUtils {

    /** Tag Logs **/
    private static final String TAG_LOG = CodedUtils.class.getName();

    /** Algorithm used to calculate hash **/
    private static final String ALGORITHM = "MD5";

    /** Random Hash Number **/
    private static final int RANDOM_HASH = 0xFF;

    /** Private constructor to avoid instances * */
    private CodedUtils() {}

    /**
     * This method calculates MD5 for a given String
     *
     * @param pureString
     *         String to be converted to MD5 Hash
     *
     * @return Calculated MD5 Hash
     */
    public static String buildMd5(String pureString) {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG_LOG, "Error finding encrypt algorithm", e);
            return null;
        }
        digest.update(pureString.getBytes());

        byte messageDigest[] = digest.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte aMessageDigest : messageDigest) {
            hexString.append(Integer.toHexString(RANDOM_HASH & aMessageDigest));
        }

        return hexString.toString();
    }

}
