package de.itemis.jmo.dodo.tests.util;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.ArrayList;
import java.util.List;

public class DownloadListener extends PostServeAction {

    public static final String ARTIFACT_NAME_KEY = "artifactName";

    private final List<String> servedRequests = new ArrayList<>();

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public void doAction(ServeEvent serveEvent, Admin admin, Parameters parameters) {
        servedRequests.add(parameters.getString(ARTIFACT_NAME_KEY));
    }

    /**
     * @return {@code true} if a download request for this artifact has been served. Otherwise
     *         {@code false}.
     */
    public boolean requestServed(String artifactName) {
        return servedRequests.contains(artifactName);
    }
}
