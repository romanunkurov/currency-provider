package ru.alfa.currencyprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GifDTO {

    private List<Gif> data;

    @Data
    public static class Gif {
        private String type;
        private String id;
        private Images images;


        @Data
        public static class Images {
            private Downsized downsized;

            @Data
            public static class Downsized {
                private String height;
                private String width;
                private String size;
                private String url;
            }
        }
    }
}
