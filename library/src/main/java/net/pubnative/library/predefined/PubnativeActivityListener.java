package net.pubnative.library.predefined;

public interface PubnativeActivityListener
{
    /**
     * Invoked when predefined ad display is started
     * @param identifier unique id of activity delegate
     */
    void onPubnativeActivityStarted(String identifier);

    /**
     * Invoked when predefined ad display is failed
     * @param identifier unique id of activity delegate
     * @param exception  Exception that caused failure
     */
    void onPubnativeActivityFailed(String identifier, Exception exception);

    /**
     * Invoked when the predefined ad is completely displayed
     * @param identifier unique id of activity delegate
     */
    void onPubnativeActivityOpened(String identifier);

    /**
     * Invoked when the displayed ad is closed
     * @param identifier unique id of activity delegate
     */
    void onPubnativeActivityClosed(String identifier);
}
