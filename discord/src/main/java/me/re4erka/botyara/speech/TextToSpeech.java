package me.re4erka.botyara.speech;

import lombok.extern.log4j.Log4j2;
import me.re4erka.botyara.speech.name.VoiceName;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class TextToSpeech {
    private final String key;
    private final VoiceName voice;

    private final boolean ssl;

    private TextToSpeech(@NotNull String key, @NotNull VoiceName voice, boolean ssl) {
        this.key = key;
        this.voice = voice;

        this.ssl = ssl;
    }

//    public CompletableFuture<byte[]> synthesise(String text) {
//        return CompletableFuture.supplyAsync(() -> {
//            final String request = request(text);
//            final URL url = new URL((ssl ? "https" : "http") + "://api.voicerss.org/");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            conn.setConnectTimeout(60000);
//            conn.setDoOutput(true);
//            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
//            writer.write(request);
//            writer.close();
//            outStream.close();
//            if (conn.getResponseCode() != 200) {
//                throw new Exception(conn.getResponseMessage());
//            } else {
//                ByteArrayOutputStream outArray = new ByteArrayOutputStream();
//                InputStream inStream = conn.getInputStream();
//                byte[] buffer = new byte[4096];
//                int n = true;
//
//                int n;
//                while((n = inStream.read(buffer)) > 0) {
//                    outArray.write(buffer, 0, n);
//                }
//
//                byte[] response = outArray.toByteArray();
//                inStream.close();
//                String responseString = new String(response, "UTF-8");
//                if (responseString.indexOf("ERROR") == 0) {
//                    throw new Exception(responseString);
//                } else {
//                    return response;
//                }
//            }
//        });
//    }

    private String request(@NotNull String text) {
        return StringUtils.join(
                new String[] {
                        "key=" + key, "src=" + text,
                        "hl=ru-ru", "v=" + voice.getType(),
                        "r=0", "c=WAV",
                        "f=44khz_16bit_stereo",
                        "ssml=false", "b64=false"
                }, '&'
        );
    }

    public static TextToSpeech.Builder builder() {
        return new TextToSpeech.Builder();
    }

    public static final class Builder {
        private String key;
        private VoiceName voice = VoiceName.PETER;

        private boolean ssl = false;

        public Builder setKey(@NotNull String key) {
            this.key = key;
            return this;
        }

        public Builder setVoice(@NotNull VoiceName voice) {
            this.voice = voice;
            return this;
        }

        public Builder setSsl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public TextToSpeech build() {
            return new TextToSpeech(key, voice, ssl);
        }
    }
}
