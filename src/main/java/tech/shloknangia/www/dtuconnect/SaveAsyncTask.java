package tech.shloknangia.www.dtuconnect;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SHLOK on 16-04-2017.
 */
public class SaveAsyncTask  extends AsyncTask<MyContact, Void, Boolean> {

    @Override
    protected Boolean doInBackground(MyContact... arg0) {

        BufferedReader reader = null;
        try {
            MyContact contact = arg0[0];
            QueryBuilder qb = new QueryBuilder();
            URL url = new URL(qb.buildContactsSaveURL());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            String params = qb.createContact(contact);
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

//            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            con.setDoOutput(true);
            con.setChunkedStreamingMode(0);
            con.connect();


            DataOutputStream wr = new DataOutputStream (con.getOutputStream());
            wr.writeBytes (params);
            wr.flush ();
            wr.close ();
            int no;
            if((no = con.getResponseCode())<205)
            {
                return true;
            }
            else
            {
                return false;
            }



        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {//to close http request
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        /*try
		{
			MyContact contact = arg0[0];

			QueryBuilder qb = new QueryBuilder();

			HttpClients httpClient = new DefaultHttpClient();
	        HttpPostHC4 request = new HttpPostHC4(qb.buildContactsSaveURL());

	        StringEntity params =new StringEntity(qb.createContact(contact));
	        request.addHeader("content-type", "application/json");
	        request.setEntity(params);
            HttpResponse response = httpClient.
                    //.execute(request);


	        if(response.getStatusLine().getStatusCode()<205)
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;
	        }
		} catch (Exception e) {
			//e.getCause();
			String val = e.getMessage();
			String val2 = val;
			return false;
		}
	}*/

    }
}

