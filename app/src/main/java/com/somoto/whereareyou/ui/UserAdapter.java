package com.somoto.whereareyou.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.somoto.whereareyou.R;
import com.somoto.whereareyou.util.User;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context) {
        super(context, R.layout.user_adapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User element = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_adapter, parent, false);
        }
        TextView umidTV = (TextView) convertView.findViewById(R.id.umid);
        TextView latTV = (TextView) convertView.findViewById(R.id.lat);
        TextView lngTV = (TextView) convertView.findViewById(R.id.lng);
        umidTV.setText(element.umid);
        latTV.setText(element.latitude);
        lngTV.setText(element.longitude);
        return convertView;
    }

}