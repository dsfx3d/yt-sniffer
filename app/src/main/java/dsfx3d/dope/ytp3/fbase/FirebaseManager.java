package dsfx3d.dope.ytp3.fbase;

import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 *  @FirebaseManager
 *
 *  firebase real time database manager
 */

public class FirebaseManager {

    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    public FirebaseManager(){}

    public FirebaseManager(String refUrl, ValueEventListener eventListener) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(refUrl);
        addValueEventListener(eventListener);
    }

    public void addValueEventListener(ValueEventListener eventListener) {
        valueEventListener = eventListener;
        addValueEventListener();
    }

    private void addValueEventListener() {
        reference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (valueEventListener!=null) valueEventListener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (valueEventListener!=null) valueEventListener.onCancelled(databaseError);
            }
        });
    }

    public interface ValueEventListener {
        void onDataChange(DataSnapshot dataSnapshot);
        void onCancelled(DatabaseError databaseError);
    }

    public static String SUGGESTED_TEXT_REF = "https://ytp3-d71d4.firebaseio.com/suggested_text";
    public static String DEFAULT_REF = "https://ytp3-d71d4.firebaseio.com/";
    public static String APP_VERSION_REF = "https://ytp3-d71d4.firebaseio.com/ver";
}
