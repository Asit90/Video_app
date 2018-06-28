package cakart.cakart.in.video_app.videoclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cakart.cakart.in.video_app.db.HttpHandler;
import cakart.cakart.in.video_app.R;

public class VideoClassesTypeFragment extends Fragment{
    ProgressDialog pDialog;
    String TAG = "akhil";
    ImageView foundation,cpt,tips;
    VideoClassesListFragment sf;
    public boolean is_showing = false;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video_classes_type_fragment, container, false);
        // Inflate the layout for this fragment
        SharedPreferences pf = getActivity().getSharedPreferences("video_data", Context.MODE_PRIVATE);

        String last_downloaded = pf.getString("video_lec2_downloaded",null);



        try {
            if (last_downloaded == null || (getDifferenceMinutes(df.parse(last_downloaded), new Date())) > 60*2) {
                Log.d("Akhil","Downloading");
                new get_lectures().execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        foundation = view.findViewById(R.id.ca_foundation_studymaterial);
        cpt = view.findViewById(R.id.ca_cpt_studymaterial);
        tips = view.findViewById(R.id.tips);

        foundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sf = new VideoClassesListFragment();
                    String ca_found = readTxt();

                    JSONArray js = new JSONArray(ca_found);
                    JSONObject j = (JSONObject) js.get(0);
                    Bundle b = new Bundle();
                    b.putString("current_list", j.getJSONArray("childs").toString());
                    b.putInt("stack",0);
                    sf.setArguments(b);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        cpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sf = new VideoClassesListFragment();
                    String ca_found = readTxt();
                    JSONArray js = new JSONArray(ca_found);
                    JSONObject j = (JSONObject) js.get(1);
                    Log.d("Asit","massege"+j);
                    Bundle b = new Bundle();
                    b.putString("current_list", j.getJSONArray("childs").toString());
                    b.putInt("stack",0);
                    sf.setArguments(b);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sf = new VideoClassesListFragment();
                    String ca_found = readTxt();
                    JSONArray js = new JSONArray(ca_found);
                    JSONObject j = (JSONObject) js.get(2);
                    Bundle b = new Bundle();
                    b.putString("current_list", j.getJSONArray("childs").toString());
                    b.putInt("stack",0);
                    sf.setArguments(b);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flcontent, sf).commit();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        is_showing = true;
        return view;
    }

    public int getDifferenceMinutes(Date d1, Date d2) {
        int mindiff = 0;
        long diff = d2.getTime() - d1.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        mindiff = (int) diffMinutes;
        Log.d("Akhil","DAte diife "+mindiff);
        return mindiff;
    }

    private String readTxt() {
        File f = new File(Environment.getExternalStorageDirectory()+"/CA Foundation Downloads/video_data.txt");
        Log.d("Akhil","File - "+f.exists());
        if(!f.exists()){
            return  null;
        }
        try {
            FileInputStream fis = new FileInputStream(f);
            byte b[] = new byte[fis.available()];
            fis.read(b);
            fis.close();
            return (new String(b));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public class get_lectures extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading videos.. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("https://www.cakart.in/get_app_video_lectures.json");
            if (jsonStr != null) {
                try {
                    File folder = new File(Environment.getExternalStorageDirectory()+"/CA Foundation Downloads");
                    folder.mkdirs();
                    File f = new File(Environment.getExternalStorageDirectory()+"/CA Foundation Downloads/video_data.txt");
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(jsonStr.getBytes());
                    fos.close();

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("video_data",Context.MODE_PRIVATE).edit();
                    editor.putString("video_lec2_downloaded",df.format(new Date()));
                    editor.commit();

                } catch (Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();}
        }
    }


    public  void goback(){
        if(sf!=null) {
            sf.goback();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        is_showing = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        is_showing = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        is_showing = true;
    }

}

