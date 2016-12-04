package com.lightcyclesoftware.photoscodeexample.model;


public class GraphQLBody {
    private String query;

    public String getQuery() {
        return query;
    }

    public GraphQLBody setQuery(String query) {
        this.query = query.trim();
        return this;
    }
}
