package tech.shloknangia.www.dtuconnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewUpdateActivity extends AppCompatActivity {

    TextView textViewName, textViewEmail, textViewBranch, textViewCourse, textViewYear, textViewBloodGroup, textViewPhone;
    Button update, logout;
    String doc_id, name, email, branch, course, year, bloodgroup, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        doc_id = getIntent().getStringExtra("doc_id");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        branch = getIntent().getStringExtra("branch");
        course = getIntent().getStringExtra("course");
        year = getIntent().getStringExtra("year");
        password = getIntent().getStringExtra("password");
        bloodgroup = getIntent().getStringExtra("bloodgroup");
        phone = getIntent().getStringExtra("phone");
        textViewName = (TextView) findViewById(R.id.name);
        textViewEmail = (TextView) findViewById(R.id.email);
        textViewBranch = (TextView) findViewById(R.id.branch);
        textViewCourse = (TextView) findViewById(R.id.course);
        textViewYear = (TextView) findViewById(R.id.year);
        textViewBloodGroup = (TextView) findViewById(R.id.bloodgroup);
        textViewPhone = (TextView) findViewById(R.id.phone);
        update = (Button) findViewById(R.id.update);
        logout = (Button) findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("EmailKey", "");
                editor.putString("PassKey", "");
                editor.commit();
                finish();
            }
        });
        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewBranch.setText(branch);
        textViewCourse.setText(course);
        textViewYear.setText(year);
        textViewBloodGroup.setText(bloodgroup);
        textViewPhone.setText(phone);
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ViewUpdateActivity.this, UpdateActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("doc_id", doc_id);
                intent.putExtra("branch", branch);
                intent.putExtra("course", course);
                intent.putExtra("year", year);
                intent.putExtra("bloodgroup", bloodgroup);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                startActivityForResult(intent, 1);
            }
        });
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
        } else if (id == R.id.help) {

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {


                name = data.getStringExtra("name");
                email = data.getStringExtra("email");
                branch = data.getStringExtra("branch");
                course = data.getStringExtra("course");
                year = data.getStringExtra("year");
                password = data.getStringExtra("password");
                bloodgroup = data.getStringExtra("bloodgroup");
                phone = data.getStringExtra("phone");
                textViewName.setText(name);
                textViewEmail.setText(email);
                textViewBranch.setText(branch);
                textViewCourse.setText(course);
                textViewYear.setText(year);
                textViewBloodGroup.setText(bloodgroup);
                textViewPhone.setText(phone);
            }
        }
    }

}
