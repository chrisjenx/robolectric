package org.robolectric.shadows;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import org.robolectric.TestRunners;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(TestRunners.WithDefaults.class)
public class ShapeDrawableTest {
    @Test
    public void getPaint_ShouldReturnTheSamePaint() throws Exception {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        Paint paint = shapeDrawable.getPaint();
        assertNotNull(paint);
        assertThat(shapeDrawable.getPaint(), is(paint));
    }
}
