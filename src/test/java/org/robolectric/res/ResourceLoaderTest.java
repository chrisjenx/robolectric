package org.robolectric.res;


import android.preference.PreferenceActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import org.robolectric.R;
import org.robolectric.Robolectric;
import org.robolectric.TestRunners;
import org.robolectric.annotation.Values;
import org.robolectric.res.builder.LayoutBuilder;
import org.robolectric.util.I18nException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.robolectric.Robolectric.shadowOf;
import static org.robolectric.util.TestUtil.resourceFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

@RunWith(TestRunners.WithDefaults.class)
public class ResourceLoaderTest {
    private ResourcePath resourcePath;

    @Before
    public void setUp() throws Exception {
        resourcePath = new ResourcePath(R.class, resourceFile("res"), resourceFile("assets"));
    }

    @Test
    public void shouldLoadSystemResources() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        String stringValue = resourceLoader.getStringValue(resourceLoader.getResourceExtractor().getResName(android.R.string.copy), "");
        assertEquals("Copy", stringValue);

        ViewNode node = resourceLoader.getLayoutViewNode(new ResName("android:layout/simple_spinner_item"), "");
        assertNotNull(node);
    }

    @Test
    public void shouldLoadLocalResources() throws Exception {
        ResourceLoader resourceLoader = new PackageResourceLoader(resourcePath);
        String stringValue = resourceLoader.getStringValue(resourceLoader.getResourceExtractor().getResName(R.string.copy), "");
        assertEquals("Local Copy", stringValue);
    }

    @Test(expected=I18nException.class)
    public void shouldThrowExceptionOnI18nStrictModeInflateView() throws Exception {
        shadowOf(Robolectric.application).setStrictI18n(true);
        ResourceLoader resourceLoader = shadowOf(Robolectric.application).getResourceLoader();
        ViewGroup vg = new FrameLayout(Robolectric.application);
        new LayoutBuilder(resourceLoader).inflateView(Robolectric.application, R.layout.text_views, vg, "");
    }

    @Test(expected=I18nException.class)
    public void shouldThrowExceptionOnI18nStrictModeInflatePreferences() throws Exception {
        shadowOf(Robolectric.application).setStrictI18n(true);
        PreferenceActivity preferenceActivity = new PreferenceActivity() {
        };
        preferenceActivity.addPreferencesFromResource(R.xml.preferences);
    }

    @Test @Values(qualifiers = "doesnotexist-land-xlarge")
    public void testChoosesLayoutBasedOnSearchPath_respectsOrderOfPath() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        ViewGroup viewGroup = new FrameLayout(Robolectric.application);
        ViewGroup view = (ViewGroup) new LayoutBuilder(resourceLoader).inflateView(Robolectric.application, R.layout.different_screen_sizes, viewGroup, "doesnotexist-land-xlarge");
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        assertThat(textView.getText().toString(), equalTo("land"));
    }

    @Test
    public void checkForPollution1() throws Exception {
        checkForPollutionHelper();
    }

    @Test
    public void checkForPollution2() throws Exception {
        checkForPollutionHelper();
    }

    private void checkForPollutionHelper() {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        ViewGroup viewGroup = new FrameLayout(Robolectric.application);
        ViewGroup view = (ViewGroup) new LayoutBuilder(resourceLoader).inflateView(Robolectric.application, R.layout.different_screen_sizes, viewGroup, "");
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        assertThat(textView.getText().toString(), equalTo("default"));
        Robolectric.shadowOf(Robolectric.getShadowApplication().getResources().getConfiguration()).overrideQualifiers("land"); // testing if this pollutes the other test
    }
    
    @Test
    public void testStringsAreResolved() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        assertThat(Arrays.asList(resourceLoader.getStringArrayValue(resourceLoader.getResourceExtractor().getResName(R.array.items), "")), hasItems("foo", "bar"));
    }

    @Test
    public void testStringsAreWithReferences() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        assertThat(Arrays.asList(resourceLoader.getStringArrayValue(resourceLoader.getResourceExtractor().getResName(R.array.greetings), "")), hasItems("hola", "Hello"));
    }

    @Test
    public void shouldAddAndroidToSystemStringArrayName() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        assertThat(Arrays.asList(resourceLoader.getStringArrayValue(resourceLoader.getResourceExtractor().getResName(android.R.array.emailAddressTypes), "")), hasItems("Home", "Work", "Other", "Custom"));
        assertThat(Arrays.asList(resourceLoader.getStringArrayValue(resourceLoader.getResourceExtractor().getResName(R.array.emailAddressTypes), "")), hasItems("Doggy", "Catty"));
    }

    @Test
    public void testIntegersAreResolved() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        assertThat(resourceLoader.getIntegerArrayValue(resourceLoader.getResourceExtractor().getResName(R.array.zero_to_four_int_array), ""),
                equalTo(new int[]{0, 1, 2, 3, 4}));
    }

    @Test
    public void testEmptyArray() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        assertThat(resourceLoader.getIntegerArrayValue(resourceLoader.getResourceExtractor().getResName(R.array.empty_int_array), "").length,
                equalTo(0));
    }

    @Test
    public void testIntegersWithReferences() throws Exception {
        ResourceLoader resourceLoader = Robolectric.getShadowApplication().getResourceLoader();
        assertThat(resourceLoader.getIntegerArrayValue(resourceLoader.getResourceExtractor().getResName(R.array.with_references_int_array), ""),
                equalTo(new int[]{0, 2000, 1}));
    }

    @Test public void shouldLoadForAllQualifiers() throws Exception {
        ResourceLoader resourceLoader = new PackageResourceLoader(resourcePath);
        assertThat(resourceLoader.getStringValue(resourceLoader.getResourceExtractor().getResName(R.string.hello), ""), equalTo("Hello"));
        assertThat(resourceLoader.getStringValue(resourceLoader.getResourceExtractor().getResName(R.string.hello), "fr"), equalTo("Bonjour"));
    }
}
