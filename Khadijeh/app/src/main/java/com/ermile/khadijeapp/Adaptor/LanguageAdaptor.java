package com.ermile.khadijeapp.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ermile.khadijeapp.Activity.MainActivity;
import com.ermile.khadijeapp.Activity.Splash;
import com.ermile.khadijeapp.Item.item_Language;
import com.ermile.khadijeapp.R;
import com.ermile.khadijeapp.Static.file;
import com.ermile.khadijeapp.Static.format;
import com.ermile.khadijeapp.utility.FileManager;
import com.ermile.khadijeapp.utility.SaveManager;
import com.ermile.khadijeapp.utility.set_language_device;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LanguageAdaptor extends RecyclerView.Adapter<LanguageAdaptor.MyViewHolder> {

    private List<item_Language> itemList;
    private Context mContext;

    public LanguageAdaptor(List<item_Language> itemList, Context mContext) {
        this.itemList = itemList;
        this.mContext = mContext;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View aView = LayoutInflater.from(mContext).inflate(R.layout.item_language, parent, false);
        return new MyViewHolder(aView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final item_Language aItem = itemList.get(position);

        holder.tik.setVisibility(View.GONE);
        holder.titel.setText(aItem.getTitle());
        holder.titel.setTag(aItem.getTag());

        if (aItem.isChBoxVisibel()){
            holder.tik.setVisibility(View.VISIBLE);
        }
/*
        holder.checkLanguage.setVisibility(aItem.getChBoxVisibel());
*/

        holder.linrLnaguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String choseLanguage = holder.titel.getTag().toString();
                String appLanguage = SaveManager.get(mContext).getstring_appINFO().get(SaveManager.appLanguage);
                if (!choseLanguage.equals(appLanguage)){
                    SaveManager.get(mContext).change_appLanguage(choseLanguage);
                    SaveManager.get(mContext).change_LanguageByUser(false);
                    SaveManager.get(mContext).change_apiV6_URL(aItem.getLocal_URL());
                    FileManager.write_OutStorage(mContext, file.setting, format.json,"");
                    new set_language_device(mContext);
                    final Intent refresh1 = new Intent(mContext, Splash.class);
                    refresh1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ((Activity)mContext).finish();
                    mContext.startActivity(refresh1);
                }
                else {
                    Toast.makeText(mContext, mContext.getString(R.string.language_is_selected), Toast.LENGTH_SHORT).show();
                    final Intent refresh2 = new Intent(mContext, MainActivity.class);
                    refresh2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ((Activity)mContext).finish();
                    mContext.startActivity(refresh2);
                }
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titel;
        ImageView tik;
/*
        ImageView checkLanguage;
*/
        CardView linrLnaguage;


        public MyViewHolder(View itemView) {
            super(itemView);
            linrLnaguage = itemView.findViewById(R.id.linrLnaguage);
            titel = itemView.findViewById(R.id.titleLanguage);
            tik = itemView.findViewById(R.id.language_tik);
/*
            checkLanguage = itemView.findViewById(R.id.checkLanguage);
*/
        }
    }

}