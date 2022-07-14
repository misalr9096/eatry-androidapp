package com.example.eatery;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.PRICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.androidbrowserhelper.playbilling.digitalgoods.ItemDetails;

import java.util.ArrayList;

public class list extends Fragment {
    private int TotalPrice=0;

    final ArrayList<ItemView> arrayList = new ArrayList<ItemView>();
    ListView li;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        arrayList.add(new ItemView(R.drawable.vadapav,"Vada Pav",50));
        arrayList.add(new ItemView(R.drawable.thalipeethtype,"Thalipeeth",150));
        arrayList.add(new ItemView(R.drawable.steamedmodak,"Modak",200));
        arrayList.add(new ItemView(R.drawable.shrikhand,"Shrikhand",50));
        arrayList.add(new ItemView(R.drawable.shakarpara,"Shakrarpara",50));
        arrayList.add(new ItemView(R.drawable.sabudanakhichdi, "Sabudana Khichdi",150));
        arrayList.add(new ItemView(R.drawable.ragdapattice1, "Ragda Pattice",150));
        arrayList.add(new ItemView(R.drawable.puranpoli, "Puranpoli",200));
        arrayList.add(new ItemView(R.drawable.pohaflattenedrice, "Poha",50));
        arrayList.add(new ItemView(R.drawable.pithlabhakar,"PithlaBhakar",150));
        arrayList.add(new ItemView(R.drawable.misalpav1,"MisalPav", 150));
        arrayList.add(new ItemView(R.drawable.kolhapurivegetables,"Kolhapuri Rasa",150));
        arrayList.add(new ItemView(R.drawable.bombayduck1,"Bombay duck", 300));
        arrayList.add(new ItemView(R.drawable.bhelpuripuffedriceandvegetables,"Bhelpuri",30));
        arrayList.add(new ItemView(R.drawable.bharlivangi,"BharliVangi",150));
        arrayList.add(new ItemView(R.drawable.bhakarwadi,"BhakarWadi",50));
        arrayList.add(new ItemView(R.drawable.bhajirassa,"Bhaji Rassa",50));
        arrayList.add(new ItemView(R.drawable.basundirabri,"Basundi",80));
        arrayList.add(new ItemView(R.drawable.aluvadisteamedcolocasialeaves,"Aluvadi",80));
        arrayList.add(new ItemView(R.drawable.aamtidal,"Aamti dal",80));


        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ListView numbersListView = v.findViewById(R.id.listViewPassword);
        Button order=v.findViewById(R.id.order);
        Button cancle=v.findViewById(R.id.Cancle);

        ItemViewAdapter itemViewAdapter = new ItemViewAdapter(getActivity() , arrayList);

        // set the numbersViewAdapter for ListView
        numbersListView.setAdapter(itemViewAdapter);
        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String item="";


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ItemView imageView=arrayList.get(i);
                int Price=imageView.getItemPrice();
                item=imageView.getItemName();
                TotalPrice+=Price;
                Toast toast=new Toast(getContext());
                view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
                TextView textView=view.findViewById(R.id.textviewtoast);
                String sourceString = "<b>" + item + "</b> ";
                textView.setText( Html.fromHtml(sourceString) + "Added in order list");
                toast.setView(view);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment ldf = new payment ();
                Bundle args = new Bundle();
                args.putInt("TotalPrice", TotalPrice);
                ldf.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.frameLayout,ldf).commit();
                TotalPrice=0;
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalPrice=0;
                Toast toast=new Toast(getContext());
                view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
                TextView textView=view.findViewById(R.id.textviewtoast);
                textView.setText("Order Cancelled");
                toast.setView(view);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        return v;
    }



}
