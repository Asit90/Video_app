package cakart.cakart.in.video_app.videoclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cakart.cakart.in.video_app.model.Node;
import cakart.cakart.in.video_app.R;


public class VideoAdapter extends ArrayAdapter<Node> {

    private ArrayList<Node> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        ImageView icon;
    }

    public VideoAdapter(ArrayList<Node> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Node Node = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.video_row_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.name.setText(Node.getName());
        if (Node.getUrl() == null) {
            viewHolder.icon.setImageResource(R.drawable.folder);
        } else {
            Glide.with(getContext())
                    .load(Node.getVideo_thumb()).into(viewHolder.icon);
        }
        // Return the completed view to render on screen
        return convertView;
    }
}