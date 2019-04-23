package com.captaincool.projectapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.parse.Parse.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleHome extends Fragment {


    public ScheduleHome() {
        // Required empty public constructor
    }

    ListView listView;
    EditText editText;
    Button button,myPlan;
    String url;
    int price,id;
    ArrayList<String> nameList,urlList,idList,priceList;
    ProgressDialog progress;
    ArrayAdapter<String> arrayAdapter;
    ParseUser user ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_schedule_home, container, false);
        listView =(ListView) v.findViewById(R.id.listView3);
        editText = v.findViewById(R.id.place);
        button =(Button) v.findViewById(R.id.sButton);
        myPlan = v.findViewById(R.id.myPlan);
        user = ParseUser.getCurrentUser();
        nameList = new ArrayList<String>();
        urlList = new ArrayList<String>();
        priceList = new ArrayList<String>();
        idList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nameList);

        if(user.get("pkgCode") == null)
        {
            myPlan.setVisibility(View.INVISIBLE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = new ProgressDialog(view.getContext());
                search();
            }
        });
        myPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyPlan();
            }
        });
//        ParseObject places = new ParseObject("Places");
//        places.put("name","Manali");
//        places.put("id",1002);
//        places.put("url","https://api.myjson.com/bins/1fn4d6");
//        places.put("price",1500);
//        places.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e == null) {
//                    generateToast("Successfully Inserted");
//                    Log.i("myapp","Inserted Successfully");
//                }else {
//                    e.printStackTrace();
//                }
//            }
//        });
        return v;
    }
    public void generateToast(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }

    public void showMyPlan()
    {
        Intent intent = new Intent(getApplicationContext(),planPage.class);
        startActivity(intent);
    }
    public void search()
    {
        progress.setMessage("Please wait");
        progress.show();
        generateToast("Working 1");
        if(TextUtils.isEmpty(editText.getText()))
        {
            editText.setError("place is required");
        }
        try {

//            SQLiteDatabase myDb = this.openOrCreateDatabase("places", MODE_PRIVATE, null);
//            myDb.execSQL("create table if not exists places (name VARCHAR,url VARCHAR)");
//            myDb.execSQL("insert into places (name,url) values ('Lonavala','https://api.myjson.com/bins/1fn4d6')");
//            myDb.execSQL("insert into places (name,url) values ('Goa','https://api.myjson.com/bins/1fn4d6')");
//            myDb.execSQL("insert into places (name,url) values ('Manali','https://api.myjson.com/bins/1fn4d6')");
//        myDb.execSQL("DELETE FROM places where name = 'Manali'");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
            query.whereContains("name",""+editText.getText());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null)
                    {
                        if(objects.size() > 0)
                        {
                            for(ParseObject object : objects)
                            {
                                Log.i("myapp",object.getString("name"));
                                nameList.add(object.getString("name"));
                                urlList.add(object.getString("url"));
                                priceList.add(String.valueOf(object.getNumber("price")));
                                idList.add(String.valueOf(object.getNumber("id")));
                                Log.i("myapp",object.getString("url"));
                            }
                        }
                    }
                    listView.setAdapter(arrayAdapter);
                    progress.dismiss();
                }
            });
//            Cursor c = myDb.rawQuery("SELECT * FROM places where name like '"+ editText.getText().toString() +"%'", null);
//            Cursor c = myDb.rawQuery("SELECT * FROM places", null);
            Log.i("myapp",editText.getText().toString());
//
//            int nameIndex = c.getColumnIndex("name");
//            int urlIndex = c.getColumnIndex("url");
//            c.moveToFirst();
//            while(c != null)
//            {
//                Log.i(TAG,c.getString(nameIndex));
//                Log.i(TAG,c.getString(urlIndex));
//                nameList.add(c.getString(nameIndex));
//                urlList.add(c.getString(urlIndex));
//                c.moveToNext();
//            }
        }
        catch (Exception e)
        { e.printStackTrace(); }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                url = urlList.get(i);
                price = Integer.parseInt(String.valueOf(priceList.get(i)));
                id = Integer.parseInt(String.valueOf(idList.get(i)));
                nextAct();
            }
        });
    }
    private void nextAct() {
        Log.i("myapp",url);
        Intent intent = new Intent(getApplicationContext(),scheduleResult.class);
        intent.putExtra("url",url);
        intent.putExtra("price",price);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
