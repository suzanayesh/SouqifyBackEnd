package com.code.auth.controller;

import com.code.auth.dto.user.FollowDto;
import com.code.auth.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{retailerId}/{supplierId}")
    public FollowDto follow(@PathVariable Long retailerId, @PathVariable Long supplierId) {
        return followService.follow(retailerId, supplierId);
    }

    @GetMapping("/followers/{supplierId}")
    public List<FollowDto> getFollowers(@PathVariable Long supplierId) {
        return followService.getFollowers(supplierId);
    }

    @GetMapping("/following/{retailerId}")
    public List<FollowDto> getFollowing(@PathVariable Long retailerId) {
        return followService.getFollowing(retailerId);
    }
}