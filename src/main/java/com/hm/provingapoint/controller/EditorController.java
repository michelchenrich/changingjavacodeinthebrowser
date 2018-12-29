package com.hm.provingapoint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class EditorController {
    
    private static final String PATH_STRING = "src/main/java/com/hm/provingapoint/mutable/Behavior.java";
    private static final Path PATH = Paths.get(PATH_STRING);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @GetMapping
    public String editAClassInRuntime() throws IOException {
        return "<form method='post' action='/' id='editor'>" +
               "  <input type='submit' >" +
               "</form>" +
               "<textarea style='width: 100%; height: 100%' name='content' form='editor'>" +
               new String(Files.readAllBytes(PATH)) +
               "</textarea>";
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> editTheClass(@RequestParam Map<String, String> body)
        throws IOException {
        Files.write(PATH, body.get("content").getBytes(Charset.forName("UTF-8")));
        
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(System.in, System.out, System.err, PATH_STRING);
        
        return ResponseEntity.ok("Submitted. " +
                                 "<a href='/test'>See the new behavior here</a>");
    }
}
