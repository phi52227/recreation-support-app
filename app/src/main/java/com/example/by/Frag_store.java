package com.example.by;

import static com.example.by.MainActivity.mLogDao;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

public class Frag_store extends Fragment {

    public ListViewAdapter store_adapter;
    private View view;
    private ListView lv_store;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_store, container, false);

        // store 리스트뷰 생성
        lv_store = (ListView) view.findViewById(R.id.lv_store);
        store_adapter = new ListViewAdapter();

        int iResId;
        List<Item> storeList = MainActivity.mItemDao.getItemAsc();
        for (int i = 0; i < storeList.size(); i++) {
            iResId = getResources().getIdentifier("item" + storeList.get(i).getId(), "drawable", getActivity().getPackageName());
            store_adapter.addItem(ContextCompat.getDrawable(getActivity(), iResId), storeList.get(i).getName(), String.valueOf(storeList.get(i).getPrice()) + " G");
        }

        lv_store.setAdapter(store_adapter);
        store_adapter.notifyDataSetChanged();

        // 리스트뷰 클릭 이벤트 (구매)
        lv_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<Item> storeList = MainActivity.mItemDao.getItemAsc();
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("아이팀 구매");
                ad.setMessage(storeList.get(i).getName() + " 을(를) 구매하시겠습니까?");

                int j = i;

                final TextView tv = new TextView(getActivity());
                tv.setText("  가격 : " + storeList.get(i).getPrice() + " G");
                ad.setView(tv);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        User user = new User();
                        user.setId(1);

                        Item item = new Item();
                        item.setId(storeList.get(j).getId());

                        if ((Integer.parseInt(MainActivity.tv_gold.getText().toString())) >= (storeList.get(j).getPrice())){

                            String result = String.valueOf(Integer.parseInt(MainActivity.tv_gold.getText().toString()) - storeList.get(j).getPrice());

                            Log log = new Log();
                            log.setName(storeList.get(j).getName() + " 구매");
                            log.setCur_value(MainActivity.tv_gold.getText().toString());
                            log.setResult(Integer.parseInt(result));
                            log.setTime(MainActivity.getTime());
                            mLogDao.setInsertLog(log);

                            MainActivity.tv_gold.setText(result);
                            user.setPoint(MainActivity.tv_point.getText().toString());
                            user.setGold(result);
                            MainActivity.mUserDao.setUpdateUser(user);

                            item.setName(storeList.get(j).getName());
                            item.setPrice(storeList.get(j).getPrice());
                            item.setQty(storeList.get(j).getQty() + 1);
                            item.setVisible(storeList.get(j).getVisible());
                            MainActivity.mItemDao.setUpdateUser(item);


                            Toast.makeText(getActivity(),"아이템을 구매했습니다.", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                        else{
                            Toast.makeText(getActivity(), "골드가 부족합니다", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }

                    }
                });

                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                ad.show();
            }
        });

        return view;
    }
}
