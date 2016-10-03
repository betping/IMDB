package acadgild.imdb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tungenwar on 25/04/2015.
 */
public class launchScreen extends ActionBarActivity {

    private String url = "http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
    private static final String TAG_MOVIES = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_RELEASE_DATE = "release_date";
    private static final String TAG_POPULARITY= "popularity";
    private static final String TAG_VOTE_COUNT= "vote_count";
    private static final String TAG_VOTE_AVERAGE= "vote_average";
    private static final String TAG_POSTER_PATH="poster_path";

    JSONArray movies = null;

    ArrayList<HashMap<String, String>> movieList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);
        lv=(ListView)findViewById(R.id.listView);
        new GetMovies().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(launchScreen.this,detailScreen.class);
                intent.putExtra("id",movieList.get(i).get(TAG_ID));
                startActivity(intent);
            }
        });
    }

    private class GetMovie extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            movieList = new ArrayList<HashMap<String,String>>();

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject c = new JSONObject(jsonStr);

                    String id = c.getString(TAG_ID);
                    String title = c.getString(TAG_TITLE);
                    String release_date = c.getString(TAG_RELEASE_DATE);
                    String popularity = c.getString(TAG_POPULARITY);
                    String vote_average = c.getString(TAG_VOTE_AVERAGE);
                    String vote_count = c.getString(TAG_VOTE_COUNT);
                    String poster_path="http://image.tmdb.org/t/p/w45"+c.getString(TAG_POSTER_PATH);

                    HashMap<String, String> contact = new HashMap<String, String>();

                    contact.put(TAG_ID, id);
                    contact.put(TAG_TITLE, title);
                    contact.put(TAG_RELEASE_DATE, release_date);
                    contact.put(TAG_POPULARITY, popularity);
                    contact.put(TAG_VOTE_AVERAGE, vote_average);
                    contact.put(TAG_VOTE_COUNT, vote_count);
                    contact.put(TAG_POSTER_PATH, poster_path);

                    movieList.add(contact);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(launchScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(launchScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MyAdapter adapter=new MyAdapter(launchScreen.this,movieList);
            lv.setAdapter(adapter);
        }

    }

    private class GetMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            movieList = new ArrayList<HashMap<String,String>>();

            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    movies = jsonObj.getJSONArray(TAG_MOVIES);

                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String title = c.getString(TAG_TITLE);
                        String release_date = c.getString(TAG_RELEASE_DATE);
                        String popularity = c.getString(TAG_POPULARITY);
                        String vote_average = c.getString(TAG_VOTE_AVERAGE);
                        String vote_count = c.getString(TAG_VOTE_COUNT);
                        String poster_path="http://image.tmdb.org/t/p/w45"+c.getString(TAG_POSTER_PATH);

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, id);
                        contact.put(TAG_TITLE, title);
                        contact.put(TAG_RELEASE_DATE, release_date);
                        contact.put(TAG_POPULARITY, popularity);
                        contact.put(TAG_VOTE_AVERAGE, vote_average);
                        contact.put(TAG_VOTE_COUNT, vote_count);
                        contact.put(TAG_POSTER_PATH, poster_path);

                        movieList.add(contact);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(launchScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    Toast.makeText(launchScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MyAdapter adapter=new MyAdapter(launchScreen.this,movieList);
            lv.setAdapter(adapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoptions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mostPopular:
                url="http://api.themoviedb.org/3/movie/popular?api_key=8496be0b2149805afa458ab8ec27560c";
                new GetMovies().execute();
                return true;
            case R.id.upcomingMovies:
                url="http://api.themoviedb.org/3/movie/upcoming?api_key=8496be0b2149805afa458ab8ec27560c";
                new GetMovies().execute();
                return true;
            case R.id.nowPlaying:
                url="http://api.themoviedb.org/3/movie/now_playing?api_key=8496be0b2149805afa458ab8ec27560c";
                new GetMovies().execute();
                return true;
            case R.id.topRated:
                url="http://api.themoviedb.org/3/movie/top_rated?api_key=8496be0b2149805afa458ab8ec27560c";
                new GetMovies().execute();
                return true;
            case R.id.latestMovies:
                url="http://api.themoviedb.org/3/movie/latest?api_key=8496be0b2149805afa458ab8ec27560c";
                new GetMovie().execute();
                return true;
            case R.id.myFavorite:
                Intent intent=new Intent(launchScreen.this,listScreen.class);
                intent.putExtra("id","1");
                startActivity(intent);
                return true;
            case R.id.myWatchlist:
                Intent intent2=new Intent(launchScreen.this,listScreen.class);
                intent2.putExtra("id","0");
                startActivity(intent2);
                return true;
            case R.id.Refresh:
                MyAdapter adapter=new MyAdapter(launchScreen.this,movieList);
                lv.setAdapter(adapter);
        }
    return super.onOptionsItemSelected(item);
    }
}
