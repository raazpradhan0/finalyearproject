package com.example.restaurantlocator.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.restaurantlocator.Interface.ItemClickListner;
import com.example.restaurantlocator.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtRestauranttName, txtRestaurantDescription, txtRestaurantCatagory;
    public ImageView imageView;
    public ItemClickListner listner;

    public RestaurantViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.restaurant_image);
        txtRestauranttName = (TextView) itemView.findViewById(R.id.restaurant_name);
        txtRestaurantDescription = (TextView) itemView.findViewById(R.id.restaurant_description);
        txtRestaurantCatagory = itemView.findViewById(R.id.restaurant_catagory);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);

    }
}
