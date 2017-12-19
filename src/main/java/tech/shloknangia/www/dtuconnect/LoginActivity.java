package tech.shloknangia.www.dtuconnect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login;
    String doc_id;
    ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Email = "EmailKey";
    public static final String Password = "PassKey";
    SharedPreferences sharedpreferences;
    String OriginalObject = "";
    String server_output = null;
    String temp_output = null;
    TextView showPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
//        showPassword = (TextView) findViewById(R.id.showpassword);
        String e = (sharedpreferences.getString(Email, ""));
        String p = (sharedpreferences.getString(Password, ""));
        email.setText(e);
        password.setText(p);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString();
                if (emailText.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please fill email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmailAddress(emailText)) {
                    Toast.makeText(getApplicationContext(), "please fill a valid email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordText.length() < 8) {
                    Toast.makeText(getApplicationContext(), "password must contain atleast 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<MyContact> returnValues = new ArrayList<MyContact>();
                if (!isInternetOn()) {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                GetContactsAsyncTask task = new GetContactsAsyncTask();


                try {
                    returnValues = task.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                for (MyContact x : returnValues) {

                    if (x.email.equals(emailText)) {
                        if (x.password.equals(passwordText)) {

                            doc_id = x.getDoc_id();
                            String ContactName = x.getName();
                            String ContactEmail = x.getEmail();
                            String ContactBranch = x.getBranch();
                            String ContactCourse = x.getCourse();
                            String ContactYear = x.getYear();
                            String ContactBloodGroup = x.getBlood_Group();
                            String ContactPhone = x.getPhone();
                            String ContactPassword = x.getPassword();
                            Intent intent = new Intent(LoginActivity.this, ViewUpdateActivity.class);
                            intent.putExtra("doc_id", doc_id);
                            intent.putExtra("name", ContactName);
                            intent.putExtra("email", ContactEmail);
                            intent.putExtra("branch", ContactBranch);
                            intent.putExtra("course", ContactCourse);
                            intent.putExtra("year", ContactYear);
                            intent.putExtra("bloodgroup", ContactBloodGroup);
                            intent.putExtra("phone", ContactPhone);
                            intent.putExtra("password", ContactPassword);

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(Email, x.email);
                            editor.putString(Password, x.password);
                            editor.commit();
                            startActivityForResult(intent, 2);

                            return;
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    }
                }
                Toast.makeText(getApplicationContext(), "You Have not registered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        String e = sharedpreferences.getString(Email, "");
        String p = sharedpreferences.getString(Password, "");
        email.setText(e);
        password.setText(p);


    }

    private class GetContactsAsyncTask extends AsyncTask<MyContact, Void, ArrayList<MyContact>> {


        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, "please wait", "loading", true);


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
