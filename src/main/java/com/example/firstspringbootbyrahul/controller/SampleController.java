package com.example.firstspringbootbyrahul.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/api/data")
public class SampleController {
    private static final Map<Integer, String> mockDb = new HashMap<>();
    private static int idCounter = 1;

    @PostMapping
    
    public ResponseEntity<Map<String,Object>> createData(
        @RequestBody Map<String,String> request
    ) {
        String value = request.get("value");
        int id = idCounter++;
        mockDb.put(id, value);

        Map<String, Object> response = new HashMap<>();
        response.put("message","Data created!");
        response.put("id",id);
        response.put("value", value);

        return ResponseEntity.ok(response);
    }

    // ---------------- READ ALL ----------------
    // GET /api/data
    @GetMapping
    
    public ResponseEntity<Map<String, Object>> getAllData() {

        Map<String, Object> response = new HashMap<>();
        response.put("data", mockDb);
        response.put("total", mockDb.size());

        return ResponseEntity.ok(response);
    }

    // ---------------- READ BY ID ----------------
    // GET /api/data/{id}
    @GetMapping("/{id}")
    
    public ResponseEntity<Map<String, Object>> getDataById(
            @PathVariable int id) {

        Map<String, Object> response = new HashMap<>();

        if (!mockDb.containsKey(id)) {
            response.put("error", "Data not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("id", id);
        response.put("value", mockDb.get(id));

        return ResponseEntity.ok(response);
    }

    // ---------------- UPDATE ----------------
    // PUT /api/data/{id}
    @PutMapping("/{id}")
    
    public ResponseEntity<Map<String, Object>> updateData(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        if (!mockDb.containsKey(id)) {
            response.put("error", "Data not found");
            return ResponseEntity.status(404).body(response);
        }

        String updatedValue = request.get("value");
        mockDb.put(id, updatedValue);

        response.put("message", "Data updated successfully");
        response.put("id", id);
        response.put("value", updatedValue);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    
    public ResponseEntity<Map<String, Object>> patchData(
            @PathVariable int id,
            @RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        if (!mockDb.containsKey(id)) {
            response.put("error", "Data not found");
            return ResponseEntity.status(404).body(response);
        }

        String updatedValue = request.get("value");
        mockDb.put(id, updatedValue);

        response.put("message", "Data patched successfully");
        response.put("id", id);
        response.put("value", updatedValue);

        return ResponseEntity.ok(response);
    }

    // ---------------- DELETE ----------------
    // DELETE /api/data/{id}
    @DeleteMapping("/{id}")
    
    public ResponseEntity<Map<String, Object>> deleteData(
            @PathVariable int id) {

        Map<String, Object> response = new HashMap<>();

        if (!mockDb.containsKey(id)) {
            response.put("error", "Data not found");
            return ResponseEntity.status(404).body(response);
        }

        mockDb.remove(id);
        response.put("message", "Data deleted successfully");
        response.put("id", id);

        return ResponseEntity.ok(response);
    }
}