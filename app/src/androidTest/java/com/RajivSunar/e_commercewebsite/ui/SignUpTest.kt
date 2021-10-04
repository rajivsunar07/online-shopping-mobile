package com.RajivSunar.e_commercewebsite.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.RajivSunar.e_commercewebsite.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers

import androidx.test.espresso.ViewAction




@LargeTest
@RunWith(AndroidJUnit4::class)
class SignUpTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    fun waitFor(delay: Long): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    @Test
    fun signUpTest() {
        onView(isRoot()).perform(waitFor(5000));
        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Open"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
            allOf(
                withId(R.id.nav_login),
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.navView),
                            0
                        )
                    ),
                    8
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        val materialTextView = onView(
            allOf(
                withId(R.id.tvNoAccount), withText("No account? Sign Up"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.etEmail),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("imptest@test.com"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.etName),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("test"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.etPassword),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.etConfirmPassword),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("test"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.btnRegister), withText("Register"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val textView = onView(
            allOf(
                withText("Login"),
                withParent(withParent(withId(android.R.id.content))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Login")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
