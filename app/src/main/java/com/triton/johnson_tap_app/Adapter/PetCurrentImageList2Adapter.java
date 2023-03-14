package com.triton.johnson_tap_app.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.PetAppointmentCreateRequest;

import java.util.List;

public class PetCurrentImageList2Adapter extends RecyclerView.Adapter<PetCurrentImageList2Adapter.AddImageListHolder> {

    List<PetAppointmentCreateRequest.PetImgBean> pet_imgList;
    private String TAG = PetCurrentImageList2Adapter.class.getSimpleName();
    private OnItemClickDataChangeListener onItemClickDataChangeListener;

    public PetCurrentImageList2Adapter(List<PetAppointmentCreateRequest.PetImgBean> pet_imgList, OnItemClickDataChangeListener onItemClickDataChangeListener) {
        this.pet_imgList = pet_imgList;
        this.onItemClickDataChangeListener = onItemClickDataChangeListener;
    }

    @NonNull
    @Override
    public AddImageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_images_upload_2, parent, false);
        return new AddImageListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddImageListHolder holder, final int position) {
        final PetAppointmentCreateRequest.PetImgBean petImgBean = pet_imgList.get(position);
        Log.i(TAG, "onBindViewHolder: ImagePic -> " + petImgBean.getPet_img());

        if (petImgBean.getPet_img() != null) {
            Glide.with(holder.certificate_pics_1.getContext())
                    .load(petImgBean.getPet_img())
                    .into(holder.certificate_pics_1);
        } else {
            Glide.with(holder.certificate_pics_1.getContext())
                    .load(RetrofitClient.BANNER_IMAGE_URL)
                    .into(holder.certificate_pics_1);
        }

        holder.removeImg.setOnClickListener(view -> {
            onItemClickDataChangeListener.itemClickDataChangeListener(position, "remove", "");
//            pet_imgList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return pet_imgList.size();
    }

    public static class AddImageListHolder extends RecyclerView.ViewHolder {
        ImageView removeImg, certificate_pics_1;

        public AddImageListHolder(View itemView) {
            super(itemView);
            certificate_pics_1 = itemView.findViewById(R.id.certificate_pics_1);
            removeImg = itemView.findViewById(R.id.close);
        }
    }
}