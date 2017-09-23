package com.example.cobus.simpleweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.WeatherDataViewHolder>{

    private WeatherDataItem[] weatherDataItems;
    private Context context;

    public RecyclerViewAdapter(Context context, WeatherDataItem[] weatherDataItems) {
        this.weatherDataItems = weatherDataItems;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return weatherDataItems.length;
    }

    @Override
    public WeatherDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new WeatherDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherDataViewHolder holder, int position) {
        holder.bind(weatherDataItems[position]);
    }

    class WeatherDataViewHolder extends RecyclerView.ViewHolder{

        TextView listItemDay;
        TextView listItemMaxTemp;
        TextView listItemMinTemp;
        ImageView listItemWeatherIcon;

        public WeatherDataViewHolder(View itemView) {
            super(itemView);

            listItemDay = (TextView) itemView.findViewById(R.id.tv_list_item_day);
            listItemMaxTemp = (TextView) itemView.findViewById(R.id.tv_list_item_max);
            listItemMinTemp = (TextView) itemView.findViewById(R.id.tv_list_item_min);
            listItemWeatherIcon = (ImageView) itemView.findViewById(R.id.iv_list_item_image);
        }

        public void bind(WeatherDataItem item){
            try{
                listItemDay.setText(item.getDayOfWeek());
            }catch (WeatherDataItem.WeatherItemException e){
                listItemDay.setText(R.string.literal_error);
            }
            listItemMaxTemp.setText(item.getmMaxTemp());
            listItemMinTemp.setText(item.getmMinTemp());
            listItemWeatherIcon.setImageResource(item.getmIcon());
        }
    }
}
