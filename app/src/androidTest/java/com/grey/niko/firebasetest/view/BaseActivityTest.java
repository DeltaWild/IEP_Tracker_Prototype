package com.grey.niko.firebasetest.view;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import com.grey.niko.firebasetest.R;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BaseActivityTest {

    @Test
    public void clickingNavStudentListShouldStartMainFragment() {

        try (ActivityScenario<BaseActivity> scenario = ActivityScenario.launch(BaseActivity.class)) {
            scenario.onActivity(activity -> {
                // Given
                scenario.moveToState(Lifecycle.State.STARTED);

                // When
                activity.findViewById(R.id.navStudentList).performClick();

                // Then
                String result = activity.getSupportFragmentManager().getBackStackEntryAt(activity.getSupportFragmentManager().getBackStackEntryCount() - 1).getClass().toString();
                assertEquals("MainFragment.class", result);
            });
        }

    }
}
