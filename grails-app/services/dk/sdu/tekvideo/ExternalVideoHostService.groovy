package dk.sdu.tekvideo

import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import org.codehaus.groovy.grails.commons.GrailsApplication

import javax.annotation.PostConstruct

import static dk.sdu.tekvideo.ServiceResult.*

/**
 * A service for gathering information about a video hosted on an external site
 */
class ExternalVideoHostService {
    def grailsApplication

    private YouTube youtube
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpRequestInitializer INITIALIZER = new HttpRequestInitializer() {
        void initialize(HttpRequest httpRequest) throws IOException {}
    }

    @PostConstruct
    private void init() {
        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, INITIALIZER)
                .setApplicationName("tekvideo.sdu.dk")
                .build()
    }

    ServiceResult<Map> getVideoInformation(String id, Boolean isYoutube) {
        if (isYoutube) {
            return getYouTubeVideoInformation(id)
        } else {
            fail "Video service ikke understøttet!"
        }
    }

    ServiceResult<Map> getYouTubeVideoInformation(String videoId) {
        try {
            List videos = youtube.videos().list("snippet")
                    .setId(videoId)
                    .setFields("items(id,snippet/title,snippet/description)")
                    .setKey(apiKey)
                    .execute()
                    .getItems()

            if (videos.size() != 1) {
                fail "Kunne ikke hente information om denne video!"
            } else {
                def video = videos[0]
                ok item: [id         : video.getId(),
                          title      : video.getSnippet().getTitle(),
                          description: video.getSnippet().getDescription()]
            }
        } catch (Exception e) {
            e.printStackTrace()
            fail message: "Kunne ikke hente information om denne video!", exception: e
        }
    }

    private String getApiKey() {
        grailsApplication.config.apis.youtube.key
    }

}
