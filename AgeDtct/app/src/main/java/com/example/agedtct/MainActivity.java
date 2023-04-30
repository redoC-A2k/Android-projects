package com.example.agedtct;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import com.example.agedtct.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

    ImageView imgview;
    Bitmap bp;
    Mat mat;
    int SELECT_CODE=100;
    int CAMERA_CODE=101;
    int CAMERA_PERM_REQ_CODE=102;
    Button selectbtn,camerabtn;
    int imageSize=96;
    String TAG = "Main Activity";

    public void detectage(Bitmap bp){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 96, 96, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            bp.getPixels(intValues, 0, bp.getWidth(), 0, 0, bp.getWidth(), bp.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF)/(255.0f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF)/(255.0f));
                    byteBuffer.putFloat((val & 0xFF)/(255.0f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature1 = outputs.getOutputFeature1AsTensorBuffer();
            float age[] = outputFeature0.getFloatArray();
            float gender[] = outputFeature1.getFloatArray();
            Log.i(TAG,Float.toString(age[0]));
            Log.i(TAG,Float.toString(gender[0]));
//            TextView txtview = findViewById(R.id.txtview);
            for (float t : age) {
                System.out.println(t);
                Log.d(TAG,Integer.toString((int)t));
            }
//            txtview.setText((int)age[0]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "Succes in loading opencv 4");
        } else {
            Log.d(TAG, "Error while loading");
        }
        getPermissions();
        camerabtn=findViewById(R.id.camerabtn);
        selectbtn=findViewById(R.id.selectbtn);
        imgview=findViewById(R.id.imgview);

        selectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_CODE);
            }
        });

        camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CODE && data!=null){
            try {
                bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
//                int dimn = Math.min(bp.getWidth(),bp.getHeight());
//                bp = ThumbnailUtils.extractThumbnail(bp,dimn,dimn);
//                imgview.setImageBitmap(bp);
//                mat = new Mat();
//                Utils.bitmapToMat(bp,mat);
////                Imgproc.cvtColor();
                bp = Bitmap.createScaledBitmap(bp,imageSize,imageSize,false);
                imgview.setImageBitmap(bp);
                detectage(bp);
            } catch (IOException e){

            }
        }
        if (requestCode == CAMERA_CODE && data!=null){
            bp = (Bitmap) data.getExtras().get("data");
            imgview.setImageBitmap(bp);
            mat = new Mat();
            Utils.bitmapToMat(bp,mat);
        }
    }

    void getPermissions(){
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERM_REQ_CODE);
        }
    }
}