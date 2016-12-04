package dsfx3d.dope.ytp3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.gson.Gson;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import dsfx3d.dope.ytp3.MusicGraph.MusicGraphArtist;
import dsfx3d.dope.ytp3.fbase.FirebaseManager;
import dsfx3d.dope.ytp3.utils.ClerkRequest;
import dsfx3d.dope.ytp3.utils.HyperClerk;
import dsfx3d.dope.ytp3.utils.L;
import dsfx3d.dope.ytp3.utils.LocalStore;

public class FeaturedActivity extends AppCompatActivity {

    private L l;
    private LocalStore store;
    private HyperClerk clerk;
    private ImageView posterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);

        init();
        lookForFeaturedContent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.hold, R.anim.slidedownout);
    }

    private void init() {
        l = new L(getClass().getSimpleName());
        l.p("Initializing FeaturedActivity..",L.VERBOSE);

        store = new LocalStore(this);
        clerk = new HyperClerk(this);
    }

    private boolean hasFeaturedContent(){
        return store.fetch(StoreKeys.HasFeaturedContent,false);
    }

    private void lookForFeaturedContent() {
        if (hasFeaturedContent()) {
            l.p("Featured Content Found.",L.VERBOSE);
        } else {
            l.p("Featured Content not Found. Will request HyperClerk for data..",L.VERBOSE);
            getFeaturedArtist();
        }
    }

    private void getFeaturedPlaylist() {

    }

    private void getFeaturedArtist() {
        l.p("Requesting featured artist data..",L.VERBOSE);
        new FirebaseManager(getString(R.string.fb_featured_artist_ref), new FirebaseManager.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MusicGraphArtist artist = dataSnapshot.getValue(MusicGraphArtist.class);
                l.p("Firebase:: Updated Featured Artist data received." +
                        "\n\tArtist Name: "+artist.name+
                        "\n\t\" "+artist.quote+" \""+
                        "\n\tGenre: "+artist.genre+
                        "\n\tCountry: "+artist.country+
                        "\n\tMusicGraphId: "+artist.id+
                        "\n\tYouTube: "+getString(R.string.yt_channel_url_prefix)+artist.yt_id
                        ,L.VERBOSE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                l.p("Firebase:: Database Error occurred \n"+databaseError.getMessage(),L.ERROR);
            }
        });
    }


    @SuppressWarnings("FieldCanBeLocal")
    private class StoreKeys {
        public static final String HasFeaturedContent = "has-featured-content";
    }

}
