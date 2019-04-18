package de.itemis.jmo.dodo.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;

/**
 * A factory that produces {@link ChecksumValidator} instances.
 */
public class ChecksumValidatorFactory implements BiFunction<byte[], String, ChecksumValidator> {

    private static final Logger LOG = LoggerFactory.getLogger(ChecksumValidatorFactory.class);

    /**
     * @param downloadSup - Used to download the checksum source.
     */
    @Override
    public ChecksumValidator apply(byte[] expectedChecksum, String moduleName) {
        ChecksumValidator result = new NopChecksumValidator();

        if ("MD5".equals(moduleName)) {
            result = new Md5ChecksumValidator(expectedChecksum);
        } else if ("SHA-1".equals(moduleName)) {
            result = new Sha1ChecksumValidator();
        } else if ("SHA-256".equals(moduleName)) {
            result = new Sha256ChecksumValidator();
        } else if ("SHA-512".equals(moduleName)) {
            result = new Sha512ChecksumValidator();
        } else {
            LOG.warn("Encountered unknown hash code algorithm name '" + moduleName + "'. Validation will fail.");
        }

        return result;
    }
}
