package com.smailnet.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smailnet.eamil.Callback.GetReceiveCallback;
import com.smailnet.eamil.EmailMessage;
import com.smailnet.eamil.EmailReceiveClient;
import com.smailnet.islands.Interface.OnRunningListener;
import com.smailnet.islands.Islands;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MailListActivity extends AppCompatActivity {

    private ListView mailList;
    private List<EmailMessage> ml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mailList = findViewById(R.id.mail_list);
        mailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickMail(position);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MailListActivity.this, "233", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MailListActivity.this,MainActivity.class));
            }
        });

        loadMail();

    }

    public void clickMail(int position){
        Intent intent = new Intent(MailListActivity.this, MailDetail.class);
        intent.putExtra("mail", new Gson().toJson(ml.get(position)));
        startActivity(intent);
    }

    /**
     * 加载邮件
     */
    public void loadMail() {
        Islands.circularProgress(this)
                .setCancelable(false)
                .setMessage("同步中...")
                .show()
                .run(new OnRunningListener() {
                    @Override
                    public void onRunning(final ProgressDialog progressDialog) {
                        EmailReceiveClient emailReceiveClient = new EmailReceiveClient(EmailApp.emailConfig());
                        emailReceiveClient
                                .popReceiveAsyn(MailListActivity.this, new GetReceiveCallback() {
                                    @Override
                                    public void gainSuccess(List<EmailMessage> messageList, int count) {
                                        Log.i("oversee", "邮件总数：" + count );

                                        progressDialog.dismiss();
                                        if (count > 0) {
                                            ml = messageList;
                                            List<String> mailTitles = new ArrayList<>();
                                            for (int i = 0; i < messageList.size(); i++) {
                                                mailTitles.add(messageList.get(i).getSubject());
                                            }
                                            Log.i("oversee", "邮件标题：" + mailTitles );

                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                                    MailListActivity.this, android.R.layout.simple_list_item_1, mailTitles);
                                            mailList.setAdapter(adapter);
                                            Log.i("oversee", "邮件总数：" + count + " 标题：" +  messageList.get(0).getSubject() + "content" + messageList.get(0).getContent());
                                        } else {
                                            Toast.makeText(MailListActivity.this, "没有邮件", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void gainFailure(String errorMsg) {
                                        progressDialog.dismiss();
                                        Toast.makeText(MailListActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                        Log.e("oversee", "错误日志：" + errorMsg);
                                    }
                                });
                    }
                });
    }


}
