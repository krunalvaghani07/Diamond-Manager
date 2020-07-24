package com.example.splitmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ShowUserDetailbuy extends AppCompatActivity {
    DatabaseReference dbr;
    String bname;
    ListView list;
    Button update;
    TextView totalbuydetail;
   // ArrayList<String> ulist = new ArrayList<String>();
    ArrayList<UserHelper> listdata = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_detail);
        totalbuydetail=(TextView)findViewById(R.id.totalbuydetail);
        list=(ListView)findViewById(R.id.listbuydetail);
        bname= String.valueOf(getIntent().getExtras().get("Broker_Name").toString());
        dbr=FirebaseDatabase.getInstance().getReference("BUY").child(bname);
        final UserlistAdapter adp = new UserlistAdapter(this,R.layout.row,listdata);
        // final ArrayAdapter <String> adp =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ulist);
     //   View view = getLayoutInflater().inflate(R.layout.headerlist,null);
      //  list.addHeaderView(view);
        list.setAdapter(adp);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total=0;
                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    int inv = Integer.parseInt(String.valueOf(dsp.getKey()));
                    UserHelper user = new
                            UserHelper(inv,Float.parseFloat(String.valueOf(dsp.child("amount").getValue())));
                    String mount = String.valueOf(dsp.child("amount").getValue());
                    Float smount=Float.parseFloat(mount);
                     total = total + smount;
                     listdata.add(user);
                  //  ulist.add(String.valueOf(dsp.getKey()));
                    adp.notifyDataSetChanged();
                    if(smount < 1){
                        dsp.getRef().removeValue();
                    }
                }

                String totalamount = "Total = "+ total;
                totalbuydetail.setText(totalamount);
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
               Intent intent = new Intent(getApplicationContext(), UpdateData.class);
               intent.putExtra("invoice",invoice);
               intent.putExtra("amount",amount);
               intent.putExtra("broker_name",bname);
               startActivity(intent);
           }
       });




    }


}
