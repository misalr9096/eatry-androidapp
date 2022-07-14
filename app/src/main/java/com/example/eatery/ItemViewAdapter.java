package com.example.eatery;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class ItemViewAdapter extends ArrayAdapter<ItemView>{
    Button order;
    // define listener
    public interface DataShare{
        public ArrayList<Integer> putData();
        public void getData();
    }


    //    ArrayList<Integer> sendPrices=new ArrayList<>();
//    ArrayList<String> sendItemNames=new ArrayList<>();
    int sendPrices;
    String sendItemNames;
    // invoke the suitable constructor of the ArrayAdapter class
    public ItemViewAdapter(@NonNull Context context, ArrayList<ItemView> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_item_fargment, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        ItemView currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView numbersImage = currentItemView.findViewById(R.id.imageViewpasslay);
        assert currentNumberPosition != null;
        numbersImage.setImageResource(currentNumberPosition.ItemImageId());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.textViewpasslay);
        textView1.setText(currentNumberPosition.getItemName());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.price);
        textView2.setText("Price "+Integer.toString(currentNumberPosition.getItemPrice()));
        final ArrayList<Integer>TotalPrice=new ArrayList<>();

        return currentItemView;
    }






}