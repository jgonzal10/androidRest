package dev.level33.com.androidasynctaskexampl;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;

import java.net.URI;


/**
 * jgo development
 */
public class TestFragment extends Fragment {

    //here I define the web service url.

    private static final String TAG = "AATestFragment";
    private static final String TEST_URL                   = "http://localhost:52424/api/Usuario/GetUsuarios";
    private static final String ACTION_FOR_INTENT_CALLBACK = "THIS_IS_A_UNIQUE_KEY_WE_USE_TO_COMMUNICATE";

    ProgressDialog progress;
    private TextView ourTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ourTextView = (TextView)getActivity().findViewById(R.id.myTextView);
        getContent();
    }


    private void getContent()
    {
        // the request
        try
        {
            HttpGet httpGet = new HttpGet(new URI(TEST_URL));
            RestTask task = new RestTask(getActivity(), ACTION_FOR_INTENT_CALLBACK);
            task.execute(httpGet);
            progress = ProgressDialog.show(getActivity(), "Getting Data ...", "Waiting For Results...", true);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

    }


    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter(ACTION_FOR_INTENT_CALLBACK));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }



    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // clear the progress indicator
            if (progress != null)
            {
                progress.dismiss();
            }
            String response = intent.getStringExtra(RestTask.HTTP_RESPONSE);
            ourTextView.setText(response);
            Log.i(TAG, "RESPONSE = " + response);
            //
            // my old json code was here. this is where you will parse it.
            //
        }
    };
}
