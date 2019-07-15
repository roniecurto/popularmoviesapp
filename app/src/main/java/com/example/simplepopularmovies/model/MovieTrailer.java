package com.example.simplepopularmovies.model;

public class MovieTrailer {
    private String id;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public MovieTrailer(String id, String key, String name, String site, String size, String type){
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getName() { return this.name; }
    public String getKey() { return this.key; }

    /*
    http://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=apikey
    {
        "id": "5c3deb71c3a36821623eaee4",
        "iso_639_1": "en",
        "iso_3166_1": "US",
        "key": "DYYtuKyMtY8",
        "name": "SPIDER-MAN: FAR FROM HOME - Official Teaser Trailer",
        "site": "YouTube",
        "size": 1080,
        "type": "Teaser"
    }
 */
}
