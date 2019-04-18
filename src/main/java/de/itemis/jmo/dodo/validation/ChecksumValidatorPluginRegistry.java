package de.itemis.jmo.dodo.validation;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import de.itemis.jmo.dodo.io.DodoDownload;

public class ChecksumValidatorPluginRegistry implements BiFunction<Supplier<DodoDownload>, String, ChecksumValidator> {

    private final BiFunction<byte[], String, ChecksumValidator> checksumValidatorFactory;

    public ChecksumValidatorPluginRegistry(BiFunction<byte[], String, ChecksumValidator> checksumValidatorFactory) {
        this.checksumValidatorFactory = checksumValidatorFactory;
    }

    @Override
    public ChecksumValidator apply(Supplier<DodoDownload> checksumSource, String pluginId) {
        checksumSource.get().getDataSource().read();
        return new NopChecksumValidator();
    }
}
