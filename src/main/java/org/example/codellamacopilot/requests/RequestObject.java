package org.example.codellamacopilot.requests;

import org.example.codellamacopilot.requests.requestparameters.RequestParameters;

/**
 * Request object for the request.
 */
public abstract class RequestObject {
    public abstract String getInputData();
    public abstract void setInputData(String inputData);
    public abstract RequestParameters getParameters();
    public abstract void setParameters(RequestParameters parameters);

}
