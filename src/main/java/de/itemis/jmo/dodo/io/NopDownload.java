package de.itemis.jmo.dodo.io;

public class NopDownload implements DodoDownload {

    @Override
    public DataSource getDataSource() {
        return null;
    }

    @Override
    public long getSize() {
        // TODO Auto-generated method stub
        return 0;
    }
}
