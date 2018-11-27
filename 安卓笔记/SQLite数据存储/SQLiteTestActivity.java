package com.puquan.health.bodyhealth.main.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.puquan.health.bodyhealth.R;
import com.puquan.health.bodyhealth.common.util.MySQLiteUtil;
import com.puquan.health.bodyhealth.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SQLiteTestActivity extends AppCompatActivity {

    private MySQLiteUtil mySQLiteUtil;
    private SQLiteDatabase db;

    Button btAdd,btSelect;//向数据库添加和查询按钮
    EditText etSubject,etBody,etDate;//用户信息编辑
    TextView tvResult;//显示查询的结果


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_test);
        init();
        setListener();
    }

    public void init(){
        btAdd= (Button) findViewById(R.id.bt_add);
        btSelect= (Button) findViewById(R.id.bt_select);
        etSubject= (EditText) findViewById(R.id.et_subject);
        etBody= (EditText) findViewById(R.id.et_body);
        etDate= (EditText) findViewById(R.id.et_date);
        tvResult= (TextView) findViewById(R.id.tv_result);

        mySQLiteUtil=new MySQLiteUtil(SQLiteTestActivity.this,"memento.db",null,1);
        db=mySQLiteUtil.getReadableDatabase();
    }


    public void setListener(){
        //插入数据
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSubject.getText().toString().equals("")){
                    ToastUtils.show("请输入subject内容！");
                    return;
                }

                if (etBody.getText().toString().equals("")){
                    ToastUtils.show("请输入body内容！");
                    return;
                }

                if (etDate.getText().toString().equals("")){
                    ToastUtils.show("请输入date内容！");
                    return;
                }

                add(db,etSubject.getText().toString(),etBody.getText().toString(),etDate.getText().toString());

            }
        });

        //查询按钮
        btSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Map<String,Object>> results=query(db,etSubject.getText().toString(),etBody.getText().toString(),etDate.getText().toString());
                tvResult.setText(results+"");
            }
        });
    }

    //向数据库插入数据
    public void add(SQLiteDatabase db,String subject,String body,String date){
        db.execSQL("insert into memento_tb values(null,?,?,?)",new String[]{subject,body,date});
        ToastUtils.show("添加记录成功！");
        etSubject.setText("");
        etBody.setText("");
        etDate.setText("");
    }

    //查询数据库
    public ArrayList<Map<String,Object>> query(SQLiteDatabase db,String sub,String body,String date){
        ArrayList<Map<String,Object>> records=new ArrayList<>();
        Cursor cursor= db.rawQuery("select * from memento_tb where subject like ? and body like ? and date like ?",
                new String[]{"%"+sub+"%","%"+body+"%","%"+date+"%",});
        while (cursor.moveToNext()){
            Map<String ,Object> item=new HashMap<>();
            item.put("id",cursor.getInt(0));
            item.put("subject",cursor.getString(1));
            item.put("body",cursor.getString(2));
            item.put("date",cursor.getString(3));
            records.add(item);
        }
        return records;
    }


}
