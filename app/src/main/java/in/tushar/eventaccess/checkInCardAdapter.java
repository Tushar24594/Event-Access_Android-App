package in.tushar.eventaccess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class checkInCardAdapter extends RecyclerView.Adapter<checkInCardAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<checkInModel> checkInModelArrayList;

    public checkInCardAdapter(Context ctx, ArrayList<checkInModel> checkInModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.checkInModelArrayList = checkInModelArrayList;
    }

    @NonNull
    @Override
    public checkInCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.checkincard, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull checkInCardAdapter.MyViewHolder holder, int position) {
        holder.header.setText(checkInModelArrayList.get(position).getHeader());
        holder.checkIn.setText(checkInModelArrayList.get(position).getCheckInTime());
        holder.checkOut.setText(checkInModelArrayList.get(position).getCheckOutTime());
    }

    @Override
    public int getItemCount() {
        return checkInModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView header, checkIn, checkOut;

        public MyViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.headerText);
            checkIn = itemView.findViewById(R.id.checkinTimeText);
            checkOut = itemView.findViewById(R.id.checkoutTimeText);
        }
    }
}

