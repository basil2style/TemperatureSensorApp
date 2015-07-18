package com.makeinfo.temperaturereader;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ProgressDialog mProgressDialog;
    List<model> modelList = null;
    boolean checkOnline;
    List<ParseObject> ob;
    Date date;
    int size=0;
    ListView listview;
    ListViewAdapter listadapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        checkOnline = isOnline();
        if(checkOnline) {

            new RemoteDataTask().execute();

        }
        else {
            Toast.makeText(getApplicationContext(), "Internet Connection Needed", Toast.LENGTH_SHORT).show();
        }


    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           mProgressDialog = new ProgressDialog(MainActivity.this);
            // Set progressdialog title
            //mProgressDialog.setTitle("Syncing");
            // Set progressdialog message
            mProgressDialog.setMessage("Syncing...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            // Show progressdialog
            mProgressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... params) {
            modelList = new ArrayList<model>();
            try{
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TempScore");
             //   query.orderByDescending("createdAt");
                ob = query.find();
                for (ParseObject files : ob){
                    model modeler = new model();
                    modeler.setTemperature((String) files.get("temperatures"));
                    date = files.getCreatedAt();
                     modeler.setDate(date);
                    modelList.add(modeler);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

          return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            listview = (ListView)findViewById(R.id.listview);
            listadapter = new ListViewAdapter(getApplicationContext(),modelList);
            listview.setAdapter(listadapter);

           // Toast.makeText(getApplicationContext(), String.valueOf(modelList.size()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            if(checkOnline){
                new RemoteDataTask().execute();
           }

            else {
                Toast.makeText(getApplicationContext(), "Internet Connection Needed", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo !=null && netInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }
}
