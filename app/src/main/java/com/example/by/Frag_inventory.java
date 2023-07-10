package com.example.by;

import static com.example.by.MainActivity.mLogDao;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;
import java.util.List;

public class Frag_inventory extends Fragment {

//    public List<String> inven_data;
//    public ArrayAdapter<String> inven_adapter;
    public ListViewAdapter inven_adapter;
    private View view;
    private ListView lv_inven;
    private Button btn_mix;
    private Button btn_add;
    private TextView tv_mix;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_inventory, container, false);

        btn_mix = (Button)view.findViewById(R.id.btn_mix);
        btn_add = (Button)view.findViewById(R.id.btn_add);

        User user = new User();

        // inven 리스트뷰 생성
        create_inven();

        final int[] refresh_check = {0};

        // storyList
        List<Story> storyList = MainActivity.mStoryDao.getStoryAll();

        // allList에 DB값 넣기
        List<Item> allList = MainActivity.mItemDao.getItemAll();

        // invisibleList에 DB값 넣기
        List<Item> invisibleList = MainActivity.mItemDao.getItemInvisible();

        // invenList에 DB값 넣기
        int iResId;
        List<Item> invenList = MainActivity.mItemDao.getItemInventoryAsc();
        for (int i = 0; i < invenList.size(); i++) {
            iResId = getResources().getIdentifier("item" + invenList.get(i).getId(), "drawable", getActivity().getPackageName());
            inven_adapter.addItem(ContextCompat.getDrawable(getActivity(), iResId), invenList.get(i).getName(), (invenList.get(i).getQty()) + " 개");
        }
        lv_inven.setAdapter(inven_adapter);
        inven_adapter.notifyDataSetChanged();

        // ListView 클릭 이벤트 (판매)
        lv_inven.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (refresh_check[0] == 0) {

                    //visible이 0인 품목은 판매 불가능하도록
                    if(invenList.get(i).getVisible() == 1) {

                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                        ad.setTitle("아이템 판매");
                        ad.setMessage(invenList.get(i).getName() + " 1개를 판매하시겠습니까?");

                        int j = i;

                        final TextView tv = new TextView(getActivity());
                        tv.setText("  판매 시 " + invenList.get(i).getPrice() + " Gold를 획득합니다.");
                        ad.setView(tv);

                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                User user = new User();
                                user.setId(1);

                                Item item = new Item();
                                item.setId(invenList.get(j).getId());

                                String result = String.valueOf(Integer.parseInt(MainActivity.tv_gold.getText().toString()) + invenList.get(j).getPrice());

                                Log log = new Log();
                                log.setName(invenList.get(j).getName() + " 판매");
                                log.setCur_value(MainActivity.tv_gold.getText().toString());
                                log.setResult(Integer.parseInt(result));
                                log.setTime(MainActivity.getTime());
                                mLogDao.setInsertLog(log);

                                MainActivity.tv_gold.setText(result);
                                user.setPoint(MainActivity.tv_point.getText().toString());
                                user.setGold(result);
                                MainActivity.mUserDao.setUpdateUser(user);

                                item.setName(invenList.get(j).getName());
                                item.setPrice(invenList.get(j).getPrice());
                                item.setQty(invenList.get(j).getQty() - 1);
                                item.setVisible(invenList.get(j).getVisible());
                                MainActivity.mItemDao.setUpdateUser(item);

                                refresh_check[0] = 1;
                                Toast.makeText(getActivity(), "아이템을 판매했습니다.", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        });

                        ad.setNegativeButton(" 취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        ad.show();
                    }

                    // 의문의 쪽지들 클릭 이벤트
                    else if (invenList.get(i).getName().contains("의문의 쪽지")){

                        TextView tv_note, tv_hint;

                        LayoutInflater layoutInflater = getLayoutInflater();
                        final View view_note = layoutInflater.inflate(R.layout.dialog_note, null);

                        tv_note = view_note.findViewById(R.id.tv_note);
                        tv_hint = view_note.findViewById(R.id.tv_hint);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setView(view_note);

                        switch (invenList.get(i).getName().substring(8)){
                            case "1":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("Black");
                                break;
                            case "2":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("W");
                                break;
                            case "3":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("Baby");
                                break;
                            case "4":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("SM");
                                break;
                            case "5":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("21");
                                break;
                            case "6":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("4강");
                                break;
                            case "7":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("붉은 악마");
                                break;
                            case "8":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("XX");
                                break;
                            case "9":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("oi");
                                break;
                            case "10":
                                tv_note.setText(invenList.get(i).getName());
                                tv_hint.setText("DANCE");
                                break;

                        }

                        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        builder.show();

                    }
                    // 개발자의 편지 클릭 이벤트
                    else if (invenList.get(i).getName().equals("개발자의 편지")){
                        TextView tv_note, tv_hint;

                        LayoutInflater layoutInflater = getLayoutInflater();
                        final View view_note = layoutInflater.inflate(R.layout.dialog_note, null);

                        tv_note = view_note.findViewById(R.id.tv_note);
                        tv_hint = view_note.findViewById(R.id.tv_hint);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setView(view_note);
                        tv_note.setText(invenList.get(i).getName());
                        tv_hint.setText("댕댕이랑 여행가고 싶다.");
                        builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        builder.show();
                    }
                    else {
                        Toast.makeText(getActivity(), "등록/합성 아이템은 판매할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getActivity(), "다른 화면으로 갔다와주세요..ㅜㅜ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 등록 버튼 클릭 이벤트
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refresh_check[0] == 0) {

                    Log log = new Log();

                    LayoutInflater inflater1 = getLayoutInflater();
                    final View view_add = inflater1.inflate(R.layout.add_dialog, null);

                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                    ad.setTitle("아이템 등록");
                    ad.setMessage("아이템 코드를 입력해주세요.");

                    ad.setView(view_add);

                    final EditText et = view_add.findViewById(R.id.editTextCode);

                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String item = et.getText().toString();

                            List<Item> addList = MainActivity.mItemDao.getItemInvisible();

                            Item item_add = new Item();

                            switch (item){

                                // 메인1 재료 아이템
                                case "243798" :
                                    item_add.setId(addList.get(3).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(3).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(3).getVisible());
                                    item_add.setPrice(addList.get(3).getPrice());
                                    item_add.setName(addList.get(3).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("만드레이크 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);

                                    refresh_check[0] = 1;
                                    break;
                                // 메인2 재료 아이템
                                case "141240" :
                                    item_add.setId(addList.get(4).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(4).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(4).getVisible());
                                    item_add.setPrice(addList.get(4).getPrice());
                                    item_add.setName(addList.get(4).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("붉은 사자 버섯 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 메인3 재료 아이템
                                case "790523" :
                                    item_add.setId(addList.get(5).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(5).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(5).getVisible());
                                    item_add.setPrice(addList.get(5).getPrice());
                                    item_add.setName(addList.get(5).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("블루 크리스탈 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트1
                                case "58911" :
                                    item_add.setId(addList.get(14).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(14).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(14).getVisible());
                                    item_add.setPrice(addList.get(14).getPrice());
                                    item_add.setName(addList.get(14).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #8 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트2
                                case "23145" :
                                    item_add.setId(addList.get(15).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(15).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(15).getVisible());
                                    item_add.setPrice(addList.get(15).getPrice());
                                    item_add.setName(addList.get(15).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #9 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트3
                                case "56842" :
                                    item_add.setId(addList.get(16).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(16).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(16).getVisible());
                                    item_add.setPrice(addList.get(16).getPrice());
                                    item_add.setName(addList.get(16).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #10 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트4
                                case "18964" :
                                    item_add.setId(addList.get(9).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(9).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(9).getVisible());
                                    item_add.setPrice(addList.get(9).getPrice());
                                    item_add.setName(addList.get(9).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #4 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트5
                                case "56489" :
                                    item_add.setId(addList.get(10).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(10).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(10).getVisible());
                                    item_add.setPrice(addList.get(10).getPrice());
                                    item_add.setName(addList.get(10).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #5 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트6
                                case "56104" :
                                    item_add.setId(addList.get(11).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(11).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(11).getVisible());
                                    item_add.setPrice(addList.get(11).getPrice());
                                    item_add.setName(addList.get(11).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #6 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;
                                // 2라운드 보상 힌트7
                                case "76805" :
                                    item_add.setId(addList.get(12).getId());                                   // 아이템 확정나면 수정해야 된다..!!!!
                                    item_add.setQty(addList.get(12).getQty() + 1);        // 이거 ID 값이 아니고 visible 0 인 데이터 리스트의 인덱스!!
                                    item_add.setVisible(addList.get(12).getVisible());
                                    item_add.setPrice(addList.get(12).getPrice());
                                    item_add.setName(addList.get(12).getName());
                                    MainActivity.mItemDao.setUpdateUser(item_add);
                                    refresh_check[0] = 1;
                                    Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    log.setName("의문의 쪽지 #7 획득");
                                    log.setCur_value("0");
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);
                                    break;

                                /*
                                이거는 튜토리얼 스토리 구상하고 추가하자
                                DB에 클리어 컬럼을 만들어서 클리어됐으면 다시 사용 못하게
                                튜토 클리어시 골드 0값으로 만들면 될듯
                                 */
                                case "Gold" :

                                    if(storyList.get(0).getClear() == 0) {

                                        MainActivity.tv_gold.setText("1000");

                                        user.setId(1);
                                        user.setPoint(MainActivity.tv_point.getText().toString());
                                        user.setGold("1000");
                                        MainActivity.mUserDao.setUpdateUser(user);

                                        log.setName("튜토리얼 골드 획득 코드");
                                        log.setCur_value("0");
                                        log.setResult(1000);
                                        log.setTime(MainActivity.getTime());
                                        mLogDao.setInsertLog(log);

                                        refresh_check[0] = 1;

                                        Toast.makeText(getActivity(), "아이템이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "사용할 수 없는 코드입니다.", Toast.LENGTH_SHORT).show();
                                    }

                                    break;
                                default:
                                    Toast.makeText(getActivity(), "존재하지 않는 코드입니다.", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            dialogInterface.dismiss();
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
                else {
                    Toast.makeText(getActivity(), "다른 화면으로 갔다와주세요..ㅜㅜ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 합성 버튼 클릭 이벤트
        btn_mix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater1 = getLayoutInflater();
                final View view_mix = inflater1.inflate(R.layout.mix_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("아이템 합성");
                builder.setView(view_mix);


                final RadioGroup rg = (RadioGroup) view_mix.findViewById(R.id.mix_rg);
                final int[] checkId = {rg.getCheckedRadioButtonId()};

                tv_mix = (TextView) view_mix.findViewById(R.id.tv_mix);

                Item item = new Item();

                // 라디오버튼 선택 이벤트
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                        switch (checkId) {
                            case R.id.mix_rb_1:
                                tv_mix.setText("필요 아이템 : 베이스 허브, 골드 허브, 만드레이크");
                                break;
                            case R.id.mix_rb_2:
                                tv_mix.setText("필요 아이템 : 블루 허브, 1급 정제수, 붉은 사자 버섯");
                                break;
                            case R.id.mix_rb_3:
                                tv_mix.setText("필요 아이템 : 피닉스의 깃털, 만년철, 블루 크리스탈");
                                break;
                            case R.id.mix_rb_4:
                                tv_mix.setText("필요 아이템 : 종이, 펜");
                                break;
                        }
                    }
                });

                // 합성 클릭 이벤트
                builder.setPositiveButton("합성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        refresh_check[0] = 1;
                        checkId[0] = rg.getCheckedRadioButtonId();
                        RadioButton rb = rg.findViewById(checkId[0]);
                        String nation = "";
                        try {
                            nation = rb.getText().toString();
                        } catch (Exception e) {

                        }

                        switch (nation) {
                            case "변신 해제 물약":
                                if (allList.get(0).getQty() > 0 && allList.get(1).getQty() > 0 && allList.get(15).getQty() > 0) {

                                    item.setId(invisibleList.get(0).getId());
                                    item.setName(invisibleList.get(0).getName());
                                    item.setPrice(invisibleList.get(0).getPrice());
                                    item.setQty(invisibleList.get(0).getQty() + 1);
                                    item.setVisible(invisibleList.get(0).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(0).getId());
                                    item.setName(allList.get(0).getName());
                                    item.setPrice(allList.get(0).getPrice());
                                    item.setQty(allList.get(0).getQty() - 1);
                                    item.setVisible(allList.get(0).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(1).getId());
                                    item.setName(allList.get(1).getName());
                                    item.setPrice(allList.get(1).getPrice());
                                    item.setQty(allList.get(1).getQty() - 1);
                                    item.setVisible(allList.get(1).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(15).getId());
                                    item.setName(allList.get(15).getName());
                                    item.setPrice(allList.get(15).getPrice());
                                    item.setQty(allList.get(15).getQty() - 1);
                                    item.setVisible(allList.get(15).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    Log log = new Log();
                                    log.setName(invisibleList.get(0).getName() + " (합성)");
                                    log.setCur_value(MainActivity.tv_gold.getText().toString());
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);

                                    Toast.makeText(getActivity(), "합성에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "재료 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.dismiss();
                                break;
                            case "자백제":

                                if (allList.get(2).getQty() > 0 && allList.get(3).getQty() > 0 && allList.get(16).getQty() > 0) {

                                    item.setId(invisibleList.get(1).getId());
                                    item.setName(invisibleList.get(1).getName());
                                    item.setPrice(invisibleList.get(1).getPrice());
                                    item.setQty(invisibleList.get(1).getQty() + 1);
                                    item.setVisible(invisibleList.get(1).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(2).getId());
                                    item.setName(allList.get(2).getName());
                                    item.setPrice(allList.get(2).getPrice());
                                    item.setQty(allList.get(2).getQty() - 1);
                                    item.setVisible(allList.get(2).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(3).getId());
                                    item.setName(allList.get(3).getName());
                                    item.setPrice(allList.get(3).getPrice());
                                    item.setQty(allList.get(3).getQty() - 1);
                                    item.setVisible(allList.get(3).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(16).getId());
                                    item.setName(allList.get(16).getName());
                                    item.setPrice(allList.get(16).getPrice());
                                    item.setQty(allList.get(16).getQty() - 1);
                                    item.setVisible(allList.get(16).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    Log log = new Log();
                                    log.setName(invisibleList.get(1).getName() + " (합성)");
                                    log.setCur_value(MainActivity.tv_gold.getText().toString());
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);

                                    Toast.makeText(getActivity(), "합성에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "재료 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.dismiss();
                                break;
                            case "문자 해독 돋보기":

                                if (allList.get(4).getQty() > 0 && allList.get(5).getQty() > 0 && allList.get(17).getQty() > 0) {

                                    item.setId(invisibleList.get(2).getId());
                                    item.setName(invisibleList.get(2).getName());
                                    item.setPrice(invisibleList.get(2).getPrice());
                                    item.setQty(invisibleList.get(2).getQty() + 1);
                                    item.setVisible(invisibleList.get(2).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(4).getId());
                                    item.setName(allList.get(4).getName());
                                    item.setPrice(allList.get(4).getPrice());
                                    item.setQty(allList.get(4).getQty() - 1);
                                    item.setVisible(allList.get(4).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(5).getId());
                                    item.setName(allList.get(5).getName());
                                    item.setPrice(allList.get(5).getPrice());
                                    item.setQty(allList.get(5).getQty() - 1);
                                    item.setVisible(allList.get(5).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(17).getId());
                                    item.setName(allList.get(17).getName());
                                    item.setPrice(allList.get(17).getPrice());
                                    item.setQty(allList.get(17).getQty() - 1);
                                    item.setVisible(allList.get(17).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    Log log = new Log();
                                    log.setName(invisibleList.get(2).getName() + " (합성)");
                                    log.setCur_value(MainActivity.tv_gold.getText().toString());
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);

                                    Toast.makeText(getActivity(), "합성에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "재료 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.dismiss();
                                break;

                            case "개발자의 편지" :
                                if (allList.get(25).getQty() > 0 && allList.get(26).getQty() > 0) {

                                    item.setId(invisibleList.get(13).getId());
                                    item.setName(invisibleList.get(13).getName());
                                    item.setPrice(invisibleList.get(13).getPrice());
                                    item.setQty(invisibleList.get(13).getQty() + 1);
                                    item.setVisible(invisibleList.get(13).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(25).getId());
                                    item.setName(allList.get(25).getName());
                                    item.setPrice(allList.get(25).getPrice());
                                    item.setQty(allList.get(25).getQty() - 1);
                                    item.setVisible(allList.get(25).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    item.setId(allList.get(26).getId());
                                    item.setName(allList.get(26).getName());
                                    item.setPrice(allList.get(26).getPrice());
                                    item.setQty(allList.get(26).getQty() - 1);
                                    item.setVisible(allList.get(26).getVisible());
                                    MainActivity.mItemDao.setUpdateUser(item);

                                    Log log = new Log();
                                    log.setName(invisibleList.get(13).getName() + " (합성)");
                                    log.setCur_value(MainActivity.tv_gold.getText().toString());
                                    log.setResult(Integer.parseInt(MainActivity.tv_gold.getText().toString()));
                                    log.setTime(MainActivity.getTime());
                                    mLogDao.setInsertLog(log);

                                    Toast.makeText(getActivity(), "합성에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getActivity(), "재료 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.dismiss();
                                break;
                            default:
                                Toast.makeText(getActivity(), "합성할 아이템을 선택해주세요.", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });

                // 취소 클릭 이벤트
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });


        return view;
    }

    // inven 리스트뷰 생성
    void create_inven() {
        lv_inven = (ListView) view.findViewById(R.id.lv_inven);
        inven_adapter = new ListViewAdapter();
    }

}
