/**
 *
 */
package de.itemis.jmo.dodo.validation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import de.itemis.jmo.dodo.io.DataSource;

/**
 * {@link ChecksumValidator} implementation that validates according to the MD5 algorithm.
 *
 */
public class Md5ChecksumValidator implements ChecksumValidator {

    private static final String ALGORITHM_NAME = "MD5";
    private final byte[] expectedChecksum;

    /**
     * Create a new instance.
     *
     * @param expectedChecksum
     */
    public Md5ChecksumValidator(byte[] expectedChecksum) {
        this.expectedChecksum = expectedChecksum;
    }

    @Override
    public boolean verify(DataSource actualData) {
        MessageDigest digest = getDigest();

        byte[] dataBuf = null;
        while ((dataBuf = actualData.read()).length != 0) {
            digest.update(dataBuf);
        }

        return Arrays.equals(digest.digest(), expectedChecksum);
    }

    private MessageDigest getDigest() {
        MessageDigest result = null;
        try {
            result = MessageDigest.getInstance(ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Provisioning error: Encountered unknown digest algorithm '" + ALGORITHM_NAME + "'", e);
        }
        return result;
    }
}
