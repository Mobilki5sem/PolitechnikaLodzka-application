package com.team.szkielet.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.team.szkielet.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> listOfEvents;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgV;
        public TextView tvNameEvent, tvMore, tvDescription;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imgV = itemView.findViewById(R.id.imgV);
            tvNameEvent = itemView.findViewById(R.id.tvNameEvent);
            tvMore = itemView.findViewById(R.id.tvMore);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    public EventAdapter(ArrayList<Event> list) {
        listOfEvents = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event currentEvent = listOfEvents.get(position);

        holder.imgV.setImageResource(currentEvent.getImage());
        holder.tvNameEvent.setText(currentEvent.getEventName());
        holder.tvDescription.setText(currentEvent.getDescription());
        holder.tvMore.setText(currentEvent.getLinkToEvent());
    }

    @Override
    public int getItemCount() {
        return listOfEvents.size();
    }
}
