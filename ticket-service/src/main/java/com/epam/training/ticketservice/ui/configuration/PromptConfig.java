package com.epam.training.ticketservice.ui.configuration;

import org.jline.utils.AttributedString;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

import static org.jline.utils.AttributedStyle.DEFAULT;
import static org.jline.utils.AttributedStyle.GREEN;

@Configuration
public class PromptConfig implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Ticket service>", DEFAULT.foreground(GREEN));
    }
}
