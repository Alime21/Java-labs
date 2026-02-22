package msku.ceng.madlab.weekk8;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import msku.ceng.madlab.weekk8.R;

public class MainActivity extends AppCompatActivity {
/* 1. ADIM  sınıf tanımı ve değişkenler */
    EditText txtURL;
    Button btnDownload;
    ImageView imgView;
    private static final int REQUEST_EXTERNAL_STORAGE =1;  /*İzin isteği için benzersiz kod */
    private static String [] PERMISSIONS_STORAGE = {       /*İstenen depolama izinleri listesi*/
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*2. ADIM UI BİLEŞENLERİNİ BAĞLAMAK */
        txtURL = findViewById(R.id.txtURL);
        btnDownload = findViewById(R.id.btnDownload);
        imgView = findViewById(R.id.imgView);

/*3. ADIM button Click listener  */
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // depolama iznini kontrol et
                int permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permission != PackageManager.PERMISSION_GRANTED){
                    // izin yoksa iste
                    ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE);
//                } else {
//                    /* izin varsa: indirme önizleme dosya yolunu belirleyip indirmeyi başlatıyoruz */
//                    String fileName = "temp_image.jpg";
//                    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
//                            + "/" + fileName;
//                    downloadFile(txtURL.getText().toString(), filePath);
//                    preview(filePath);

//                    DownloadTask backgroundTask = new DownloadTask();
//                    String [] urls = new String[1];
//                    urls[0] = txtURL.getText().toString();
//                    backgroundTask.execute(urls);

                    Thread backgroundThread = new Thread(new DownloadRunnable(txtURL.getText().toString()));
                    backgroundThread.start();
                }
            }
        });
    }
    private void downloadFile(String file_Url, String filePath) {
        try{
            URL strUrl = new URL(file_Url);
            URLConnection urlConnection = strUrl.openConnection();
            urlConnection.connect();
            InputStream input = new BufferedInputStream(strUrl.openStream(),8192);
            OutputStream output = new FileOutputStream(filePath);

            byte data[] = new byte[1824];
            int count;
            // veriyi oku ve dosyaya yaz
            while((count = input.read(data)) != -1) {
                output.write(data,0,count);
            }

            output.close();
            input.close();

        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
    // resmi görüntüleme metodu
    private void preview(String filePath) {
        Bitmap image = BitmapFactory.decodeFile(filePath);
        // resmi boyutlandır
        float width = image.getWidth();
        float height = image.getHeight();
        int W = 400;
        int H = (int)((height * W) / width);

        Bitmap.createScaledBitmap(image,W,H,false);
        imgView.setImageBitmap(image);
    }
// izin sonuçlarını işleme
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_EXTERNAL_STORAGE){
            if(grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && //okuma izni
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){  //yazma izni
//                // izinler verildiyse indirmeyi başlat
//                String fileName = "temp_image.jpg";
//                String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
//                        + "/" + fileName;
//                downloadFile(txtURL.getText().toString(),filePath);
//                preview(filePath);

//                DownloadTask backgroundTask = new DownloadTask();
//                String [] urls = new String[1];
//                urls[0] = txtURL.getText().toString();
//                backgroundTask.execute(urls);

                Thread backgroundThread = new Thread(new DownloadRunnable(txtURL.getText().toString()));
                backgroundThread.start();

            }
        }
        else{  //izin vermediyse:
            Toast.makeText(this,"External Storage permission not granted",Toast.LENGTH_SHORT).show();
        }
    }

    class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        ProgressDialog PD;
        @Override
        protected Bitmap doInBackground(String... urls) {
            String fileName = "temp.jpg";
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            downloadFile(urls[0], imagePath + "/" + fileName);

            return scaleBitmap(imagePath + "/" + fileName);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgView.setImageBitmap(bitmap);
            PD.dismiss();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PD = new ProgressDialog(MainActivity.this);
            PD.setMax(100);
            PD.setIndeterminate(false);
            PD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            PD.setTitle("Downloading");
            PD.setMessage("Please wait...");
            PD.show();
        }
    }
    private  Bitmap scaleBitmap(String imagePath) {
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        // resmi boyutlandır
        float width = image.getWidth();
        float height = image.getHeight();
        int W = 400;
        int H = (int)((height * W) / width);

        Bitmap bitmap = Bitmap.createScaledBitmap(image,W,H,false);
        return bitmap;
    }

    class DownloadRunnable implements Runnable {
        String url;

        public DownloadRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            String fileName = "temp.jpg";
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            downloadFile(url, imagePath + "/" + fileName);
            scaleBitmap(imagePath + "/" + fileName);

            Bitmap bitmap = scaleBitmap(imagePath + "/" + fileName);
            runOnUiThread(new UpdateBitmap(bitmap));


        }
// asyn class da  paralel processing yapamazsın
        // asyn class use single background task ; aynı anda indirmek isitoyrsan thread kullanmalı
        class UpdateBitmap implements Runnable {
            Bitmap bitmap;

            public UpdateBitmap(Bitmap bitmap) {
                this.bitmap = bitmap;
            }

            @Override
            public void run() {
                imgView.setImageBitmap(bitmap);
            }
        }
    }
}