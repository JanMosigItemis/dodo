package de.itemis.jmo.dodo.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;

/**
 * A factory that produces {@link HashCodeValidator} instances. Known algorithm names are those
 * known to the JVM.
 */
public class HashCodeValidatorFactory implements BiFunction<Supplier<DodoDownload>, String, HashCodeValidator> {

    private static final Logger LOG = LoggerFactory.getLogger(HashCodeValidatorFactory.class);

    /**
     * @param downloadSup - Used to download the expected hashCode.
     */
    @Override
    public HashCodeValidator apply(Supplier<DodoDownload> downloadSup, String algorithmName) {
        HashCodeValidator result = new NopHashCodeValidator();

        if ("MD5".equals(algorithmName)) {
            result = new Md5HashCodeValidator(downloadSup);
        } else if ("SHA-1".equals(algorithmName)) {
            result = new Sha1HashCodeValidator();
        } else if ("SHA-256".equals(algorithmName)) {
            result = new Sha256HashCodeValidator();
        } else if ("SHA-512".equals(algorithmName)) {
            result = new Sha512HashCodeValidator();
        } else {
            LOG.warn("Encountered unknown hash code algorithm name '" + algorithmName + "'. Validation will fail.");
        }

        return result;
    }
}
