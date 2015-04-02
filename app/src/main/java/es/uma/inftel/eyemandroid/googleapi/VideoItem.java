package es.uma.inftel.eyemandroid.googleapi;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {

    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>(){

        @Override
        public VideoItem createFromParcel(Parcel in){
            return new VideoItem(in);
        }

        @Override
        public VideoItem[] newArray (int size){
            return new VideoItem[size];
        }

    };

    private String id;
    private String title;
    private String description;
    private String thumbnailURL;

    public VideoItem() {
    }

    private VideoItem(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.thumbnailURL = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnailURL);
    }
}