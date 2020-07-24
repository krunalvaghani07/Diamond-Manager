package com.example.splitmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddUser extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    AutoCompleteTextView uname;
    static EditText prize;
    EditText invoice;
    EditText phone;
    Button save;
    EditText party;
    EditText carat;
    EditText cutoff;
    RadioGroup rg;
    RadioButton rb;
    String status;
    static TextView duedate;
    ImageView duedateimg;
    DatabaseReference reference;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter <String> adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        duedateimg=(ImageView)findViewById(R.id.duedateimg);
        duedate=(TextView) findViewById(R.id.duedatetxt);
        uname=(AutoCompleteTextView) findViewById(R.id.uname);
        prize=(EditText)findViewById(R.id.number);
        phone=(EditText)findViewById(R.id.phonenumber);
        save=(Button)findViewById(R.id.savebutton);
        cutoff=(EditText)findViewById(R.id.prizecut);
        party=(EditText)findViewById(R.id.party);
        carat=(EditText)findViewById(R.id.carat);
        rg=(RadioGroup)findViewById(R.id.rgroup);
        invoice=(EditText)findViewById(R.id.invoice);
        reference=FirebaseDatabase.getInstance().getReference();
        if(!isconnected()){
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Internet Connection Alert")
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
        duedateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateshow=new Dateshow();
                dateshow.show(getSupportFragmentManager(),"Due Date");
            }
        });
        //FOR SENDING SMS
        adp = new ArrayAdapter<>(this,android.R.layout.select_dialog_item,list);
        uname.setThreshold(1);
        uname.setAdapter(adp);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren())
                {
                    if(dsp!= null){
                        for (DataSnapshot dsp1 : dsp.getChildren()){
                            list.add(String.valueOf(dsp1.getKey()));
                            adp.notifyDataSetChanged();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pname=uname.getText().toString();
                final String phonenumber=phone.getText().toString();
                String amn=prize.getText().toString();
                final float cat=Float.parseFloat(carat.getText().toString());
                final float cut=Float.parseFloat(cutoff.getText().toString());
                final float priz=Float.parseFloat(amn);
                float amount=cat*priz;
                float per=(amount*cut)/100;
                final float totalamount=amount-per;
                final int invoice_no=Integer.parseInt(invoice.getText().toString());
                final String message = "WELCOME \nDue Date:"+duedate.getText()+"\nPARTY NAME:"+ party.getText() +"\nBROKER NAME:"+uname.getText()+"\nCARAT:"+cat+ "\nPRIZE:" + priz +"\nPERCENTAGE:"+cut+"\nTOTAL AMOUNT:"+totalamount+"";
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(status.equals("BUY")){
                            reference=FirebaseDatabase.getInstance().getReference("BUY");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot data: dataSnapshot.getChildren()){
                                            if(data.hasChild(String.valueOf(invoice_no))){
                                                Toast.makeText(getApplicationContext(),"INVOICE NO ALREADY EXIST",Toast.LENGTH_LONG).show();
                                                break;
                                            }
                                            else{
                                                if(dataSnapshot.hasChild(pname)){
                                                    if (phonenumber.length()>0 && message.length()>0) {
                                                        sendMessage(phonenumber, message);
                                                        UserHelper adduser = new UserHelper(party.getText().toString(),pname,phonenumber,status,cat,priz,cut,totalamount,invoice_no,duedate.getText().toString(),Calendar.getInstance().getTime());
                                                        reference=FirebaseDatabase.getInstance().getReference("BUY").child(pname);
                                                        reference.child(String.valueOf(invoice_no)).setValue(adduser);
                                                        opennewActivity();
                                                        //   dbh.addUser(new UserHelper(party.getText().toString(),pname,phonenumber,cat,priz,cut,totalamount,status,invoice_no));

                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),"PLZ Enter ",Toast.LENGTH_LONG).show();

                                                    }

                                                }
                                                else if (phonenumber.length()>0 && message.length()>0) {
                                                    sendMessage(phonenumber, message);
                                                    UserHelper adduser = new UserHelper(party.getText().toString(),pname,phonenumber,status,cat,priz,cut,totalamount,invoice_no,duedate.getText().toString(),Calendar.getInstance().getTime());

                                                    reference.child(pname).child(String.valueOf(invoice_no)).setValue(adduser);
                                                    opennewActivity();
                                                    //   dbh.addUser(new UserHelper(party.getText().toString(),pname,phonenumber,cat,priz,cut,totalamount,status,invoice_no));

                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"PLZ Enter ",Toast.LENGTH_LONG).show();

                                                }
                                            }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else if(status.equals("SELL")){
                            reference=FirebaseDatabase.getInstance().getReference("SELL");
                            if(dataSnapshot.hasChild(pname)){
                                if (phonenumber.length()>0 && message.length()>0) {
                                    sendMessage(phonenumber, message);
                                    UserHelper adduser = new UserHelper(party.getText().toString(),pname,phonenumber,status,cat,priz,cut,totalamount,invoice_no,duedate.getText().toString(),Calendar.getInstance().getTime());
                                    reference=FirebaseDatabase.getInstance().getReference("SELL").child(pname);
                                    reference.child(String.valueOf(invoice_no)).setValue(adduser);
                                    opennewActivity();
                                    //   dbh.addUser(new UserHelper(party.getText().toString(),pname,phonenumber,cat,priz,cut,totalamount,status,invoice_no));

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"PLZ Enter ",Toast.LENGTH_LONG).show();

                                }

                            }
                            else if (phonenumber.length()>0 && message.length()>0) {
                                sendMessage(phonenumber, message);
                                UserHelper adduser = new UserHelper(party.getText().toString(),pname,phonenumber,status,cat,priz,cut,totalamount,invoice_no,duedate.getText().toString(),Calendar.getInstance().getTime());

                                reference.child(pname).child(String.valueOf(invoice_no)).setValue(adduser);
                                opennewActivity();
                                //   dbh.addUser(new UserHelper(party.getText().toString(),pname,phonenumber,cat,priz,cut,totalamount,status,invoice_no));

                            }
                            else{
                                Toast.makeText(getApplicationContext(),"PLZ Enter ",Toast.LENGTH_LONG).show();

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }






        });

    }

    private void opennewActivity() {
        Intent intent = new Intent(this,SecondActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String Duedate= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        duedate.setText(Duedate);
    }

    private void sendMessage(String phonenumber, String message){
        try {
            String SENT = "SMS_SENT";
            String DELIVERED = "SMS_DELIVERED";

            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(SENT), 0);

            PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                    new Intent(DELIVERED), 0);

            SmsManager smsManager= SmsManager.getDefault();
            ArrayList<String> parts =smsManager.divideMessage(message);

            ArrayList<PendingIntent> sendList = new ArrayList<>();
            sendList.add(sentPI);

            ArrayList<PendingIntent> deliverList = new ArrayList<>();
            deliverList.add(deliveredPI);
            smsManager.sendMultipartTextMessage(phonenumber,null,parts,sendList,deliverList);
            Toast.makeText(getApplicationContext(),"SENT",Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"SENT Failed",Toast.LENGTH_LONG).show();
        }


    }
    public void rstatus(View v){
        int rbid=rg.getCheckedRadioButtonId();
        rb=(RadioButton)findViewById(rbid);
        status=rb.getText().toString();
    }

    @Override
    public void onBackPressed() {

        finish();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
    public boolean isconnected(){

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

}
