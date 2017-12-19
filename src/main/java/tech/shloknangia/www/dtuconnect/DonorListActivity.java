package tech.shloknangia.www.dtuconnect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DonorListActivity extends AppCompatActivity {

    ListView listView;
    Spinner filter;
    Button sendEmail;

    ArrayList<MyContact> returnValues;
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> bloodgroup = new ArrayList<String>();
    ArrayList<String> phone = new ArrayList<String>();
    ArrayList<String> email = new ArrayList<String>();
    ArrayList<String> branch = new ArrayList<String>();
    ArrayList<String> course = new ArrayList<String>();
    ArrayList<String> year = new ArrayList<String>();
    List<String> emails = new ArrayList<String>();
    TextView count;
    String no, em;
    int Counter;
    String msg_subject, msg_body;
    String blood_group;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        returnValues = (ArrayList<MyContact>) getIntent().getSerializableExtra("mylist");
        listView = (ListView) findViewById(R.id.list);
        filter = (Spinner) findViewById(R.id.filter);
        sendEmail = (Button) findViewById(R.id.sendemail);
        count = (TextView) findViewById(R.id.count);
        sendEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (email.size() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "List is Empty, can't send", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }
                // TODO Auto-generated method stub

                for (String x : email) {
                    emails.add(x);
                }
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                Iterator<String> it = emails.iterator();
                String[] email_array = new String[emails.size()];
                int i = 0;
                while (it.hasNext()) {
                    email_array[i] = it.next();
                    i++;
                }

                blood_group = "";
                String temp = filter.getSelectedItem().toString();
                if (!temp.equals("All"))
                    blood_group = temp;
                msg_subject = "Urgent requirement of " + blood_group
                        + " blood donor";

                msg_body = "Hello everyone\nThere is an urgent requirement of "
                        + blood_group
                        + " blood group."
                        + "\nIf you can help please reply to this mail.\n\nThank you\n";

                emailIntent.putExtra(Intent.EXTRA_EMAIL, email_array);

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, msg_subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, msg_body);

                try {
                    startActivity(Intent.createChooser(emailIntent,
                            "Send mail..."));


                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DonorListActivity.this,
                            "There is no email client installed.",
                            Toast.LENGTH_SHORT).show();
                }
                emails.clear();
            }

        });
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter
                .createFromResource(this, R.array.bloodgroup_array2,
                        android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        filterAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        filter.setAdapter(filterAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String ContactName = name.get(position);
                String ContactEmail = email.get(position);
                String ContactBranch = branch.get(position);
                String ContactCourse = course.get(position);
                String ContactYear = year.get(position);
                String ContactBloodGroup = bloodgroup.get(position);
                String ContactPhone = phone.get(position);
                Intent intent = new Intent(DonorListActivity.this,
                        ViewDataActivity.class);
                intent.putExtra("name", ContactName);
                intent.putExtra("email", ContactEmail);
                intent.putExtra("branch", ContactBranch);
                intent.putExtra("course", ContactCourse);
                intent.putExtra("year", ContactYear);
                intent.putExtra("bloodgroup", ContactBloodGroup);
                intent.putExtra("phone", ContactPhone);
                startActivity(intent);

            }
        });
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                no = phone.get(position);
                em = email.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(DonorListActivity.this);
                builder.setMessage("");
                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String uri = "tel:" + no.trim() ;
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
//                        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + no));
//
//                        try {
//                            startActivity(in);
//                        } catch (android.content.ActivityNotFoundException ex) {
//                            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
//                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                }).setNeutralButton("Send Message", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        blood_group = "";
                        String temp = filter.getSelectedItem().toString();
                        if (!temp.equals("All"))
                            blood_group = temp;
                        msg_subject = "Urgent requirement of " + blood_group
                                + " blood donor";

                        msg_body = "Hello everyone\nThere is an urgent requirement of "
                                + blood_group
                                + " blood group."
                                + "\nIf you can help please reply to this mail.\n\nThank you\n";
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        String[] email_array = new String[1];
                        email_array[0] = em;
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, email_array);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, msg_subject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, msg_body);

                        try {
                            startActivity(Intent.createChooser(emailIntent,
                                    "Send mail..."));


                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(DonorListActivity.this,
                                    "There is no email client installed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                // TODO Auto-generated method stub
                return true;
            }
        });


        for (MyContact x : returnValues) {


            name.add(x.getName());

            bloodgroup.add(x.getBlood_Group());
            phone.add(x.getPhone());
            email.add(x.phone);
            branch.add(x.branch);
            course.add(x.course);
            year.add(x.year);

        }

        final CustomAdapter adapter = new CustomAdapter(this, name, bloodgroup,
                phone);
        listView.setAdapter(adapter);


        listView.setAdapter(adapter);
        Counter = name.size();
        count.setText("Showing " + Integer.toString(Counter) + " Items");
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // TODO Auto-generated method stub
                String s = parent.getItemAtPosition(position).toString();
                name.clear();
                bloodgroup.clear();
                phone.clear();
                email.clear();
                course.clear();
                year.clear();
                branch.clear();
                if (s.equals("All")) {
                    for (MyContact contact : returnValues) {
                        name.add(contact.name);
                        bloodgroup.add(contact.bloodgroup);
                        phone.add(contact.phone);
                        email.add(contact.email);
                        branch.add(contact.branch);
                        course.add(contact.course);
                        year.add(contact.year);

                    }

                } else {
                    for (MyContact contact : returnValues) {
                        if (contact.bloodgroup.equals(s)) {
                            name.add(contact.name);
                            bloodgroup.add(contact.bloodgroup);
                            phone.add(contact.phone);
                            email.add(contact.email);
                            branch.add(contact.branch);
                            course.add(contact.course);
                            year.add(contact.year);
                        }
                    }

                }
                Counter = name.size();
                count.setText("Showing " + Integer.toString(Counter) + " Items");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

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
            "Preferably use wifi connection.\n\nFor any hugs or bugs contact \nSHLOK NANGIA\n";

    String about = "DTU Connect is an initiative to make a bond between Blood donor and donee." +
            "\nBlood is the most precious gift a human can give to another.\n";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

}
