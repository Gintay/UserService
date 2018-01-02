package com.userservice;

import com.usersservice.Launcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
//@PrepareForTest(Launcher.class)
public class LauncherTest {

    @Mock
    SpringApplicationBuilder application;

    @Mock
    ConfigurableApplicationContext context;

    @Test
    public void testMain_ShouldExecuteWithoutErrors() throws Exception {
        PowerMockito.whenNew(SpringApplicationBuilder.class).withNoArguments().thenReturn(application);
        when(application.sources(any())).thenReturn(application);
        when(application.properties(anyMap())).thenReturn(application);
        when(application.run()).thenReturn(context);

        String[] args = new String[0];
        Launcher.main(args);
    }
}
