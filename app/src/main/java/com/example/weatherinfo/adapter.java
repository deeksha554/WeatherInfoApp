package com.example.weatherinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder>{
    List<modal> l1;
    Context context;

    public adapter(List<modal> l1, Context context) {
        this.l1 = l1;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        modal modal1 = l1.get(position);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(modal1.getTime());
            holder.tvi1.setText(output.format(t));
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        holder.tvi2.setText(modal1.temper);
        holder.tvi3.setText(modal1.wind);
        holder.cv.setBackgroundResource(modal1.getBack());
        Picasso.with(context).load("https:".concat(modal1.getImage())).into(holder.img1);
    }

    @Override
    public int getItemCount() {
        return l1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvi1,tvi2,tvi3;
        ImageView img1;
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvi1 = itemView.findViewById(R.id.textView);
            tvi2 = itemView.findViewById(R.id.textView2);
            tvi3 = itemView.findViewById(R.id.textView3);
            img1 = itemView.findViewById(R.id.imageView);
            cv = itemView.findViewById(R.id.cv);
        }
    }
}
