package org.robolectric.shadows;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import org.robolectric.Robolectric;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.robolectric.Robolectric.shadowOf;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(BluetoothAdapter.class)
public class ShadowBluetoothAdapter {
    private Set<BluetoothDevice> bondedDevices = new HashSet<BluetoothDevice>();
    private boolean isDiscovering;
    private String address;
    private boolean enabled;

    @Implementation
    public static BluetoothAdapter getDefaultAdapter() {
        return (BluetoothAdapter) shadowOf(Robolectric.application).getBluetoothAdapter();
    }

    @Implementation
    public Set<BluetoothDevice> getBondedDevices() {
        return Collections.unmodifiableSet(bondedDevices);
    }

    public void setBondedDevices(Set<BluetoothDevice> bluetoothDevices) {
        bondedDevices = bluetoothDevices;
    }

    @Implementation
    public boolean startDiscovery() {
        isDiscovering = true;
        return true;
    }

    @Implementation
    public boolean cancelDiscovery() {
        isDiscovering = false;
        return true;
    }

    @Implementation
    public boolean isDiscovering() {
        return isDiscovering;
    }

    @Implementation
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }
    @Implementation
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
