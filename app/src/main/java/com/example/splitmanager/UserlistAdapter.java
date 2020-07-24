package com.example.splitmanager;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserlistAdapter extends ArrayAdapter<UserHelper> {

    Context mcontext;
    ArrayList<UserHelper>users;
    int mres;

    public UserlistAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserHelper> objects) {
        super(context, resource, objects);
        mcontext = context;
        mres=resource;
        users=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mres,parent,false);
        UserHelper user = users.get(position);

        int invoice = user.getInvoice();
        Float amount =user.getAmount();
        TextView invoicecol = (TextView)convertView.findViewById(R.id.invoicecolumn);
        TextView amountcol = (TextView)convertView.findViewById(R.id.amountcolomn);
        invoicecol.setText(String.valueOf(invoice));
        amountcol.setText(String.valueOf(amount));

        return convertView;

    }

}


