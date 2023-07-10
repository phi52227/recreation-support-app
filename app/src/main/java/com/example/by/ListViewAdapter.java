package com.example.by;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> arrayList = new ArrayList<>();

    public ListViewAdapter(){

    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listviewitem, viewGroup, false);
        }

        ImageView imageView = view.findViewById(R.id.iv_item);
        TextView text1 = view.findViewById(R.id.tv_store_name);
        TextView text2 = view.findViewById(R.id.tv_store_value);

        ListViewItem listViewItem = arrayList.get(i);

        imageView.setImageDrawable(listViewItem.getDrawable());
        text1.setText(listViewItem.getText1());
        text2.setText(listViewItem.getText2());
        return view;
    }

    public void addItem(Drawable drawable, String text1, String text2){
        ListViewItem listViewItem = new ListViewItem();
        listViewItem.setDrawable(drawable);
        listViewItem.setText1(text1);
        listViewItem.setText2(text2);

        arrayList.add(listViewItem);
    }
}
