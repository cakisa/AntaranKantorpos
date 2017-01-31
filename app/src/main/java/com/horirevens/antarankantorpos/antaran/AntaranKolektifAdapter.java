package com.horirevens.antarankantorpos.antaran;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;
import com.horirevens.antarankantorpos.libs.MyDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horirevens on 1/20/17.
 */
public class AntaranKolektifAdapter extends ArrayAdapter<AntaranKolektif> {

    private List<AntaranKolektif> antaranList;
    private ArrayList<AntaranKolektif> arrayList;
    private Context context;

    public AntaranKolektifAdapter(ArrayList<AntaranKolektif> antaranList, Context context) {
        super(context, R.layout.listview_kolektif, antaranList);
        this.antaranList = antaranList;
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(antaranList);
    }

    public int getCount() {
        return antaranList.size();
    }

    public AntaranKolektif getItem(int position) {
        return antaranList.get(position);
    }

    public long getItemId(int position) {
        return antaranList.get(position).hashCode();
    }

    private static class ViewHolder {
        public TextView awktlokal, aketerangan;
        public CheckBox cbAkditem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_kolektif, parent, false);
            holder.awktlokal = (TextView) convertView.findViewById(R.id.awktlokal);
            holder.aketerangan = (TextView) convertView.findViewById(R.id.aketerangan);
            holder.cbAkditem = (CheckBox) convertView.findViewById(R.id.cbAkditem);

            convertView.setTag(holder);

            holder.cbAkditem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    AntaranKolektif antaran = (AntaranKolektif) cb.getTag();
                    antaran.setSelected(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyDate myDate = new MyDate();
        AntaranKolektif antaran = antaranList.get(position);
        holder.cbAkditem.setChecked(antaran.isSelected());
        holder.cbAkditem.setText(antaran.getAkditem());
        holder.cbAkditem.setTag(antaran);
        holder.cbAkditem.setTextSize(24);
        holder.awktlokal.setText(myDate.datetimeIndo(antaran.getAwktlokal()));
        holder.aketerangan.setText("No. DO " + antaran.getAdo());

        return convertView;
    }

    public void filter(String akditem) {
        antaranList.clear();
        if (akditem.length() == 0) {
            antaranList.addAll(arrayList);
        } else {
            for (AntaranKolektif antaranDetail : arrayList) {
                if (akditem.length() != 0 && antaranDetail.getAkditem().contains(akditem)) {
                    antaranList.add(antaranDetail);
                } else if (akditem.length() != 0 && antaranDetail.getAkditem().contains(akditem)) {
                    antaranList.add(antaranDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
