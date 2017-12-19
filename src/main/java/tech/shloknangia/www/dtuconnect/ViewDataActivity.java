package tech.shloknangia.www.dtuconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewDataActivity extends AppCompatActivity {

    TextView textViewName,textViewEmail,textViewBranch,textViewCourse,textViewYear,textViewBloodGroup,textViewPhone;
    String name,email,branch,course,year,bloodgroup,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        branch = getIntent().getStringExtra("branch");
        course =  getIntent().getStringExtra("course");
        year = getIntent().getStringExtra("year");
        bloodgroup = getIntent().getStringExtra("bloodgroup");
        phone = getIntent().getStringExtra("phone");
        textViewName = (TextView)findViewById(R.id.name);
        textViewEmail = (TextView)findViewById(R.id.email);
        textViewBranch = (TextView)findViewById(R.id.branch);
        textViewCourse = (TextView)findViewById(R.id.course);
        textViewYear = (TextView)findViewById(R.id.year);
        textViewBloodGroup = (TextView)findViewById(R.id.bloodgroup);
        textViewPhone = (TextView)findViewById(R.id.phone);
        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewBranch.setText(branch);
        textViewCourse.setText(course);
        textViewYear.setText(year);
        textViewBloodGroup.setText(bloodgroup);
        textViewPhone.setText(phone);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_data, menu);
        return true;
    }
    String help = "The app works by accessing information from an online server." +
            "Hence it might take time depending upon your network speed.\n" +
            "Preferably use wifi connection.\n\nFor any hugs or bugs contact \nSHLOK NANGIA\n";

    String about = "DTU Connect is an initiative to make a bond between Blood donor and donee." +
            "\nBlood is the most precious gift a human can give to another.\n";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(about);
            builder.setIcon(R.mipmap.ic_launcher);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else if(id == R.id.help){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(help);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.cancel();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        else if(id == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void call(View view){
        String uri = "tel:" + phone.trim() ;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void mail (View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(email);
        Uri uri = Uri.parse(uriText);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        }
    }






}
