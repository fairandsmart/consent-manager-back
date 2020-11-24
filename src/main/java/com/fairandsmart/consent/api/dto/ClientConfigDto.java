package com.fairandsmart.consent.api.dto;

import com.fairandsmart.consent.common.config.ClientConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientConfigDto {

    private boolean isUserPageEnabled;
    private List<String> userPageElements;

    public ClientConfigDto() {
        userPageElements = new ArrayList<>();
    }

    public boolean isUserPageEnabled() {
        return isUserPageEnabled;
    }

    public void setUserPageEnabled(boolean userPageEnabled) {
        isUserPageEnabled = userPageEnabled;
    }

    public List<String> getUserPageElements() {
        return userPageElements;
    }

    public void setUserPageElements(List<String> userPageElements) {
        this.userPageElements = userPageElements;
    }

    public static ClientConfigDto fromClientConfig(ClientConfig config) {
        ClientConfigDto dto = new ClientConfigDto();
        dto.setUserPageEnabled(config.isUserPageEnabled());
        if (config.userPageElements().length()>0) {
            dto.setUserPageElements(Arrays.asList(config.userPageElements().split(",")));
        }
        return dto;
    }
}
