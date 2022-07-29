package com.wicoding.winwebradio;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class EmissionFragment extends Fragment {

    CustomAdapter customAdapter;
    ViewPager viewPager;

    public EmissionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_emission, container, false);
        customAdapter = new CustomAdapter(getContext());
        viewPager = (ViewPager)rootView.findViewById(R.id.view_pager);


        viewPager.setAdapter(customAdapter);
        return rootView;
    }

    private class CustomAdapter extends PagerAdapter {

        private int[] imgs = {
                R.drawable.apps01,R.drawable.apps02,R.drawable.apps03,R.drawable.apps06,
                R.drawable.apps04,R.drawable.apps05,R.drawable.apps07,R.drawable.apps08,R.drawable.apps10,R.drawable.apps11,R.drawable.apps12};

        private LayoutInflater inflater;
        private Context ctx;

        public CustomAdapter(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.invalidate();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.template_slider,container,false);
            ImageView iv = (ImageView)v.findViewById(R.id.imageSlide);
            iv.setBackgroundResource(imgs[position]);
            container.addView(v);
            return v;
        }
    }

}
