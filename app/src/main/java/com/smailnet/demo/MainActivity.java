package com.smailnet.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
// import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.smailnet.demo.com.smailnet.demo.model.Contacts;
import com.smailnet.eamil.Callback.GetReceiveCallback;
import com.smailnet.eamil.EmailReceiveClient;
import com.smailnet.eamil.EmailSendClient;
import com.smailnet.eamil.Callback.GetSendCallback;
import com.smailnet.eamil.EmailMessage;
import com.smailnet.islands.Interface.OnRunningListener;
import com.smailnet.islands.Islands;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener  {

    private EditText address_editText;
    private EditText title_editText;
    private EditText text_editText;
    private String url;// 附件地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("sh.c")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView(){
        address_editText = findViewById(R.id.address_editText);
        title_editText = findViewById(R.id.title_editText);
        text_editText = findViewById(R.id.text_editText);
        Button button = findViewById(R.id.send);
//        Button button1 = findViewById(R.id.receive);
//        Button button2 = findViewById(R.id.receive2);

        button.setOnClickListener(this);
        address_editText.setOnFocusChangeListener(this);

//        button1.setOnClickListener(this);
//        button2.setOnClickListener(this);
    }


    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.mail_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_1:
                startActivity(new Intent(this,ConstantsActivity.class));

                break;
            case R.id.choose_file:
                new LFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(1000)
                        .withMutilyMode(false)
                        .start();
                default:
                    break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 附件选择完
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                url = list.get(0);
            }
        }
    }

    /**
     * 发送邮件
     */
    private void sendMessage(){
        EmailSendClient emailSendClient = new EmailSendClient(EmailApp.emailConfig());
        emailSendClient
                .setTo(address_editText.getText().toString())                //收件人的邮箱地址
                // .setNickname("我的小可爱")                                    //发件人昵称
                .setSubject(title_editText.getText().toString())             //邮件标题
                .setText(text_editText.getText().toString())//邮件文本
                .setContent(url)
                .sendAsyn(this, new GetSendCallback() {
                    @Override
                    public void sendSuccess() {
                        Toast.makeText(MainActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void sendFailure(String errorMsg) {
                        Log.e("send mail", "错误日志：" + errorMsg);

                        Islands.ordinaryDialog(MainActivity.this)
                                .setText(null, "发送失败 ：" + errorMsg)
                                .setButton("关闭", null, null)
                                .click().show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                sendMessage();
                break;
//            case R.id.receive:
//                /**
//                 * 获取邮件
//                 */
//                Islands.circularProgress(this)
//                        .setCancelable(false)
//                        .setMessage("同步中...")
//                        .show()
//                        .run(new OnRunningListener() {
//                            @Override
//                            public void onRunning(final ProgressDialog progressDialog) {
//                                EmailReceiveClient emailReceiveClient = new EmailReceiveClient(EmailApp.emailConfig());
//                                emailReceiveClient
//                                        .popReceiveAsyn(MainActivity.this, new GetReceiveCallback() {
//                                            @Override
//                                            public void gainSuccess(List<EmailMessage> messageList, int count) {
//                                                Log.i("oversee", "邮件总数：" + count );
//
//                                                progressDialog.dismiss();
//                                                if (count > 0) {
//                                                    Log.i("oversee", "邮件总数：" + count + " 标题：" +  messageList.get(0).getSubject() + "content" + messageList.get(0).getContent());
//                                                } else {
//                                                    Toast.makeText(MainActivity.this, "没有邮件", Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void gainFailure(String errorMsg) {
//                                                progressDialog.dismiss();
//                                                Log.e("oversee", "错误日志：" + errorMsg);
//                                            }
//                                        });
//                            }
//                        });
//                break;
//            case R.id.receive2:
//                Islands.circularProgress(this)
//                        .setCancelable(false)
//                        .setMessage("同步中...")
//                        .show()
//                        .run(new OnRunningListener() {
//                            @Override
//                            public void onRunning(final ProgressDialog progressDialog) {
//                                EmailReceiveClient emailReceiveClient = new EmailReceiveClient(EmailApp.emailConfig());
//                                emailReceiveClient
//                                        .imapReceiveAsyn(MainActivity.this, new GetReceiveCallback() {
//                                            @Override
//                                            public void gainSuccess(List<EmailMessage> messageList, int count) {
//                                                progressDialog.dismiss();
//                                                Log.i("oversee", "邮件总数：" + count + " 标题：" +  messageList.get(0).getSubject());
//                                            }
//
//                                            @Override
//                                            public void gainFailure(String errorMsg) {
//                                                progressDialog.dismiss();
//                                                Log.e("oversee", "错误日志：" + errorMsg);
//                                            }
//                                        });
//                            }
//                        });
//                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            showListPopulWindow(); //调用显示PopuWindow 函数
        }
    }

    private void showListPopulWindow(){
        Realm mRealm = Realm.getDefaultInstance();

        RealmResults<Contacts> c = mRealm.where(Contacts.class).findAll();
        final List<Contacts> contactsList = mRealm.copyFromRealm(c);
        List<String> nickList = new ArrayList<>();
        for (Contacts cs : contactsList) {
            nickList.add(cs.getNickName());
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, nickList));
        listPopupWindow.setAnchorView(address_editText);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                address_editText.setText(contactsList.get(i).getEmail());
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();

    }

}
