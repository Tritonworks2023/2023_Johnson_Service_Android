package com.triton.johnson_tap_app.Service_Activity.serviceVisibilityModule;

import static com.triton.johnson_tap_app.RestUtils.getContentType;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImage;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.triton.johnson_tap_app.Adapter.PetCurrentImageList2Adapter;
import com.triton.johnson_tap_app.R;
import com.triton.johnson_tap_app.api.APIInterface;
import com.triton.johnson_tap_app.api.RetrofitClient;
import com.triton.johnson_tap_app.interfaces.OnItemClickDataChangeListener;
import com.triton.johnson_tap_app.requestpojo.PetAppointmentCreateRequest;
import com.triton.johnson_tap_app.requestpojo.ServiceVisibilityCheckDateRequest;
import com.triton.johnson_tap_app.requestpojo.ServiceVisibilityRequest;
import com.triton.johnson_tap_app.responsepojo.FileUploadResponse;
import com.triton.johnson_tap_app.responsepojo.NewJobListServiceVisibilityResponse;
import com.triton.johnson_tap_app.responsepojo.SuccessResponse;
import com.triton.johnson_tap_app.utils.CommonFunction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherServiceVisibilityFormActivity extends AppCompatActivity implements View.OnClickListener {

    private static String formattedDate = "";
    int PERMISSION_CLINIC = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private Context context;
    private List<PetAppointmentCreateRequest.PetImgBean> pet_imgList = new ArrayList();
    private SharedPreferences sharedPreferences;
    private String TAG = OtherServiceVisibilityFormActivity.class.getSimpleName(), se_id, se_user_name, se_user_mobile_no,
            se_user_location, strSelectedCatCode, strSelectedCatName, userid = "", strDateType = "", uploadImagePath = "";
    private TextView txt_cat, txt_job_no, txt_cust_name, txt_emp_id, txt_emp_name, txt_program_date;
    private int day, month, year;
    private DatePickerDialog datePickerDialog;
    private ImageView img_back;
    private MultipartBody.Part filePart;
    private RequestBody rbJobId, ebCatType, rbProgramDate;
    private RecyclerView rv_upload_images_list;
    private Button btn_upload_image, btn_submit;
    private Dialog dialog;
    private NewJobListServiceVisibilityResponse.Data newJobListServiceVisibilityDataResponse = new NewJobListServiceVisibilityResponse.Data();
    private ServiceVisibilityRequest serviceVisibilityRequest = new ServiceVisibilityRequest();
    private ArrayList<ServiceVisibilityRequest.Images_ary> serviceVisibilityImagesAryListRequest = new ArrayList<>();
    private ServiceVisibilityCheckDateRequest serviceVisibilityCheckDateRequest = new ServiceVisibilityCheckDateRequest();
    private boolean checkDate = false;

    private String getFilePathFromURI(Context context, Uri contentUri) {
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String path = context.getFilesDir() + "/" + "MyFirstApp/";

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fullName = path + "mylog";
            File copyFile = new File(fullName);

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    private void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "copy: error -> " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_other_service_visibility_form);

        context = this;

        img_back = findViewById(R.id.img_back);

        txt_cat = findViewById(R.id.txt_cat);
        txt_job_no = findViewById(R.id.txt_job_no);
        txt_cust_name = findViewById(R.id.txt_cust_name);
        txt_emp_id = findViewById(R.id.txt_emp_id);
        txt_emp_name = findViewById(R.id.txt_emp_name);
        txt_program_date = findViewById(R.id.txt_program_date);

        rv_upload_images_list = findViewById(R.id.rv_upload_images_list);

        btn_upload_image = findViewById(R.id.btn_upload_image);
        btn_submit = findViewById(R.id.btn_submit);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        se_id = sharedPreferences.getString("user_id", "default value");
        se_user_name = sharedPreferences.getString("user_name", "default value");
        se_user_mobile_no = sharedPreferences.getString("user_mobile_no", "default value");
        se_user_location = sharedPreferences.getString("user_location", "default value");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("newJobListServiceVisibilityDataResponse")) {
                newJobListServiceVisibilityDataResponse = extras.getParcelable("newJobListServiceVisibilityDataResponse");
            }
            if (extras.containsKey("cat_code")) {
                strSelectedCatCode = extras.getString("cat_code");
            }
            if (extras.containsKey("cat_name")) {
                strSelectedCatName = extras.getString("cat_name");
            }

            Log.i(TAG, "onCreate: strSelectedCatCode -> " + strSelectedCatCode + " strSelectedCatName -> " + strSelectedCatName);
            Log.i(TAG, "onCreate: newJobListServiceVisibilityDataResponse -> " + new Gson().toJson(newJobListServiceVisibilityDataResponse));
        }

        txt_program_date.setOnClickListener(this);
        img_back.setOnClickListener(this);
        btn_upload_image.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        txt_cat.setText(strSelectedCatName);
        txt_job_no.setText(newJobListServiceVisibilityDataResponse.getJOBNO());
        txt_cust_name.setText(newJobListServiceVisibilityDataResponse.getCUST_NAME());
        txt_emp_id.setText(se_id);
        txt_emp_name.setText(se_user_name);

        serviceVisibilityCheckDateRequest.setJob_id(CommonFunction.nullPointer(newJobListServiceVisibilityDataResponse.getJOBNO()));
        serviceVisibilityCheckDateRequest.setCat_type(strSelectedCatCode);
        serviceVisibilityCheckDateRequest.setSubmitted_by_num(se_user_mobile_no);

        rbJobId = RequestBody.create(MediaType.parse("multipart/form-data"), newJobListServiceVisibilityDataResponse.getJOBNO());
        ebCatType = RequestBody.create(MediaType.parse("multipart/form-data"), strSelectedCatCode);

        serviceVisibilityRequest.setJob_id(CommonFunction.nullPointer(newJobListServiceVisibilityDataResponse.getJOBNO()));
        serviceVisibilityRequest.setCat_type(strSelectedCatCode);
        serviceVisibilityRequest.setCus_name(CommonFunction.nullPointer(newJobListServiceVisibilityDataResponse.getCUST_NAME()));
        serviceVisibilityRequest.setSubmitted_by_emp_code(se_id);
        serviceVisibilityRequest.setBrcode(se_user_location);
        serviceVisibilityRequest.setSubmitted_by_num(se_user_mobile_no);
        serviceVisibilityRequest.setSubmitted_by_name(se_user_name);

        initLoadingDialog();
        getTodayDate();
    }

    private void initLoadingDialog() {
        dialog = new Dialog(context, R.style.NewProgressDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progroess_popup);
        dialog.setCancelable(false);
    }

    private void choosePetImage() {
        if (pet_imgList != null && pet_imgList.size() >= 4) {
            Toasty.warning(getApplicationContext(), " Sorry You Can't Add More Than 4", Toast.LENGTH_SHORT).show();
        } else {
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
            } else {
                CropImage.activity().start(OtherServiceVisibilityFormActivity.this);
            }
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = null;
                    if (result != null) {
                        resultUri = result.getUriContent();
                    }

                    if (resultUri != null) {
                        Log.i(TAG, "onActivityResult: resultUri -> " + resultUri);

                        String filename = getFileName(resultUri);
                        Log.i(TAG, "onActivityResult: filename -> " + filename);

                        String filePath = getFilePathFromURI(this, resultUri);

                        assert filePath != null;

                        File file = new File(filePath); // initialize file here

                        long length = file.length() / 1024; // Size in KB

                        Log.i(TAG, "onActivityResult: length -> " + length);

                        if (length > 2000) {

                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("File Size")
                                    .setContentText("Please choose file size less than 2 MB ")
                                    .setConfirmText("Ok")
                                    .show();
                        } else {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());

                            filePart = MultipartBody.Part.createFormData("sampleFile", userid + currentDateandTime + filename, RequestBody.create(MediaType.parse("image/*"), file));
                            getServiceVisibilityUpload();
                        }
                    } else {
                        Toasty.warning(this, "Image Error!!Please upload Some other image", Toasty.LENGTH_LONG).show();
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "onActivityResult: error -> " + e.getMessage());
        }
    }

    private void getServiceVisibilityUpload() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getImageClient().create(APIInterface.class);

        Call<FileUploadResponse> call = apiInterface.getServiceVisibilityUpload(rbJobId, ebCatType, rbProgramDate, filePart);

        Log.i(TAG, "getServiceVisibilityUpload: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {

                dialog.dismiss();
                Log.i(TAG, "getServiceVisibilityUpload: onResponse: FileUploadResponse - " + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        uploadImagePath = response.body().getData();
                        PetAppointmentCreateRequest.PetImgBean petImgBean = new PetAppointmentCreateRequest.PetImgBean();
                        petImgBean.setPet_img(uploadImagePath);
                        pet_imgList.add(petImgBean);
                        if (uploadImagePath != null) {
                            setImageListView();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getServiceVisibilityUpload: onFailure: error -> " + t.getMessage());
            }
        });
    }

    private void getServiceVisibilityCheckDate() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Log.i(TAG, "getServiceVisibilityCheckDate: serviceVisibilityCheckDateRequest -> " + new Gson().toJson(serviceVisibilityCheckDateRequest));
        Call<SuccessResponse> call = apiInterface.getServiceVisibilityCheckDate(getContentType(), serviceVisibilityCheckDateRequest);
        Log.i(TAG, "getServiceVisibilityCheckDate: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getServiceVisibilityCheckDate: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (response.body().getCode() == 200) {
                        checkDate = false;
                        ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
                    } else {
                        checkDate = true;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getServiceVisibilityCheckDate: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void setImageListView() {
        rv_upload_images_list.setVisibility(View.VISIBLE);
        rv_upload_images_list.setItemAnimator(new DefaultItemAnimator());
        PetCurrentImageList2Adapter petCurrentImageListAdapter = new PetCurrentImageList2Adapter(pet_imgList, new OnItemClickDataChangeListener() {
            @Override
            public void itemClickDataChangeListener(int position, String strParam, String strData) {
                if (strParam.equalsIgnoreCase("remove")) {
                    pet_imgList.remove(position);
                }
            }
        });
        rv_upload_images_list.setAdapter(petCurrentImageListAdapter);
    }

    private void getTodayDate() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        strDateType = "txt_both";
        setDate(day, month, year);
    }

    private void setDate(int dayOfMonth, int monthOfYear, int year1) {

        String dateTime = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
        txt_program_date.setText(dateTime);

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateTime);
            str = outputFormat.format(date);
            formattedDate = str;/*.replace("AM", "am").replace("PM", "pm")*/

            Log.i(TAG, "setDate: formattedDate-> " + formattedDate);
            if (strDateType.equalsIgnoreCase("txt_both")) {
                serviceVisibilityRequest.setProgram_date(formattedDate);
                serviceVisibilityRequest.setSubmitted_by_on(formattedDate);
                serviceVisibilityCheckDateRequest.setProgram_date(formattedDate);
                rbProgramDate = RequestBody.create(MediaType.parse("multipart/form-data"), formattedDate);
                getServiceVisibilityCheckDate();
            } else if (strDateType.equalsIgnoreCase("txt_program_date")) {
                serviceVisibilityRequest.setProgram_date(formattedDate);
                serviceVisibilityCheckDateRequest.setProgram_date(formattedDate);
                rbProgramDate = RequestBody.create(MediaType.parse("multipart/form-data"), formattedDate);
                getServiceVisibilityCheckDate();
            }

        } catch (ParseException e) {
            Log.e(TAG, "setDate: ParseException-> ", e);
        }
    }

    private void setValidationCreateSmartServiceVisibility() {

        if (!checkDate) {
            ErrorMsgDialog("Already you have submitted for this Date.\nKindly select another Date.");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getBrcode())) {
            ErrorMsgDialog("Please Enter Branch Code");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getJob_id())) {
            ErrorMsgDialog("Please Enter Job ID");
        } /*else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getBuilding_name())) {
            ErrorMsgDialog("Please Enter Building Name");
        }*/ else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getCat_type())) {
            ErrorMsgDialog("Please Enter Category Type");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getCus_name())) {
            ErrorMsgDialog("Please Enter Customer Name");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getSubmitted_by_emp_code())) {
            ErrorMsgDialog("Please Enter Emp ID");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getSubmitted_by_num())) {
            ErrorMsgDialog("Please Enter Conductor Mobile Number");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getSubmitted_by_name())) {
            ErrorMsgDialog("Please Enter Conductor Name");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getSubmitted_by_on())) {
            ErrorMsgDialog("Please Enter Submitted Date");
        } else if (!CommonFunction.nullPointerValidator(serviceVisibilityRequest.getProgram_date())) {
            ErrorMsgDialog("Please Enter Program Date");
        } else if (pet_imgList == null || pet_imgList.isEmpty()) {
            ErrorMsgDialog("Please Upload At Least One Image");
        } else {
            Log.i(TAG, "setValidationCreateSmartServiceVisibility: count -> " + pet_imgList.size() + " pet_imgList -> " + new Gson().toJson(pet_imgList));
            serviceVisibilityImagesAryListRequest = new ArrayList<>();
            for (PetAppointmentCreateRequest.PetImgBean img : pet_imgList) {
                serviceVisibilityImagesAryListRequest.add(new ServiceVisibilityRequest.Images_ary(img.getPet_img()));
            }

            if (!serviceVisibilityImagesAryListRequest.isEmpty()) {
                serviceVisibilityRequest.setImages_ary(serviceVisibilityImagesAryListRequest);
            }

            Log.i(TAG, "setValidationCreateSmartServiceVisibility: serviceVisibilityImagesAryListRequest -> " + new Gson().toJson(serviceVisibilityImagesAryListRequest));

            getCreateOtherServiceVisibility();
        }
        Log.i(TAG, "setValidationCreateSmartServiceVisibility: serviceVisibilityRequest -> " + new Gson().toJson(serviceVisibilityRequest));
    }

    private void getCreateOtherServiceVisibility() {

        if (!dialog.isShowing()) {
            dialog.show();
        }

        APIInterface apiInterface = RetrofitClient.getClient().create(APIInterface.class);

        Call<SuccessResponse> call = apiInterface.getCreateOtherServiceVisibility(getContentType(), serviceVisibilityRequest);
        Log.i(TAG, "getCreateOtherServiceVisibility: URL -> " + call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                dialog.dismiss();
                Log.i(TAG, "getCreateOtherServiceVisibility: onResponse: SuccessResponse -> " + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (response.body().getCode() == 200) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        ErrorMsgDialog(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "getCreateOtherServiceVisibility: onFailure: error --> " + t.getMessage());
                ErrorMsgDialog(t.getMessage());
            }
        });
    }

    private void ErrorMsgDialog(String strMsg) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = getLayoutInflater().inflate(R.layout.popup_tryagain, null);

        TextView txt_Message = mView.findViewById(R.id.txt_message);
        Button btn_Ok = mView.findViewById(R.id.btn_ok);

        txt_Message.setText(strMsg);

        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_program_date) {
            strDateType = "txt_program_date";
            callDatePicker();
        } else if (view.getId() == R.id.img_back) {
            onBackPressed();
        } else if (view.getId() == R.id.btn_upload_image) {
            choosePetImage();
        } else if (view.getId() == R.id.btn_submit) {
            setValidationCreateSmartServiceVisibility();
        }
    }

    private void callDatePicker() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);

        datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) ->
                        setDate(dayOfMonth, monthOfYear, year1), year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}