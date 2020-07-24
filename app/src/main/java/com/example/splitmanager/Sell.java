package com.example.splitmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sell extends Fragment {

    ListView mlist;
    ArrayList <String> list= new ArrayList<String>();
    TextView totalsell;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sell,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("SELL");
        mlist= getActivity().findViewById(R.id.listviewsell);
        totalsell=getActivity().findViewById(R.id.totalsell);
        final ArrayAdapter <String> adp =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        mlist.setAdapter(adp);

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total=0;
                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    list.add(String.valueOf(dsp.getKey()));
                    adp.notifyDataSetChanged();
                }
                for (DataSnapshot dsp1 : dataSnapshot.getChildren()){
                    if(dsp1 != null){
                        for (DataSnapshot dsp2 : dsp1.getChildren()){
                            String mount = String.valueOf(dsp2.child("amount").getValue());
                            Float smount = Float.parseFloat(mount);
                            total = total + smount;
                            if(!dsp2.exists()){
                                dsp1.getRef().removeValue();
                            }
                        }
                    }
                }
                String totalamount = "Total = "+ total;
                totalsell.setText(totalamount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bname=String.valueOf(adp.getItem(position).toString());
                Intent intent = new Intent(getActivity(),ShowUserDetailsell.class);
                intent.putExtra("Broker_Name",bname);
                startActivity(intent);
            }
        });


    }



}
