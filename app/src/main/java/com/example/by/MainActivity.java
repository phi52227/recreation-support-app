package com.example.by;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; //바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag_story frag_story;
    private Frag_store frag_store;
    private Frag_inventory frag_inventory;
    private Frag_log frag_log;

    public static UserDao mUserDao;
    public static LogDao mLogDao;
    public static ItemDao mItemDao;
    public static StoryDao mStoryDao;

    private long backBtnTime = 0;
    ImageButton btn_setting;            // 상단 설정 버튼
    TextView tv_team;                   // 상단 조 표시

    public static TextView tv_gold;                   // 상단 골드 표시
    static TextView tv_point;                  // 상단 점수 표시

    String shared ="file";              // SharedPreferences 변수

    private EditText editTextGold;      // 다이얼로그 골드 기입
    private EditText editTextPassword;  // 다이얼로그 패스워드 기입

    // 아이템 추가 리스트
    ArrayList<String> array_item_name = new ArrayList<String>();
    ArrayList<String> array_item_price = new ArrayList<String>();
    ArrayList<Integer> array_item_visible = new ArrayList<Integer>();

    // 스토리 추가 리스트
    ArrayList<String> array_story_name = new ArrayList<String>();
    ArrayList<String> array_story_subName = new ArrayList<String>();
    ArrayList<Integer> array_story_point = new ArrayList<Integer>();
    ArrayList<Integer> array_story_visible = new ArrayList<Integer>();


    private List<String> log_data;
    private ArrayAdapter<String> log_adapter;
    private ListView lv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserDatabase database = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "state")
                .fallbackToDestructiveMigration()       // 스키마 버전 변경 가능
                .allowMainThreadQueries()               // Main Thread에서 DB에 IO를 가능하게 함
                .build();

        mUserDao = database.userDao(); // 인터페이스 객체 할당

        LogDatabase logDatabase = Room.databaseBuilder(getApplicationContext(), LogDatabase.class, "Log")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mLogDao = logDatabase.logDao();

        ItemDatabase itemDatabase = Room.databaseBuilder(getApplicationContext(), ItemDatabase.class, "Item")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mItemDao = itemDatabase.itemDao();

        StoryDatabase storyDatabase = Room.databaseBuilder(getApplicationContext(), StoryDatabase.class, "Story")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        mStoryDao = storyDatabase.storyDao();
        
        // 컴포넌트 연결
        btn_setting = (ImageButton)findViewById(R.id.btn_setting);
        tv_team = (TextView)findViewById(R.id.tv_team);

        tv_gold = (TextView)findViewById(R.id.tv_gold);
        tv_point = (TextView)findViewById(R.id.tv_point);

        // 조 이름 SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String value_team = sharedPreferences.getString("team", "");
        tv_team.setText(value_team);

        // 데이터 삽입

        int value_check = sharedPreferences.getInt("check", 0);
        if (value_check != 1) {
            User user = new User(); // 객체 인스턴스 생성
            user.setGold("0");
            user.setPoint("0");
            mUserDao.setInsertUser(user);

            AddStore();
            for (int i = 0; i < array_item_price.size(); i++) {
                Item item = new Item();
                item.setName(array_item_name.get(i));
                item.setPrice(Integer.parseInt(array_item_price.get(i)));
                item.setVisible(array_item_visible.get(i));
                item.setQty(0);
                mItemDao.setInsertUser(item);
            }

            AddStory();
            for (int i = 0; i < array_story_name.size(); i++) {
                Story story = new Story();
                story.setName(array_story_name.get(i));
                story.setSubMame(array_story_subName.get(i));
                story.setPoint(array_story_point.get(i));
                story.setVisible(array_story_visible.get(i));
                story.setClear(0);
                mStoryDao.setInsetStory(story);
            }

            dataSave();

        }

        // 골드, 포인트 불러오기
        List<User> userList = mUserDao.getUserAll();
        tv_gold.setText(userList.get(0).getGold());
        tv_point.setText(userList.get(0).getPoint());


        // 바텀 메뉴
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_story:
                        setFrag(0);
                        break;
                    case R.id.action_store:
                        setFrag(1);
                        break;
                    case R.id.action_inventory:
                        setFrag(2);
                        break;
                    case R.id.action_log:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        frag_story = new Frag_story();
        frag_store = new Frag_store();
        frag_inventory = new Frag_inventory();
        frag_log = new Frag_log();
        setFrag(0); //첫 프래그먼트 설정 (스토리로)

        
        // 골드 점수로 변환
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setIcon(R.drawable.ic_baseline_border_color_24);
                ad.setTitle("골드 -> 점수");
                ad.setMessage("정말 변환하시겠습니까?\n변환 배율은 1/10 입니다.");

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double result = Integer.parseInt(tv_point.getText().toString()) + (0.1 * (Integer.parseInt(tv_gold.getText().toString())));

                        Log log = new Log();
                        log.setName("점수 변환");
                        log.setCur_value(tv_point.getText().toString());
                        log.setResult((int) result);
                        log.setTime(getTime());
                        mLogDao.setInsertLog(log);

                        tv_point.setText(String.valueOf((int) result));
                        tv_gold.setText("0");

                        User user = new User();
                        user.setId(1);
                        user.setGold(tv_gold.getText().toString());
                        user.setPoint(tv_point.getText().toString());
                        mUserDao.setUpdateUser(user);

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
        });

        // 조 설정 버튼
        tv_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setIcon(R.drawable.ic_baseline_border_color_24);
                ad.setTitle("조 입력");
                ad.setMessage("몇조입니까?");


                final EditText et = new EditText(MainActivity.this);
                ad.setView(et);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String result = et.getText().toString();
                        tv_team.setText(result);
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
        });

        // 골드 조정
        tv_gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_gold:
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.gold_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("골드 수정");
                        builder.setView(dialogView);

                        String plus = "증가";
                        String password = "113333";

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final EditText editTextGold = (EditText) dialogView.findViewById(R.id.editTextGold);
                                final EditText editTextPassword = (EditText) dialogView.findViewById(R.id.editTextPassword);
                                final RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.dialog_rg);

                                User user_gold = new User();
                                user_gold.setId(1);


                                int checkedId = rg.getCheckedRadioButtonId();
                                int result;

                                RadioButton rb = (RadioButton) rg.findViewById(checkedId);
                                String nation = "";
                                try {
                                    nation = rb.getText().toString();
                                } catch (Exception e) {

                                }

                                if (editTextPassword.getText().toString().equals(password)){
                                    if (nation.equals(plus)) {
                                        result = Integer.parseInt(tv_gold.getText().toString()) + Integer.parseInt(editTextGold.getText().toString());
                                        dialogInterface.dismiss();
                                    }
                                    else{
                                        result = Integer.parseInt(tv_gold.getText().toString()) - Integer.parseInt(editTextGold.getText().toString());
                                        dialogInterface.dismiss();
                                    }

                                    Log log = new Log();
                                    log.setName("Gold");
                                    log.setCur_value(tv_gold.getText().toString());
                                    log.setResult(result);
                                    log.setTime(getTime());
                                    mLogDao.setInsertLog(log);


                                    tv_gold.setText(String.valueOf(result));

                                    user_gold.setGold(String.valueOf(result));
                                    user_gold.setPoint(tv_point.getText().toString());
                                    mUserDao.setUpdateUser(user_gold);

                                    Toast.makeText(getApplicationContext(), "수정했습니다", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.setCanceledOnTouchOutside(false);

                dialog.show();

                break;
                }
            }
        });

        // 점수 조정
        tv_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_point:
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.point_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("점수 수정");
                        builder.setView(dialogView);

                        String password = "113333";

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final EditText editTextPoint = (EditText) dialogView.findViewById(R.id.editTextPoint);
                                final EditText editTextPassword = (EditText) dialogView.findViewById(R.id.editTextPassword);

                                User user_point = new User();
                                user_point.setId(1);

                                if (editTextPassword.getText().toString().equals(password)){
                                    int result = Integer.parseInt(tv_point.getText().toString()) + Integer.parseInt(editTextPoint.getText().toString());

                                    Log log = new Log();
                                    log.setName("Point");
                                    log.setCur_value(tv_point.getText().toString());
                                    log.setResult(result);
                                    log.setTime(getTime());
                                    mLogDao.setInsertLog(log);

                                    tv_point.setText(String.valueOf(result));
                                    dialogInterface.dismiss();
                                    user_point.setPoint(String.valueOf(result));
                                    user_point.setGold(tv_gold.getText().toString());
                                    mUserDao.setUpdateUser(user_point);


                                    Toast.makeText(getApplicationContext(), "수정했습니다", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();

                        dialog.setCanceledOnTouchOutside(false);

                        dialog.show();

                        break;
                }
            }
        });

    } //onCreate 끝



    //프래그먼트 교체 실행문
    void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, frag_story);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, frag_store);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, frag_inventory);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, frag_log);
                ft.commit();
                break;
        }
    }

    
    // 뒤로가기 두 번 눌러 종료
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && gapTime <= 2000){
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

    // onDestroy 생명주기
    @Override
    protected void onDestroy() {
        super.onDestroy();

        dataSave();
    }

    // sharedPreferences Data save
    protected void dataSave() {
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value_team = tv_team.getText().toString();
        editor.putString("team", value_team);
        editor.putInt("check", 1);
        editor.commit();
    }

    // 현재 시간 구하기
    public static String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }

    // 상점 아이템 추가
    private void AddStore(){
        array_item_name.add("베이스 허브");
        array_item_price.add("1200");
        array_item_visible.add(1);

        array_item_name.add("골드 허브");
        array_item_price.add("1500");
        array_item_visible.add(1);

        array_item_name.add("블루 허브");
        array_item_price.add("1300");
        array_item_visible.add(1);

        array_item_name.add("1급 정제수");
        array_item_price.add("1000");
        array_item_visible.add(1);

        array_item_name.add("피닉스의 깃털");
        array_item_price.add("1200");
        array_item_visible.add(1);

        array_item_name.add("만년철");
        array_item_price.add("1500");
        array_item_visible.add(1);

        //all 6

        array_item_name.add("물");
        array_item_price.add("1000");
        array_item_visible.add(1);

        array_item_name.add("용병단 고용");
        array_item_price.add("1200");
        array_item_visible.add(1);

        array_item_name.add("가짜 행운의 묘약");
        array_item_price.add("1000");
        array_item_visible.add(1);

        array_item_name.add("함정");
        array_item_price.add("800");
        array_item_visible.add(1);

        array_item_name.add("귀환 스크롤");
        array_item_price.add("900");
        array_item_visible.add(1);

        array_item_name.add("꽃다발");
        array_item_price.add("1200");
        array_item_visible.add(1);

        array_item_name.add("변신 해제 물약");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("자백제");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("문자 해독 돋보기");
        array_item_price.add("0");
        array_item_visible.add(0);

        // all 15 add 3
        array_item_name.add("만드레이크");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("붉은 사자 버섯");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("블루 크리스탈");
        array_item_price.add("0");
        array_item_visible.add(0);

        // all 18
        array_item_name.add("의문의 쪽지 #1");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #2");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #3");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #4");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #5");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #6");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #7");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("종이");
        array_item_price.add("500");
        array_item_visible.add(1);

        array_item_name.add("펜");
        array_item_price.add("500");
        array_item_visible.add(1);

        // all 27 invisible 13
        array_item_name.add("개발자의 편지");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #8");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #9");
        array_item_price.add("0");
        array_item_visible.add(0);

        array_item_name.add("의문의 쪽지 #10");
        array_item_price.add("0");
        array_item_visible.add(0);
    }

    // 스토리 아이템 추가
    private void AddStory() {

        array_story_name.add("Tutorial");
        array_story_subName.add("어플 사용법");
        array_story_point.add(100);
        array_story_visible.add(1);

        array_story_name.add("Main - 1");
        array_story_subName.add("재상의 의뢰");
        array_story_point.add(1000);
        array_story_visible.add(1);

        array_story_name.add("Main - 2");
        array_story_subName.add("정보를 얻어야 한다.");
        array_story_point.add(1000);
        array_story_visible.add(0);

        array_story_name.add("Main - 3");
        array_story_subName.add("소환 의식을 막아라.");
        array_story_point.add(1000);
        array_story_visible.add(0);

        array_story_name.add("Sub - 1");
        array_story_subName.add("화재");
        array_story_point.add(500);
        array_story_visible.add(1);

        array_story_name.add("Sub - 2");
        array_story_subName.add("야만인들의 침략");
        array_story_point.add(500);
        array_story_visible.add(1);

        array_story_name.add("Sub - 3");
        array_story_subName.add("붉은 실");
        array_story_point.add(500);
        array_story_visible.add(1);

        array_story_name.add("Sub - 4");
        array_story_subName.add("보름달의 암살자");
        array_story_point.add(500);
        array_story_visible.add(1);

        array_story_name.add("Sub - 5");
        array_story_subName.add("던전 조사 의뢰");
        array_story_point.add(500);
        array_story_visible.add(1);

        array_story_name.add("Sub - 6");
        array_story_subName.add("공녀의 초대");
        array_story_point.add(500);
        array_story_visible.add(1);
    }
}