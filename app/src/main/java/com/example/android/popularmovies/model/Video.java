package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso_639_1;
    @SerializedName("iso_3166_1")
    private String iso_3166_1;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private Integer size;
    @SerializedName("type")
    private String type;



    public Video(String id, String iso_639_1, String iso_3166_1, String key,
                 String name, String site, Integer size, String type) {
        this.id = id;
        this.iso_639_1= iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public Video(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1(){ return iso_639_1; }

    public void setIso_639_1(String iso_639_1) { this.iso_639_1 = iso_639_1;}

    public String getIso_3166_1(){ return iso_3166_1; }

    public void setIso_3166_1(String iso_3166_1) { this.iso_3166_1 = iso_3166_1;}

    public String getKey(){ return key; }

    public void setKey(String key) { this.key = key;}

    public String getName(){ return name; }

    public void setName(String key) { this.name = name;}

    public String getSite(){ return site; }

    public void setSite(String site) { this.site = site;}

    public Integer getSize(){ return size; }

    public void setSize(Integer size) { this.size = size;}

    public String getType(){ return type; }

    public void setType(String type) { this.type = type;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso_639_1);
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeValue(this.size);
        dest.writeString(this.type);
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.iso_3166_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}