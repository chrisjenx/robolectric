package org.robolectric.res;

import android.R;
import org.robolectric.util.PropertiesHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class AndroidResourcePathFinder {
    public static final String ANDROID_SDK_HELP_TEXT = "See http://pivotal.github.com/robolectric/resources.html#unable_to_find_android_sdk for more info.";
    private final int sdkVersion;
    private final ResourcePath resourcePath;

    public static ResourcePath getSystemResourcePath(int sdkVersion, ResourcePath resourcePath) {
        String pathToAndroidResources = new AndroidResourcePathFinder(sdkVersion, resourcePath).getPathToAndroidResources();
        if (pathToAndroidResources == null)
            throw new RuntimeException("Unable to find Android SDK. " + ANDROID_SDK_HELP_TEXT);

        File path = new File(pathToAndroidResources);
        if (!path.isDirectory())
            throw new RuntimeException("Unable to find Android SDK: " + path.getAbsolutePath() + " is not a directory. " + ANDROID_SDK_HELP_TEXT);

        return new ResourcePath(R.class, path, null);
    }

    public AndroidResourcePathFinder(int sdkVersion, ResourcePath resourcePath) {
        this.resourcePath = resourcePath == null ? new ResourcePath(null, new File("."), null) : resourcePath;
        this.sdkVersion = sdkVersion;
    }

    String getPathToAndroidResources() {
        String pathToAndroidResources;
        if ((pathToAndroidResources = getAndroidResourcePathFromLocalProperties()) != null) {
            return pathToAndroidResources;
        }
        if ((pathToAndroidResources = getAndroidResourcePathFromSystemEnvironment()) != null) {
            return pathToAndroidResources;
        }
        if ((pathToAndroidResources = getAndroidResourcePathFromSystemProperty()) != null) {
            return pathToAndroidResources;
        }
        if ((pathToAndroidResources = getAndroidResourcePathByExecingWhichAndroid()) != null) {
            return pathToAndroidResources;
        }
        return null;
    }

    private String getAndroidResourcePathFromLocalProperties() {
        // Hand tested
        // This is the path most often taken by IntelliJ
        File rootDir = resourcePath.resourceBase.getParentFile();
        String localPropertiesFileName = "local.properties";
        File localPropertiesFile = new File(rootDir, localPropertiesFileName);
        if (!localPropertiesFile.exists()) {
            localPropertiesFile = new File(localPropertiesFileName);
        }
        if (localPropertiesFile.exists()) {
            Properties localProperties = new Properties();
            try {
                localProperties.load(new FileInputStream(localPropertiesFile));
                PropertiesHelper.doSubstitutions(localProperties);
                String sdkPath = localProperties.getProperty("sdk.dir");
                if (sdkPath != null) {
                    return getResourcePathFromSdkPath(sdkPath);
                }
            } catch (IOException e) {
                // fine, we'll try something else
            }
        }
        return null;
    }

    private String getAndroidResourcePathFromSystemEnvironment() {
        // Hand tested
        String resourcePath = System.getenv().get("ANDROID_HOME");
        if (resourcePath != null) {
            return new File(resourcePath, getAndroidResourceSubPath()).toString();
        }
        return null;
    }

    private String getAndroidResourcePathFromSystemProperty() {
        // this is used by the android-maven-plugin
        String resourcePath = System.getProperty("android.sdk.path");
        if (resourcePath != null) {
            return new File(resourcePath, getAndroidResourceSubPath()).toString();
        }
        return null;
    }

    private String getAndroidResourcePathByExecingWhichAndroid() {
        // Hand tested
        // Should always work from the command line. Often fails in IDEs because
        // they don't pass the full PATH in the environment
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"which", "android"});
            String sdkPath = new BufferedReader(new InputStreamReader(process.getInputStream())).readLine();
            if (sdkPath != null && sdkPath.endsWith("tools/android")) {
                return getResourcePathFromSdkPath(sdkPath.substring(0, sdkPath.indexOf("tools/android")));
            }
        } catch (IOException e) {
            // fine we'll try something else
        }
        return null;
    }

    private String getResourcePathFromSdkPath(String sdkPath) {
        File androidResourcePath = new File(sdkPath, getAndroidResourceSubPath());
        return androidResourcePath.exists() ? androidResourcePath.toString() : null;
    }

    private String getAndroidResourceSubPath() {
        return "platforms/android-" + sdkVersion + "/data/res";
    }
}
