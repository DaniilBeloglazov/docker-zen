package com.example.shell.config;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConnectedPromptProvider implements PromptProvider {
    private final Authentication authentication;

    @Override
    public AttributedString getPrompt() {
        String prompt = "shell " + ((authentication.getStatus()) ? "(logged in)" : "(not authorized)") + ":>";
        val style = new AttributedStyle(AttributedStyle.MAGENTA, AttributedStyle.MAGENTA);
        return new AttributedString(prompt, style);
    }
}