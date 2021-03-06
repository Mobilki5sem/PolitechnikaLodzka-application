package com.team.szkielet.event;

import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.team.szkielet.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> listOfEvents;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onLongClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgV;
        public TextView tvNameEvent, tvMore, tvDescription;
        public CardView cvID;
        public LinearLayout llWitam;
        public ImageView ivBackground;

        public EventViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgV = itemView.findViewById(R.id.imgV);
            tvNameEvent = itemView.findViewById(R.id.tvNameEvent);
            tvMore = itemView.findViewById(R.id.tvMore);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            cvID = itemView.findViewById(R.id.cvID);
            llWitam = itemView.findViewById(R.id.llWitam);
            ivBackground = itemView.findViewById(R.id.ivBackground);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onLongClicked(position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public EventAdapter(ArrayList<Event> list) {
        listOfEvents = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        EventViewHolder evh = new EventViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = listOfEvents.get(position);
      
        String date = currentEvent.getDay() + "." + currentEvent.getMonth() + "." +  currentEvent.getYear();
        //String userEmail = "Dodane przez: " + currentEvent.getUserEmail();
        //holder.imgV.setImageResource(currentEvent.getImage());
        if (currentEvent.getImage().equals("Spotkanie")) {
            holder.imgV.setImageResource(R.drawable.ic_meeting_lol);
        } else if (currentEvent.getImage().equals("Impreza")) {
            holder.imgV.setImageResource(R.drawable.ic_confetti);
        } else if (currentEvent.getImage().equals("Prezentacja")) {
            holder.imgV.setImageResource(R.drawable.ic_classroom);
        } else if (currentEvent.getImage().equals("Wydarzenie PŁ")) {
            holder.imgV.setImageResource(R.drawable.ic_school);
        }
        holder.tvNameEvent.setText(currentEvent.getEventName());
        holder.tvDescription.setText(currentEvent.getDescription());
        holder.tvMore.setText(date);
        if (currentEvent.getLinkToEvent().equals("noLink")) {
            holder.ivBackground.setBackgroundResource(R.drawable.background_no_link);
        } else {
            holder.ivBackground.setBackgroundResource(R.drawable.background_link);
        }
    }

    @Override
    public int getItemCount() {
        return listOfEvents.size();
    }
}
