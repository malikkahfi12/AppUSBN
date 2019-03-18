package com.example.asus.qunasta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asus.qunasta.Common.Common;
import com.example.asus.qunasta.Model.Category;
import com.example.asus.qunasta.QuizActivity;
import com.example.asus.qunasta.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    List<Category> categories;


    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_category_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_category_name.setText(categories.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView card_category;
        TextView txt_category_name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            resetCountAnswer();
            correctResultReset();

            card_category = (CardView)itemView.findViewById(R.id.card_category);
            txt_category_name = (TextView)itemView.findViewById(R.id.txt_category_name);
            card_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialStyledDialog.Builder(context)
                            .setTitle("Ingin mengerjakan soal?" )
                            .setDescription("Yakin ingin mengerjakan soal latihan ?")
                            .setIcon(R.drawable.ic_videogame_asset_white_24dp)
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Common.selectedCategory = categories.get(getAdapterPosition());
                                    Intent intent = new Intent(context,QuizActivity.class);
                                    context.startActivity(intent);
                                }
                            })
                            .setNegativeText("CANCEL")
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }
    }

    private void correctResultReset() {
        Common.right_answer_count = 0;
    }

    private void resetCountAnswer() {
        Common.total_answer_count = 0;
    }


}
