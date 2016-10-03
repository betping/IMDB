package acadgild.imdb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import acadgild.imdb.Utils.ImageLoader;

/**
 * Created by Tungenwar on 05/05/2015.
 */
public class MyAdapter extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    private ArrayList<HashMap<String, String>> movieList=new ArrayList<HashMap<String,String>>();

    public MyAdapter(Activity a, ArrayList<HashMap<String, String>> movieList) {
        activity = a;
        this.movieList=movieList;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return movieList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public TextView text2;
        public TextView text3;
        public RatingBar ratingBar;
        public RatingBar user_rating;
        public ImageView image;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            vi = inflater.inflate(R.layout.movielist, null);

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.textView);
            holder.text1=(TextView)vi.findViewById(R.id.textView2);
            holder.text2=(TextView)vi.findViewById(R.id.textView3);
            holder.text3=(TextView)vi.findViewById(R.id.textView4);
            holder.ratingBar=(RatingBar)vi.findViewById(R.id.ratingBar);
            holder.user_rating=(RatingBar)vi.findViewById(R.id.user_rating);
            holder.image=(ImageView)vi.findViewById(R.id.imageView);

            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        holder.text.setText(movieList.get(position).get("title"));
        holder.text1.setText("Released on : "+movieList.get(position).get("release_date"));
        holder.ratingBar.setRating(Float.parseFloat(movieList.get(position).get("vote_average"))/2);
        holder.text3.setText("("+movieList.get(position).get("vote_average")+"/10) voted by "+movieList.get(position).get("vote_count")+" users");
        holder.user_rating.setRating(Float.parseFloat(movieList.get(position).get("vote_average"))/10);
        ImageView image = holder.image;

        imageLoader.DisplayImage(movieList.get(position).get("poster_path"), image);
        return vi;
    }
}
