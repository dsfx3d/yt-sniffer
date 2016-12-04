package dsfx3d.dope.ytp3.MusicGraph;


/**
 *
 *  @MusicGraphPlaylist
 *
 *  Entity class for MusicGraphAPI's playlist json object
 *
 * */

public class MusicGraphPlaylist {

    public ResultStatus status;
    public ResultPagination pagination;
    public MusicGraphTrack[] data;

    public class ResultStatus{
        public int code;
        public String message;
        public String api;
    }

    public class ResultPagination{
        public int count;
        public int total;
        public int offset;
    }
}
