package com.johannlau.baking_app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<RecipesListActivity> mainActivityScreenTest = new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void checkRecipeListItem_RecipesListActivity() {

//        onView(w(R.id.mainrecipeslist_recyclerview)).check(matches(atPosition(0,withText("Nutella Pie")));
        onData(anything()).inAdapterView(withId(R.id.mainrecipeslist_recyclerview)).atPosition(0).perform(click());
    }
}
