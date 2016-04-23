package com.edvaldotsi.nodejsandandroid.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Edvaldo on 23/04/2016.
 */
public class JSON<T> {

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    @SerializedName("results")
    @Expose
    private List<T> results;

    public Metadata getMetadata() {
        return metadata;
    }

    public List<T> getResults() {
        return results;
    }

    public class Metadata {

        @SerializedName("count")
        @Expose
        private int count;

        @SerializedName("limit")
        @Expose
        private int limit;

        @SerializedName("page")
        @Expose
        private int page;

        public int getCount() {
            return count;
        }

        public int getLimit() {
            return limit;
        }

        public int getPage() {
            return page;
        }
    }
}
