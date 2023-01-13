package com.example.seminar04.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminar04.MainActivity;
import com.example.seminar04.R;
import com.example.seminar04.model.Address;
import com.example.seminar04.model.Offices;
import com.example.seminar04.model.Officials;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> {
    private LayoutInflater inflater;
    private List<Offices> listOffices;
    private List<Officials> listOfficials;
    private MainActivity context;
    officialClickListener officialClickListener;

    public MainActivityAdapter(MainActivity context, List<Offices> listOffices, List<Officials> listOfficials, officialClickListener officialClickListener) {
        this.context = context;
        this.listOffices = listOffices;
        this.listOfficials = listOfficials;
        this.officialClickListener = officialClickListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MainActivityAdapter.MainActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.government_officials_list, parent, false);
        return new MainActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityViewHolder holder, int position) {
        Officials officials = listOfficials.get(position);
        holder.officialName.setText(officials.getName());

        for (int i = 0; i < listOffices.size(); i++) {
            for (int j = 0; j < listOffices.get(i).getOfficialIndices().size(); j++) {
                if (listOffices.get(i).getOfficialIndices().get(j) == position) {
                    Offices offices = listOffices.get(i);
                   holder.governmentOfficials.setText(offices.getName());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int countRecyclerView = 0;
        for (int i = 0; i < listOffices.size(); i++) {
            countRecyclerView += listOffices.get(i).getOfficialIndices().size();

        }
        return countRecyclerView;
    }

    public interface officialClickListener {
        void onClick(View v, int position);
    }

    public class MainActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.government_officials)
        TextView governmentOfficials;
        @BindView(R.id.officialName)
        TextView officialName;

        public MainActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            officialClickListener.onClick(view, getAdapterPosition());
        }
    }
}
