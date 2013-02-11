package org.robolectric.shadows;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.internal.RealObject;

/**
 * Shadow of {@code SpannableStringBuilder} implemented using a regular {@code StringBuilder}
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(SpannableStringBuilder.class)
public class ShadowSpannableStringBuilder implements CharSequence {
    @RealObject private SpannableStringBuilder realSpannableStringBuilder;

    private StringBuilder builder = new StringBuilder();

    public void __constructor__(CharSequence text) {
        builder.append(text);
    }

    @Implementation
    public SpannableStringBuilder append(char text) {
        builder.append(text);
        return realSpannableStringBuilder;
    }

    @Implementation
    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        return replace(start, end, tb, 0, tb.length());
    }

    @Implementation
    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbStart, int tbEnd) {
        builder.replace(start, end, tb.subSequence(tbStart, tbEnd).toString());
        return realSpannableStringBuilder;
    }

    @Implementation
    public Editable insert(int where, CharSequence text) {
        builder.insert(where, text);
        return realSpannableStringBuilder;
    }

    @Implementation
    public SpannableStringBuilder append(CharSequence text) {
        builder.append(text);
        return realSpannableStringBuilder;
    }

    @Implementation
    @Override public int length() {
        return builder.length();
    }

    @Implementation
    @Override public char charAt(int index) {
        return builder.charAt(index);
    }

    @Implementation
    @Override public CharSequence subSequence(int start, int end) {
        return builder.subSequence(start, end);
    }

    @Implementation
    public String toString() {
        return builder.toString();
    }
    
    @Implementation
    public SpannableStringBuilder delete( int start, int end ) {
    	builder.delete( start, end );
        return realSpannableStringBuilder;
    }
}
