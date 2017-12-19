package tech.shloknangia.www.dtuconnect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    Button LogInButton;
    Button BecomeADonorButton;
    Button DonorListButton;
    ArrayList<MyContact> returnValues;
    BasicDBObject user = null;
    String server_output = null;
    String temp_output = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        LogInButton = (Button) findViewById(R.id.login);
        BecomeADonorButton = (Button) findViewById(R.id.becomeadonor);
        DonorListButton = (Button) findViewById(R.id.donorlist);

        returnValues = new ArrayList<MyContact>();
        LogInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this,
                        LoginActivity.class));
            }

        });
        BecomeADonorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(MainActivity.this,
                        SignupActivity.class));
            }

        });
        DonorListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isInternetOn()) {
                    Toast.makeText(MainActivity.this,
                            "Check Your Internet Connection",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    MainActivity.GetContactsAsyncTask task = new MainActivity.GetContactsAsyncTask();

                    try {
                        returnValues = task.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MainActivity.this, DonorListActivity.class);
                    intent.putExtra("mylist", returnValues);
                    startActivity(intent);


                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    String help = "The app works by accessing information from an online server." +
            "Hence it might take time depending upon your network speed.\n" +
            "Preferably use wifi connection.\n\nFor any hugs or bugs contact \nSHLOK NANGIA\n9953055177";

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
        return super.onOptionsItemSelected(item);
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

        }
        return false;
    }

    private class GetContactsAsyncTask extends AsyncTask<MyContact, Void, ArrayList<MyContact>> {


        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait! loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected ArrayList<MyContact> doInBackground(MyContact... arg0) {


            ArrayList<MyContact> mycontacts = new ArrayList<MyContact>();
            try {

                QueryBuilder qb = new QueryBuilder();
                URL url = new URL(qb.buildContactsGetURL());
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                while ((temp_output = br.readLine()) != null) {
                    server_output = temp_output;
                }

                // create a basic db list
                String mongoarray = "{ artificial_basicdb_list: " + server_output + "}";
                Object o = com.mongodb.util.JSON.parse(mongoarray);


                DBObject dbObj = (DBObject) o;
                BasicDBList contacts = (BasicDBList) dbObj.get("artificial_basicdb_list");

                for (Object obj : contacts) {
                    DBObject userObj = (DBObject) obj;

                    MyContact temp = new MyContact();
                    temp.setDoc_id(userObj.get("_id").toString());
                    temp.setName(userObj.get("name").toString());
                    temp.setEmail(userObj.get("email").toString());
                    temp.setBranch(userObj.get("branch").toString());
                    temp.setCourse(userObj.get("course").toString());
                    temp.setYear(userObj.get("year").toString());
                    temp.setBlood_Group(userObj.get("bloodgroup").toString());
                    temp.setPhone(userObj.get("phone").toString());
                    temp.setPassword(userObj.get("password").toString());
                    mycontacts.add(temp);

                }

            } catch (Exception e) {
                e.getMessage();
            }
            return mycontacts;
        }

        protected void onPostExecute(ArrayList<MyContact> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

        }
    }

}
