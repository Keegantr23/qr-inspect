package com.lonelyjones.qrandbarcodescanner;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemHolder> {

    ArrayList<CodeItem> historyItems;
    Context context;
    MyClickInterface myClickInterface;
    ViewGroup parent;

    public HistoryAdapter(ArrayList<CodeItem> historyItems, Context context, MyClickInterface myClickInterface){
        this.historyItems = historyItems;
        this.context = context;
        this.myClickInterface = myClickInterface;
    }

    @NonNull
    @Override
    public HistoryItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item_row, parent, false);
        this.parent = parent;
        return new HistoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemHolder holder, int position) {
        holder.img_qr_recycler.setImageBitmap(DbBitmapUtility.getImage(historyItems.get(position).getImg_code_gen()));
        holder.txt_data_scanned.setText(historyItems.get(position).getDateScanned());
        holder.txt_code_recycler.setText(historyItems.get(position).getCodeScanned());
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public class HistoryItemHolder extends RecyclerView.ViewHolder{

        TextView txt_code_recycler;
        TextView txt_data_scanned;
        ImageView img_qr_recycler;

        public HistoryItemHolder(@NonNull View itemView) {
            super(itemView);

            txt_code_recycler = itemView.findViewById(R.id.txt_code_recycler);
            txt_data_scanned = itemView.findViewById(R.id.txt_date_scanned);
            img_qr_recycler = itemView.findViewById(R.id.img_qr_recycler);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickInterface.OnItemClick(getAdapterPosition());
                }
            });

        }
    }

    interface MyClickInterface{
        void OnItemClick(int positionOfItem);
    }


}
