package com.miaoyu.naigos.api.BlueArchive.controller;

import com.miaoyu.naigos.api.BlueArchive.service.RecreateBAClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ba/classify/recreate")
public class RecreateBAClassifyController {
    @Autowired private RecreateBAClassifyService recreateBAClassifyService;
    @GetMapping("/all_brief")
    public Map<String, Object> getAllBAClassifyBriefControl() {
        return recreateBAClassifyService.getAllBAClassifyBriefService();
    }
    @GetMapping("/eligible")
    public Map<String, Object> getEligibleBAClassifyControl(
            @RequestParam("classify_id") String classifyId
    ) {
        return recreateBAClassifyService.getEligibleBAClassify(classifyId);
    }
}