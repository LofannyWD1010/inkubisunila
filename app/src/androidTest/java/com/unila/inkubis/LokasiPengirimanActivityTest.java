package com.unila.inkubis;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

public class LokasiPengirimanActivityTest {

    @Rule
    public ActivityTestRule<LokasiPengirimanActivity> mActivityRule = new ActivityTestRule<LokasiPengirimanActivity>(LokasiPengirimanActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("ASAL", "beli");
            intent.putExtra("TOTAL_HARGA", 115000);
            intent.putExtra("NAMA", "Konsentrat B");
            intent.putExtra("SATUAN", "50 kg");
            intent.putExtra("ID_PAKAN", 43);
            intent.putExtra("ID_PENJUAL", 34);
            return intent;
        }
    };


    @Test
    public void memastikanValueSpinner() {


        SystemClock.sleep(3000);
        onView(withId(R.id.sp_wilayah)).perform(click());
        SystemClock.sleep(3000);
        onData(instanceOf(String.class)).atPosition(1).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.et_detail_alamat)).perform(typeText("Kampung Baru Unila"), closeSoftKeyboard());
        SystemClock.sleep(2000);
        onView(withId(R.id.btn_konfirmasi)).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.tv_total_pembayaran)).check(matches(withText("Rp. 615.000")));
    }


}