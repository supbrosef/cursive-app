package com.nvarelas.nick.cursivemadeeasy;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(AndroidJUnit4.class)
//public class ExampleInstrumentedTest {
//
//    Intent intent;
//    SharedPreferences.Editor preferencesEditor;
//
//    @Rule
//    public ActivityTestRule<AccountInfoActivity> mActivityRule = new ActivityTestRule<>(
//            AccountInfoActivity.class,
//            true,
//            false); // Activity is not launched immediately
//
//    @Before
//    public void setUp() {
//        Context targetContext = getInstrumentation().getTargetContext();
//        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit();
//    }
//
//    @Test
//    public void populateUsernameFromSharedPrefsTest() {
//        preferencesEditor.putString("username", "testUsername");
//        preferencesEditor.commit();
//
//        // Launch activity
//        mActivityRule.launchActivity(new Intent());
//
//        onView(withId(R.id.textview_account_username))
//                .check(matches(isDisplayed()))
//                .check(matches(withText(testUsername)));
//    }
//}
