package com.demo.utils;

public class GPS {
    /**
     * Use on ContentValues when update cloumn value LATITUDE/LATITUDE on local_media table.
     * Such as:
     *         ContentValues values = new ContentValues();
     *         values.put(GalleryStore.LocalMediaColumns.LATITUDE, DEFAULT_LAT_LNG_VALUE);
     *         values.put(GalleryStore.LocalMediaColumns.LATITUDE, DEFAULT_LAT_LNG_VALUE);
     */
    public static final String  DEFAULT_LAT_LNG_VALUE = "NAN";
    private static StringBuilder sb = new StringBuilder(20);

    /**
     * returns ref for latitude which is S or N.
     *
     * @param latitude
     * @return S or N
     */
    public static String latitudeRef(double latitude) {
        return (latitude < 0.0d) ? "S" : "N";
    }

    /**
     * returns ref for latitude which is S or N.
     *
     * @param longitude
     * @return S or N
     */
    public static String longitudeRef(double longitude) {
        return (longitude < 0.0d) ? "W" : "E";
    }

    /**
     * convert latitude into DMS (degree minute second) format. For instance<br/>
     * -79.948862 becomes<br/>
     * 79/1,56/1,55903/1000<br/>
     * It works for latitude and longitude<br/>
     *
     * @param latitude could be longitude.
     * @return String
     */
    public static synchronized String convert(double latitude) {
        latitude = Math.abs(latitude);
        int degree = (int) latitude;
        int num = 60;
        latitude *= num;
        latitude -= (degree * 60.0d);
        int minute = (int) latitude;
        latitude *= num;
        latitude -= (minute * 60.0d);
        int second = (int) (latitude * 1000.0d);

        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }

    public static boolean isLatLngValid(double[] latlng) {
        if ((latlng == null) || (latlng.length < 2)) {
            return false;
        }
        return !isLatLngInvalid(latlng[0], latlng[1]);
    }

    public static boolean isLatLngInvalid(double[] latlng) {
        if ((latlng == null) || (latlng.length != 2)) {
            return true;
        }
        return isLatLngInvalid(latlng[0], latlng[1]);
    }

    /**
     * We think (0.0, 0.0) and (xx, NAN)，(NAN, xx) is the invalid Latitude and longitude
     * @param latitude
     * @param longitude
     * @return boolean
     */
    public static boolean isLatLngInvalid(double latitude, double longitude) {
        return (isLatZero(latitude) && isLatZero(longitude)) || (Double.isNaN(latitude) || Double.isNaN(longitude));
    }

    public static boolean isLatInValid(double latitude) {
        return Double.isNaN(latitude) || isLatZero(latitude);
    }

    public static boolean isLatZero(double latitude) {
        return Double.compare(Math.abs(latitude), 0) == 0;
    }
}
