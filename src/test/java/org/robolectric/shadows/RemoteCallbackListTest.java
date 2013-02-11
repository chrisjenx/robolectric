package org.robolectric.shadows;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import org.robolectric.TestRunners;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(TestRunners.WithDefaults.class)
public class RemoteCallbackListTest {
    @Test
    public void testBasicWiring() throws Exception {
        RemoteCallbackList<Foo> fooRemoteCallbackList = new RemoteCallbackList<Foo>();
        Foo callback = new Foo();
        fooRemoteCallbackList.register(callback);

        fooRemoteCallbackList.beginBroadcast();

        assertThat(fooRemoteCallbackList.getBroadcastItem(0), sameInstance(callback));
    }

    public static class Foo implements IInterface {

        @Override
        public IBinder asBinder() {
            return new Binder();
        }
    }
}