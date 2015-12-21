package com.iskcon.pb;

import android.app.Application;

import com.iskcon.pb.models.Darshan;
import com.iskcon.pb.models.Media;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by maygupta on 11/13/15.
 */
public class MediaApplication extends Application {
    public static final String YOUR_APPLICATION_ID = "Lz9vxEOgxhDmn3QPzaZPgELMZxYJidNxVI5F827B";
    public static final String YOUR_CLIENT_KEY = "bbWyssb20AJ5FHOQythPVkU2zJJW8SwMY4Gzy3MM";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Media.class);
        ParseObject.registerSubclass(Darshan.class);
        Parse.enableLocalDatastore(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

}
