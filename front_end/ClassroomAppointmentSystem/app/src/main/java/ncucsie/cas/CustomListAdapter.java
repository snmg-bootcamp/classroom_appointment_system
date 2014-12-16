package ncucsie.cas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private ArrayList listData;

    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.simple_list_body, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.simple_list_title);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.simple_list_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyAppointmentClass item = (MyAppointmentClass) listData.get(position);
        holder.titleView.setText(item.getNum() + " " + item.getDate());
        holder.descriptionView.setText(item.getClassroom() + " " + item.getTime() + " " + item.getName() + " " + item.getTeacher() + " " + item.getPhone());
        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        TextView descriptionView;
    }

}
