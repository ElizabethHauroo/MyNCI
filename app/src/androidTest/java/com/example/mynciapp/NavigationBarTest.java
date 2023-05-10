package com.example.mynciapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NavigationBarTest {

    @Rule
    public ActivityScenarioRule<HomeActivity> activityScenarioRule =
            new ActivityScenarioRule<>(HomeActivity.class);

    @Test
    public void testNavigationBar() {
        onView(withId(R.id.navigation_view)).check(matches(isDisplayed()));

        // Click the profile button
        onView(withId(R.id.profile_bottomnav)).perform(click());

        // Check that the ProfileActivity is loaded
        onView(withId(R.id.profile_header)).check(matches(isDisplayed()));
    }
}

