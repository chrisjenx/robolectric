package org.robolectric.shadows;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.ImageView;
import org.robolectric.R;
import org.robolectric.Robolectric;
import org.robolectric.TestRunners;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;
import static org.robolectric.Robolectric.visualize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(TestRunners.WithDefaults.class)
public class ImageViewTest {
    private ImageView imageView;

    @Before
    public void setUp() throws Exception {
        Resources resources = Robolectric.application.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources,
                R.drawable.an_image);
        imageView = new ImageView(Robolectric.application);
        imageView.setImageBitmap(bitmap);
    }

    @Test
    public void shouldDrawWithImageMatrix() throws Exception {
        imageView.setImageMatrix(new Matrix());
        assertEquals("Bitmap for resource:org.robolectric:drawable/an_image",
                visualize(imageView));

        Matrix matrix = new Matrix();
        matrix.setTranslate(15, 20);
        imageView.setImageMatrix(matrix);
        assertEquals("Bitmap for resource:org.robolectric:drawable/an_image at (15,20)",
                visualize(imageView));
    }

    @Test
    public void shouldCopyMatrixSetup() throws Exception {
        Matrix matrix = new Matrix();
        matrix.setTranslate(15, 20);
        imageView.setImageMatrix(matrix);
        assertEquals("Bitmap for resource:org.robolectric:drawable/an_image at (15,20)",
                visualize(imageView));

        matrix.setTranslate(30, 40);
        assertEquals("Bitmap for resource:org.robolectric:drawable/an_image at (15,20)",
                visualize(imageView));

        imageView.setImageMatrix(matrix);
        assertEquals("Bitmap for resource:org.robolectric:drawable/an_image at (30,40)",
                visualize(imageView));
    }

    @Test
    public void visualizeWithEmpty() throws Exception {
        assertEquals("", Robolectric.visualize(new ImageView(application)));
    }

    @Test
    public void testSetImageResource_drawable() {
        imageView.setImageResource(R.drawable.l0_red);
        assertTrue("Drawable", imageView.getDrawable() instanceof Drawable);
        assertFalse("LayerDrawable",
                imageView.getDrawable() instanceof LayerDrawable);
    }

    @Test
    public void testSetAnimatedImage_drawable() {
        imageView.setImageResource(R.drawable.animation_list);
        Drawable animation = imageView.getDrawable();
        assertTrue(animation instanceof Drawable);
        assertTrue(animation instanceof AnimationDrawable);
    }

    @Test
    public void testSetAnimationItem() throws Exception {
        imageView.setImageResource(R.drawable.animation_list);
        AnimationDrawable animation = (AnimationDrawable) imageView.getDrawable();
        assertEquals(3, animation.getNumberOfFrames());
        assertEquals(400, animation.getDuration(0));
        assertEquals(300, animation.getDuration(2));
    }
    
    @Test
    public void testSetImageResource_layerDrawable() {
        imageView.setImageResource(R.drawable.rainbow);
        assertTrue("Drawable", imageView.getDrawable() instanceof Drawable);
        assertTrue("LayerDrawable",
                imageView.getDrawable() instanceof LayerDrawable);
        assertThat(shadowOf(imageView.getDrawable()).getLoadedFromResourceId(), is(R.drawable.rainbow));
    }

    @Test
    public void testSetImageLevel() throws Exception {
        imageView.setImageLevel(2);
        assertThat(shadowOf(imageView).getImageLevel(), equalTo(2));
    }
}
