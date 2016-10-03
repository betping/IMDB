package acadgild.imdb;

/**
 * Created by Tungenwar on 20/05/2015.
 */
public class Movies {
    String _id,_vote_count,_vote_average,_popularity,_poster_path;
    String _title,release__date,_is_favorite,_is_watchlist;

    // Empty constructor
    public Movies(){

    }
    // constructor
    public Movies(String id, String is_favorite, String is_watchlist){
        this._id = id;
        this._is_favorite=is_favorite;
        this._is_watchlist=is_watchlist;
    }

    public Movies(String _id, String _title, String _popularity, String release__date,
                  String _poster_path, String _vote_average, String _vote_count, String _is_favorite, String _is_watchlist){
        this._id = _id;
        this._title=_title;
        this._popularity=_popularity;
        this.release__date=release__date;
        this._poster_path=_poster_path;
        this._vote_average=_vote_average;

        this._vote_count=_vote_count;
        this._is_favorite=_is_favorite;
        this._is_watchlist=_is_watchlist;
    }

    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getTitle(){
        return this._title;
    }

    // setting name
    public void setTitle(String title){
        this._title = title;
    }

    public String getDate(){
        return this.release__date;
    }

    public void setDate(String date){
        this.release__date = date;
    }

    // getting name
    public String getVoteCount(){
        return this._vote_count;
    }

    // setting name
    public void setVoteCount(String title){
        this._vote_count = title;
    }
    // getting name
    public String getVoteAverage(){
        return this._vote_average;
    }

    // setting name
    public void setVoteAverage(String title){
        this._vote_average = title;
    }
    // getting name
    public String getPosterPath(){
        return this._poster_path;
    }

    // setting name
    public void setPosterPath(String title){
        this._poster_path = title;
    }
    // getting name
    public String getIsFavorite(){
        return this._is_favorite;
    }

    // setting name
    public void setIsFavorite(String title){
        this._is_favorite = title;
    }
    // getting name
    public String getIsWatchlist(){
        return this._is_watchlist;
    }

    // setting name
    public void setIsWatchlist(String title){
        this._is_watchlist = title;
    }
}
