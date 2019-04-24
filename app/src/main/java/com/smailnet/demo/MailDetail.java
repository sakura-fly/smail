package com.smailnet.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smailnet.eamil.EmailMessage;
import com.zzhoujay.richtext.RichText;

public class MailDetail extends AppCompatActivity {

    private TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_detail);

        mail = findViewById(R.id.mail);
        EmailMessage em = new Gson().fromJson(getIntent().getStringExtra("mail"),EmailMessage.class);
        Log.i("mailcontent",em.getContent());
        RichText.from(em.getContent()).into(mail);

    }
}
