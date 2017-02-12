package com.horirevens.antarankantorpos.ado;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.horirevens.antarankantorpos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horirevens on 2/12/17.
 */

public class LapDOAdapter extends RecyclerView.Adapter<LapDOAdapter.MyViewHolder> {
    private List<LapDO> lapDOList;
    private ArrayList<LapDO> arrayList;
    private Context context;

    public LapDOAdapter(List<LapDO> lapDOList, Context context) {
        this.lapDOList = lapDOList;
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(lapDOList);
    }
    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAdo, tvAdoKeterangan;
        public ImageView ivAdoStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAdo = (TextView) itemView.findViewById(R.id.ado);
            tvAdoKeterangan = (TextView) itemView.findViewById(R.id.adoKeterangan);
            ivAdoStatus = (ImageView) itemView.findViewById(R.id.adoStatus);
        }
    }
    
    @Override
    public LapDOAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_lap_do, parent, false);
        return new LapDOAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LapDOAdapter.MyViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return lapDOList.size();
    }
}
