package com.bluescript.youknowit.api;

import android.util.Log;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.io.File;
import java.nio.ByteBuffer;
import android.util.Log;

import com.bluescript.youknowit.MainActivity;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetAllQuestionsFromServerCallback extends UrlRequest.Callback{

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        Log.i("onRedirectReceived", "call on Redirect" );
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        Log.i("adsf", "on response STart");
        int httpStatusCode = info.getHttpStatusCode();
        if(httpStatusCode == 200){
            request.read(ByteBuffer.allocateDirect(102400));
        } else if(httpStatusCode == 400){
            Log.e("400", "bad request");
        } else{
            Log.e(String.valueOf(httpStatusCode), "bad status code");
        }
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        Log.i("asdf", "onReadCompleted");
//        File file = new File(path);
//        file.delete();
        request.read(byteBuffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i("200", "Downloaded positively");

    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.i("Failed", error.getMessage());
    }
}
