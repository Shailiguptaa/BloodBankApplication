package com.example.bloodbankapplication.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbankapplication.Model.User;
import com.example.bloodbankapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.ViewHolder> {

    private Context context;
    private List<User> userList;


    public UserAdaptor(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdaptor.ViewHolder holder, int position) {
              final User user = userList.get(position);
              holder.type.setText(user.getType());
              holder.useremail.setText(user.getEmail());
              holder.phoneNumber.setText(user.getPhonenumber());
              holder.username.setText(user.getName());
              holder.bloodGroup.setText(user.getBloodgroup());
        Glide.with(context).load(user.getProfilepictureurl()).into(holder.userProfileImage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userProfileImage;
        public TextView type, username, useremail, phoneNumber, bloodGroup;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            type = itemView.findViewById(R.id.type);
            username = itemView.findViewById(R.id.username);
            useremail = itemView.findViewById(R.id.useremail);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            bloodGroup = itemView.findViewById(R.id.bloodGroup);
        }
    }
}
