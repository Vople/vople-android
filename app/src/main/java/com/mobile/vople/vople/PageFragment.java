package com.mobile.vople.vople;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;

/**
 * Created by parkjaemin on 16/11/2018.
 */


public class PageFragment extends Fragment {
    private int mPageNumber;
    Bitmap drawable;
    public static DisplayMetrics imgMinSize;
    public static Context mContext;

    public static PageFragment create(int pageNumber, DisplayMetrics minSize, Context context) {
        imgMinSize = minSize;
        mContext = context;
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");
    }

    @Override
    public void onDestroy() {
        if(drawable!=null)
            drawable.recycle();
        drawable = null;
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView ;
        rootView = (ViewGroup) inflater.inflate(R.layout.tutorial_page, container, false);

        drawable = null;



        switch(mPageNumber)
        {
            case 1:
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showInputMethodPicker();
                    }
                });
                break;
            case 2:
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                break;
        }

        String englishNumbers[] = {"one", "two", "three"};
        String tutorialResourceName = String.format("splash_%s_all",
                englishNumbers[mPageNumber]);
        int resourceId = getActivity().getResources().getIdentifier(
                tutorialResourceName, "drawable", "com.mobile.vople.vople");
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        drawable = BitmapFactory.decodeResource(getActivity().getResources(), resourceId, opts);

        ImageView ivTutorial = (ImageView)((LinearLayout)rootView).findViewById(R.id.iv_tutorial);
        ivTutorial.setImageBitmap(drawable);

        return rootView;
    }

    public static Bitmap decodeFile( int minImageSize, InputStream is ){
        Bitmap b = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream( is, null, options );
        int scale = 1;
        if( options.outHeight > minImageSize || options.outWidth > minImageSize ) {
            scale = (int)Math.pow(  2,  (int)Math.round( Math.log( minImageSize / (double)Math.max( options.outHeight, options.outWidth ) ) / Math.log( 0.5 ) ) );
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        b = BitmapFactory.decodeStream( is, null, o2 );

        return b;
    }


}
