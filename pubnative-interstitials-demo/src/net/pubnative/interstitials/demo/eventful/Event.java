package net.pubnative.interstitials.demo.eventful;

import org.droidparts.annotation.serialize.JSON;
import org.droidparts.model.Model;

public class Event extends Model
{
    private static final long serialVersionUID = 1L;
    @JSON
    public String             title;
    @JSON
    public String             description;
    @JSON(key = "image" + JSON.SUB + "block250" + JSON.SUB + "url", optional = true)
    public String             iconUrl;
}
