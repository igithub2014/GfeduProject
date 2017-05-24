package jc.cici.android.atom.adapter;

import android.support.v7.widget.CardView;
/**
 * viewPagerdapter
 * Created by atom on 2017/5/10.
 */

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
