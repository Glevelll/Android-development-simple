package com.project.scotch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.project.scotch.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Cards> {
    private final LayoutInflater inflater;
    private final int resourceId;

    public arrayAdapter(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);
        this.resourceId = resourceId;
        inflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        final TextView name;
        final ImageView image;
        final TextView age;
        final TextView gender;

        ViewHolder(View view) {
            name = view.findViewById(R.id.name_profile);
            image = view.findViewById(R.id.imageView1);
            age = view.findViewById(R.id.age_profile);
            gender = view.findViewById(R.id.gender_profile);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Cards card_item = getItem(position);

        holder.name.setText(card_item.getName());
        holder.age.setText(card_item.getAge());
        holder.gender.setText(card_item.getGender());
        Glide.with(convertView.getContext()).load(card_item.getImageView()).into(holder.image);

        return convertView;
    }
}