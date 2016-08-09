package sample.hawk.com.mybasicappcomponents;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ha271 on 2016/8/8.
 */

public class MyListViewActivity extends ListActivity {

    private ListView listV;
    List<MyListRowDataItem> rowItems = new ArrayList<MyListRowDataItem>();
    MyListDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create DB
        rowItems.add(new MyListRowDataItem(0,"HBO電影台","")); // Channel 1
        rowItems.add(new MyListRowDataItem(1,"綠光戰警","7:00"));
        rowItems.add(new MyListRowDataItem(2,"鋼鐵人","9:00"));
        rowItems.add(new MyListRowDataItem(1,"蝙蝠俠:開戰時刻","11:00"));

        rowItems.add(new MyListRowDataItem(0,"Discovery","")); // Channel 2
        rowItems.add(new MyListRowDataItem(1,"動物生死鬥","7:30"));
        rowItems.add(new MyListRowDataItem(2,"超級獵人","9:00"));
        rowItems.add(new MyListRowDataItem(1,"阿貓阿狗","12:00"));
        rowItems.add(new MyListRowDataItem(1,"Lion King","13:30"));
        rowItems.add(new MyListRowDataItem(0,"衛視電影台","")); // Channel 3
        rowItems.add(new MyListRowDataItem(1,"海角七號","7:00"));
        rowItems.add(new MyListRowDataItem(2,"陣頭","9:00"));
        rowItems.add(new MyListRowDataItem(1,"星空","11:00"));
        rowItems.add(new MyListRowDataItem(1,"我的少女時代","14:00"));
        rowItems.add(new MyListRowDataItem(1,"英雄","18:00"));
        rowItems.add(new MyListRowDataItem(1,"十面埋伏","21:00"));
        rowItems.add(new MyListRowDataItem(0,"東森新聞台","")); // Channel 4
        rowItems.add(new MyListRowDataItem(1,"午間新聞","11:30"));
        rowItems.add(new MyListRowDataItem(2,"晚間新聞","9:00"));
        rowItems.add(new MyListRowDataItem(1,"晨間新聞","6:00"));
        rowItems.add(new MyListRowDataItem(0,"ESPN","")); // Channel 5
        rowItems.add(new MyListRowDataItem(1,"NBA stuff","8:30"));
        rowItems.add(new MyListRowDataItem(2,"NBA Final Game7","9:00"));
        rowItems.add(new MyListRowDataItem(1,"F1 Race","11:00"));
        rowItems.add(new MyListRowDataItem(1,"Golf World","13:30"));

        // Get DB Adapter
        adapter = new MyListDataAdapter(this, rowItems);
        // Link DB with View
        setContentView(R.layout.mylist_activity);
        listV=(ListView)findViewById(R.id.myactivitylist);
        listV.setAdapter(adapter);
    }


    // ListView有個很方便的功能叫做setTextFilterEnabled
    // 但是自從我們自訂ListView的內容以後, 這個方法不能直接套用 所以要自己override一些東西
    public class MyListDataAdapter extends BaseAdapter {
        private LayoutInflater myInflater;
        private List<MyListRowDataItem> items;

        public MyListDataAdapter(Context context, List<MyListRowDataItem> items){
            myInflater = LayoutInflater.from(context);
            this.items = items;
        }
        @Override
        public int getCount() { return items.size(); }
        @Override
        public Object getItem(int arg0) { return items.get(arg0); }
        @Override
        public long getItemId(int position) { return items.indexOf(getItem(position)); }


        private class ViewHolder {
            TextView txtTitle;
            TextView txtTime;
            public ViewHolder(TextView txtTitle, TextView txtTime){
                this.txtTitle = txtTitle;
                this.txtTime = txtTime;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView==null){
                convertView = myInflater.inflate(R.layout.mylist_rowitem, null);
                holder = new ViewHolder(
                        (TextView) convertView.findViewById(R.id.title),
                        (TextView) convertView.findViewById(R.id.time)
                );
                // About Tag: http://www.cnblogs.com/qingblog/archive/2012/07/03/2575145.html
                convertView.setTag(holder); // setTag
            }else{
                holder = (ViewHolder) convertView.getTag(); // getTag
            }

            // 如果你的每個item都長一樣就不需要那麼大費周章了+++++++++++++++
            // 接著先取得現在要做的的資料
            MyListRowDataItem movie = (MyListRowDataItem)getItem(position);
            // type_num={      [0]:channel, [1]:movie,   [2]:time }
            int type_num = movie.getType();
            int color_title[] ={Color.WHITE,Color.WHITE, Color.YELLOW};
            int color_time[] = {Color.WHITE,Color.WHITE, Color.YELLOW};
            int color_back[] = {Color.BLUE, Color.BLACK, Color.BLACK};
            int time_vis[] =   {View.GONE,  View.VISIBLE,View.VISIBLE};

            String strTitle = movie.getName();
            holder.txtTitle.setText(strTitle);
            holder.txtTitle.setTextColor(color_title[type_num]);
            holder.txtTitle.setBackgroundColor(color_back[type_num]);
            String strTime = movie.getTime();
            holder.txtTime.setText(strTime);
            holder.txtTime.setTextColor(color_time[type_num]);
            holder.txtTime.setBackgroundColor(color_back[type_num]);
            holder.txtTime.setVisibility(time_vis[type_num]);

            return convertView;
        }

    }

}