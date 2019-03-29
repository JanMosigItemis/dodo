package de.itemis.jmo.dodo.io;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FinishableProgressListenerTest {

    private static final long UPPER_BOUND = 100;

    private Runnable callback;

    private FinishableProgressListener underTest;

    @BeforeEach
    public void setUp() {
        callback = mock(Runnable.class);

        underTest = new FinishableProgressListener(UPPER_BOUND, callback);
    }

    @Test
    public void pass_upper_bound_as_progress_results_in_callback() {
        underTest.updateProgress(UPPER_BOUND);

        verify(callback).run();
    }

    @Test
    public void pass_more_than_upper_bound_as_progress_results_in_complete() {
        underTest.updateProgress(UPPER_BOUND + 1);

        verify(callback).run();
    }

    @Test
    public void accumulation_to_upper_bound_completes_progress() {
        int stepCount = 4;
        long step = UPPER_BOUND / stepCount;

        for (var i = 0; i < stepCount; i++) {
            verify(callback, never()).run();
            underTest.updateProgress(step);
        }

        verify(callback).run();
    }
}
