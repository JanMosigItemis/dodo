package de.itemis.jmo.dodo;

import static de.itemis.jmo.dodo.tests.util.FutureHelper.kill;
import static de.itemis.jmo.dodo.tests.util.FutureHelper.waitForLatch;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.base.Strings;

import org.assertj.core.description.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Application;
import javafx.application.Platform;

@RunWith(JUnitPlatform.class)
public class DodoUiBootstrapTest {

    private ExecutorService executor;

    @BeforeEach
    public void setUp() {
        executor = Executors.newSingleThreadExecutor();
    }

    @AfterEach
    public void tearDown() {
        kill(executor);
    }

    /**
     * <p>
     * This is a very special test case. It tests, that the Dodo application bootstrapping via
     * {@link Application#launch(Class, String...)} works, i. e. if the app is actually able to
     * start up. This is necessary, because the TestFX based UI tests do use a different strategy to
     * bootstrap the app, which means, that errors during a real start up phase via main method will
     * not be recognizable by those tests.
     * </p>
     */
    // ATM this test requires a display which is not available on CI. This makes the test fail on
    // CI. Idea: Use categories + filtering to run this test only locally.
    @Disabled
    @Test
    public void dodo_main_startsup_normally() {
        var latch = new CountDownLatch(1);
        var thrownExceptionRef = new AtomicReference<Exception>();

        executor.submit(() -> {
            try {
                Dodo.main(new String[0]);
            } catch (Exception e) {
                thrownExceptionRef.set(e);
                latch.countDown();
            }
        });

        assertThat(waitForLatch(latch, Duration.ofSeconds(10))).as(new BootstrapDescr(thrownExceptionRef)).isFalse();
        Platform.exit();
    }

    private static final class BootstrapDescr extends Description {
        private final AtomicReference<Exception> thrownExceptionRef;

        public BootstrapDescr(AtomicReference<Exception> thrownExceptionRef) {
            this.thrownExceptionRef = thrownExceptionRef;
        }

        @Override
        public String value() {
            Exception thrownException = thrownExceptionRef.get();
            String details = "No further details.";
            if (thrownException != null) {
                String causeMsg = thrownException.getMessage();
                if (Strings.isNullOrEmpty(causeMsg)) {
                    causeMsg = details;
                }
                details = thrownException.getClass().getSimpleName() + ": " + causeMsg;
            }

            return "Bootstrapping Dodo raised an error: " + details;
        }
    }
}
