package in.tushar.eventaccess.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

import in.tushar.eventaccess.R;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<notificationModel> notificationModelArrayList;

    public notificationAdapter(Context ctx, ArrayList<notificationModel> notificationModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @NonNull
    @Override
    public notificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.notificationcard,parent,false);
        MyViewHolder holder =new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull notificationAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(notificationModelArrayList.get(position).getImageUrl()).into(holder.imageView);
        holder.header.setText(notificationModelArrayList.get(position).getNotificationHeader());
        holder.description.setText(notificationModelArrayList.get(position).getNotificationDes());
        holder.dateTime.setText(notificationModelArrayList.get(position).getDateTime());
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView dateTime,header,description;
        ImageView imageView;
        public MyViewHolder(View item){
            super(item);
            dateTime = item.findViewById(R.id.notificationTimeText);
            header = item.findViewById(R.id.notificationheader);
            description = item.findViewById(R.id.notificationdescription);
            imageView = item.findViewById(R.id.notificationImage);
        }
    }
}
