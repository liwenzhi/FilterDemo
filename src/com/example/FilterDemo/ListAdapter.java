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
    private class PersonFilter extends Filter {

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
            notifyDataSetChanged();// 刷新适配器
        }

    }

    /**
     * 暴露出新的刷选出来的ListView
     */
    public List<Person> getNewList() {
        return newList;
    }


}
