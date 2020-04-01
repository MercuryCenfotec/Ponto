package adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cenfotec.ponto.utils.CarouselImage;
import com.cenfotec.ponto.utils.CarouselVideo;

import java.util.List;

public class Carousel_Adapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<String> carouselFiles;

    public Carousel_Adapter(FragmentManager fm, List<String> carouselFiles) {
        super(fm);
        this.carouselFiles = carouselFiles;
    }

    @Override
    public Fragment getItem(int position) {
        if (carouselFiles.get(position).contains("/videos%")) {
            return new CarouselVideo(carouselFiles.get(position));
        } else  {
            if (carouselFiles.get(position).contains("/icons%")) {
                return new CarouselImage(carouselFiles.get(position), true);
            } else {
                return new CarouselImage(carouselFiles.get(position));
            }
        }
    }

    @Override
    public int getCount() {
        return carouselFiles.size();
    }
}
