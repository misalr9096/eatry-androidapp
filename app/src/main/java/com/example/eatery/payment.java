package com.example.eatery;

import static android.app.Activity.RESULT_OK;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class payment extends Fragment
{
    EditText noteEt, nameEt, upiIdEt;
    Button send;
    final int UPI_PAYMENT = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private ArrayList<String>  mParam2;
    private TextView SetValue,showText;
    private EditText Price;
    ListView lvDashboard;
    private AccessibilityService context;

    public payment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment payment.
     */
    // TODO: Rename and change types and number of parameters
    public static payment newInstance(String param1, String param2) {
        payment fragment = new payment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt("Price");
            mParam2 = getArguments().getStringArrayList("Item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_payment, container, false);
        Price=(EditText)v.findViewById(R.id.Totalpay);
        SetValue=(TextView) v.findViewById(R.id.Show);
        send=v.findViewById(R.id.send);
        noteEt=v.findViewById(R.id.note);
        nameEt=v.findViewById(R.id.name);
        upiIdEt= v.findViewById(R.id.upi_id);
        showText=v.findViewById(R.id.showText);
        try{
            Price.setText(String.valueOf(getArguments().getInt("TotalPrice")));
            SetValue.setText("Your Total Order Bill: "+ String.valueOf(getArguments().getInt("TotalPrice")));
        } catch(Exception ex){
            Price.setText("0");
            SetValue.setText("Your Total Order Bill: 0 ");
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = Price.getText().toString();
                String note = noteEt.getText().toString();
                String name = nameEt.getText().toString();
                String upiId = upiIdEt.getText().toString();
                if(!amount.equals("0")) {
                    payUsingUpi(amount, upiId, name, note);
                }else{
                    showText.setText("Please Taste Our Tasty Food");
                }

            }
        });

        return v;

    }
    void payUsingUpi(String amount, String upiId, String name, String note) {



            Uri uri = Uri.parse("upi://pay").buildUpon()
                    .appendQueryParameter("pa", upiId)
                    .appendQueryParameter("pn", name)
                    .appendQueryParameter("tn", note)
                    .appendQueryParameter("am", amount)
                    .appendQueryParameter("cu", "INR")
                    .build();


            Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
            upiPayIntent.setData(uri);

            // will always show a dialog to user to choose an app
            Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

            // check if intent resolves
            if (null != chooser.resolveActivity(this.getContext().getPackageManager())) {
                startActivityForResult(chooser, UPI_PAYMENT);
            } else {
                Toast toast=new Toast(getContext());
                View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
                TextView textView=view.findViewById(R.id.textviewtoast);
                textView.setText("No UPI app found, please install one to continue");
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();

            }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getContext())) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast toast=new Toast(getContext());
                View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
                TextView textView=view.findViewById(R.id.textviewtoast);
                textView.setText("Transaction successful.");
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();

                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast toast=new Toast(getContext());
                View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
                TextView textView=view.findViewById(R.id.textviewtoast);
                textView.setText("Payment cancelled by user.");
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();

            }
            else {
                Toast toast=new Toast(getContext());
                View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
                TextView textView=view.findViewById(R.id.textviewtoast);
                textView.setText("Transaction failed.Please try again");
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM,0,0);
                toast.show();

            }
        } else {
            Toast toast=new Toast(getContext());
            View view= LayoutInflater.from(getContext()).inflate(R.layout.custom_toast,null);
            TextView textView=view.findViewById(R.id.textviewtoast);
            textView.setText("Internet connection is not available. Please check and try again");
            toast.setView(view);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,0);
            toast.show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}


