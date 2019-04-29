package com.smailnet.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smailnet.demo.com.smailnet.demo.model.Contacts;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CAddActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText nickName;
    private EditText addr;
    private Button addc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadd);
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("sh.c")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        nickName = findViewById(R.id.nick_name);
        addc = findViewById(R.id.addc);
        addr = findViewById(R.id.addr);

        addc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        try {
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            Contacts c = new Contacts(nickName.getText().toString(),addr.getText().toString());
            realm.copyToRealm(c);
            realm.commitTransaction();
            Toast.makeText(CAddActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CAddActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

        }

    }
}
