package com.elder.expandablerecyclerviewdemo.models;

/**
 * Created by chris on 2017-06-13.
 */

public class SectionSubHeader
{
    //================================================================================
    // region VARIABLES

    private String expandString;
    private String minimizeString;
    private String loadingString;
    private String errorString;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public SectionSubHeader(String expandString, String minimizeString, String loadingString, String errorString)
    {
        this.expandString = expandString;
        this.minimizeString = minimizeString;
        this.loadingString = loadingString;
        this.errorString = errorString;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region GETTER & SETTER

    public String getExpandString() {
        return expandString;
    }

    public void setExpandString(String expandString) {
        this.expandString = expandString;
    }

    public String getMinimizeString() {
        return minimizeString;
    }

    public void setMinimizeString(String minimizeString) {
        this.minimizeString = minimizeString;
    }

    public String getLoadingString() {
        return loadingString;
    }

    public void setLoadingString(String loadingString) {
        this.loadingString = loadingString;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    // endregion
    //================================================================================
}
