package net.pubnative.library.predefined;

import java.util.ArrayList;

public class PubnativeActivityDelegateManager {
    private static PubnativeActivityDelegateManager instance;
    private ArrayList<PubnativeActivityDelegate> delegates = new ArrayList<PubnativeActivityDelegate>();

    private PubnativeActivityDelegateManager() {
    }

    /**
     * Gets you the singleton instance of activity deligate manager
     *
     * @return Activity deligate manager instance
     */
    private static PubnativeActivityDelegateManager getInstance() {
        if (instance == null) {
            instance = new PubnativeActivityDelegateManager();
        }
        return instance;
    }

    /**
     * Add activity deligate object to the cache
     *
     * @param delegate Activity deligate object to be added to cache
     */
    public static void addDelegate(PubnativeActivityDelegate delegate) {
        if (!PubnativeActivityDelegateManager.getInstance().delegates.contains(delegate)) {
            PubnativeActivityDelegateManager.getInstance().delegates.add(delegate);
        }
    }

    /**
     * Remove activity deligate object from the cache
     *
     * @param delegate Activity deligate object to be removed from cache
     */
    public static void removeDelegate(PubnativeActivityDelegate delegate) {
        PubnativeActivityDelegateManager.getInstance().delegates.remove(delegate);
    }
}
