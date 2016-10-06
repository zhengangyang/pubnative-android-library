package net.pubnative.interstitials.demo.eventful;

import java.util.HashMap;

public interface Eventful
{
    String                  URL    = "http://api.eventful.com/json/events/search";
    HashMap<String, String> PARAMS = new HashMap<String, String>()
                                   {
                                       private static final long serialVersionUID = 1L;
                                       {
                                           put("app_key", "pd5PdshD44wckpD7");
                                           put("location", "Berlin");
                                           put("date", "Today");
                                           put("categories", "singles_social,music");
                                           put("image_sizes", "block250,large");
                                           put("page_size", "100");
                                       }
                                   };
}
