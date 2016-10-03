package acadgild.imdb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import acadgild.imdb.Model.Casts;
import acadgild.imdb.Utils.ImageLoader;

/**
 * Created by Tungenwar on 10/05/2015.
 */
public class detailScreen extends ActionBarActivity {
    private static String url = "http://api.themoviedb.org/3/movie/";
    private static String cast_url ;
    private static String detail_url ;
    private static String trailer_url ;
    private static String poster_url ;
    private static final String TAG_TRAILERS = "results";
    private static final String TAG_BACKDROPS = "backdrops";
    private static final String TAG_CAST="cast";
    private static final String TAG_CREW="crew";
    private static final String TAG_ID = "id";
    private static final String TAG_KEY="key";
    private static final String TAG_IMDB_ID = "imdb_id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_RELEASE_DATE = "release_date";
    private static final String TAG_BUDGET= "budget";
    private static final String TAG_POPULARITY= "popularity";
    private static final String TAG_VOTE_COUNT= "vote_count";
    private static final String TAG_VOTE_AVERAGE= "vote_average";
    private static final String TAG_POSTER_PATH="poster_path";
    private static final String TAG_REVENUE="revenue";
    private static final String TAG_DESCRIPTION="overview";
    private static final String TAG_TAG_LINE="tagline";
    private static final String TAG_STATUS="status";
    private static final String TAG_CAST_ID="cast_id";
    private static final String TAG_CHARACTER="character";
    private static final String TAG_NAME="name";
    private static final String TAG_PROFILE_PATH="profile_path";
    private static final String TAG_CREDIT_ID="credit_id";
    private static final String TAG_DEPARTMENT="department";
    private static final String TAG_FILE_PATH="file_path";
    TextView title;
    TextView tag_line;
    TextView r_date;
    TextView budget;
    TextView revenue;
    TextView status;
    TextView vote_average;
    TextView description;
    RatingBar Popularity;
    RatingBar user_rating;
    ImageView poster;
    ImageView favourite;
    ImageView watchlist;
    ImageLoader imageLoader;
    JSONArray casts = null;
    JSONArray crews = null;
    JSONArray trailers = null;
    JSONArray posters = null;
    Context context;
    Movies movie;
    Bundle bundle;

