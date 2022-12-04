/*
 *
 *  *  /**
 *  *  * Copyright (C) 2017  Grbl Controller Contributors
 *  *  *
 *  *  * This program is free software; you can redistribute it and/or modify
 *  *  * it under the terms of the GNU General Public License as published by
 *  *  * the Free Software Foundation; either version 2 of the License, or
 *  *  * (at your option) any later version.
 *  *  *
 *  *  * This program is distributed in the hope that it will be useful,
 *  *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *  * GNU General Public License for more details.
 *  *  *
 *  *  * You should have received a copy of the GNU General Public License along
 *  *  * with this program; if not, write to the Free Software Foundation, Inc.,
 *  *  * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *  *  * <http://www.gnu.org/licenses/>
 *  *
 *
 */

package com.arturmaslov.miniplazma.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;
import com.arturmaslov.miniplazma.BuildConfig;

import com.arturmaslov.miniplazma.R;
import com.arturmaslov.miniplazma.model.Constants;
import com.arturmaslov.miniplazma.model.GrblNotification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private static final String TAG = NotificationAdapter.class.getSimpleName();

    private List<GrblNotification> dataSet;
    private Context context;

    public NotificationAdapter(Context context, List<GrblNotification> grblNotifications){
        this.context = context;
        this.dataSet = grblNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        GrblNotification grblNotification = dataSet.get(position);
        holder.notificationTitle.setText(grblNotification.title);
        holder.notificationMessage.setText(grblNotification.message);
        holder.notificationRecievedOn.setText(grblNotification.getNotificationTime());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private TextView notificationTitle, notificationMessage, notificationRecievedOn;
        private IconTextView faTrash;

        private ViewHolder(View itemView){
            super(itemView);
            notificationTitle       = itemView.findViewById(R.id.title_text);
            notificationMessage     = itemView.findViewById(R.id.message_text);
            notificationRecievedOn  = itemView.findViewById(R.id.recieved_on_text);
            faTrash                 = itemView.findViewById(R.id.delete_notification);

            faTrash.setOnClickListener(this);
            notificationMessage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){

            if(view.equals(faTrash)){
                removeNotification(getAdapterPosition());
            }

            if(view.equals(notificationMessage)){
                handleMessageClick(getAdapterPosition());
            }
        }
    }

    private void handleMessageClick(final int position){

        final GrblNotification notification = dataSet.get(position);

        if(notification.categoryName.equalsIgnoreCase(Constants.TEXT_CATEGORY_UPDATE)){
            int versionCode = Integer.valueOf(notification.categoryValue);
            if(versionCode > BuildConfig.VERSION_CODE){
                try {
                    this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.context.getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + this.context.getPackageName())));
                }
            }

        }else if(notification.categoryName.equalsIgnoreCase(Constants.TEXT_CATEGORY_LINK)){
            this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(notification.categoryValue)));

        }else if(notification.categoryName.equalsIgnoreCase(Constants.TEXT_CATEGORY_PROMOTION)){
            try {
                this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.arturmaslov.miniplazma.plus")));
            } catch (android.content.ActivityNotFoundException anfe) {
                this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.arturmaslov.miniplazma.plus")));
            }
        }

    }

    private void removeNotification(final int position){

        final GrblNotification notification = dataSet.get(position);
        if(notification != null){
            new AlertDialog.Builder(this.context)
                    .setTitle(notification.title)
                    .setMessage(context.getString(R.string.text_delete_notification))
                    .setPositiveButton(this.context.getString(R.string.text_yes_confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            notification.delete();
                            dataSet.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, dataSet.size());
                        }
                    })
                    .setNegativeButton(this.context.getString(R.string.text_cancel), null)
                    .setCancelable(true)
                    .show();
        }
    }

}
