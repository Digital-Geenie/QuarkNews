package org.anirudh.redquark.quarknews.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.anirudh.redquark.quarknews.R;
import org.anirudh.redquark.quarknews.WebActivity;
import org.anirudh.redquark.quarknews.model.NewsItem;
import org.anirudh.redquark.quarknews.util.SharedPreference;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<NewsItem> {

    private Activity activity;
    private Typeface typeface;
    private Context context;
    private SharedPreference sharedPreference;
    private List<NewsItem> newsItems;

    public CustomListViewAdapter(Context context, Activity activity, int resource,
                                 List<NewsItem> items, Typeface typeface) {
        super(activity, resource, items);
        this.activity = activity;
        this.typeface = typeface;
        this.newsItems = items;
        this.context = context;
        sharedPreference = new SharedPreference();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from the XML file
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.item_listview, parent, false);
            }
            //get all UI view
            holder = new ViewHolder(convertView);
            // set Tag for holder
            if (convertView != null) {
                convertView.setTag(holder);
            }
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        final NewsItem item = getItem(position);
        if (item != null) {
            holder.title.setText(item.getTitle());
            holder.publisher.setText(String.format("From: %s", item.getPublisher()));
            //  holder.timestamp.setText((int) item.getTimestamp());
            String businessCategory = item.getCategory();

            switch (businessCategory) {

                case "b":
                    businessCategory = "Business";
                    break;

                case "t":
                    businessCategory = "Science & Technology";
                    break;

                case "e":
                    businessCategory = "Entertainment";
                    break;

                case "m":
                    businessCategory = "Health";
            }
            holder.category.setText(String.format("Category: %s", businessCategory));

            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, WebActivity.class);
                    intent.putExtra("loadUrl", item.getUrl());
                    activity.startActivity(intent);
                }
            });

            holder.viewStory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, WebActivity.class);
                    intent.putExtra("loadUrl", item.getUrl());
                    activity.startActivity(intent);
                }
            });

            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean flag = checkFavoriteItem(item);
                    if(!flag){
                        sharedPreference.addFavorite(activity, item);
                    }
                    Toast.makeText(activity, "Item added to Favorites", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /*If a product exists in shared preferences then set heart_red drawable
         * and set a tag*/
        if (checkFavoriteItem(item)) {
            holder.favorite.setTypeface(typeface);
            holder.favorite.setTag("red");
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView title, category;
        private TextView publisher;
        private Button favorite, viewStory;

        ViewHolder(View v) {
            title = v.findViewById(R.id.title);
            publisher = v.findViewById(R.id.publisher);
            favorite = v.findViewById(R.id.favorite);
            viewStory = v.findViewById(R.id.viewStory);
            category = v.findViewById(R.id.category);
        }
    }

    /*Checks whether a particular product exists in SharedPreferences*/
    private boolean checkFavoriteItem(NewsItem newsItem) {
        boolean check = false;
        List<NewsItem> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (NewsItem product : favorites) {
                if (product.equals(newsItem)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(NewsItem newsItem) {
        super.add(newsItem);
        newsItems.add(newsItem);
        notifyDataSetChanged();
    }

    @Override
    public void remove(NewsItem newsItem) {
        super.remove(newsItem);
        newsItems.remove(newsItem);
        notifyDataSetChanged();
    }
}