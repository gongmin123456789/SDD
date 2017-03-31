package com.example.sddclienttest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.sdd.common.SDDDevice;

import java.util.List;

/**
 * Created by 80066158 on 2017-03-31.
 */

public class DeviceListViewAdapter extends BaseAdapter {
    private List<SDDDevice> deviceList = null;
    private LayoutInflater inflater = null;

    public DeviceListViewAdapter(Context context, List<SDDDevice> deviceList) {
        this.deviceList = deviceList;
        inflater = LayoutInflater.from(context);
    }

    public void setDeviceList(List<SDDDevice> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == deviceList) {
            return 0;
        }
        return deviceList.size();
    }

    @Override
    public Object getItem(int i) {
        if (null == deviceList) {
            return null;
        }

        return deviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (null == view) {
            view = inflater.inflate(R.layout.device_list_view_item_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.subtitle = (TextView) view.findViewById(R.id.subtitle);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(deviceList.get(i).getName());
        viewHolder.subtitle.setText(deviceList.get(i).getIp());

        return view;
    }

    private static class ViewHolder {
        public ImageView icon = null;
        public TextView title = null;
        public TextView subtitle = null;
    }
}
