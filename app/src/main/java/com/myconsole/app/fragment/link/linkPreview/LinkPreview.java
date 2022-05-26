
package com.myconsole.app.fragment.link.linkPreview;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.webkit.URLUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class LinkPreview {
    private static final String TAG = LinkPreview.class.getSimpleName();
    private final MetaData metaData;
    private final ResponseListener responseListener;
    private String givenUrl;
    public static final String URL_REGEX_PATTERN = "^(https?://)?(www\\.)?([-a-z0-9]{1,63}\\.)*?[a-z0-9][-a-z0-9]{0,61}[a-z0-9]\\.[a-z]{2,6}(/[-\\w@\\+\\.~#\\?&/=%]*)?$";


    public LinkPreview(ResponseListener responseListener) {
        this.responseListener = responseListener;
        metaData = new MetaData();
    }

    /**
     * Method call before convert url to Bitmap
     */
    private void enableStrictMode() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private int ordinalIndexOf(String str, int n) {
        int pos = -1;
        do {
            pos = str.indexOf("\"", pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    public void getPreview(String url) {
        this.givenUrl = url;
        new BackgroundTaskForLinkData().execute();
    }

    private String resolveURL(String url, String part) {
        if (URLUtil.isValidUrl(part)) {
            return part;
        } else {
            URI base_uri = null;
            try {
                base_uri = new URI(url);
            } catch (URISyntaxException e) {
                e.getMessage();
            }
            if (base_uri != null) {
                base_uri = base_uri.resolve(part);
            }
            if (base_uri != null) {
                return base_uri.toString();
            }
        }
        return part;
    }

    @SuppressLint("StaticFieldLeak")
    private class BackgroundTaskForLinkData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Document doc;
            try {
                if (givenUrl.matches(URL_REGEX_PATTERN)) {
                    doc = Jsoup.connect(givenUrl).timeout(30 * 1000).get();
                    Elements elements = doc.getElementsByTag("meta");
                    // getTitle doc.select("meta[property=og:title]")
                    String title = doc.select("meta[property=og:title]").attr("content");
                    if (title == null || title.isEmpty()) {
                        title = doc.title();
                    }
                    metaData.setTitle(title);
                    //getDescription
                    String description = doc.select("meta[name=description]").attr("content");
                    if (description == null || description.isEmpty()) {
                        description = doc.select("meta[name=Description]").attr("content");
                    }
                    if (description == null || description.isEmpty()) {
                        description = doc.select("meta[property=og:description]").attr("content");
                    }
                    if (description == null || description.isEmpty()) {
                        description = "";
                    }
                    metaData.setDescription(description);
                    // getMediaType
                    Elements mediaTypes = doc.select("meta[name=medium]");
                    String type;
                    if (mediaTypes.size() > 0) {
                        String media = mediaTypes.attr("content");
                        type = media.equals("image") ? "photo" : media;
                    } else {
                        type = doc.select("meta[property=og:type]").attr("content");
                    }
                    metaData.setMediatype(type);
                    //getImages
                    Elements imageElements = doc.select("meta[property=og:image]");
                    if (imageElements.size() > 0) {
                        String image = imageElements.attr("content");
                        if (!image.isEmpty()) {
                            metaData.setImageurl(resolveURL(givenUrl, image));
                        }
                    }
                    if (metaData.getImageurl().isEmpty()) {
                        String src = doc.select("link[rel=image_src]").attr("href");
                        if (!src.isEmpty()) {
                            metaData.setImageurl(resolveURL(givenUrl, src));
                        } else {
                            src = doc.select("link[rel=apple-touch-icon]").attr("href");
                            if (!src.isEmpty()) {
                                metaData.setImageurl(resolveURL(givenUrl, src));
                                metaData.setFavicon(resolveURL(givenUrl, src));
                            } else {
                                src = doc.select("link[rel=icon]").attr("href");
                                if (!src.isEmpty()) {
                                    metaData.setImageurl(resolveURL(givenUrl, src));
                                    metaData.setFavicon(resolveURL(givenUrl, src));
                                }
                            }
                        }
                    }
                    //Favicon
                    String src = doc.select("link[rel=apple-touch-icon]").attr("href");
                    if (!src.isEmpty()) {
                        metaData.setFavicon(resolveURL(givenUrl, src));
                    } else {
                        src = doc.select("link[rel=icon]").attr("href");
                        if (!src.isEmpty()) {
                            metaData.setFavicon(resolveURL(givenUrl, src));
                        }
                    }
                    for (Element element : elements) {
                        if (element.hasAttr("property")) {
                            String str_property = element.attr("property").trim();
                            if (str_property.equals("og:url")) {
                                metaData.setUrl(element.attr("content"));
                            }
                            if (str_property.equals("og:site_name")) {
                                metaData.setSitename(element.attr("content"));
                            }
                        }
                    }
                    if (metaData.getUrl().equals("") || metaData.getUrl().isEmpty()) {
                        URI uri = null;
                        try {
                            uri = new URI(givenUrl);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        if (givenUrl == null) {
                            metaData.setUrl(null);
                        } else {
                            if (uri != null) {
                                metaData.setUrl(uri.getHost());
                            }
                        }
                    }
                } else {
                    responseListener.onError(new Exception("Given Url is invalid"));
                }
            } catch (IOException e) {
                responseListener.onError(new Exception("No Html Received from " + givenUrl + " Check your Internet " + e.getLocalizedMessage()));
            }
            final String ZERO = "0";
            final String ONE = "1";
            enableStrictMode();
            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url1 = new URL(givenUrl);
                connection = (HttpURLConnection) url1.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String buffer = ZERO;
                String Line;
                while ((Line = reader.readLine()) != null) {
                    try {
                        if (Line.contains("og:video:url")) {
                            Line = Line.substring(Line.indexOf("og:video:url"));
                            Line = Line.substring(ordinalIndexOf(Line, 1) + 1, ordinalIndexOf(Line, 2));
                            if (Line.contains("amp;")) {
                                Line = Line.replace("amp;", "");
                            }
                            if (!Line.contains("https")) {
                                Line = Line.replace("http", "https");
                            }
                            buffer = ONE;
                            break;
                        } else {
                            buffer = ZERO;
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                metaData.setSourceVideoUrl(buffer);
            } catch (IOException e) {
                metaData.setSourceVideoUrl(ZERO);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            responseListener.onData(metaData);
        }
    }
}
