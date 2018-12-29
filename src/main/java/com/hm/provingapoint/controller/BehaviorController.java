package com.hm.provingapoint.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

@RestController
public class BehaviorController {
    
    @GetMapping("/test")
    public String someBehavior() throws Exception {
        Class<?> behaviorClass = loadBehavior();
        Method invokeMethod = behaviorClass.getMethod("invoke");
        return (String) invokeMethod.invoke(behaviorClass.newInstance());
    }
    
    private Class<?> loadBehavior() throws ClassNotFoundException, MalformedURLException {
        return makeClassLoader().loadClass("com.hm.provingapoint.mutable.Behavior");
    }
    
    private URLClassLoader makeClassLoader() throws MalformedURLException {
        return URLClassLoader.newInstance(new URL[]{Paths.get("src/main/java").toUri().toURL()},
                                          this.getClass().getClassLoader().getParent());
    }
}
