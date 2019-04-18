package de.itemis.jmo.dodo.util;

import static de.itemis.jmo.dodo.tests.util.ExpectedExceptions.EXPECTED_IO_EXCEPTION;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import de.itemis.jmo.dodo.io.DataSource;
import de.itemis.jmo.dodo.tests.util.Console;

public class DataSourceUtilTest {

    @RegisterExtension
    Console console = new Console();

    private DataSource dataSourceMock;

    @BeforeEach
    public void setUp() {
        dataSourceMock = mock(DataSource.class);
    }

    @Test
    public void close_is_called() throws Exception {
        DataSourceUtil.safeClose(this.getClass(), dataSourceMock);

        verify(dataSourceMock).close();
    }

    @Test
    public void errors_on_close_are_logged() throws Exception {
        doThrow(EXPECTED_IO_EXCEPTION).when(dataSourceMock).close();

        DataSourceUtil.safeClose(this.getClass(), dataSourceMock);

        console.assertAnyLineContains("Encountered unexpected error while closing dataSource.");
        console.assertAnyLineContains(EXPECTED_IO_EXCEPTION.getMessage());
    }
}
