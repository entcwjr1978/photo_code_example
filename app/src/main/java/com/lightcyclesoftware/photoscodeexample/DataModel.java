package com.lightcyclesoftware.photoscodeexample;


import java.util.List;

public class DataModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public DataModel setData(Data data) {
        this.data = data;
        return this;
    }

    public class Data {
        private Album album;

        public Album getAlbum() {
            return album;
        }

        public Data setAlbum(Album album) {
            this.album = album;
            return this;
        }
    }

    public class Album {
        private String id;
        private String name;
        private Photos photos;

        public String getId() {
            return id;
        }

        public Album setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Album setName(String name) {
            this.name = name;
            return this;
        }

        public Photos getPhotos() {
            return photos;
        }

        public Album setPhotos(Photos photos) {
            this.photos = photos;
            return this;
        }
    }

    public class Photos {
        private List<Record> records;

        public List<Record> getRecords() {
            return records;
        }

        public Photos setRecords(List<Record> records) {
            this.records = records;
            return this;
        }
    }

    public class Record {
        private String id;
        private List<Url> urls;

        public String getId() {
            return id;
        }

        public Record setId(String id) {
            this.id = id;
            return this;
        }

        public List<Url> getUrls() {
            return urls;
        }

        public Record setUrls(List<Url> urls) {
            this.urls = urls;
            return this;
        }
    }

    public class Url {
        private String size_code;
        private String url;
        private int width;
        private int height;
        private float quality;
        private String mime;

        public String getSize_code() {
            return size_code;
        }

        public Url setSize_code(String size_code) {
            this.size_code = size_code;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public Url setUrl(String url) {
            this.url = url;
            return this;
        }

        public int getWidth() {
            return width;
        }

        public Url setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Url setHeight(int height) {
            this.height = height;
            return this;
        }

        public float getQuality() {
            return quality;
        }

        public Url setQuality(float quality) {
            this.quality = quality;
            return this;
        }

        public String getMime() {
            return mime;
        }

        public Url setMime(String mime) {
            this.mime = mime;
            return this;
        }
    }
}
