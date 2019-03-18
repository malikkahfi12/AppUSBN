package com.example.asus.qunasta;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class MatematikaActivity extends AppCompatActivity {
    private MediaPlayer audio;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matematika);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Prediksi Essay Matematika");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PDFView pdfView = findViewById(R.id.pdfView);
        pdfView.fromAsset("essaymtk.pdf")
                .pages(0, 1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14,15)
                .enableSwipe(true)
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(0)
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
        audio = MediaPlayer.create(this,R.raw.mtk);
        audio.setVolume(1,1);
        audio.start();
    }
}
