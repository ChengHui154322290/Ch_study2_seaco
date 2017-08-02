package com.tp.common.vo.supplier;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {随机数生成} <br>
 * Create on : 2014年12月22日 上午11:19:18<br>
 *
 * @author szy
 * @version 0.0.1
 */
public final class RandomGUIDUtil {

    private static final String STR = "-";

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomGUIDUtil.class);

    private static Random myRand;

    private static SecureRandom mySecureRand;

    private static String sId;

    public String valueBeforeMD5 = "";

    public String valueAfterMD5 = "";

    /*
     * Static block to take care of one time secureRandom seed. It takes a few seconds to initialize SecureRandom. You
     * might want to consider removing this static block or replacing it with a "time since first loaded" seed to
     * reduce this time. This block will run only once per JVM instance.
     */
    static {
        mySecureRand = new SecureRandom();
        final long secureInitializer = mySecureRand.nextLong();
        myRand = new Random(secureInitializer);
        try {
            sId = InetAddress.getLocalHost().toString();
        } catch (final UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Default constructor. With no specification of security option, this constructor defaults to lower security, high
     * performance.
     */
    public RandomGUIDUtil() {
        getRandomGUID(false);
    }

    /**
     * Constructor with security option. Setting secure true enables each random number generated to be
     * cryptographically strong. Secure false defaults to the standard Random function seeded with a single
     * cryptographically strong random number.
     */
    public RandomGUIDUtil(final boolean secure) {
        getRandomGUID(secure);
    }

    /**
     * {Method to generate the random GUID}.
     *
     * @param secure
     */
    private void getRandomGUID(final boolean secure) {
        MessageDigest md5 = null;
        final StringBuffer sbValueBeforeMD5 = new StringBuffer();

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
        }

        try {
            final long time = System.currentTimeMillis();
            long rand = 0;

            if (secure) {
                rand = mySecureRand.nextLong();
            } else {
                rand = myRand.nextLong();
            }

            // This StringBuffer can be a long as you need; the MD5
            // hash will always return 128 bits. You can change
            // the seed to include anything you want here.
            // You could even stream a file through the MD5 making
            // the odds of guessing it at least as great as that
            // of guessing the contents of the file!
            sbValueBeforeMD5.append(sId);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));

            valueBeforeMD5 = sbValueBeforeMD5.toString();
            if (md5 != null) {
                md5.update(valueBeforeMD5.getBytes());
            }

            byte[] array = null;
            if (md5 != null) {
                array = md5.digest();
            }
            final StringBuffer sb = new StringBuffer();
            if (array != null) {

                for (int j = 0; j < array.length; ++j) {
                    final int b = array[j] & 0xFF;
                    if (b < 0x10) {
                        sb.append('0');
                    }
                    sb.append(Integer.toHexString(b));
                }
            }

            valueAfterMD5 = sb.toString();

        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Convert to the standard format for GUID (Useful for SQL Server UniqueIdentifiers, etc.) Example:
     * C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
     */
    @Override
    public String toString() {
        final String raw = valueAfterMD5.toUpperCase();
        final StringBuffer sb = new StringBuffer();
        sb.append(raw.substring(0, 8));
        sb.append(STR);
        sb.append(raw.substring(8, 12));
        sb.append(STR);
        sb.append(raw.substring(12, 16));
        sb.append(STR);
        sb.append(raw.substring(16, 20));
        sb.append(STR);
        sb.append(raw.substring(20));

        return sb.toString();
    }

    /**
     * 产生唯一的随机字符串
     *
     * @return String
     */
    public static String generateKey() {
        return new RandomGUIDUtil(true).toString();
    }
}
