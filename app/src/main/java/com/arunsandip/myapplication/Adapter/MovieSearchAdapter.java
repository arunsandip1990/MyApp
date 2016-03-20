package com.arunsandip.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.arunsandip.myapplication.AppController;
import com.arunsandip.myapplication.R;
import com.arunsandip.myapplication.Util.MovieSearchPojo;

import java.util.ArrayList;

/**
 * Created by Arun on 3/18/2016.
 */
public class MovieSearchAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<MovieSearchPojo> alMovie;
    private static LayoutInflater inflater=null;
    MovieSearchPojo tempValues=null;
    int i=0;

    public MovieSearchAdapter(Activity a, ArrayList<MovieSearchPojo>  alMovie) {

        activity = a;
        this.alMovie=alMovie;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(alMovie.size()<=0)
            return 1;
        return alMovie.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView textTitle;
        public TextView textType;
        public TextView textYear;
        public NetworkImageView imageMovieThumbnail;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.inflater_movie_list, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.textTitle = (TextView) vi.findViewById(R.id.textTitle);
            holder.textType=(TextView)vi.findViewById(R.id.texType);
            holder.textYear=(TextView)vi.findViewById(R.id.textYear);
            holder.imageMovieThumbnail=(NetworkImageView)vi.findViewById(R.id.imageMovieThumbnail);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(alMovie.size()<=0)
        {
            holder.textTitle.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( MovieSearchPojo ) alMovie.get( position );
            holder.textTitle.setText( tempValues.getTitle() );
            holder.textType.setText( tempValues.getType() );
            holder.textYear.setText(tempValues.getYear());


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

            holder.imageMovieThumbnail.setImageUrl(tempValues.getPoster(), imageLoader);

        }
        return vi;
    }
}