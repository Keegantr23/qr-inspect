package com.lonelyjones.qrandbarcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import java.net.Inet4Address;
import java.util.ArrayList;

public class HistoryCodes extends AppCompatActivity implements HistoryAdapter.MyClickInterface {

    ArrayList<CodeItem> codeItemsList;
    HistoryAdapter historyAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_codes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HistoryCodes.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        ItemTouchHelper.SimpleCallback itemTouchCallback =new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new CodeItemHandler(HistoryCodes.this).delete(codeItemsList.get(viewHolder.getAdapterPosition()).getId());
                codeItemsList.remove(viewHolder.getAdapterPosition());
                historyAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadCodeItems();

    }

    public ArrayList<CodeItem> readItems(){
        ArrayList<CodeItem> itemsList = new CodeItemHandler(this).readAllItems();
        return itemsList;
    }

    public void loadCodeItems(){
        codeItemsList = readItems();
        historyAdapter = new HistoryAdapter(codeItemsList, this, this);
        recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public void OnItemClick(int positionOfItem) {
        Intent i = new Intent(HistoryCodes.this, DisplayCodeActivity.class);
        i.putExtra("codeResult", codeItemsList.get(positionOfItem).getCodeScanned());
        i.putExtra("backToMain", false);
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}