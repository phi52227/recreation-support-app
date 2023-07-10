package com.example.by;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Frag_story extends Fragment {

    private View view;
    public ListViewStoryAdapter story_adapter;

    public ListView lv_story;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_story, container, false);

        final int[] refresh_check = {0};

        // stroy_name 리스트뷰 생성
        lv_story = view.findViewById(R.id.lv_story);
        story_adapter = new ListViewStoryAdapter();
        lv_story.setAdapter(story_adapter);

        // 리스트뷰에 내용 추가
        List<Story> storyList = MainActivity.mStoryDao.getStoryAll();
        for (int i = 0; i < storyList.size(); i++) {
            if (storyList.get(i).getVisible() == 1 && storyList.get(i).getClear() == 0) {
                story_adapter.addItem(storyList.get(i).getName(), storyList.get(i).getSubMame());
            }
            else if (storyList.get(i).getVisible() == 1 && storyList.get(i).getClear() > 0) {
                story_adapter.addItem(storyList.get(i).getName(), storyList.get(i).getSubMame() + " (완료)");
            }
            else {
                story_adapter.addItem(storyList.get(i).getName(), "?????");
            }

        }

        // 인벤토리 리스트 추가
        List<Item> itemList = MainActivity.mItemDao.getItemAll();

        story_adapter.notifyDataSetChanged();

        // 리스트뷰 클릭 이벤트
        lv_story.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (refresh_check[0] == 0) {

                    int j = i;
                    Item item = new Item();
                    Log log = new Log();
                    User user = new User();
                    Story story = new Story();

                    LayoutInflater layoutInflater = getLayoutInflater();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    int curPoint = Integer.parseInt(MainActivity.tv_point.getText().toString());
                    final int[] result = new int[1];

                    // 리스트뷰 별 이벤트
                    switch (i) {
                        // 튜토리얼
                        case 0:
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_tutorial = layoutInflater.inflate(R.layout.dialog_tutorial, null);

                                builder.setView(view_tutorial);

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0) {
                                            if (itemList.get(27).getQty() > 0) {

                                                result[0] = curPoint + storyList.get(j).getPoint();

                                                item.setId(itemList.get(27).getId());
                                                item.setName(itemList.get(27).getName());
                                                item.setPrice(itemList.get(27).getPrice());
                                                item.setQty(itemList.get(27).getQty());
                                                item.setVisible(itemList.get(27).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                story.setClear(storyList.get(j).getClear() + 1);
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "이미 스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 클리어 후
                            else {

                                final View view_tutorial_clear = layoutInflater.inflate(R.layout.dialog_tutorial_clear, null);

                                builder.setView(view_tutorial_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }
                            break;

                        case 1:     //메인 1
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_main1 = layoutInflater.inflate(R.layout.dialog_main1, null);

                                builder.setView(view_main1);

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0) {
                                            if (itemList.get(12).getQty() > 0) {

                                                result[0] = curPoint + storyList.get(j).getPoint();

                                                item.setId(itemList.get(12).getId());
                                                item.setName(itemList.get(12).getName());
                                                item.setPrice(itemList.get(12).getPrice());
                                                item.setQty(itemList.get(12).getQty() - 1);
                                                item.setVisible(itemList.get(12).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                item.setId(itemList.get(18).getId());
                                                item.setName(itemList.get(18).getName());
                                                item.setPrice(itemList.get(18).getPrice());
                                                item.setQty(itemList.get(18).getQty() + 1);
                                                item.setVisible(itemList.get(18).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                story.setClear(storyList.get(j).getClear() + 1);
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                story.setId(storyList.get(j + 1).getId());
                                                story.setClear(storyList.get(j + 1).getClear());
                                                story.setName(storyList.get(j + 1).getName());
                                                story.setSubMame(storyList.get(j + 1).getSubMame());
                                                story.setVisible(storyList.get(j + 1).getVisible() + 1);
                                                story.setPoint(storyList.get(j + 1).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "이미 스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 클리어 후
                            else {

                                final View view_main1_clear = layoutInflater.inflate(R.layout.dialog_main1_clear, null);

                                builder.setView(view_main1_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 2:     //메인 2
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                if (storyList.get(j).getVisible() == 1) {
                                    final View view_main2 = layoutInflater.inflate(R.layout.dialog_main2, null);

                                    builder.setView(view_main2);

                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // 클리어 하지 않은 퀘스트
                                            if (storyList.get(j).getClear() == 0 && storyList.get(j).getVisible() == 1) {
                                                if (itemList.get(13).getQty() > 0) {

                                                    result[0] = curPoint + storyList.get(j).getPoint();

                                                    item.setId(itemList.get(13).getId());
                                                    item.setName(itemList.get(13).getName());
                                                    item.setPrice(itemList.get(13).getPrice());
                                                    item.setQty(itemList.get(13).getQty() - 1);
                                                    item.setVisible(itemList.get(13).getVisible());
                                                    MainActivity.mItemDao.setUpdateUser(item);

                                                    item.setId(itemList.get(19).getId());
                                                    item.setName(itemList.get(19).getName());
                                                    item.setPrice(itemList.get(19).getPrice());
                                                    item.setQty(itemList.get(19).getQty() + 1);
                                                    item.setVisible(itemList.get(19).getVisible());
                                                    MainActivity.mItemDao.setUpdateUser(item);

                                                    log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                    log.setCur_value(String.valueOf(curPoint));
                                                    log.setResult(result[0]);
                                                    log.setTime(MainActivity.getTime());
                                                    MainActivity.mLogDao.setInsertLog(log);

                                                    MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                    user.setId(1);
                                                    user.setGold(MainActivity.tv_gold.getText().toString());
                                                    user.setPoint(MainActivity.tv_point.getText().toString());
                                                    MainActivity.mUserDao.setUpdateUser(user);

                                                    story.setId(storyList.get(j).getId());
                                                    story.setClear(storyList.get(j).getClear() + 1);
                                                    story.setName(storyList.get(j).getName());
                                                    story.setSubMame(storyList.get(j).getSubMame());
                                                    story.setVisible(storyList.get(j).getVisible());
                                                    story.setPoint(storyList.get(j).getPoint());
                                                    MainActivity.mStoryDao.setUpdateStory(story);

                                                    story.setId(storyList.get(j + 1).getId());
                                                    story.setClear(storyList.get(j + 1).getClear());
                                                    story.setName(storyList.get(j + 1).getName());
                                                    story.setSubMame(storyList.get(j + 1).getSubMame());
                                                    story.setVisible(storyList.get(j + 1).getVisible() + 1);
                                                    story.setPoint(storyList.get(j + 1).getPoint());
                                                    MainActivity.mStoryDao.setUpdateStory(story);

                                                    refresh_check[0] = 1;

                                                    Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "이전 스토리를 클리어해주세요.", Toast.LENGTH_SHORT).show();
                                            }

                                            dialogInterface.dismiss();
                                        }
                                    });

                                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    builder.show();
                                } else {
                                    Toast.makeText(getActivity(), "이전 스토리를 클리어해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // 클리어 후
                            else {

                                final View view_main2_clear = layoutInflater.inflate(R.layout.dialog_main2_clear, null);

                                builder.setView(view_main2_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 3:     //메인 3
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                if (storyList.get(j).getVisible() == 1) {
                                    final View view_main3 = layoutInflater.inflate(R.layout.dialog_main3, null);

                                    builder.setView(view_main3);

                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // 클리어 하지 않은 퀘스트
                                            if (storyList.get(j).getClear() == 0) {
                                                if (itemList.get(14).getQty() > 0) {

                                                    result[0] = curPoint + storyList.get(j).getPoint();

                                                    item.setId(itemList.get(14).getId());
                                                    item.setName(itemList.get(14).getName());
                                                    item.setPrice(itemList.get(14).getPrice());
                                                    item.setQty(itemList.get(14).getQty() - 1);
                                                    item.setVisible(itemList.get(14).getVisible());
                                                    MainActivity.mItemDao.setUpdateUser(item);

                                                    item.setId(itemList.get(20).getId());
                                                    item.setName(itemList.get(20).getName());
                                                    item.setPrice(itemList.get(20).getPrice());
                                                    item.setQty(itemList.get(20).getQty() + 1);
                                                    item.setVisible(itemList.get(20).getVisible());
                                                    MainActivity.mItemDao.setUpdateUser(item);

                                                    log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                    log.setCur_value(String.valueOf(curPoint));
                                                    log.setResult(result[0]);
                                                    log.setTime(MainActivity.getTime());
                                                    MainActivity.mLogDao.setInsertLog(log);

                                                    MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                    user.setId(1);
                                                    user.setGold(MainActivity.tv_gold.getText().toString());
                                                    user.setPoint(MainActivity.tv_point.getText().toString());
                                                    MainActivity.mUserDao.setUpdateUser(user);

                                                    story.setId(storyList.get(j).getId());
                                                    story.setClear(storyList.get(j).getClear() + 1);
                                                    story.setName(storyList.get(j).getName());
                                                    story.setSubMame(storyList.get(j).getSubMame());
                                                    story.setVisible(storyList.get(j).getVisible());
                                                    story.setPoint(storyList.get(j).getPoint());
                                                    MainActivity.mStoryDao.setUpdateStory(story);

                                                    refresh_check[0] = 1;

                                                    Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "이전 스토리를 클리어해주세요.", Toast.LENGTH_SHORT).show();
                                            }

                                            dialogInterface.dismiss();
                                        }
                                    });

                                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });

                                    builder.show();
                                } else {
                                    Toast.makeText(getActivity(), "이전 스토리를 클리어해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            // 클리어 후
                            else {

                                final View view_main3_clear = layoutInflater.inflate(R.layout.dialog_main3_clear, null);

                                builder.setView(view_main3_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 4:     //서브 1
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_sub1 = layoutInflater.inflate(R.layout.dialog_sub1, null);

                                builder.setView(view_sub1);

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0) {
                                            if (itemList.get(6).getQty() > 0) {

                                                result[0] = curPoint + storyList.get(j).getPoint();

                                                item.setId(itemList.get(6).getId());
                                                item.setName(itemList.get(6).getName());
                                                item.setPrice(itemList.get(6).getPrice());
                                                item.setQty(itemList.get(6).getQty() - 1);
                                                item.setVisible(itemList.get(6).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                story.setClear(storyList.get(j).getClear() + 1);
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "이미 스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 클리어 후
                            else {

                                final View view_sub1_clear = layoutInflater.inflate(R.layout.dialog_sub1_clear, null);

                                builder.setView(view_sub1_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 5:     //서브 2
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_sub2 = layoutInflater.inflate(R.layout.dialog_sub2, null);

                                builder.setView(view_sub2);

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0) {
                                            if (itemList.get(7).getQty() > 0) {

                                                result[0] = curPoint + storyList.get(j).getPoint();

                                                item.setId(itemList.get(7).getId());
                                                item.setName(itemList.get(7).getName());
                                                item.setPrice(itemList.get(7).getPrice());
                                                item.setQty(itemList.get(7).getQty() - 1);
                                                item.setVisible(itemList.get(7).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                story.setClear(storyList.get(j).getClear() + 1);
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "이미 스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 클리어 후
                            else {

                                final View view_sub2_clear = layoutInflater.inflate(R.layout.dialog_sub2_clear, null);

                                builder.setView(view_sub2_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 6:     //서브 3
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_sub3 = layoutInflater.inflate(R.layout.dialog_sub3, null);

                                builder.setView(view_sub3);

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0) {
                                            if (itemList.get(8).getQty() > 0) {

                                                result[0] = curPoint + storyList.get(j).getPoint();

                                                item.setId(itemList.get(8).getId());
                                                item.setName(itemList.get(8).getName());
                                                item.setPrice(itemList.get(8).getPrice());
                                                item.setQty(itemList.get(8).getQty() - 1);
                                                item.setVisible(itemList.get(8).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                story.setClear(storyList.get(j).getClear() + 1);
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "이미 스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 클리어 후
                            else {

                                final View view_sub3_clear = layoutInflater.inflate(R.layout.dialog_sub3_clear, null);

                                builder.setView(view_sub3_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 7:     //서브 4
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_sub4 = layoutInflater.inflate(R.layout.dialog_sub4, null);

                                builder.setView(view_sub4);

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0) {
                                            if (itemList.get(9).getQty() > 0) {

                                                result[0] = curPoint + storyList.get(j).getPoint();

                                                item.setId(itemList.get(9).getId());
                                                item.setName(itemList.get(9).getName());
                                                item.setPrice(itemList.get(9).getPrice());
                                                item.setQty(itemList.get(9).getQty() - 1);
                                                item.setVisible(itemList.get(9).getVisible());
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                story.setClear(storyList.get(j).getClear() + 1);
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "이미 스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 클리어 후
                            else {

                                final View view_sub4_clear = layoutInflater.inflate(R.layout.dialog_sub4_clear, null);

                                builder.setView(view_sub4_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 8:     //서브 5
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_sub5 = layoutInflater.inflate(R.layout.dialog_sub5, null);

                                builder.setView(view_sub5);

                                final RadioGroup rg = view_sub5.findViewById(R.id.choice_rg);
                                final int[] checkId = {rg.getCheckedRadioButtonId()};

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        int check = 0;
                                        checkId[0] = rg.getCheckedRadioButtonId();
                                        RadioButton rb = rg.findViewById(checkId[0]);
                                        String nation = "";
                                        try {
                                            nation = rb.getText().toString();
                                        } catch (Exception e) {
                                            check = 1;
                                        }

                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0 && check == 0) {
                                            if (itemList.get(10).getQty() > 0) {

                                                if (nation.equals("조사대장과 같이 던전을 탈출한다.")) {
                                                    result[0] = curPoint + storyList.get(j).getPoint();
                                                } else {
                                                    result[0] = curPoint + storyList.get(j).getPoint() + 200;
                                                }

                                                item.setId(itemList.get(10).getId());
                                                item.setName(itemList.get(10).getName());
                                                item.setPrice(itemList.get(10).getPrice());
                                                item.setQty(itemList.get(10).getQty() - 1);
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                if (nation.equals("조사대장과 같이 던전을 탈출한다.")) {
                                                    story.setClear(storyList.get(j).getClear() + 1);
                                                } else {
                                                    story.setClear(storyList.get(j).getClear() + 2);
                                                }
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "선택지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 1번 선택지 선택 클리어
                            else if (storyList.get(j).getClear() == 1) {

                                final View view_sub5_clear = layoutInflater.inflate(R.layout.dialog_sub5_clear, null);

                                builder.setView(view_sub5_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }
                            // 2번 선택지 선택 클리어
                            else {

                                final View view_sub5_clear_1 = layoutInflater.inflate(R.layout.dialog_sub5_clear_1, null);

                                builder.setView(view_sub5_clear_1);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;

                        case 9:     //서브 6
                            // 클리어 전
                            if (storyList.get(j).getClear() == 0) {
                                final View view_sub6 = layoutInflater.inflate(R.layout.dialog_sub6, null);

                                builder.setView(view_sub6);

                                final RadioGroup rg = view_sub6.findViewById(R.id.choice_rg);
                                final int[] checkId = {rg.getCheckedRadioButtonId()};

                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        int check = 0;
                                        checkId[0] = rg.getCheckedRadioButtonId();
                                        RadioButton rb = rg.findViewById(checkId[0]);
                                        String nation = "";
                                        try {
                                            nation = rb.getText().toString();
                                        } catch (Exception e) {
                                            check = 1;
                                        }

                                        // 클리어 하지 않은 퀘스트
                                        if (storyList.get(j).getClear() == 0 && check == 0) {
                                            if (itemList.get(11).getQty() > 0) {

                                                if (nation.equals("라넌큘러스")) {
                                                    result[0] = curPoint + storyList.get(j).getPoint() + 300;
                                                } else {
                                                    result[0] = curPoint + storyList.get(j).getPoint();
                                                }

                                                item.setId(itemList.get(11).getId());
                                                item.setName(itemList.get(11).getName());
                                                item.setPrice(itemList.get(11).getPrice());
                                                item.setQty(itemList.get(11).getQty() - 1);
                                                MainActivity.mItemDao.setUpdateUser(item);

                                                log.setName(storyList.get(j).getName() + " 클리어 보상");
                                                log.setCur_value(String.valueOf(curPoint));
                                                log.setResult(result[0]);
                                                log.setTime(MainActivity.getTime());
                                                MainActivity.mLogDao.setInsertLog(log);

                                                MainActivity.tv_point.setText(String.valueOf(result[0]));
                                                user.setId(1);
                                                user.setGold(MainActivity.tv_gold.getText().toString());
                                                user.setPoint(MainActivity.tv_point.getText().toString());
                                                MainActivity.mUserDao.setUpdateUser(user);

                                                story.setId(storyList.get(j).getId());
                                                if (nation.equals("라넌큘러스")) {
                                                    story.setClear(storyList.get(j).getClear() + 2);
                                                } else {
                                                    story.setClear(storyList.get(j).getClear() + 1);
                                                }
                                                story.setName(storyList.get(j).getName());
                                                story.setSubMame(storyList.get(j).getSubMame());
                                                story.setVisible(storyList.get(j).getVisible());
                                                story.setPoint(storyList.get(j).getPoint());
                                                MainActivity.mStoryDao.setUpdateStory(story);

                                                refresh_check[0] = 1;

                                                Toast.makeText(getActivity(), "스토리를 클리어하였습니다.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "필요 아이템이 부족합니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "선택지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        }

                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();
                            }
                            // 1,2번 선택지 선택 클리어
                            else if (storyList.get(j).getClear() == 1) {

                                final View view_sub6_clear = layoutInflater.inflate(R.layout.dialog_sub6_clear, null);

                                builder.setView(view_sub6_clear);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }
                            // 3번 선택지 선택 클리어
                            else {

                                final View view_sub6_clear_1 = layoutInflater.inflate(R.layout.dialog_sub6_clear_1, null);

                                builder.setView(view_sub6_clear_1);

                                builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                builder.show();

                            }

                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), "다른 화면으로 갔다와주세요..ㅜㅜ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}
