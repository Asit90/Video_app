package cakart.cakart.in.video_app.videoclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cakart.cakart.in.video_app.model.Node;
import cakart.cakart.in.video_app.R;


public class VideoClassesListFragment extends Fragment {


    String current_list;
    String parent;
    boolean is_root = true;
    ListView list;
    int stack = 0;
    ProgressDialog mProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studymaterialsfragment, container, false);
        current_list = getArguments().getString("current_list");
        parent = getArguments().getString("parent");
        stack = getArguments().getInt("stack");
        list = (ListView) view.findViewById(R.id.list);
        showList(current_list, true);

        return view;
    }


    public void showNext(String next_a) {

        VideoClassesListFragment sf = new VideoClassesListFragment();
        Bundle b = new Bundle();
        b.putString("current_list", next_a);
        b.putInt("stack", stack);
        sf.setArguments(b);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack("stc_" + stack).replace(R.id.flcontent, sf).commit();
    }


    public void showList(String json_s, boolean is_root) {
        try {
            JSONArray json = new JSONArray(json_s);
            final ArrayList<Node> a = new ArrayList<Node>();
            for (int i = 0; i < json.length(); i++) {
                JSONObject j = (JSONObject) json.get(i);
                Node n = new Node();

                if (j.getString("type").equals("video")) {
                    n.setUrl(j.getString("video_url"));
                    n.setVideo_thumb(j.getString("thumb"));
                    n.setName(j.getString("title"));
                }else{
                    n.setName(j.getString("name"));
                }
                if (j.has("childs")) {
                    n.setChilds(j.getJSONArray("childs").toString());
                }
                n.setParent(json.toString());
                a.add(n);
            }
            VideoAdapter adapter = new VideoAdapter(a, getContext());
            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> p, View view, int position, long id) {
                    Node clicked_node = a.get(position);
                    if (clicked_node.getUrl() != null) {
                        Uri uri = Uri.parse( clicked_node.getUrl());
                        if(clicked_node.getUrl().contains("v=")) {
                            uri = Uri.parse("vnd.youtube:" + uri.getQueryParameter("v"));
                        }else{
                            uri = Uri.parse("vnd.youtube:" + clicked_node.getUrl().substring(clicked_node.getUrl().lastIndexOf("/")+1));
                        }
                        startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
                    } else {
                        stack = stack + 1;
                        DeckListActivity d = (DeckListActivity) getActivity();
                        d.stack_count = stack;
                        parent = clicked_node.getParent();
                        Log.d("Akhil", "stacj" + stack);
                        showNext(clicked_node.getChilds());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goback() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getActivity().finish();
            return;
        } else {
            getFragmentManager().popBackStack();
        }
    }


    public static VideoClassesListFragment newInstance(String current_list, String parent) {

        VideoClassesListFragment f = new VideoClassesListFragment();
        Bundle b = new Bundle();
        b.putString("current_list", current_list);
        b.putString("parent", parent);
        f.setArguments(b);

        return f;
    }
}