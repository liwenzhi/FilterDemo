#EditText结合Fileter过滤器效果的设计
之前看到一个SerchView的搜索框，发现里面使用到了Filter，可以很方便的实现输入框中数据的过滤和判断，这里介绍下给大家。
过滤效果：

![1](http://i.imgur.com/i3O4VPk.gif)

程序实现的功能是：用户输入用户名或家乡里面包含的任何字符，都会产生符合条件的ListView列表。

程序的实现：


#1.主页面的布局

一个EditText和ListView
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent"
        >


    <EditText
            android:id="@+id/et_input"
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:singleLine="true"
            android:drawableLeft="@drawable/search"
            android:hint="请输入你要搜索的姓名"
            />
    <ListView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/lv_show"
            />
</LinearLayout>


```

#2.ListView的条目布局也是比较简单的，两个TextView

#3.适配器的类结合Filter使用，需要重点理解的地方
```
package com.example.FilterDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 在适配器中使用过滤效果
 */
public class ListAdapter extends BaseAdapter implements Filterable {
    private List<Person> list;
    private List<Person> newList;//刷选后得到的ListView
    private Context context;

    private PersonFilter filter;

    public ListAdapter(Context context, List<Person> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list, null);
        }
        Person p = list.get(position);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_home = (TextView) convertView.findViewById(R.id.tv_home);
        tv_name.setText(p.name);
        tv_home.setText(p.home);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PersonFilter(list);
        }
        return filter;
    }

    /**
     * 重要的过滤器的类
     */
    public class PersonFilter extends Filter {

        private List<Person> original;

        public PersonFilter(List<Person> list) {
            this.original = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {    //如果是没输入的情况下，可显示全部，也可以不显示任何数据
//                results.values = original;
//                results.count = original.size();
                results.values = new ArrayList<Person>();
                results.count = 0;
            } else {
                List<Person> mList = new ArrayList<Person>();
                for (Person p : original) {
                    //判断符合情况的数据，可以选择多个条件哦
                    //1.第一个字符相同
                    //2.输入的字符被包含在数据里面的
                    if (p.name.toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || p.home.toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || p.name.contains(constraint.toString())
                            || p.home.contains(constraint.toString())
                            ) {
                        mList.add(p);
                    }
                }
                results.values = mList;
                newList = mList;//
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<Person>) results.values;
            notifyDataSetChanged();
        }

    }

    /**
     * 暴露出新的刷选出来的ListView
     */
    public List<Person> getNewList() {
        return newList;
    }


}


```


4.主方法的类的设计
```
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
            listAdapter.getFilter().filter(s); //这里传入数据給过滤器处理
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

```
其实程序中过滤器Filter的具体实现是在适配器Adapter中的，然后在外面通过Filter.filter(s);传入需要过滤的数据，从而得出刷选后所求的数据。

网上也有很多封装得比较多的searchView，也是使用Filter来实现数据过滤的。


![2](http://i.imgur.com/YpglaoI.jpg)


共勉：念念不忘必有回响。

