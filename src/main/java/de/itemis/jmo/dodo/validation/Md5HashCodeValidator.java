/**
 *
 */
package de.itemis.jmo.dodo.validation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.io.DodoDownload;

/**
 * {@link HashCodeValidator} implementation that validates according to the MD5 algorithm.
 *
 */
public class Md5HashCodeValidator implements HashCodeValidator {

    private static final String ALGORITHM_NAME = "MD5";
    private final Supplier<DodoDownload> hashSup;

    /**
     * Create a new instance.
     *
     * @param hashSup - Supplies a download for the expected hash code.
     */
    public Md5HashCodeValidator(Supplier<DodoDownload> hashSup) {
        this.hashSup = hashSup;
    }

    @Override
    public boolean verify(DataSource dataSource) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Provisioning error: Encountered unknown digest algorithm '" + ALGORITHM_NAME + "'", e);
        }

        var download = hashSup.get();
        var expectedHashCodeDataSource = download.getDataSource();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] expectedHashCodeBuf = null;
        while ((expectedHashCodeBuf = expectedHashCodeDataSource.read()).length != 0) {
            try {
                out.write(expectedHashCodeBuf);
            } catch (IOException e) {
                throw new RuntimeException("Encountered unexpected error while writing to ByteArrayOutputStream.", e);
            }
        }
        byte[] expectedHashCode = out.toByteArray();

        byte[] dataBuf = null;
        while ((dataBuf = dataSource.read()).length != 0) {
            digest.update(dataBuf);
        }


        return Arrays.equals(digest.digest(), expectedHashCode);
    }
}
