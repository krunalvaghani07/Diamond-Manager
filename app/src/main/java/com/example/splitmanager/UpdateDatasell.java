package com.example.splitmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateDatasell extends AppCompatActivity {

    DatabaseReference dbr;
    TextView amounttopay;
    EditText amountpaid;
    Button saveupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        amounttopay=(TextView)findViewById(R.id.amountpay);
        amountpaid=(EditText)findViewById(R.id.amountpaid);
        saveupdate=(Button)findViewById(R.id.updatesave);
        String bname =String.valueOf(getIntent().getExtras().get("broker_name").toString());
        String invoice= String.valueOf(getIntent().getExtras().get("invoice").toString());
        String amount =String.valueOf(getIntent().getExtras().get("amount").toString());
        amountpaid.setText(String.valueOf(amount));
        Double total= (Double) getIntent().getExtras().get("total");
        dbr= FirebaseDatabase.getInstance().getReference("SELL").child(bname).child(invoice);
        if(!isconnected()){
            new AlertDialog.Builder(getApplicationContext()).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
        saveupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String mount = String.valueOf(dataSnapshot.child("amount").getValue());
                        Float smount = Float.parseFloat(mount);
                        String amountval = amountpaid.getText().toString();
                        float updateamount = Float.parseFloat(amountval);
                        final float updatedamount = (float) (smount-updateamount);
                        dataSnapshot.getRef().child("amount").setValue(updatedamount);
                        Toast.makeText(getApplicationContext(),"Amount Paid ",Toast.LENGTH_LONG).show();
                        opennewAcitvity();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    private void opennewAcitvity() {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }
    public boolean isconnected(){

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }
}
