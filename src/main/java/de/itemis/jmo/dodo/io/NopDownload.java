package de.itemis.jmo.dodo.io;

public class NopDownload implements DodoDownload {

    @Override
    public DataSource getDataSource() {
        return null;
    }
}
