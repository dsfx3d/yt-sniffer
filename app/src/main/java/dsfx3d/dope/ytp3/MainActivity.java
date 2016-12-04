package dsfx3d.dope.ytp3;


import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import dsfx3d.dope.ytp3.fbase.FirebaseManager;
import dsfx3d.dope.ytp3.fbase.UpdateManager;
import dsfx3d.dope.ytp3.ui.FontFitEditText;
import dsfx3d.dope.ytp3.utils.ClerkRequest;
import dsfx3d.dope.ytp3.utils.HyperClerk;
import dsfx3d.dope.ytp3.utils.L;

public class MainActivity extends AppCompatActivity implements
            ClerkRequest.Listener<String>,
            View.OnClickListener,
            FirebaseManager.ValueEventListener
{
    private FontFitEditText queryET;
    private TextView res;
    private ImageView featuredActivityButton, notifIC;
    private Button submit;
    private Spring spring;
    private LinearLayout menuLayout;
    private AVLoadingIndicatorView loadingIndicatorView;

    boolean commFlag;

    private L l;
    private HyperClerk clerk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.checkUpdate();

        init();
        getLayout();
        initRebound();
    }

    @Override
    protected void onPause() {
        super.onPause();
        submit.setBackgroundColor(getResources().getColor(R.color.transparent_black));
        queryET.setEnabled(true);
        submit.setText(getString(R.string.sniff_b_state_1));
        res.setText(getString(R.string.query_label));
    }

    void init() {
        l = new L("MainActivity");
        clerk = new HyperClerk(this);
        new FirebaseManager(FirebaseManager.SUGGESTED_TEXT_REF, this);
    }

    void initRebound() {
        SpringSystem springSystem = SpringSystem.create();
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
                showLoader();
            }
        });
    }

    void getLayout() {
        queryET = (FontFitEditText) findViewById(R.id.query_field);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/alba.ttf");
        queryET.setTypeface(typeface);

        res = (TextView) findViewById(R.id.res);
        res.setText(getString(R.string.query_label));

        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(this);

        featuredActivityButton = (ImageView) findViewById(R.id.featured_activity);
        featuredActivityButton.setOnClickListener(this);
        notifIC = (ImageView) findViewById(R.id.update_icon);
        notifIC.setOnClickListener(this);

        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loader);
        loadingIndicatorView.hide();

        menuLayout = (LinearLayout) findViewById(R.id.menu_layout);
    }

    void showLoader() {
        l.p("UI:: Loading progress started..",L.VERBOSE);
        loadingIndicatorView.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.GONE);
        queryET.setEnabled(false);
        submit.setBackgroundColor(getResources().getColor(R.color.colorBerry));
        res.setText(Html.fromHtml(getString(R.string.tip_1)));
    }

    void hideLoader() {
        l.p("UI:: Loading progress stopped",L.VERBOSE);
        loadingIndicatorView.setVisibility(View.INVISIBLE);
        menuLayout.setVisibility(View.VISIBLE);
        queryET.setEnabled(true);
        submit.setBackgroundColor(getResources().getColor(R.color.transparent_black));
        submit.setText(getString(R.string.sniff_b_state_1));
    }

    private void startSearchingMusic() {
        commFlag=true;
        submit.setText(getString(R.string.sniff_b_state_2));

        String query = queryET.getText().toString();
        query = query.replace(' ','-');

        ClerkRequest request = new ClerkRequest(getString(R.string.ytp3_api_url)+query, this, ClerkRequest.STRING_RESPONSE);
        clerk.addStringRequest(request);
        clerk.startListening();
    }

    public void stopSearchingMusic() {
        commFlag=false;
        hideLoader();
        res.setText(getString(R.string.query_label));
        if(clerk!=null) clerk.halt();
    }

    private void submitButtonClick() {
        if(queryET.getText().toString().isEmpty()) {
            Toast.makeText(this, "enter track title", Toast.LENGTH_SHORT).show();
            return;
        }

        spring.setVelocity(30);
        spring.setEndValue(0);

        if(submit.getText().toString().equals(getString(R.string.sniff_b_state_1)))
            startSearchingMusic();

        else if (submit.getText().toString().equals(getString(R.string.sniff_b_state_2)))
            stopSearchingMusic();
    }

    private void featuredActivityButtonClick() {
        Intent intent = new Intent(MainActivity.this, FeaturedActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slideupin,R.anim.hold);
    }

/**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**//**??sasd??**/

    @Override// ::: HyperClerk.Listener
    public void onErrorResponse(VolleyError error) {
        if(!commFlag)return;
        hideLoader();
        res.setText(getString(R.string.unknown_host_exception));
    }

    @Override// ::: HyperClerk.Listener
    public void onResponse(String response) {
        if(!commFlag)return;
        hideLoader();
        String downloadUrl = response.substring(20, response.length() - 4);

        res.setText(getString(R.string.query_label));
        l.p("redirecting to download url :\n"+downloadUrl,L.VERBOSE);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
        startActivity(browserIntent);
        overridePendingTransition(R.anim.slideupin,R.anim.hold);
    }

    @Override// ::: FirebaseManager.ValueEventListener
    public void onDataChange(DataSnapshot dataSnapshot) {
        queryET.setHint(dataSnapshot.getValue(String.class));
        clerk.addStringRequest(new ClerkRequest());
    }

    @Override// ::: FirebaseManager.ValueEventListener
    public void onCancelled(DatabaseError databaseError) {
        //NOTHING
        l.p("Firebase Encountered an Error\n"+databaseError.getMessage() ,L.ERROR);
    }

    @Override// ::: View.OnClickListener
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit_button:
                submitButtonClick();
                break;

            case R.id.featured_activity:
                featuredActivityButtonClick();
                break;
        }
    }
}
