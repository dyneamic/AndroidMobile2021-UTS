package id.ac.umn.utslab_musicplayer_stephentjoang_28280;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class ListLaguAdapter extends RecyclerView.Adapter<ListLaguAdapter.LaguViewHolder> implements Serializable {
    private List laguDataSet;
    private LayoutInflater mInflater;
    Context mContext;

    public ListLaguAdapter(Context context, List laguModelList) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        laguDataSet = laguModelList;
    }

    public static class LaguViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context nContext;
        List laguList;
        public TextView mTextView;

        public LaguViewHolder(Context context, List laguModelList, View v) {
            super(v);
            nContext = context;
            laguList = laguModelList;
            mTextView = v.findViewById(R.id.laguName);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();

            Intent intent = new Intent(nContext, MusicPlayerActivity.class);
            intent.putExtra("audio", (Serializable) laguList.get(itemPosition));
            intent.putExtra("fullList", (Serializable) laguList);
            intent.putExtra("position", (Serializable) itemPosition);
            nContext.startActivity(intent);
        }
    }


    @NonNull
    @Override
    public LaguViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = mInflater.inflate(R.layout.audio_list_row, viewGroup, false);

        LaguViewHolder vh = new LaguViewHolder(mContext, laguDataSet,v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LaguViewHolder audioViewHolder, int i) {
        ModelLagu temp_name = (ModelLagu) laguDataSet.get(i);
        audioViewHolder.mTextView.setText(temp_name.getLaguName());
    }

    @Override
    public int getItemCount() {
        return laguDataSet.size();
    }

}
