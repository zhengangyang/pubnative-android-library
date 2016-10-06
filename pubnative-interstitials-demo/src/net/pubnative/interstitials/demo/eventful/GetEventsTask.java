package net.pubnative.interstitials.demo.eventful;

import java.util.List;

import org.droidparts.concurrent.task.AsyncTaskResultListener;
import org.droidparts.concurrent.task.SimpleAsyncTask;
import org.droidparts.net.http.RESTClient2;
import org.droidparts.persist.serializer.JSONSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;

public class GetEventsTask extends SimpleAsyncTask<List<Event>>
{
    public GetEventsTask(Context ctx, AsyncTaskResultListener<List<Event>> resultListener)
    {
        super(ctx, resultListener);
    }

    @Override
    protected List<Event> onExecute() throws Exception
    {
        Uri.Builder builder = Uri.parse(Eventful.URL).buildUpon();
        for (String param : Eventful.PARAMS.keySet())
        {
            builder.appendQueryParameter(param, Eventful.PARAMS.get(param));
        }
        JSONObject obj = new RESTClient2(getContext()).getJSONObject(builder.toString());
        JSONArray arr = obj.getJSONObject("events").getJSONArray("event");
        List<Event> list = new JSONSerializer<Event>(Event.class, getContext()).deserializeAll(arr);
        return list;
    }
}
