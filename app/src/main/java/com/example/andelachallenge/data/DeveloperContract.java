package com.example.andelachallenge.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by codedentwickler on 3/8/17.
 */

public class DeveloperContract {

    public static final String AUTHORITY = "com.example.andelachallenge";

    public static class DeveloperEntry implements BaseColumns {

        public static final String PATH_DEVELOPER = "developers";

        private static Uri.Builder builder = new Uri.Builder().scheme("content").authority(AUTHORITY);

        public static final Uri BASE_CONTENT_URI = Uri.parse(builder.build().toString());

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DEVELOPER);

        public static final String TABLE_NAME = "developers";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_DEVELOPER_USERNAME = "username";

        public static final String COLUMN_DEVELOPER_GITHUB_PROFILE_URL = "profile_url";

        public static final String COLUMN_DEVELOPER_IMAGE_URL = "image_url";

    }

}
