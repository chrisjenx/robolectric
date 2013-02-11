package org.robolectric.shadows;

import android.widget.CheckBox;
import org.robolectric.TestRunners;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(TestRunners.WithDefaults.class)
public class CheckBoxTest {
    @Test
    public void testWorks() throws Exception {
        CheckBox checkBox = new CheckBox(null);
        assertThat(checkBox.isChecked(), equalTo(false));

        checkBox.setChecked(true);
        assertThat(checkBox.isChecked(), equalTo(true));

        checkBox.performClick();
        assertThat(checkBox.isChecked(), equalTo(false));

        checkBox.toggle();
        assertThat(checkBox.isChecked(), equalTo(true));
    }
}
