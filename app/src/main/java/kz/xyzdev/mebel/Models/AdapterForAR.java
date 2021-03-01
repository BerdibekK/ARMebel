package kz.xyzdev.mebel.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import kz.xyzdev.mebel.R;


public class AdapterForAR extends FirebaseRecyclerAdapter<ProductModels,AdapterForAR.myViewolder> {

    private OnItemClickListener listener;
    public static final int NO_POSITION = -1;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdapterForAR(@NonNull FirebaseRecyclerOptions<ProductModels> options) {
        super(options);

    }

    @NonNull
    @Override
    public myViewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ar,parent,false);
        return new  myViewolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewolder holder, int position, @NonNull ProductModels model) {
        holder.name.setText(model.getName());
        Glide.with(holder.imageView.getContext()).load(model.getImgurl()).into(holder.imageView);

        //For share
        holder.sModel = model.getModel();

    }

    class myViewolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        String sModel;
        public myViewolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.itemImg);

            name = (TextView) itemView.findViewById(R.id.itemName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION&&listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }



    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(DataSnapshot documentSnapshot, int position);
    }
}

