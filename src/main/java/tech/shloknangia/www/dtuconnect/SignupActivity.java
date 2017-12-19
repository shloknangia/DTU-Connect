package tech.shloknangia.www.dtuconnect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SignupActivity extends AppCompatActivity {

    EditText name,email,phone,password;
    Spinner branch,course,year,bloodgroup;
    String sname,semail,sphone,spassword,sbranch,scourse,syear,sbloodgroup;
    Button signup;
    ProgressDialog progressDialog;
    String OriginalObject = "";
    String server_output = null;
    String temp_output = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.phone);
        password = (EditText)findViewById(R.id.password);
        branch = (Spinner)findViewById(R.id.branchspinner);
        course = (Spinner)findViewById(R.id.coursespinner);
        year = (Spinner)findViewById(R.id.yearspinner);
        bloodgroup = (Spinner)findViewById(R.id.bloodgroupspinner);
        signup = (Button)findViewById(R.id.signup);

        ArrayAdapter<CharSequence> BranchAdapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        BranchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        branch.setAdapter(BranchAdapter);

        ArrayAdapter<CharSequence> CourseAdapter = ArrayAdapter.createFromResource(this,
                R.array.course_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        CourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        course.setAdapter(CourseAdapter);

        ArrayAdapter<CharSequence> YearAdapter = ArrayAdapter.createFromResource(this,
                R.array.year_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        YearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        year.setAdapter(YearAdapter);

        ArrayAdapter<CharSequence> BloodGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodgroup_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        BloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        bloodgroup.setAdapter(BloodGroupAdapter);
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sname = name.getText().toString().trim();
                semail = email.getText().toString().trim();
                sphone = phone.getText().toString().trim();
                spassword = password.getText().toString();
                sbranch = branch.getSelectedItem().toString();
                scourse = course.getSelectedItem().toString();
                syear = year.getSelectedItem().toString();
                sbloodgroup = bloodgroup.getSelectedItem().toString();
                if(sname.length()==0){
                    Toast.makeText(getApplicationContext(),"Fill Your Name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(semail.length()==0){
                    Toast.makeText(getApplicationContext(),"Fill Your Email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidEmailAddress(semail)){
                    Toast.makeText(getApplicationContext(),"Invalid Email ID",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bloodgroup.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Your Blood Group",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(sphone.length()!=10 || sphone.contains(" ")){
                    Toast.makeText(getApplicationContext(),"Invalid Mobile Number",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(spassword.length()<8){
                    Toast.makeText(getApplicationContext(),"Password Must Atleast Contain 8 Characters",
                            Toast.LENGTH_SHORT).show();
                    return;
                }



                MyContact contact = new MyContact();
                contact.setName(sname);
                contact.setEmail(semail);
                contact.setBranch(sbranch);
                contact.setBlood_Group(sbloodgroup);
                contact.setCourse(scourse);
                contact.setPassword(spassword);
                contact.setYear(syear);
                contact.setPhone(sphone);
                ArrayList<MyContact> returnValues = new ArrayList<MyContact>();
                if(!isInternetOn()){
                    Toast.makeText(getApplicationContext(),"Check Your Internet Connection",
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
                for(MyContact x: returnValues){

                    if(x.email.equals(semail)){
                        Toast.makeText(getApplicationContext(),"You Have Already Registered",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                SaveAsyncTask tsk = new SaveAsyncTask();
                boolean flag = false;
                try {
                    flag = tsk.execute(contact).get();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(flag){
                    Toast.makeText(getApplicationContext(),"Congratulations",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Unable to sign up",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            }
            
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signup, menu);
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
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {


        }
        return false;
    }
    private class GetContactsAsyncTask extends AsyncTask<MyContact, Void, ArrayList<MyContact>> {



        protected void onPreExecute(){

            super.onPreExecute();
            progressDialog = ProgressDialog.show(SignupActivity.this,"please wait", "loading",true);


        }
        protected ArrayList<MyContact> doInBackground(MyContact... arg0) {


            ArrayList<MyContact> mycontacts = new ArrayList<MyContact>();
            try
            {

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
                String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
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

            }catch (Exception e) {
                e.getMessage();
            }
            return mycontacts;
        }
        protected void onPostExecute(ArrayList<MyContact> result){
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

}
