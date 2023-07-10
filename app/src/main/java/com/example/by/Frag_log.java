package com.example.by;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Frag_log extends Fragment {

    public List<String> log_data;
    public ArrayAdapter<String> log_adapter;
    private View view;
    private ListView lv_log;            // 로그 화면 리스트뷰


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_log, container, false);
        // 로그 리스트뷰 생성
        lv_log = (ListView) view.findViewById(R.id.lv_log);
        log_data = new ArrayList<>();
        log_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, log_data);
        lv_log.setAdapter(log_adapter);

        List<Log> logList = MainActivity.mLogDao.getLogAll();
        for (int i = 0; i < logList.size(); i++) {
            log_add(logList.get(i).getTime(), logList.get(i).getName(), logList.get(i).getCur_value(), logList.get(i).getResult());
        }

        log_adapter.notifyDataSetChanged();

        return view;
    }

    void log_add(String name, String time, String cur_value, int result){
        log_data.add(time + "    " + name + "    " + cur_value + "    " + (result - Integer.parseInt(cur_value)) + "    " + result);
    }
}
