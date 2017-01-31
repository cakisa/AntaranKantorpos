package com.horirevens.antarankantorpos.ado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horirevens on 1/23/17.
 */
public class LapDOAdapter extends ArrayAdapter<LapDO> {
    private List<LapDO> lapDOList;
    private ArrayList<LapDO> arrayList;
    private Context context;

    public LapDOAdapter(List<LapDO> lapDOList, Context context) {
        super(context, R.layout.listview_lap_do, lapDOList);
        this.lapDOList = lapDOList;
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(lapDOList);
    }

    public int getCount() {
        return lapDOList.size();
    }

    public LapDO getItem(int position) {
        return lapDOList.get(position);
    }

    public long getItemId(int position) {
        return lapDOList.get(position).hashCode();
    }

    private static class ViewHolder {
        public TextView tvAdo, tvAdoKeterangan;
        public ImageView ivAdoStatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_lap_do, parent, false);
            holder.tvAdo = (TextView) convertView.findViewById(R.id.ado);
            holder.tvAdoKeterangan = (TextView) convertView.findViewById(R.id.adoKeterangan);
            holder.ivAdoStatus = (ImageView) convertView.findViewById(R.id.adoStatus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LapDO lapDO = lapDOList.get(position);
        holder.tvAdo.setText(lapDO.getAdo());
        holder.tvAdoKeterangan.setText(
                "Sisa: " + lapDO.getProses() + " | " +
                        "Berhasil: " + lapDO.getBerhasil() + " | " +
                        "Gagal: " + lapDO.getGagal() + " | " +
                        "Jml Item: " + lapDO.getJml_item()
        );
        if(lapDO.getProses().equals("0")) {
            holder.ivAdoStatus.setImageResource(R.drawable.ba);
        } else {
            holder.ivAdoStatus.setImageResource(R.drawable.ga);
        }



        return convertView;
    }
}
