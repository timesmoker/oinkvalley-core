package com.oinkvalley.oinkvalleycore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "board")
@Data
public class BoardConfig {
    private List<String> publicBoards = new ArrayList<>();
}