package acadgild.imdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movie Database";
    private static final String TABLE_MOVIEDETAILS = "Movies";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_POSTER_PATH = "poster_path";
    //private static final String COLUMN_STATUS = "status";
    //private static final String COLUMN_BUDGET = "budget";
    //private static final String COLUMN_REVENUE = "revenue";
    private static final String COLUMN_VOTE_AVERAGE = "vote_average";
    private static final String COLUMN_VOTE_COUNT = "vote_count";
    //private static final String COLUMN_MOVIE_RAW_DETAILS = "movie raw details";
    //private static final String COLUMN_TAGLINE = "tagline";
    private static final String COLUMN_IS_FAVORITE = "favorite";
    private static final String COLUMN_IS_WATCHLIST = "watchlist";
    private static final String DATATYPE_NUMERIC = " INTEGER";
    private static final String DATATYPE_VARCHAR = " TEXT";
    //private static final String DATATYPE_BLOB = " BLOB";
    private static final String OPEN_BRACE = "(";
    private static final String COMMA = ",";

    private static final String CREATE_TABLE_MOVIEDETAILS = new StringBuilder()
            .append(CREATE_TABLE).append(TABLE_MOVIEDETAILS).append(OPEN_BRACE)
            .append(COLUMN_ID).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_TITLE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_RELEASE_DATE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_POSTER_PATH).append(DATATYPE_VARCHAR + COMMA)
                    //.append(COLUMN_STATUS).append(DATATYPE_VARCHAR + COMMA)
                    //.append(COLUMN_BUDGET).append(DATATYPE_VARCHAR + COMMA)
                    //.append(COLUMN_REVENUE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_VOTE_AVERAGE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_VOTE_COUNT).append(DATATYPE_VARCHAR + COMMA)
                    //.append(COLUMN_MOVIE_RAW_DETAILS).append(DATATYPE_VARCHAR + COMMA)
                    //.append(COLUMN_TAGLINE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_IS_FAVORITE).append(DATATYPE_NUMERIC + COMMA)
            .append(COLUMN_IS_WATCHLIST).append(DATATYPE_NUMERIC + COMMA)
            .append("UNIQUE(").append(COLUMN_ID).append(") ON CONFLICT REPLACE)").toString();

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_MOVIEDETAILS);
    }

    public void addMovie(Movies movieInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, movieInfo.getID());
        values.put(COLUMN_TITLE, movieInfo.getTitle());
        values.put(COLUMN_RELEASE_DATE, movieInfo.getDate());
        values.put(COLUMN_POSTER_PATH, movieInfo.getPosterPath());
        //values.put(COLUMN_STATUS, movieInfo.getStatus());
        //values.put(COLUMN_BUDGET, movieInfo.getBudget());
        //values.put(COLUMN_REVENUE, movieInfo.getRevenue());
        values.put(COLUMN_VOTE_AVERAGE, movieInfo.getVoteAverage());
        values.put(COLUMN_VOTE_COUNT, movieInfo.getVoteCount());
        //values.put(COLUMN_MOVIE_RAW_DETAILS, movieInfo.getOverview());
        //values.put(COLUMN_TAGLINE, movieInfo.getTagline());
        values.put(COLUMN_IS_FAVORITE, movieInfo.getIsFavorite());
        values.put(COLUMN_IS_WATCHLIST, movieInfo.getIsWatchlist());
        db.insert(TABLE_MOVIEDETAILS, null, values);
        db.close();
    }

    public Movies getMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS, new String[] {COLUMN_TITLE, COLUMN_RELEASE_DATE,
                        COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_FAVORITE, COLUMN_IS_WATCHLIST}, COLUMN_ID + "=?",
                new String[] {id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Movies movieInfo = new Movies();
        try {
            movieInfo.setID(id);
            movieInfo.setTitle(cursor.getString(0));
            movieInfo.setDate(cursor.getString(1));
            movieInfo.setPosterPath(cursor.getString(2));
            movieInfo.setVoteAverage(cursor.getString(3));
            movieInfo.setVoteCount(cursor.getString(4));
            movieInfo.setIsFavorite(String.valueOf(cursor.getInt(5)));
            movieInfo.setIsWatchlist(String.valueOf(cursor.getInt(6)));
        }catch (Exception e){
            detailScreen d=new detailScreen();
            Toast.makeText(d.context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        db.close();
        return movieInfo;
    }

    public boolean checkMovie(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_MOVIEDETAILS, null, COLUMN_ID + "=?", new String[]{id}, null, null, null, null);
            if (cursor.getCount() > 0)
                return true;
            else
                return false;
        }
        catch(Exception e){
            detailScreen d=new detailScreen();
            Toast.makeText(d.context,e.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void updateMovieF(Movies movieInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IS_FAVORITE, movieInfo.getIsFavorite());
            db.update(TABLE_MOVIEDETAILS, values, COLUMN_ID + "=?", new String[]{movieInfo.getID()});
        }
        catch(Exception e){
            detailScreen d=new detailScreen();
            Toast.makeText(d.context,"did not update",Toast.LENGTH_LONG).show();
        }
    }

    public int updateMovieW(Movies movieInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_WATCHLIST, movieInfo.getIsWatchlist());
        return db.update(TABLE_MOVIEDETAILS, values, COLUMN_ID + "=?", new String[] {movieInfo.getID()});
    }

    public ArrayList<HashMap<String,String>> getFavorites() {
        ArrayList<HashMap<String,String>> allMovies = new ArrayList<HashMap<String,String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS,
                new String[]{COLUMN_ID,COLUMN_TITLE,COLUMN_RELEASE_DATE,COLUMN_POSTER_PATH,
                        COLUMN_VOTE_AVERAGE,COLUMN_VOTE_COUNT,COLUMN_IS_FAVORITE}
                ,COLUMN_IS_FAVORITE+"=?",new String[]{String.valueOf(1)},null,null,null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> movieInfo = new HashMap<String,String>();
                movieInfo.put("id", cursor.getString(0));
                movieInfo.put("title", cursor.getString(1));
                movieInfo.put("release_date", cursor.getString(2));
                movieInfo.put("poster_path", cursor.getString(3));
                movieInfo.put("vote_average", cursor.getString(4));
                movieInfo.put("vote_count", cursor.getString(5));
                movieInfo.put("FAV", String.valueOf(cursor.getInt(6)));
                if (cursor.getInt(6) == 1) {
                    allMovies.add(movieInfo);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMovies;
    }

    public ArrayList<HashMap<String,String>> getWatchList() {
        ArrayList<HashMap<String,String>> allMovies = new ArrayList<HashMap<String,String>>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_MOVIEDETAILS,
                    new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH,
                            COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_WATCHLIST}
                    , COLUMN_IS_WATCHLIST + "=?", new String[]{String.valueOf(1)}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> movieInfo = new HashMap<String, String>();
                    movieInfo.put("id", cursor.getString(0));
                    movieInfo.put("title", cursor.getString(1));
                    movieInfo.put("release_date", cursor.getString(2));
                    movieInfo.put("poster_path", cursor.getString(3));
                    movieInfo.put("vote_average", cursor.getString(4));
                    movieInfo.put("vote_count", cursor.getString(5));
                    movieInfo.put("WL", String.valueOf(cursor.getInt(6)));
                    if (cursor.getInt(6) == 1) {
                        allMovies.add(movieInfo);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            detailScreen d=new detailScreen();
            Toast.makeText(d.context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return allMovies;
    }

    public void deleteNonFavWatchMovie() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIEDETAILS, COLUMN_IS_FAVORITE + "=? AND "+COLUMN_IS_WATCHLIST+"=?", new String[] {String.valueOf(0),String.valueOf(0)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIEDETAILS);
        onCreate(db);
    }
}
