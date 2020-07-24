package com.example.splitmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUserDetailsell extends AppCompatActivity {

    DatabaseReference dbr;
    String bname;
    ListView list;
    TextView totalselldetail;
    Button allpaid;
   // ArrayList<String> ulist = new ArrayList<String>();
    double total=0;
    ArrayList<UserHelper> listdata = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_detailsell);
        totalselldetail=(TextView)findViewById(R.id.totalselldetail);
        list=(ListView)findViewById(R.id.listselldetail);
        bname= String.valueOf(getIntent().getExtras().get("Broker_Name").toString());
        dbr= FirebaseDatabase.getInstance().getReference("SELL").child(bname);
        final UserlistAdapter adp = new UserlistAdapter(this,R.layout.row,listdata);
        //final ArrayAdapter<String> adp =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ulist);
        list.setAdapter(adp);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    int inv = Integer.parseInt(String.valueOf(dsp.getKey()));
                    UserHelper user = new
                            UserHelper(inv,Float.parseFloat(String.valueOf(dsp.child("amount").getValue())));
                    String mount = String.valueOf(dsp.child("amount").getValue());
                    Float smount=Float.parseFloat(mount);
                    total = total + smount;
                    listdata.add(user);
                    // ulist.add(String.valueOf(dsp.getKey()));
                    adp.notifyDataSetChanged();
                    if(smount < 1){
                        dsp.getRef().removeValue();
                    }
                }
                String totalamount = "Total = "+ total;
                totalselldetail.setText(totalamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listdata.get(position);
                String invoice = String.valueOf(listdata.get(position).getInvoice());
                String amount = String.valueOf(listdata.get(position).getAmount());
                Intent intent = new Intent(getApplicationContext(), UpdateDatasell.class);
                intent.putExtra("invoice",invoice);
                intent.putExtra("amount",amount);
                intent.putExtra("broker_name",bname);
                startActivity(intent);
            }
        });

    }
}
