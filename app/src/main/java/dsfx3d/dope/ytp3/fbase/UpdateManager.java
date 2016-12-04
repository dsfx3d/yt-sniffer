package dsfx3d.dope.ytp3.fbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dsfx3d.dope.ytp3.utils.L;

public class UpdateManager implements FirebaseManager.ValueEventListener{
    private Context context;
    private L l;

    public UpdateManager(Context context) {
        this.context = context;
        l = new L("UpdateManager");
    }

    public void checkUpdate() {
        l.p("checking for updates...", L.VERBOSE);
        new FirebaseManager(FirebaseManager.APP_VERSION_REF, this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        double ver = dataSnapshot.getValue(double.class);
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            if(!String.valueOf(ver).equals(pInfo.versionName)) {
                l.p("new updates found v"+ver+" is available for download", L.VERBOSE);
                ProgressDialog updateD = new ProgressDialog(context);
                updateD.setIndeterminate(true);
                updateD.setCancelable(true);
                updateD.setCanceledOnTouchOutside(true);
                updateD.show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        l.p("update check failed \n error: "+databaseError.getMessage(), L.ERROR);
    }
}
