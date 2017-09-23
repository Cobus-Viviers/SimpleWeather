package com.example.cobus.simpleweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.WeatherDataViewHolder>{

    private WeatherDataItem[] mWeatherDataItems;
    private Context mContext;

    RecyclerViewAdapter(Context context, WeatherDataItem[] weatherDataItems) {
        this.mWeatherDataItems = weatherDataItems;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mWeatherDataItems.length;
    }

    @Override
    public WeatherDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new WeatherDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherDataViewHolder holder, int position) {
        holder.bind(mWeatherDataItems[position]);
    }

    class WeatherDataViewHolder extends RecyclerView.ViewHolder{

        TextView listItemDay;
        TextView listItemMaxTemp;
        TextView listItemMinTemp;
        ImageView listItemWeatherIcon;

        WeatherDataViewHolder(View itemView) {
            super(itemView);

            listItemDay = (TextView) itemView.findViewById(R.id.tv_list_item_day);
            listItemMaxTemp = (TextView) itemView.findViewById(R.id.tv_list_item_max);
            listItemMinTemp = (TextView) itemView.findViewById(R.id.tv_list_item_min);
            listItemWeatherIcon = (ImageView) itemView.findViewById(R.id.iv_list_item_image);
        }

        void bind(WeatherDataItem item){
            try{
                listItemDay.setText(item.getDayOfWeek());
            }catch (WeatherDataItem.WeatherItemException e){
                listItemDay.setText(R.string.literal_error);
            }
            listItemMaxTemp.setText(item.getMaxTemp());
            listItemMinTemp.setText(item.getMinTemp());
            listItemWeatherIcon.setImageResource(item.getIcon());
        }
    }
}
