package com.myai.openai;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@Theme(value = "hilla-react-app")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).run(args);
    }

}
