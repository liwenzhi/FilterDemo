package com.example.FilterDemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter的使用
 */
public class MyActivity extends Activity implements AdapterView.OnItemClickListener {

    EditText et_input;
    ListView lv_show;
    ListAdapter listAdapter;
    List<Person> personList = new ArrayList<Person>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initData();
        initEvent();

    }

    private void initView() {
        et_input = (EditText) findViewById(R.id.et_input);
        lv_show = (ListView) findViewById(R.id.lv_show);
    }

    private void initData() {
        listAdapter = new ListAdapter(this, personList);
        lv_show.setAdapter(listAdapter);

        personList.add(new Person("李小明", "江西南昌"));
        personList.add(new Person("李小明1", "江西南昌"));
        personList.add(new Person("李小明2", "江西南昌"));
        personList.add(new Person("李小明3", "江西南昌"));
        personList.add(new Person("牛福航1", "山东菏泽"));
        personList.add(new Person("牛福航2", "山东菏泽"));
        personList.add(new Person("牛福航3", "山东菏泽"));
        personList.add(new Person("牛福航4", "山东菏泽"));
        personList.add(new Person("李文", "广西岑溪"));


    }

    private void initEvent() {
        et_input.addTextChangedListener(filterTextWatcher);    //让输入框监听
        lv_show.setOnItemClickListener(this);
    }

    /**
     * Editext监听对象
     */
    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            listAdapter.getFilter().filter(s); //这里传入数据就可以了
        }

    };

    /**
     * 点击了ListView 的条目的回调监听
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String msg = et_input.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(this, "选中了：" + personList.get(i), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "选中了：" + listAdapter.getNewList().get(i), Toast.LENGTH_SHORT).show();
        }


    }
}
