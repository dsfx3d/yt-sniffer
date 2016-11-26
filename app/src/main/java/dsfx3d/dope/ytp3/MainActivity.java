package dsfx3d.dope.ytp3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import dsfx3d.dope.ytp3.ui.FontFitTextView;
import dsfx3d.dope.ytp3.utils.HttpComm;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener, View.OnClickListener {

    FontFitTextView queryET;
    TextView res;
    Button submit;
    SpringSystem springSystem;
    Spring spring;
    AVLoadingIndicatorView loadingIndicatorView;

    HttpComm communicator;
    boolean commFlag;

    DatabaseReference fartistRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        submit.setBackgroundColor(getResources().getColor(R.color.colorBerry));
        queryET.setEnabled(true);
        submit.setText(getString(R.string.sniff_b_state_1));
    }

    void init() {
        fartistRef = FirebaseDatabase.getInstance().getReference("featured_artist");
        fartistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queryET.setHint(dataSnapshot.getValue(String.class));
                Log.v("_FIREBASE__","value changed");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getLayout();
        initAnimation();
    }

    void initAnimation() {
        springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                submit.setScaleX(scale);
                submit.setScaleY(scale);
            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
                super.onSpringEndStateChange(spring);
                loadingIndicatorView.show();
            }
        });
    }

    void getLayout() {
        queryET = (FontFitTextView) findViewById(R.id.query_field);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/alba.ttf");
        queryET.setTypeface(typeface);

        res = (TextView) findViewById(R.id.res);
        res.setText(getString(R.string.query_label));

        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(this);

        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loader);
        loadingIndicatorView.hide();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onErrorResponse(VolleyError error) {
        if(!commFlag)return;
        loadingIndicatorView.hide();
        queryET.setEnabled(true);
        Log.e("__HttpResponseError","Response: " + error.getMessage());
        res.setVisibility(View.VISIBLE);
        submit.setText(getString(R.string.sniff_b_state_1));
        submit.setBackgroundColor(getResources().getColor(R.color.colorBerry));

        res.setText(getString(R.string.unknown_host_exception));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResponse(String response) {
        if(!commFlag)return;
        loadingIndicatorView.hide();
        String downloadUrl = response.substring(20, response.length() - 4);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
        startActivity(browserIntent);
        Log.v("__HttpResponse", downloadUrl);
    }

    @Override
    public void onClick(View v) {
        if(queryET.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter song name", Toast.LENGTH_SHORT).show();
            return;
        }
        res.setVisibility(View.GONE);
        spring.setVelocity(30);
        spring.setEndValue(0);

        if(submit.getText().toString().equals(getString(R.string.sniff_b_state_1))) {
            commFlag=true;
            submit.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            queryET.setEnabled(false);
            submit.setText(getString(R.string.sniff_b_state_2));

            String query = queryET.getText().toString();
            query = query.replace(' ','-');

            communicator = new HttpComm(MainActivity.this);
            communicator.addRequest(Request.Method.GET, query, MainActivity.this, MainActivity.this);
            communicator.startRequestPipeline();
        }

        else if (submit.getText().toString().equals(getString(R.string.sniff_b_state_2))) {
            commFlag=false;
            communicator.stopResquestPipeline();

            submit.setBackgroundColor(getResources().getColor(R.color.colorBerry));
            queryET.setEnabled(true);
            submit.setText(getString(R.string.sniff_b_state_1));
            loadingIndicatorView.hide();
        }
    }
}
