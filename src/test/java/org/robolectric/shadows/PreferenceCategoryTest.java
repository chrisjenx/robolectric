package org.robolectric.shadows;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.robolectric.TestRunners;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Activity;
import android.preference.PreferenceCategory;

import org.robolectric.Robolectric;

@RunWith(TestRunners.WithDefaults.class)
public class PreferenceCategoryTest {

	private PreferenceCategory category;
	private ShadowPreferenceCategory shadow;

    @Before
    public void setUp() throws Exception {
    	category = new PreferenceCategory(new Activity());
    	shadow = Robolectric.shadowOf(category);
    }
    
	@Test
	public void shouldInheritFromPreferenceGroup() {
		assertThat(shadow, instanceOf(ShadowPreferenceGroup.class));
	}	
}
