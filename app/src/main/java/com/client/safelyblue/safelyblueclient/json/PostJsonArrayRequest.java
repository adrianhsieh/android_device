package com.client.safelyblue.safelyblueclient.json;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by albin on 3/12/15.
 */
public class PostJsonArrayRequest extends JsonRequest<JSONArray> {
    /**
     * Creates a new request.
     * @param url URL to fetch the JSON from
     * @param jsonArray Body to send JSON
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public PostJsonArrayRequest(String url, JSONArray jsonArray, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, jsonArray.toString(), listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
