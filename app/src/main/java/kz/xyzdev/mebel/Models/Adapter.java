package kz.xyzdev.mebel.Models;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.DrawableWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import kz.xyzdev.mebel.MainActivity;
import kz.xyzdev.mebel.R;


public class Adapter extends FirebaseRecyclerAdapter<ProductModels,Adapter.myViewolder> {


    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter(@NonNull FirebaseRecyclerOptions<ProductModels> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public myViewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new  myViewolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewolder holder, int position, @NonNull ProductModels model) {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice().toString()+"tg");
        Glide.with(holder.imageView.getContext()).load(model.getImgurl()).into(holder.imageView);

        //For share
        holder.sName = model.getName();
        holder.sPrice = model.getPrice().toString();
        holder.sFeatures = model.getFeatures();
        holder.sColor = model.getColors();
        holder.sCountry = model.getCountry();
        holder.sModel = model.getModel();
        holder.sMaterial = model.getMaterials();
        holder.sLiked = model.getLiked();
        holder.sCatalog = model.getCatalog();
        holder.sImgUrl = model.getImgurl();



    }

    class myViewolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price;
        String sImgUrl,sName,sPrice,sColor,sCountry,sFeatures,sModel,sMaterial,sCatalog;
        Boolean sLiked;
        public myViewolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.itemImg);

            name = (TextView) itemView.findViewById(R.id.itemName);
            price = (TextView) itemView.findViewById(R.id.itemPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("test", name.getText()+" " + price.getText());
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("name", sName);
                    intent.putExtra("imgurl", sImgUrl);
                    intent.putExtra("price", sPrice);
                    intent.putExtra("color", sColor);
                    intent.putExtra("country", sCountry);
                    intent.putExtra("features", sFeatures);
                    intent.putExtra("model", sModel);
                    intent.putExtra("material", sMaterial);
                    intent.putExtra("catalog", sCatalog);
                    intent.putExtra("liked", sLiked);
                    context.startActivity(intent);
                }
            });


        }


}}

