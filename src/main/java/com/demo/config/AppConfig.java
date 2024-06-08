package com.demo.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

    private final Paging paging = new Paging();

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Paging {
        @NotNull private String sizeParameter;

        private String pageParameter;

        @NotNull private String sortParameter;

        @NotNull private Integer defaultPageSize;

        @NotNull private Integer maxPageSize;

        // Copy constructor for defensive copy
        public Paging(Paging other) {
            this.sizeParameter = other.sizeParameter;
            this.pageParameter = other.pageParameter;
            this.sortParameter = other.sortParameter;
            this.defaultPageSize = other.defaultPageSize;
            this.maxPageSize = other.maxPageSize;
        }
    }
}
