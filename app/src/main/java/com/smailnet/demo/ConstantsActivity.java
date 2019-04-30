package com.smailnet.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smailnet.demo.com.smailnet.demo.model.Contacts;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class ConstantsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout s;

    private ListView clist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("sh.c")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constants);

        FloatingActionButton fab = findViewById(R.id.addc);
        s = findViewById(R.id.refresh);
        clist = findViewById(R.id.c_list);
        s.setOnRefreshListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConstantsActivity.this, CAddActivity.class));
            }
        });

        cList();
    }

    public void cList() {
        Realm mRealm = Realm.getDefaultInstance();

        RealmResults<Contacts> c = mRealm.where(Contacts.class).findAll();
        List<Contacts> l = mRealm.copyFromRealm(c);
        Log.i("clist", l.toString());
        List<String> nickList = new ArrayList<>();
        for (Contacts cs : l) {
            nickList.add(cs.getNickName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ConstantsActivity.this, android.R.layout.simple_list_item_1, nickList);
        clist.setAdapter(adapter);


    }

    @Override
    public void onRefresh() {
        cList();
        s.setRefreshing(false);
        // new Thread(new Runnable() {
        //     @Override
        //     public void run() {
        //         cList();
        //         mHandler.sendEmptyMessage(1);
        //     }
        // }).start();
    }

    // //handler
    // @SuppressLint("HandlerLeak")
    // private Handler mHandler = new Handler(){
    //     @Override
    //     public void handleMessage(Message msg) {
    //         super.handleMessage(msg);
    //         switch (msg.what) {
    //             case 1:
    //
    //
    //                 s.setEnabled(false);
    //                 break;
    //             default:
    //                 break;
    //         }
    //     }
    // };
}
