package com.gunder.webviewtopdf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //        membuat objek dari webview
    WebView printWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        inisialisasi webview
        final WebView webView = (WebView) findViewById(R.id.wvMain);
//        inisialisasi button
        Button savePdf = (Button) findViewById(R.id.btnPdf);
//        setting webView client
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
//                inisilasasi objek dari printWeb
                printWeb = webView;
            }
        });

//        load url disini
        webView.loadUrl("https://google.com");
//        aksi ketika tombol ditekan
        savePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (printWeb != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        //                    memanggil fungsi createWebViewPrintJob()
                        PrintTheWebPage(printWeb);
                    }else {
//                    menampilkan toas kepada user
                        Toast.makeText(MainActivity.this, "Tidak tersedia untuk device Lolipop", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "laman belum load penuh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    membuat objek print Job
    PrintJob printJob;
//    boolean untuk kontrol status print Web
    boolean printBtnPressed = false;



//    set minimal API yang didukung
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView webView) {
//        mengubah printBtnPressed ke true
        printBtnPressed = true;

//        membuat instance printManager
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);
//        setting nama job
        String jobname = getString(R.string.app_name) + " Web page" + webView.getUrl();
//        membuat printDocument adapter
        PrintDocumentAdapter printDocumentAdapter = webView.createPrintDocumentAdapter(jobname);

//        membuat nama dar printjob dan adapter instance
        assert printManager != null;
        printJob = printManager.print(jobname, printDocumentAdapter,
                new PrintAttributes.Builder().build());
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (printJob != null && printBtnPressed){
            if (printJob.isCompleted()){
                Toast.makeText(this,"complete!", Toast.LENGTH_SHORT).show();
            }else if (printJob.isStarted()){
                Toast.makeText(this,"started!", Toast.LENGTH_SHORT).show();
            } else if (printJob.isBlocked()){
                Toast.makeText(this,"blocked!", Toast.LENGTH_SHORT).show();
            } else if (printJob.isCancelled()){
                Toast.makeText(this,"cancelled!", Toast.LENGTH_SHORT).show();
            } else if (printJob.isFailed()){
                Toast.makeText(this,"failed!", Toast.LENGTH_SHORT).show();
            } else if (printJob.isQueued()){
                Toast.makeText(this,"queued!", Toast.LENGTH_SHORT).show();
            }
            printBtnPressed = false;
        }
    }
}

