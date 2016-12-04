package dsfx3d.dope.ytp3.ui;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import dsfx3d.dope.ytp3.MusicGraph.MusicGraphTrack;
import dsfx3d.dope.ytp3.R;


public class PlaylistAdapter extends ArrayAdapter<MusicGraphTrack> {

    MusicGraphTrack[] playlist;

    public PlaylistAdapter(Context context, int resource, MusicGraphTrack[] playlist) {
        super(context, resource, playlist);
        addListData(playlist);
    }

    public void addListData(MusicGraphTrack[] data) {
        playlist = data;
        Log.v("__PLAYLIST_ADAPTER","playlist added. "+data.length+" tracks.");
    }

    @Override
    public MusicGraphTrack getItem(int position) {
        return playlist[position];
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.single_track_card,null);
        }

        MusicGraphTrack track = getItem(position);

        if(track!=null) {
            Log.v("__PLAYLIST", track.title+" - "+track.artist_name);

            TextView title = (TextView)v.findViewById(R.id.title);
            TextView artist = (TextView)v.findViewById(R.id.artist_name);
            TextView duration = (TextView)v.findViewById(R.id.duration);

            title.setText(track.title);
            artist.setText(track.artist_name);
        }

        return v;
    }


}