    ArrayList<HashMap<String, String>> castList;
    ArrayList<HashMap<String, String>> trailerList;
    ArrayList<HashMap<String, String>> posterList;
    ArrayList<HashMap<String, String>> crewList;
    ArrayList<HashMap<String, String>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);
        context=this;
        bundle = getIntent().getExtras();
        detail_url = url + bundle.getString("id") + "?api_key=8496be0b2149805afa458ab8ec27560c";
        trailer_url = url + bundle.getString("id") + "/videos?api_key=8496be0b2149805afa458ab8ec27560c";
        poster_url = url + bundle.getString("id") + "/images?api_key=8496be0b2149805afa458ab8ec27560c";
        title = (TextView) findViewById(R.id.title);
        tag_line = (TextView) findViewById(R.id.tag_line);
        r_date = (TextView) findViewById(R.id.r_date);
        budget = (TextView) findViewById(R.id.budget);
        revenue = (TextView) findViewById(R.id.revenue);
        status = (TextView) findViewById(R.id.status);
        vote_average = (TextView) findViewById(R.id.vote_average);
        description = (TextView) findViewById(R.id.description);
        poster = (ImageView) findViewById(R.id.imageView2);
        favourite = (ImageView) findViewById(R.id.imageView3);
        watchlist = (ImageView) findViewById(R.id.imageView4);
        imageLoader = new ImageLoader(this.getApplicationContext());
        Popularity = (RatingBar) findViewById(R.id.ratingBar2);
        user_rating = (RatingBar) findViewById(R.id.ratingBar3);
        try {
            checkMovie(bundle.getString("id"));
        }
        catch (Exception e){
        }
        new GetDetails().execute();
        new GetCast().execute();
        new GetTrailers().execute();
        new GetPosters().execute();
        movie = new Movies();
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMovie(bundle.getString("id"));
                Object tag =  favourite.getTag();
                movie._id=movieList.get(0).get(TAG_ID);
                movie._title=movieList.get(0).get(TAG_TITLE);
                movie._poster_path=movieList.get(0).get(TAG_POSTER_PATH);
                movie.release__date=movieList.get(0).get(TAG_RELEASE_DATE);
                movie._vote_average=movieList.get(0).get(TAG_VOTE_AVERAGE);
                movie._vote_count=movieList.get(0).get(TAG_VOTE_COUNT);
                if (tag == "disable") {
                    favourite.setImageResource(R.drawable.favorite_enable_normal);
                    favourite.setTag("enable");
                    movie.setIsFavorite(String.valueOf(1));
                    Database db = new Database(detailScreen.this);
                    boolean check = db.checkMovie(movie.getID());
                    if (check)
                        db.updateMovieF(movie);
                    else
                        db.addMovie(movie);
                } else {
                    favourite.setImageResource(R.drawable.favorite_disable_normal);
                    favourite.setTag("disable");
                    movie.setIsFavorite(String.valueOf(0));
                    Database db = new Database(detailScreen.this);
                    db.updateMovieF(movie);
                    db.deleteNonFavWatchMovie();
                }
            }
        });

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMovie(bundle.getString("id"));
                Object tag = watchlist.getTag();
                movie._id=movieList.get(0).get(TAG_ID);
                movie._title=movieList.get(0).get(TAG_TITLE);
                movie._poster_path=movieList.get(0).get(TAG_POSTER_PATH);
                movie.release__date=movieList.get(0).get(TAG_RELEASE_DATE);
                movie._vote_average=movieList.get(0).get(TAG_VOTE_AVERAGE);
                movie._vote_count=movieList.get(0).get(TAG_VOTE_COUNT);
                if (tag == "disable") {
                    watchlist.setImageResource(R.drawable.watchlist_enable_normal);
                    watchlist.setTag("enable");
                    movie.setIsWatchlist(String.valueOf(1));
                    Database db = new Database(detailScreen.this);
                    boolean check = db.checkMovie(movie.getID());
                    if (check)
                        db.updateMovieW(movie);
                    else
                        db.addMovie(movie);
                } else {
                    watchlist.setImageResource(R.drawable.watchlist_disable_normal);
                    watchlist.setTag("disable");
                    movie.setIsWatchlist(String.valueOf(0));
                    Database db = new Database(detailScreen.this);
                    db.updateMovieW(movie);
                    db.deleteNonFavWatchMovie();
                }
            }
        });
    }

    private void checkMovie(String id) {

        Database db = new Database(detailScreen.this);
        db.deleteNonFavWatchMovie();
        Boolean check = db.checkMovie(id);
        if (!check) { //checks if movie does not existing in database
            favourite.setImageResource(R.drawable.favorite_disable_normal);
            favourite.setTag("disable");
            watchlist.setImageResource(R.drawable.watchlist_disable_normal);
            watchlist.setTag("disable");
        } else { //if movie does exist
            Movies movieInfo = db.getMovie(id);
            if (movieInfo.getIsFavorite().equals("0")) { //set image based on database value
                favourite.setImageResource(R.drawable.favorite_disable_normal);
                favourite.setTag("disable");
                movie.setIsFavorite(String.valueOf(0));

            } else  {
                favourite.setImageResource(R.drawable.favorite_enable_normal);
                favourite.setTag("enable");
                movie.setIsFavorite(String.valueOf(1));
            }

            if (movieInfo.getIsWatchlist().equals("0")) { //set image based on database value
                watchlist.setImageResource(R.drawable.watchlist_disable_normal);
                watchlist.setTag("disable");
                movie.setIsWatchlist(String.valueOf(0));
            } else {
                watchlist.setImageResource(R.drawable.watchlist_enable_normal);
                watchlist.setTag("enable");
                movie.setIsWatchlist(String.valueOf(1));
            }
        }
    }


    private void showCasts(ArrayList<HashMap<String, String>> cast) {
        LinearLayout castsSection = (LinearLayout) findViewById(R.id.casts_section);
        List<Casts> casts = new ArrayList<Casts>();

        for(int i=0;i<cast.size();i++){
            Casts c=new Casts();
            c.setId(cast.get(i).get(TAG_CAST_ID));
            c.setName(cast.get(i).get(TAG_NAME));
            c.setCharacter(cast.get(i).get(TAG_CHARACTER));
            c.setProfilePath(cast.get(i).get(TAG_PROFILE_PATH));

            casts.add(c);
        }


        if (casts != null && !casts.isEmpty()) {
            castsSection.setVisibility(View.VISIBLE);
            setCasts(casts);
        } else {
            castsSection.setVisibility(View.GONE);
        }
    }

    // Add multiple crews in the casts container
    private void setCasts(List<Casts> casts) {
        LinearLayout castsContainer = (LinearLayout) findViewById(R.id.casts_container);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ImageLoader imageLoader = new ImageLoader(this.getApplicationContext());

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Casts cast = casts.get(i);

            if (cast != null) {
                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(
                        R.layout.clickable_column, null);
                ImageView thumbnailImage = (ImageView) clickableColumn
                        .findViewById(R.id.thumbnail_image);
                TextView titleView = (TextView) clickableColumn
                        .findViewById(R.id.title_view);
                TextView subTitleView = (TextView) clickableColumn
                        .findViewById(R.id.subtitle_view);

                imageLoader.DisplayImage(cast.getProfilePath(), thumbnailImage);
                titleView.setText(cast.getName());
                subTitleView.setText(cast.getCharacter());

                clickableColumn.setTag(cast);
                clickableColumn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Casts cast = (Casts) v.getTag();

                        Toast.makeText(getApplicationContext(), cast.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(
                            R.layout.horizontal_divider, null));
                }
            }
        }
    }

    private void showCrews(ArrayList<HashMap<String, String>> cast) {
        LinearLayout castsSection = (LinearLayout) findViewById(R.id.crews_section);
        List<Casts> casts = new ArrayList<Casts>();

        for(int i=0;i<cast.size();i++){
            Casts c=new Casts();
            c.setId(cast.get(i).get(TAG_CAST_ID));
            c.setName(cast.get(i).get(TAG_NAME));
            c.setCharacter(cast.get(i).get(TAG_CHARACTER));
            c.setProfilePath(cast.get(i).get(TAG_PROFILE_PATH));

            casts.add(c);
        }


        if (casts != null && !casts.isEmpty()) {
            castsSection.setVisibility(View.VISIBLE);
            setCrews(casts);
        } else {
            castsSection.setVisibility(View.GONE);
        }
    }

    // Add multiple crews in the casts container
    private void setCrews(List<Casts> casts) {
        LinearLayout castsContainer = (LinearLayout) findViewById(R.id.crews_container);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ImageLoader imageLoader = new ImageLoader(this.getApplicationContext());

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Casts cast = casts.get(i);

            if (cast != null) {
                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(
                        R.layout.clickable_column, null);
                ImageView thumbnailImage = (ImageView) clickableColumn
                        .findViewById(R.id.thumbnail_image);
                TextView titleView = (TextView) clickableColumn
                        .findViewById(R.id.title_view);
                TextView subTitleView = (TextView) clickableColumn
                        .findViewById(R.id.subtitle_view);

                imageLoader.DisplayImage(cast.getProfilePath(), thumbnailImage);
                titleView.setText(cast.getName());
                subTitleView.setText(cast.getCharacter());

                clickableColumn.setTag(cast);
                clickableColumn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Casts cast = (Casts) v.getTag();

                        Toast.makeText(getApplicationContext(), cast.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(
                            R.layout.horizontal_divider, null));
                }
            }
        }
    }

    private void showTrailers(ArrayList<HashMap<String, String>> cast) {
        LinearLayout castsSection = (LinearLayout) findViewById(R.id.trailers_section);
        List<Casts> casts = new ArrayList<Casts>();

        for(int i=0;i<cast.size();i++){
            Casts c=new Casts();
            c.setId(cast.get(i).get(TAG_ID));
            c.setName(cast.get(i).get(TAG_NAME));
            c.setCharacter(cast.get(i).get(TAG_KEY));

            casts.add(c);
        }


        if (casts != null && !casts.isEmpty()) {
            castsSection.setVisibility(View.VISIBLE);
            setTrailers(casts);
        } else {
            castsSection.setVisibility(View.GONE);
        }
    }

    // Add multiple crews in the casts container
    private void setTrailers(List<Casts> casts) {
        LinearLayout castsContainer = (LinearLayout) findViewById(R.id.trailers_container);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Casts cast = casts.get(i);

            if (cast != null) {
                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(
                        R.layout.trailers_column, null);
                TextView subTitleView = (TextView) clickableColumn
                        .findViewById(R.id.trailer_view);

                subTitleView.setText(cast.getName());

                clickableColumn.setTag(cast);
                clickableColumn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Casts cast = (Casts) v.getTag();
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+cast.getCharacter()));
                        startActivity(intent);
                    }
                });

                castsContainer.addView(clickableColumn);

                if (i != size - 1) {
                    castsContainer.addView(inflater.inflate(
                            R.layout.horizontal_divider, null));
                }
            }
        }
    }

    private void showPosters(ArrayList<HashMap<String, String>> cast) {
        LinearLayout castsSection = (LinearLayout) findViewById(R.id.posters_section);
        List<Casts> casts = new ArrayList<Casts>();

        for(int i=0;i<cast.size();i++){
            Casts c=new Casts();
            c.setProfilePath(cast.get(i).get(TAG_ID));

            casts.add(c);
        }


        if (casts != null && !casts.isEmpty()) {
            castsSection.setVisibility(View.VISIBLE);
            setPosters(casts);
        } else {
            castsSection.setVisibility(View.GONE);
        }
    }

    // Add multiple crews in the casts container
    private void setPosters(List<Casts> casts) {
        LinearLayout castsContainer = (LinearLayout) findViewById(R.id.posters_container);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        ImageLoader imageLoader = new ImageLoader(this.getApplicationContext());

        int size = casts.size();
        for (int i = 0; i < size; i++) {
            Casts cast = casts.get(i);

            if (cast != null) {
                LinearLayout clickableColumn = (LinearLayout) inflater.inflate(
                        R.layout.posters_column, null);
                ImageView thumbnailImage = (ImageView) clickableColumn
                        .findViewById(R.id.thumbnail_image1);

                imageLoader.DisplayImage(cast.getProfilePath(), thumbnailImage);

                clickableColumn.setTag(cast);
                clickableColumn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Casts cast = (Casts) v.getTag();
                    }
                });

                castsContainer.addView(clickableColumn);
            }
        }
    }

    private class GetCast extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            castList=new ArrayList<HashMap<String, String>>();
            crewList=new ArrayList<HashMap<String, String>>();
            cast_url = "http://api.themoviedb.org/3/movie/" + movieList.get(0).get(TAG_IMDB_ID) + "/credits?api_key=8496be0b2149805afa458ab8ec27560c";

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(cast_url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    casts = jsonObj.getJSONArray(TAG_CAST);
                    for (int i = 0; i < casts.length(); i++) {
                        JSONObject c = casts.getJSONObject(i);

                        String cast_id = c.getString(TAG_CAST_ID);
                        String character = c.getString(TAG_CHARACTER);
                        String name = c.getString(TAG_NAME);
                        String profile_path="http://image.tmdb.org/t/p/w45"+c.getString(TAG_PROFILE_PATH);

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_CAST_ID, cast_id);
                        contact.put(TAG_CHARACTER, character);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_PROFILE_PATH, profile_path);

                        castList.add(contact);
                    }
                    crews = jsonObj.getJSONArray(TAG_CREW);
                    for (int i = 0; i < crews.length(); i++) {
                        JSONObject c = crews.getJSONObject(i);

                        String crew_id = c.getString(TAG_CREDIT_ID);
                        String department = c.getString(TAG_DEPARTMENT);
                        String name = c.getString(TAG_NAME);
                        String profile_path="http://image.tmdb.org/t/p/w45"+c.getString(TAG_PROFILE_PATH);

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_CAST_ID, crew_id);
                        contact.put(TAG_CHARACTER, department);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_PROFILE_PATH, profile_path);

                        crewList.add(contact);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showCasts(castList);
            showCrews(crewList);
        }
    }

    private class GetPosters extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            posterList=new ArrayList<HashMap<String, String>>();

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(poster_url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    posters = jsonObj.getJSONArray(TAG_BACKDROPS);
                    for (int i = 0; i < posters.length(); i++) {
                        JSONObject c = posters.getJSONObject(i);

                        String file_path ="http://image.tmdb.org/t/p/w500" + c.getString(TAG_FILE_PATH);

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, file_path);

                        posterList.add(contact);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showPosters(posterList);
        }
    }

    private class GetTrailers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            trailerList=new ArrayList<HashMap<String, String>>();

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(trailer_url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    trailers = jsonObj.getJSONArray(TAG_TRAILERS);
                    for (int i = 0; i < trailers.length(); i++) {
                        JSONObject c = trailers.getJSONObject(i);

                        String cast_id = c.getString(TAG_ID);
                        String key = c.getString(TAG_KEY);
                        String name = c.getString(TAG_NAME);

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, cast_id);
                        contact.put(TAG_KEY, key);
                        contact.put(TAG_NAME, name);

                        trailerList.add(contact);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showTrailers(trailerList);
        }
    }

    private class GetDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            movieList=new ArrayList<HashMap<String, String>>();

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(detail_url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);

                    String id = c.getString(TAG_ID);
                    String title = c.getString(TAG_TITLE);
                    String release_date = c.getString(TAG_RELEASE_DATE);
                    String popularity = c.getString(TAG_POPULARITY);
                    String budget = c.getString(TAG_BUDGET);
                    String vote_average = c.getString(TAG_VOTE_AVERAGE);
                    String vote_count = c.getString(TAG_VOTE_COUNT);
                    String description = c.getString(TAG_DESCRIPTION);
                    String revenue = c.getString(TAG_REVENUE);
                    String tag_line = c.getString(TAG_TAG_LINE);
                    String status = c.getString(TAG_STATUS);
                    String poster_path="http://image.tmdb.org/t/p/w500"+c.getString(TAG_POSTER_PATH);
                    String imdb_id=c.getString(TAG_IMDB_ID);

                    HashMap<String, String> contact = new HashMap<String, String>();

                    contact.put(TAG_ID, id);
                    contact.put(TAG_TITLE, title);
                    contact.put(TAG_RELEASE_DATE, release_date);
                    contact.put(TAG_POPULARITY, popularity);
                    contact.put(TAG_BUDGET, budget);
                    contact.put(TAG_VOTE_AVERAGE, vote_average);
                    contact.put(TAG_VOTE_COUNT, vote_count);
                    contact.put(TAG_POSTER_PATH, poster_path);
                    contact.put(TAG_DESCRIPTION, description);
                    contact.put(TAG_REVENUE, revenue);
                    contact.put(TAG_TAG_LINE, tag_line);
                    contact.put(TAG_STATUS, status);
                    contact.put(TAG_IMDB_ID,imdb_id);


                    movieList.add(contact);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            title.setText(movieList.get(0).get(TAG_TITLE));
            r_date.setText(movieList.get(0).get(TAG_RELEASE_DATE));
            tag_line.setText(movieList.get(0).get(TAG_TAG_LINE));
            budget.setText("Budget:"+movieList.get(0).get(TAG_BUDGET));
            revenue.setText("Revenue:"+movieList.get(0).get(TAG_REVENUE));
            Popularity.setRating(Float.parseFloat(movieList.get(0).get(TAG_VOTE_AVERAGE))/2);
            user_rating.setRating(Float.parseFloat(movieList.get(0).get(TAG_VOTE_AVERAGE))/10);
            status.setText("Status:" + movieList.get(0).get(TAG_STATUS));
            vote_average.setText("(" + movieList.get(0).get(TAG_VOTE_AVERAGE) + "/10)" + movieList.get(0).get(TAG_VOTE_COUNT) + " users");
            description.setText(movieList.get(0).get(TAG_DESCRIPTION));
            imageLoader.DisplayImage(movieList.get(0).get(TAG_POSTER_PATH),poster);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_screen_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.myFavorite:
                Intent intent=new Intent(detailScreen.this,listScreen.class);
                intent.putExtra("id","1");
                startActivity(intent);
                return true;
            case R.id.myWatchlist:
                Intent intent2=new Intent(detailScreen.this,listScreen.class);
                intent2.putExtra("id","0");
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
