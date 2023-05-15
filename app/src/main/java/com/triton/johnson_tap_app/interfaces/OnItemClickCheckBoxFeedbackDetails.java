package com.triton.johnson_tap_app.interfaces;

import com.triton.johnson_tap_app.responsepojo.Feedback_DetailsResponse;

public interface OnItemClickCheckBoxFeedbackDetails {
    void itemClickCheckBoxFeedbackDetails(int newPosition, Feedback_DetailsResponse.DataBean selectedItem);
}