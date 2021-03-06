package com.stevenschoen.putionew.model;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PutioUploadInterface {
  @Multipart
  @POST("files/upload")
  Single<ResponseOrError.BasePutioResponse.PutioTransferFileUploadResponse> uploadFile(@Part MultipartBody.Part file, @Part("parent_id") long parentId);
}
