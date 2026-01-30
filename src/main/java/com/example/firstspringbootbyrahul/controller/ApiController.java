package com.example.firstspringbootbyrahul.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
	@GetMapping("/hello")
	public ResponseEntity<Map<String, String>> simpleGet() {
		Map<String, String> response = new HashMap<>();
		response.put("message","Hello Spring Boot!");
		response.put("status", "success");
		return ResponseEntity.ok(response);
	}

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        Map<String,Object> response = new HashMap<>();
        response.put("id",id);
        response.put("name", "Foo bar baz");
        response.put("email","abds@gmail.com");
        return ResponseEntity.ok(response);
        // http://localhost:8081/api/users/123
    }
    @GetMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<Map<String,Object>> getUserPost(
        @PathVariable String userId,
        @PathVariable String postId
    ) {
        Map<String,Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("postId", postId);
        response.put("title","Post Title");
        response.put("content", "Post Content");
        return ResponseEntity.ok(response);
        // http://localhost:8081/api/users/1/posts/12
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchUser(
        @RequestParam String name,
        @RequestParam(required=false) String city,
        @RequestParam(defaultValue="10") int limit
    ) {
        Map<String,Object> response = new HashMap<>();
        response.put("query_name", name);
        response.put("query_city", city != null ? city : "Not specified");
        response.put("limit",limit);
        response.put("results", Arrays.asList("user1", "user2", "user3"));
        return ResponseEntity.ok(response);
        // http://localhost:8081/api/search?name=John
        // http://localhost:8081/api/search?name=John&city=NYC&limit=20
        // http://localhost:8081/api/search?name=Alice&city=LA
        // http://localhost:8081/api/search?name=Bob&limit=50
    }

    /**
     * @InitBinder - HOW IT WORKS:
     * 
     * 1. PURPOSE: @InitBinder methods are used to customize the data binding process
     *    for controller methods. They allow you to register custom PropertyEditors,
     *    Formatters, or Validators that control how request parameters are converted
     *    to Java objects.
     * 
     * 2. EXECUTION TIMING: This method is called BEFORE each request handler method
     *    in the controller executes. Spring uses it to configure the WebDataBinder
     *    that will be used for that specific request.
     * 
     * 3. COMMON USE CASES:
     *    - Date/Time formatting: Convert string parameters to Date objects with specific formats
     *    - String trimming: Automatically trim whitespace from String parameters
     *    - Custom type conversion: Convert strings to custom objects
     *    - Validation: Register custom validators for form data
     * 
     * 4. SCOPE: By default, @InitBinder applies to ALL handler methods in the controller.
     *    You can limit its scope using @InitBinder("specificParamName") to target
     *    specific request parameters only.
     * 
     * 5. WebDataBinder: The parameter received is a WebDataBinder instance which provides
     *    methods to register custom editors, formatters, and validators.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Example 1: Register a custom date format for Date parameters
        // This PropertyEditor tells Spring how to convert string dates to Date objects
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Strict parsing - "2024-13-45" would fail
        
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    // Convert the string parameter to a Date object
                    setValue(dateFormat.parse(text));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd", e);
                }
            }
            
            @Override
            public String getAsText() {
                // Convert Date object back to string (for response formatting)
                return dateFormat.format((Date) getValue());
            }
        });
        
        // Example 2: Automatically trim all String parameters
        // This removes leading/trailing whitespace from all string inputs
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : text.trim());
            }
        });
    }

    /**
     * EXAMPLE ENDPOINT: Date Range Search
     * This endpoint demonstrates @InitBinder in action by accepting Date parameters.
     * Without @InitBinder, you'd need to manually parse the date strings.
     * With @InitBinder, Spring automatically converts the string to Date using our custom format.
     */
    @GetMapping("/events/search")
    public ResponseEntity<Map<String,Object>> searchEvents(
        @RequestParam Date startDate,
        @RequestParam Date endDate,
        @RequestParam(required=false) String category
    ) {
        Map<String,Object> response = new HashMap<>();
        
        // Thanks to @InitBinder, startDate and endDate are already Date objects!
        // No need for manual parsing like: new SimpleDateFormat("yyyy-MM-dd").parse(startDateString)
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("category", category); // This was automatically trimmed by @InitBinder
        response.put("events", Arrays.asList(
            "Event 1 on " + startDate,
            "Event 2 between dates",
            "Event 3 on " + endDate
        ));
        
        return ResponseEntity.ok(response);
        
        // Try these URLs:
        // http://localhost:8081/api/events/search?startDate=2024-01-01&endDate=2024-12-31
        // http://localhost:8081/api/events/search?startDate=2024-06-15&endDate=2024-06-20&category=conference
        // 
        // Invalid format will throw an error:
        // http://localhost:8081/api/events/search?startDate=01/01/2024&endDate=12/31/2024
        // (This will fail because we defined yyyy-MM-dd format in @InitBinder)
    }
}